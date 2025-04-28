package com.mindproapps.jira.integracaoponto.service.mail.impl;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.mail.Email;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.preferences.PreferenceKeys;
import com.atlassian.mail.queue.SingleMailQueueItem;
import com.atlassian.mail.server.MailServerManager;
import com.atlassian.mail.server.SMTPMailServer;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.mindproapps.jira.integracaoponto.dao.user.jira.JiraUserDAO;
import com.mindproapps.jira.integracaoponto.model.dto.email.EmailDTO;
import com.mindproapps.jira.integracaoponto.model.dto.email.EmailTemplateDTO;
import com.mindproapps.jira.integracaoponto.model.timesheets.TimesheetAction;
import com.mindproapps.jira.integracaoponto.service.i18n.I18nService;
import com.mindproapps.jira.integracaoponto.service.mail.MailService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

@Service
@Log4j
public class MailServiceImpl implements MailService {
    @ComponentImport
    MailServerManager mailServerManager;

    private JiraUserDAO jiraUserDAO;

    private I18nService i18nService;

    @Autowired
    public MailServiceImpl(MailServerManager mailServerManager,
                           JiraUserDAO jiraUserDAO,
                           I18nService i18nService) {
        this.mailServerManager = mailServerManager;
        this.jiraUserDAO = jiraUserDAO;
        this.i18nService = i18nService;
    }

    @Override
    public void sendApproval(EmailDTO emailDTO) {
        log.info("sendApproval: emailDTO = " + emailDTO);
        dispatchEmail(TimesheetAction.APPROVE, emailDTO);
    }

    @Override
    public void sendReopen(EmailDTO emailDTO) {
        log.info("sendReopen: emailDTO = " + emailDTO);
        dispatchEmail(TimesheetAction.REOPEN, emailDTO);
    }

    @Override
    public void sendRejection(EmailDTO emailDTO) {
        log.info("sendRejection: emailDTO = " + emailDTO);
        dispatchEmail(TimesheetAction.REJECT, emailDTO);
    }

    @Override
    public void sendSubmitted(EmailDTO emailDTO) {
        log.info("sendSubmitted: emailDTO = " + emailDTO);
        dispatchEmail(TimesheetAction.SUBMIT, emailDTO);
    }

    private void dispatchEmail(TimesheetAction action, EmailDTO emailDTO) {
        log.info("dispatchEmail: action = " + action + ", emailDTO = " + emailDTO);
        try {
            Map<String, String> i18n = getTextsForUser(emailDTO.getUserKey());
            if(i18n == null) {
                i18n = i18nService.getTexts("email");
            }
            String to = jiraUserDAO.getEmailByKey(emailDTO.getUserKey());
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String period = emailDTO.getPeriodFrom().format(fmt) + " - " + emailDTO.getPeriodTo().format(fmt);
            String approver = jiraUserDAO.getNameByKey(emailDTO.getApproverKey());
            String user = jiraUserDAO.getNameByKey(emailDTO.getUserKey());
            String statusKey = TimesheetAction.APPROVE.equals(action) ? "email.status.approved" :
                    TimesheetAction.REJECT.equals(action) ? "email.status.rejected" : 
                    TimesheetAction.REOPEN.equals(action) ? "email.status.reopened" : "email.status.submitted";
            String actionKey = TimesheetAction.APPROVE.equals(action) ? "email.approver.action.approve" :
                    TimesheetAction.REJECT.equals(action) ? "email.approver.action.reject" : 
                    TimesheetAction.REOPEN.equals(action) ? "email.approver.action.reopen" : "email.approver.action.submit";
            String subject = approver + " " + i18n.get(actionKey);
            String url = mountTimesheetUrl(emailDTO.getUserKey(),
                    emailDTO.getPeriodFrom().format(DateTimeFormatter.ISO_DATE),
                    emailDTO.getPeriodTo().format(DateTimeFormatter.ISO_DATE));
            String body = EmailTemplateDTO.EMAIL_TEMPLATE;
            body = body.replace("%%TIMESHEET_STATUS%%", i18n.get(statusKey))
                    .replace("%%APPROVER_NAME%%", approver)
                    .replace("%%APPROVER_ACTION%%", i18n.get(actionKey))
                    .replace("%%COMMENT_TITLE%%", i18n.get("email.comment.title"))
                    .replace("%%COMMENT_CONTENT%%", emailDTO.getComment())
                    .replace("%%PERIOD_TITLE%%", i18n.get("email.period.title"))
                    .replace("%%PERIOD_CONTENT%%", period)
                    .replace("%%USER_TITLE%%", i18n.get("email.user.title"))
                    .replace("%%USER_CONTENT%%", user)
                    .replace("%%HOURS_REQUIRED_TITLE%%", i18n.get("email.hoursrequired.title"))
                    .replace("%%HOURS_REQUIRED_CONTENT%%", emailDTO.getHoursRequired())
                    .replace("%%HOURS_LOGGED_TITLE%%", i18n.get("email.hourslogged.title"))
                    .replace("%%HOURS_LOGGED_CONTENT%%", emailDTO.getHoursSubmitted())
                    .replace("%%REVIEWER_TITLE%%", i18n.get("email.reviewer.title"))
                    .replace("%%REVIEWER_CONTENT%%", approver)
                    .replace("%%TIMESHEET_URL%%", url)
                    .replace("%%URL_LABEL%%", i18n.get("email.url.label"))
                    .replace("%%SENT_BY%%", i18n.get("email.sentby"));

            if (to != null ) {
                this.sendEmail(to, subject, body);
            }
        } catch (Exception e) {
            log.error("DAO: sending email 0error: " + e.getMessage(), e);
        }
    }

    private String mountTimesheetUrl(String userKey, String from, String to) {
        log.info("mountTimesheetUrl: userKey = " + userKey + ", from = " + from + ", to = " + to);
        String urlTemplate = ComponentAccessor.getApplicationProperties().getString("jira.baseurl") +
                "/secure/Tempo.jspa#/my-work/timesheet?worker=%%USER_KEY%%" +
                "&amp;from=%%FROM%%&amp;to=%%TO%%&amp;periodType=FIXED";
        return urlTemplate.replace("%%USER_KEY%%", userKey)
                .replace("%%FROM%%", from).replace("%%TO%%", to);
    }

    private void sendEmail(String to, String subject, String body) {
        log.info("sendEmail: to = " + to + ", subject = " + subject + ", body = " + body);
        SMTPMailServer mailServer = mailServerManager.getDefaultSMTPMailServer();
        Email email1 = new Email(to);
        email1.setFrom(mailServer.getDefaultFrom());
        email1.setSubject(subject);
        email1.setMimeType("text/html");
        email1.setBody(body);
        SingleMailQueueItem item = new SingleMailQueueItem(email1);
        ComponentAccessor.getMailQueue().addItem(item);
    }

    private Map<String, String> getTextsForUser(String userKey) {
        log.info("getTextsForUser: userKey = " + userKey);
        Locale locale = Locale.getDefault();
        ApplicationUser user = ComponentAccessor.getUserManager().getUserByKey(userKey);
        if(user != null) {
            String userLocaleString = ComponentAccessor.getUserPreferencesManager().getExtendedPreferences(user).getText(PreferenceKeys.USER_LOCALE);
            if(userLocaleString != null) {
                StringTokenizer tokenizer = new StringTokenizer(userLocaleString, "_");
                String language = tokenizer.nextToken();
                String region = tokenizer.nextToken();

                if (language != null && region != null)
                locale = new Locale(language, region);
            }
        }
        return i18nService.getTextsForLocale("email", locale);
    }


}
