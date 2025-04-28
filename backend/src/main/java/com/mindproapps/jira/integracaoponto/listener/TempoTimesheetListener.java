package com.mindproapps.jira.integracaoponto.listener;

import com.atlassian.event.api.EventListener;
import com.atlassian.event.api.EventPublisher;
import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
import com.mindproapps.jira.integracaoponto.model.timesheets.TimesheetActionOrigin;
import com.mindproapps.jira.integracaoponto.service.approval.ApproveHoursService;
import com.tempoplugin.timesheet.approval.api.Approval;
import com.tempoplugin.timesheet.approval.api.event.ApprovalEvent;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log4j
@Component
public class TempoTimesheetListener extends GenericListener {
    @JiraImport
    protected EventPublisher eventPublisher;

    protected Object instance;
    private ApproveHoursService approveHoursService;


    @Autowired
    public TempoTimesheetListener(EventPublisher eventPublisher, ApproveHoursService approveHoursService) {
        log.info("TempoTimesheetListener: eventPublisher = " + eventPublisher + ", approveHoursService = " + approveHoursService);
        this.eventPublisher = eventPublisher;
        this.approveHoursService = approveHoursService;
        this.instance = this;
    }

    @EventListener
    public void onTimesheetApprovalEvent(ApprovalEvent approvalEvent) {
        log.info("onTimesheetApprovalEvent: approvalEvent = " + approvalEvent);
        if(approvalEvent.getApproval() != null &&
                (Approval.Action.approve.equals(approvalEvent.getApproval().getAction()) ||
                Approval.Action.reject.equals(approvalEvent.getApproval().getAction())) ||
                Approval.Action.reopen.equals(approvalEvent.getApproval().getAction())) {
            log.trace("ApprovalEvent detected: " + approvalEvent.getApproval().toString());
            approveHoursService.saveOriginTrace(approvalEvent.getApproval().getId(),
                    TimesheetActionOrigin.TEMPO_TIMESHEETS);
        }
    }

    @Override
    public EventPublisher getEventPublisher() {
        return this.eventPublisher;
    }

    @Override
    public Object getInstance() {
        return instance;
    }

    @Override
    public void setInstance(Object instance) {
        this.instance = instance;
    }

}
