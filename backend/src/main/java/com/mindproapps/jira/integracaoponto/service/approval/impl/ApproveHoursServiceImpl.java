package com.mindproapps.jira.integracaoponto.service.approval.impl;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.security.GlobalPermissionManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.mindproapps.jira.integracaoponto.dao.tempo.timesheet.TempoTimesheetDAO;
import com.mindproapps.jira.integracaoponto.dao.timesheet.TimesheetApprovalsOriginTraceDAO;
import com.mindproapps.jira.integracaoponto.dao.user.depara.DeParaUserDAO;
import com.mindproapps.jira.integracaoponto.exception.NoLoggedUserException;
import com.mindproapps.jira.integracaoponto.model.dto.approval.TimesheetApprovalDTO;
import com.mindproapps.jira.integracaoponto.model.dto.approval.TimesheetSubmitRequestDTO;
import com.mindproapps.jira.integracaoponto.model.dto.approval.TimesheetsWaitingForApprovalDTO;
import com.mindproapps.jira.integracaoponto.model.dto.approval.TimesheetsWaitingForApprovalResponseDTO;
import com.mindproapps.jira.integracaoponto.model.dto.email.EmailDTO;
import com.mindproapps.jira.integracaoponto.model.dto.ponto.PontoDTO;
import com.mindproapps.jira.integracaoponto.model.timesheets.TimesheetActionOrigin;
import com.mindproapps.jira.integracaoponto.service.approval.ApproveHoursService;
import com.mindproapps.jira.integracaoponto.service.i18n.I18nService;
import com.mindproapps.jira.integracaoponto.service.mail.MailService;
import com.mindproapps.jira.integracaoponto.service.ponto.PontoService;
import com.tempoplugin.timesheet.approval.api.Approval;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Log4j
public class ApproveHoursServiceImpl implements ApproveHoursService {
    @ComponentImport
    GlobalPermissionManager globalPermissionManager;

    MailService mailService;

    private TempoTimesheetDAO tempoTimesheetDAO;
    private TimesheetApprovalsOriginTraceDAO timesheetApprovalsOriginTraceDAO;
    private PontoService pontoService;
    private I18nService i18nService;
    private Map<String, String> i18nMap;

    @Autowired
    public ApproveHoursServiceImpl(GlobalPermissionManager globalPermissionManager,
                                   TempoTimesheetDAO tempoTimesheetDAO,
                                   TimesheetApprovalsOriginTraceDAO timesheetApprovalsOriginTraceDAO,
                                   PontoService pontoService,
                                   DeParaUserDAO deParaUserDAO,
                                   I18nService i18nService,
                                   MailService mailService) {
        this.globalPermissionManager = globalPermissionManager;
        this.tempoTimesheetDAO = tempoTimesheetDAO;
        this.timesheetApprovalsOriginTraceDAO = timesheetApprovalsOriginTraceDAO;
        this.pontoService = pontoService;
        this.i18nService = i18nService;
        this.mailService = mailService;
    }

    @Override
    public TimesheetsWaitingForApprovalResponseDTO getTimesheetsWaitingForApprovalList(String startDate, String endDate) {
        log.info("getTimesheetsWaitingForApprovalList: startDate = " + startDate + ", endDate = " + endDate);
        ApplicationUser user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
        if (user == null || !user.isActive()) {
            throw new NoLoggedUserException();
        }
        String key = user.getKey();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMyy");
        String period = LocalDate.parse(startDate).format(fmt);
        TimesheetsWaitingForApprovalResponseDTO responseDTO =
                TimesheetsWaitingForApprovalResponseDTO.builder()
                    .approvedTimesheets(Collections.synchronizedList(new ArrayList<>()))
                    .openTimesheets(Collections.synchronizedList(new ArrayList<>()))
                    .readyToSubmitTimesheets(Collections.synchronizedList(new ArrayList<>()))
                    .submittedTimesheets(Collections.synchronizedList(new ArrayList<>()))
                .build();
        List<TimesheetsWaitingForApprovalDTO> list = Collections.synchronizedList(
                tempoTimesheetDAO.getTimesheetsWaitingForApprovalList(startDate, endDate, period, key));
        
        List<String> lstEmails = new ArrayList<String>();
        list.forEach(dto -> {
            dto.updateEmailPonto();
            if (!lstEmails.contains(dto.getEmailPonto())) {
                lstEmails.add(dto.getEmailPonto());
            }
        });
        List<PontoDTO> allData = pontoService.getAllData(lstEmails, LocalDate.parse(startDate), LocalDate.parse(endDate)); 

        pontoService.setPontoHours(allData, list, startDate, endDate, key);

        list.parallelStream().forEach(timesheet -> {
            if (timesheet.getStatus().equalsIgnoreCase("open") &&
                    !LocalDate.now().isBefore(LocalDate.parse(endDate))) {
                timesheet.setStatus("ready");
            }
            switch (timesheet.getStatus()) {
                case "ready":
                    responseDTO.getReadyToSubmitTimesheets().add(timesheet);
                    break;
                case "open":
                    responseDTO.getOpenTimesheets().add(timesheet);
                    break;
                case "review":
                    responseDTO.getSubmittedTimesheets().add(timesheet);
                    break;
                case "approved":
                    responseDTO.getApprovedTimesheets().add(timesheet);
                    break;
            }
        });

        return responseDTO;

    }

    @Override
    public Integer approveTimesheet(Integer previousTimesheetActionId) {
        log.info("approveTimesheet: previousTimesheetActionId = " + previousTimesheetActionId);
        TimesheetApprovalDTO previous = tempoTimesheetDAO.getTimesheetApprovalById(previousTimesheetActionId);
        if (previous != null) {
            ApplicationUser user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
            if (user == null || !user.isActive()) {
                throw new NoLoggedUserException();
            }
            String key = user.getKey();
            previous.setId(null);
            previous.setAction(Approval.Action.approve.toString());
            previous.setActorKey(key);
            previous.setReason(getI18nMap().get("aprovacao.message.viaapp.approve"));
            previous.setStatus(Approval.Status.approved.toString());
            Integer newId = tempoTimesheetDAO.saveTimesheetApproval(previous);
            this.saveOriginTrace(newId, TimesheetActionOrigin.INTEGRACAO_PONTO_APP);
            mailService.sendApproval(buildEmailDto(previous));
            return newId;
        }
        return -1;
    }

    @Override
    public Integer rejectTimesheet(Integer previousTimesheetActionId, String reason) {
        log.info("rejectTimesheet: previousTimesheetActionId = " + previousTimesheetActionId + ", reason = " + reason);
        TimesheetApprovalDTO previous = tempoTimesheetDAO.getTimesheetApprovalById(previousTimesheetActionId);
        if (previous != null) {
            ApplicationUser user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
            if (user == null || !user.isActive()) {
                throw new NoLoggedUserException();
            }
            String key = user.getKey();
            previous.setId(null);
            previous.setAction(Approval.Action.reject.toString());
            previous.setActorKey(key);
            previous.setReason(reason + " - " + getI18nMap().get("aprovacao.message.viaapp.reject"));
            previous.setStatus(Approval.Status.open.toString());
            Integer newId = tempoTimesheetDAO.saveTimesheetApproval(previous);
            this.saveOriginTrace(newId, TimesheetActionOrigin.INTEGRACAO_PONTO_APP);
            mailService.sendRejection(buildEmailDto(previous));
            return newId;
        }
        return -1;
    }

    @Override
    public Integer reopenTimesheet(Integer previousTimesheetActionId, String reason) {
        log.info("reopenTimesheet: previousTimesheetActionId = " + previousTimesheetActionId + ", reason = " + reason);
        TimesheetApprovalDTO previous = tempoTimesheetDAO.getTimesheetApprovalById(previousTimesheetActionId);
        if (previous != null) {
            ApplicationUser user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
            if (user == null || !user.isActive()) {
                throw new NoLoggedUserException();
            }
            String key = user.getKey();
            previous.setId(null);
            previous.setAction(Approval.Action.reopen.toString());
            previous.setActorKey(key);
            previous.setReason(reason + " - " + getI18nMap().get("aprovacao.message.viaapp.reopen"));
            previous.setStatus(Approval.Status.open.toString());
            Integer newId = tempoTimesheetDAO.saveTimesheetApproval(previous);
            this.saveOriginTrace(newId, TimesheetActionOrigin.INTEGRACAO_PONTO_APP);
            mailService.sendReopen(buildEmailDto(previous));
            return newId;
        }
        return -1;
    }

    @Override
    public Integer submitTimesheet(TimesheetSubmitRequestDTO timesheetSubmit) {
        log.info("submitTimesheet: timesheetSubmit = " + timesheetSubmit);
        if (timesheetSubmit != null) {
            ApplicationUser user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
            if (user == null || !user.isActive()) {
                throw new NoLoggedUserException();
            }
            String key = user.getKey();
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("ddMMyy");
            LocalDate startDate = LocalDate.parse("01" + timesheetSubmit.getPeriod(), fmt);
            LocalDate endDate = startDate.plusMonths(1);

            TimesheetApprovalDTO approval = TimesheetApprovalDTO.builder()
                .id(null)
                .userKey(timesheetSubmit.getUserKey())
                .actorKey(key)
                .reviewerKey(key)
                .status(Approval.Status.review.toString())
                .period(timesheetSubmit.getPeriod())
                .dateFrom(startDate)
                .dateTo(endDate)
                .periodType("BILLING")
                .periodView("PERIOD")
                .reason(getI18nMap().get("aprovacao.message.viaapp.submit"))
                .workedTime(timesheetSubmit.getWorkedTime())
                .submittedTime(timesheetSubmit.getSubmittedTime())
                .requiredTime(timesheetSubmit.getRequiredTime())
                .action(Approval.Action.submit.toString()).build();
            Integer newId = tempoTimesheetDAO.saveTimesheetApproval(approval);
            this.saveOriginTrace(newId, TimesheetActionOrigin.INTEGRACAO_PONTO_APP);
            mailService.sendSubmitted(buildEmailDto(approval));
            return newId;
        }
        return -1;
    }

    @Override
    public void saveOriginTrace(Integer timesheetActionId, TimesheetActionOrigin origin) {
        log.info("saveOriginTrace: timesheetActionId = " + timesheetActionId + ", origin = " + origin);
        timesheetApprovalsOriginTraceDAO.saveOriginTrace(timesheetActionId, origin);
    }

    private Map<String, String > getI18nMap() {
        if(this.i18nMap == null) {
            this.i18nMap = i18nService.getTexts("aprovacao.message.viaapp");
        }
        return this.i18nMap;
    }

    private EmailDTO buildEmailDto(TimesheetApprovalDTO timesheetApprovalDTO) {
        log.info("buildEmailDto: timesheetApprovalDTO = " + timesheetApprovalDTO);
        DecimalFormat df = new DecimalFormat("#.##");
        return EmailDTO.builder()
                .approverKey(timesheetApprovalDTO.getActorKey())
                .comment(timesheetApprovalDTO.getReason())
                .hoursRequired(
                        (df.format(timesheetApprovalDTO.getRequiredTime() / 3600)).replaceAll(",", ".")
                )
                .hoursSubmitted(
                        (df.format(timesheetApprovalDTO.getSubmittedTime() / 3600)).replaceAll(",", ".")
                )
                .periodFrom(timesheetApprovalDTO.getDateFrom())
                .periodTo(timesheetApprovalDTO.getDateTo().minusDays(1))
                .userKey(timesheetApprovalDTO.getUserKey())
                .build();
    }

}
