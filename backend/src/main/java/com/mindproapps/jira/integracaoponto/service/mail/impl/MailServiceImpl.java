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

import javax.inject.Inject;
import javax.inject.Named;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

@Named
@Log4j
public class MailServiceImpl implements MailService {


    private final MailServerManager mailServerManager;

    private final JiraUserDAO jiraUserDAO;
    private final I18nService i18nService;

    @Inject
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
        try {
            Map<String, String> i18n = getTextsForUser(emailDTO.getUserKey());
            if (i18n == null) {
                i18n = i18nService.getTexts("email");
            }

            String to = jiraUserDAO.getEmailByKey(emailDTO.getUserKey());
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String period = emailDTO.getPeriodFrom().format(fmt) + " - " + emailDTO.getPeriodTo().format(fmt);
            String approver = jiraUserDAO.getNameByKey(emailDTO.getApproverKey());
            String user = jiraUserDAO.getNameByKey(emailDTO.getUserKey());

            String statusKey = switch (action) {
                case APPROVE -> "email.status.approved";
                case REJECT -> "email.status.rejected";
                case REOPEN -> "email.status.reopened";
                default -> "email.status.submitted";
            };

            String actionKey = switch (action) {
                case APPROVE -> "email.approver.action.approve";
                case REJECT -> "email.approver.action.reject";
                case REOPEN -> "email.approver.action.reopen";
                default -> "email.approver.action.submit";
            };

            String subject = approver + " " + i18n.get(actionKey);
            String url = mountTimesheetUrl(emailDTO.getUserKey(),
                    emailDTO.getPeriodFrom().format(DateTimeFormatter.ISO_DATE),
                    emailDTO.getPeriodTo().format(DateTimeFormatter.ISO_DATE));

            String body = EmailTemplateDTO.EMAIL_TEMPLATE
                    .replace("%%TIMESHEET_STATUS%%", i18n.get(statusKey))
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

            if (to != null) {
                sendEmail(to, subject, body);
            }
        } catch (Exception e) {
            log.error("Erro ao enviar e-mail: " + e.getMessage(), e);
        }
    }

    private String mountTimesheetUrl(String userKey, String from, String to) {
        String baseUrl = ComponentAccessor.getApplicationProperties().getString("jira.baseurl");
        return baseUrl + "/secure/Tempo.jspa#/my-work/timesheet?worker=" + userKey +
                "&from=" + from + "&to=" + to + "&periodType=FIXED";
    }

    private void sendEmail(String to, String subject, String body) {
        SMTPMailServer mailServer = mailServerManager.getDefaultSMTPMailServer();
        if (mailServer == null) {
            log.error("SMTP Mail Server não configurado!");
            return;
        }

        Email email = new Email(to);
        email.setFrom(mailServer.getDefaultFrom());
        email.setSubject(subject);
        email.setMimeType("text/html");
        email.setBody(body);
        SingleMailQueueItem item = new SingleMailQueueItem(email);
        ComponentAccessor.getMailQueue().addItem(item);
    }

    private Map<String, String> getTextsForUser(String userKey) {
        Locale locale = Locale.getDefault();
        ApplicationUser user = ComponentAccessor.getUserManager().getUserByKey(userKey);
        if (user != null) {
            String localePref = ComponentAccessor.getUserPreferencesManager()
                    .getExtendedPreferences(user)
                    .getText(PreferenceKeys.USER_LOCALE);
            if (localePref != null) {
                StringTokenizer tokenizer = new StringTokenizer(localePref, "_");
                String language = tokenizer.hasMoreTokens() ? tokenizer.nextToken() : null;
                String region = tokenizer.hasMoreTokens() ? tokenizer.nextToken() : null;
                if (language != null && region != null) {
                    locale = new Locale(language, region);
                }
            }
        }
        return i18nService.getTextsForLocale("email", locale);
    }
}
