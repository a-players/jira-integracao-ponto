package com.mindproapps.jira.integracaoponto.listener;

import com.atlassian.event.api.EventPublisher;
import com.mindproapps.jira.integracaoponto.model.timesheets.TimesheetActionOrigin;
import com.mindproapps.jira.integracaoponto.service.approval.ApproveHoursService;
import com.tempoplugin.timesheet.approval.api.Approval;
import com.tempoplugin.timesheet.approval.api.event.ApprovalEvent;
import lombok.extern.log4j.Log4j;
import com.tempoplugin.timesheet.approval.api.Approval.Action;


import javax.inject.Inject;
import javax.inject.Named;

@Named
@Log4j
public class TempoTimesheetListener {

    private final EventPublisher eventPublisher;
    private final ApproveHoursService approveHoursService;

    @Inject
    public TempoTimesheetListener(EventPublisher eventPublisher, ApproveHoursService approveHoursService) {
        log.info("TempoTimesheetListener: eventPublisher = " + eventPublisher + ", approveHoursService = " + approveHoursService);
        this.eventPublisher = eventPublisher;
        this.approveHoursService = approveHoursService;
        this.eventPublisher.register(this); // <<-- Aqui já registra o listener!
    }

    @com.atlassian.event.api.EventListener
    public void onTimesheetApprovalEvent(ApprovalEvent approvalEvent) {
        log.info("onTimesheetApprovalEvent: approvalEvent = " + approvalEvent);
        if (approvalEvent.getApproval() != null &&
                (Approval.Action.approve.equals(approvalEvent.getApproval().getAction()) ||
                        Approval.Action.reject.equals(approvalEvent.getApproval().getAction()) ||
                        Approval.Action.reopen.equals(approvalEvent.getApproval().getAction()))) {
            log.trace("ApprovalEvent detected: " + approvalEvent.getApproval().toString());
            approveHoursService.saveOriginTrace(
                    approvalEvent.getApproval().getId(),
                    TimesheetActionOrigin.TEMPO_TIMESHEETS
            );
        }
    }

    public void destroy() {
        log.info("Unregistering TempoTimesheetListener");
        eventPublisher.unregister(this);
    }
}
