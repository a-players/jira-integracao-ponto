package com.mindproapps.jira.integracaoponto.dao.reports;

import com.mindproapps.jira.integracaoponto.dao.base.BaseDAO;
import com.mindproapps.jira.integracaoponto.model.dto.reports.*;
import com.mindproapps.jira.integracaoponto.conditions.ConditionsHelper;
import com.mindproapps.jira.integracaoponto.util.ConversionUtils;
import com.mindproapps.jira.integracaoponto.util.LegacySQLProcessor;

import lombok.extern.log4j.Log4j;

import javax.inject.Inject;
import javax.inject.Named;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Named
@Log4j
public class ReportsDAO extends BaseDAO {

    private final ConditionsHelper conditionsHelper;

    @Inject
    public ReportsDAO(ConditionsHelper conditionsHelper) {
        this.conditionsHelper = conditionsHelper;
    }

    private static final String SQL_REPORT_APPROVED_TIMESHEETS =
                    "SELECT * FROM (" +
                        "SELECT DISTINCT ON(ts.\"ID\") ts.\"ID\", ts.\"DATE_FROM\", ts.\"DATE_TO\", ts.\"DATE_CREATED\", ts.\"ACTION\", " +
                        "ui.\"DISPLAY_NAME\" AS \"USUARIO\", ui.\"USER_KEY\", ui.\"TEAM_ID\", ui.\"TEAM_NAME\", ui.\"EMAIL\", tr.\"ORIGIN\", " +
                        "uia.\"DISPLAY_NAME\" AS \"APPROVER\", ts.\"SUBMITTED_TIME\", " +
                        "acdpu .\"JIRA_EMAIL\", acdpu .\"INFORMED_EMAIL\", acdpu .\"PONTO_EMAIL\", ts.\"PERIOD\", ts.\"REASON\" " +
                        "FROM (" +
                            "SELECT ta.\"DATE_CREATED\", ta.\"DATE_FROM\", ta.\"DATE_TO\", ta.\"ACTION\", ta.\"USER_KEY\", " +
                                "ta.\"ID\" , ta.\"REVIEWER_KEY\", ta.\"SUBMITTED_TIME\", ta.\"PERIOD\", ta.\"REASON\" " +
                            "FROM \"AO_86ED1B_TIMESHEET_APPROVAL\" ta " +
                            "WHERE ta .\"ACTION\"  = 'approve' " +
                            "AND ta.\"ID\" = (" +
                                "SELECT max(\"ID\") " +
                                "FROM \"AO_86ED1B_TIMESHEET_APPROVAL\" ta2 " +
                                "WHERE ta2.\"PERIOD\" = ta .\"PERIOD\" " +
                                "AND ta2.\"USER_KEY\" = ta.\"USER_KEY\" AND ta2.\"REASON\" = ta.\"REASON\"" +
                            ") " +
                            "AND ta.\"DATE_FROM\" BETWEEN TO_TIMESTAMP(?, 'YYYY-MM-DD') AND TO_TIMESTAMP(?, 'YYYY-MM-DD') " +
                        ") AS ts " +
                        "INNER JOIN \"AO_AEFED0_USER_INDEX\" ui ON ui.\"USER_KEY\" = ts.\"USER_KEY\" AND NOT ui.\"TEAM_ID\" IS NULL " +
                        "JOIN cwd_user cu ON cu.\"user_name\" = ui.\"USER_NAME\" \n" +
                        "JOIN cwd_directory cd ON (cu.directory_id = cd.id) \n" +
                        "INNER JOIN \"AO_AEFED0_USER_INDEX\" uia ON uia .\"USER_KEY\" = ts.\"REVIEWER_KEY\" " +
                        "LEFT JOIN \"AO_441C88_TSAPPR_ORIGIN_TRACE\" tr ON tr.\"TIMESHEET_APPROVAL_ID\" = ts.\"ID\" " +
                        "LEFT JOIN \"AO_441C88_DE_PARA_USER\" acdpu ON acdpu .\"USER_KEY\" = ts.\"USER_KEY\" " +
                        "WHERE cd.lower_directory_name = 'linx ad v.01' \n" +
                        "AND ui.\"EMAIL\" NOT LIKE '%@terceiroslinx.com.br' \n" +
                        "AND ui.\"EMAIL\" NOT LIKE '%@franqueadolinx.com.br' \n" +
                        "AND ui.\"EMAIL\" NOT LIKE '%@parceiroslinx.com.br' \n" +
                        "ORDER BY ts.\"ID\"" +
                    ") AS sq ORDER BY sq.\"DATE_FROM\", sq.\"USUARIO\"";
    private static final String SQL_REPORT_SUBMITTED_TIMESHEETS =
                    "SELECT * FROM (" +
                        "SELECT DISTINCT ON(ts.\"ID\") ts.\"ID\", ts.\"DATE_FROM\", ts.\"DATE_TO\", ts.\"DATE_CREATED\", ts.\"ACTION\", " +
                        "ui.\"DISPLAY_NAME\" AS \"USUARIO\", ui.\"USER_KEY\", ui.\"TEAM_ID\", ui.\"TEAM_NAME\", ui.\"EMAIL\", tr.\"ORIGIN\", " +
                        "uia.\"DISPLAY_NAME\" AS \"APPROVER\", ts.\"SUBMITTED_TIME\", " +
                        "acdpu .\"JIRA_EMAIL\", acdpu .\"INFORMED_EMAIL\", acdpu .\"PONTO_EMAIL\", ts.\"PERIOD\", ts.\"REASON\" " +
                        "FROM (" +
                            "SELECT ta.\"DATE_CREATED\", ta.\"DATE_FROM\", ta.\"DATE_TO\", ta.\"ACTION\", ta.\"USER_KEY\", " +
                            "ta.\"ID\" , ta.\"REVIEWER_KEY\", ta.\"SUBMITTED_TIME\", ta.\"PERIOD\", ta.\"REASON\" " +
                            "FROM \"AO_86ED1B_TIMESHEET_APPROVAL\" ta " +
                            "WHERE ta .\"ACTION\"  = 'submit' " +
                            "AND ta.\"ID\" = (" +
                                "SELECT max(\"ID\") " +
                                "FROM \"AO_86ED1B_TIMESHEET_APPROVAL\" ta2 " +
                                "WHERE ta2.\"PERIOD\" = ta .\"PERIOD\" " +
                                "AND ta2.\"USER_KEY\" = ta.\"USER_KEY\" AND ta2.\"REASON\" = ta.\"REASON\"" +
                            ") " +
                            "AND ta.\"DATE_FROM\" BETWEEN TO_TIMESTAMP(?, 'YYYY-MM-DD') AND TO_TIMESTAMP(?, 'YYYY-MM-DD') " +
                        ") AS ts " +
                        "INNER JOIN \"AO_AEFED0_USER_INDEX\" ui ON ui.\"USER_KEY\" = ts.\"USER_KEY\" AND NOT ui.\"TEAM_ID\" IS NULL " +
                        "JOIN cwd_user cu ON cu.\"user_name\" = ui.\"USER_NAME\" \n" +
                        "JOIN cwd_directory cd ON (cu.directory_id = cd.id) \n" +
                        "INNER JOIN \"AO_AEFED0_USER_INDEX\" uia ON uia .\"USER_KEY\" = ts.\"REVIEWER_KEY\" " +
                        "LEFT JOIN \"AO_441C88_TSAPPR_ORIGIN_TRACE\" tr ON tr.\"TIMESHEET_APPROVAL_ID\" = ts.\"ID\" " +
                        "LEFT JOIN \"AO_441C88_DE_PARA_USER\" acdpu ON acdpu .\"USER_KEY\" = ts.\"USER_KEY\" " +
                        "WHERE cd.lower_directory_name = 'linx ad v.01' \n" +
                        "AND ui.\"EMAIL\" NOT LIKE '%@terceiroslinx.com.br' \n" +
                        "AND ui.\"EMAIL\" NOT LIKE '%@franqueadolinx.com.br' \n" +
                        "AND ui.\"EMAIL\" NOT LIKE '%@parceiroslinx.com.br' \n" +
                        "ORDER BY ts.\"ID\"" +
                    ") AS sq ORDER BY sq.\"DATE_FROM\", sq.\"USUARIO\"";

    private static final String SQL_UNSUBMITTEDHOURS =
            "SELECT * FROM (" +
                "SELECT DISTINCT ON(mui.\"USER_KEY\") mui.\"USER_KEY\" ,mui.\"DISPLAY_NAME\" , " +
                        "COALESCE(t.\"TOTAL_HOURS\", 0) AS \"TOTAL_HOURS\", mui .\"DISPLAY_NAME\" AS \"USUARIO\", mui.\"TEAM_ID\", " +
                        "mui.\"TEAM_NAME\", aui.\"DISPLAY_NAME\" AS \"LEAD\", mui.\"EMAIL\", " +
                        "acdpu .\"JIRA_EMAIL\", acdpu .\"INFORMED_EMAIL\", acdpu .\"PONTO_EMAIL\" " +
                "FROM \"AO_AEFED0_USER_INDEX\" mui " +
                    "JOIN cwd_user cu ON cu.\"user_name\" = mui.\"USER_NAME\" \n" +
                    "JOIN cwd_directory cd ON (cu.directory_id = cd.id) \n" +
                    "LEFT JOIN (" +
                            "SELECT SUM(wl.timeworked) AS \"TOTAL_HOURS\", wl.author " +
                            "FROM worklog wl " +
                            "WHERE wl.startdate BETWEEN TO_TIMESTAMP('%%START_DATE%%', 'YYYY-MM-DD HH24:MI:SS') AND TO_TIMESTAMP('%%END_DATE%%', 'YYYY-MM-DD HH24:MI:SS') " +
                            "AND NOT EXISTS(" +
                                "SELECT 1 " +
                                "FROM \"AO_86ED1B_TIMESHEET_APPROVAL\" aebta " +
                                "WHERE aebta .\"USER_KEY\" = wl.author " +
                                "AND aebta .\"DATE_FROM\" = '%%START_DATE%%' " +
                                "AND (aebta .\"STATUS\" = 'review' OR aebta .\"STATUS\" = 'approved') " +
                                "AND aebta .\"ID\" = (" +
                                    "SELECT MAX(\"ID\") " +
                                    "FROM \"AO_86ED1B_TIMESHEET_APPROVAL\" aebta2 " +
                                    "WHERE aebta2.\"USER_KEY\" = aebta .\"USER_KEY\" " +
                                    "AND aebta2 .\"DATE_FROM\" = aebta .\"DATE_FROM\"" +
                                ")" +
                            ")" +
                            "GROUP BY(wl.author)" +
                        ") AS t " +
                        "ON t.author = mui.\"USER_KEY\" " +
                    "INNER JOIN \"AO_AEFED0_TEAM_V2\" aatv " +
                        "ON aatv .\"ID\" = mui.\"TEAM_ID\" " +
                    "INNER JOIN \"AO_AEFED0_USER_INDEX\" aui " +
                        "ON aui .\"USER_KEY\" = aatv.\"LEAD\" " +
                    "LEFT JOIN \"AO_441C88_DE_PARA_USER\" acdpu " +
                        "ON acdpu .\"USER_KEY\" = mui.\"USER_KEY\" " +
                "WHERE cd.lower_directory_name = 'linx ad v.01' \n" +
                "AND mui.\"EMAIL\" NOT LIKE '%@terceiroslinx.com.br' \n" +
                "AND mui.\"EMAIL\" NOT LIKE '%@franqueadolinx.com.br' \n" +
                "AND mui.\"EMAIL\" NOT LIKE '%@parceiroslinx.com.br' \n" +
                "AND NOT EXISTS(" +
                    "SELECT 1 " +
                    "FROM \"AO_86ED1B_TIMESHEET_APPROVAL\" aebta3 " +
                    "WHERE aebta3 .\"USER_KEY\" = mui.\"USER_KEY\" " +
                    "AND aebta3 .\"DATE_FROM\" = '%%START_DATE%%' " +
                    "AND (aebta3 .\"STATUS\" = 'review' OR aebta3 .\"STATUS\" = 'approved') " +
                    "AND aebta3 .\"ID\" = (" +
                        "SELECT MAX(\"ID\") " +
                        "FROM \"AO_86ED1B_TIMESHEET_APPROVAL\" aebta4 " +
                        "WHERE aebta4.\"USER_KEY\" = aebta3 .\"USER_KEY\" " +
                        "AND aebta4 .\"DATE_FROM\" = aebta3 .\"DATE_FROM\"" +
                    ")" +
                ")" +
            ") AS sq ORDER BY sq.\"USUARIO\"";

    private static final String SQL_UNDERSUBMITTED_HOURS =
            "SELECT * FROM (" +
                "SELECT DISTINCT ON(aebta .\"ID\") aebta .\"ID\", aebta .\"DATE_FROM\", aebta .\"DATE_TO\", aebta .\"DATE_CREATED\", " +
                       "ui.\"DISPLAY_NAME\" AS \"USUARIO\", ui.\"TEAM_ID\", ui.\"TEAM_NAME\", uia.\"DISPLAY_NAME\" AS \"APPROVER\", " +
                        "aebta .\"SUBMITTED_TIME\" , aebta .\"REQUIRED_TIME\", ui.\"EMAIL\", aebta.\"USER_KEY\", " +
                       "acdpu .\"JIRA_EMAIL\", acdpu .\"INFORMED_EMAIL\", acdpu .\"PONTO_EMAIL\", aebta.\"PERIOD\", aebta.\"REASON\" " +
                "FROM \"AO_86ED1B_TIMESHEET_APPROVAL\" aebta " +
                "INNER JOIN \"AO_AEFED0_USER_INDEX\" ui " +
                        "ON ui.\"USER_KEY\" = aebta.\"USER_KEY\" AND NOT ui.\"TEAM_ID\" IS NULL " +
                "JOIN cwd_user cu ON cu.\"user_name\" = ui.\"USER_NAME\" \n" +
                "JOIN cwd_directory cd ON (cu.directory_id = cd.id) \n" +
                "INNER JOIN \"AO_AEFED0_USER_INDEX\" uia ON uia .\"USER_KEY\" = aebta.\"REVIEWER_KEY\" " +
                "LEFT JOIN \"AO_441C88_DE_PARA_USER\" acdpu ON acdpu .\"USER_KEY\" = aebta.\"USER_KEY\" " +
                "WHERE cd.lower_directory_name = 'linx ad v.01' \n" +
                "AND ui.\"EMAIL\" NOT LIKE '%@terceiroslinx.com.br' \n" +
                "AND ui.\"EMAIL\" NOT LIKE '%@franqueadolinx.com.br' \n" +
                "AND ui.\"EMAIL\" NOT LIKE '%@parceiroslinx.com.br' \n" +
                "AND aebta .\"REQUIRED_TIME\" * 0.6 > aebta .\"SUBMITTED_TIME\" " +
                "AND aebta .\"DATE_FROM\" BETWEEN TO_TIMESTAMP(?, 'YYYY-MM-DD') AND TO_TIMESTAMP(?, 'YYYY-MM-DD') " +
                "AND aebta.\"STATUS\" = 'review' " +
                "AND aebta .\"ID\" = (" +
                        "SELECT MAX(\"ID\") " +
                        "FROM \"AO_86ED1B_TIMESHEET_APPROVAL\" aebta2 " +
                        "WHERE aebta2.\"USER_KEY\" = aebta.\"USER_KEY\" " +
                        "AND aebta2.\"PERIOD\" = aebta.\"PERIOD\")" +
                "ORDER BY aebta.\"ID\"" +
            ") AS sq ORDER BY sq.\"DATE_FROM\", sq.\"USUARIO\"";

    private static final String SQL_ACCOUNT_HOURS =
    "SELECT * FROM (" +
                "SELECT DISTINCT ON(account.\"ID\") account.\"ID\", account.\"KEY\" as \"KEY_ACCOUNT\", account.\"LEAD\", account.\"NAME\" as \"ACCOUNT_NAME\", account.\"STATUS\", "+
                "account.\"CATEGORY_ID\", ui.\"DISPLAY_NAME\" AS \"USUARIO\", category.\"CATEGORY_TYPE_ID\", category.\"NAME\" as \"CATEGORY_NAME\", category.\"ID\", "+
                "uia.\"DISPLAY_NAME\" AS \"LEADER\", ui.\"EMAIL\", acdpu .\"JIRA_EMAIL\", uia.\"TEAM_ID\", acdpu .\"INFORMED_EMAIL\", ui.\"USER_KEY\", acdpu .\"PONTO_EMAIL\", round(sum(worklog.timeworked)/3600,2)Horas "+
                "FROM \"AO_C3C6E8_ACCOUNT_V1\" account "+
                "INNER JOIN \"AO_AEFED0_USER_INDEX\" ui "+
                "ON ui.\"USER_KEY\" = account.\"LEAD\" "+
                "INNER JOIN \"AO_C3C6E8_CATEGORY_V1\" category ON account.\"CATEGORY_ID\" = category.\"ID\" "+
                "JOIN cwd_user cu ON cu.\"user_name\" = ui.\"USER_NAME\" "+
                "JOIN cwd_directory cd ON (cu.directory_id = cd.id) "+
                "INNER JOIN \"AO_AEFED0_USER_INDEX\" uia ON uia .\"USER_KEY\" = account.\"LEAD\" "+
                "LEFT JOIN \"AO_441C88_DE_PARA_USER\" acdpu ON acdpu .\"USER_KEY\" = account.\"LEAD\" "+
                "left join public.\"AO_013613_WA_VALUE\" value on value.\"VALUE\" = account.\"KEY\" "+
                "left join public.\"jiraissue\" issue on value.\"WORKLOG_ID\" = issue.id "+
                "left join public.\"worklog\" worklog on value.\"WORKLOG_ID\" = worklog.issueid "+
                "WHERE cd.lower_directory_name = 'linx ad v.01' "+
                "AND ui.\"EMAIL\" NOT LIKE '%@terceiroslinx.com.br' "+
                "AND ui.\"EMAIL\" NOT LIKE '%@franqueadolinx.com.br' "+
                "AND ui.\"EMAIL\" NOT LIKE '%@parceiroslinx.com.br' "+
                "group by account.\"ID\", "+
                "account.\"KEY\", "+
                "account.\"LEAD\", "+
                "account.\"NAME\", "+
                "account.\"STATUS\", "+
                "account.\"CATEGORY_ID\", "+
                "ui.\"DISPLAY_NAME\", "+
                "category.\"CATEGORY_TYPE_ID\", "+
                "category.\"NAME\", "+
                "category.\"ID\", "+
                "uia.\"DISPLAY_NAME\", "+
                "uia.\"TEAM_ID\", "+
                "ui.\"EMAIL\", "+
                "ui.\"USER_KEY\", "+
                "acdpu .\"JIRA_EMAIL\", "+
                "acdpu .\"INFORMED_EMAIL\", "+
                "acdpu .\"PONTO_EMAIL\" "+
                "ORDER BY account.\"ID\" "+
                ") AS sq ORDER by sq.\"ACCOUNT_NAME\" ";

    private static final String SQL_ALL_TEAMS =
            "SELECT DISTINCT \"TEAM_ID\", \"TEAM_NAME\" " +
            "FROM \"AO_AEFED0_USER_INDEX\" " +
            "WHERE NOT \"TEAM_ID\" IS NULL " +
            "AND \"TEAM_ID\" != 0 " +
            "ORDER BY \"TEAM_NAME\"";

    private static final String SQL_ALL_ACCOUNTS =
            "SELECT DISTINCT \"ID\", \"NAME\" " +
            "FROM \"AO_C3C6E8_ACCOUNT_V1\" " +
            "WHERE NOT \"ID\" IS NULL " +
            "AND \"ID\" != 0 " +
            "ORDER BY \"NAME\"";

    private static final String SQL_ALL_CATEGORY_ACCOUNTS =
            "SELECT DISTINCT \"CATEGORY_TYPE_ID\", \"NAME\" " +
            "FROM \"AO_C3C6E8_CATEGORY_V1\" " +
            "WHERE NOT \"CATEGORY_TYPE_ID\" IS NULL " +
            "AND \"CATEGORY_TYPE_ID\" != 0 " +
            "ORDER BY \"NAME\"";

    public List<TimesheetReportsDTO> getTimesheetsApproved(String startDate, String endDate) {
        log.info("getTimesheetsApproved: startDate = " + startDate + ", endDate = " + endDate);
        List<TimesheetReportsDTO> list = new ArrayList<>();
        try (LegacySQLProcessor sqlProcessor = this.createSQLProcessor()){
            sqlProcessor.prepareStatement(SQL_REPORT_APPROVED_TIMESHEETS);
            sqlProcessor.setValue(startDate);
            sqlProcessor.setValue(endDate);
            ResultSet result = sqlProcessor.executeQuery();
            DecimalFormat df = new DecimalFormat("#.##");
            while (result.next()) {
                if (conditionsHelper.hasUserTempoTeamLeadOrViewTimesheetPermissions(result.getInt("TEAM_ID")) || conditionsHelper.hasUserTempoAdminPermissions()) {
                TimesheetReportsDTO dto = TimesheetReportsDTO.builder()
                        .id(result.getInt("ID"))
                        .approver(result.getString("APPROVER"))
                        .origin(result.getString("ORIGIN"))
                        .status(result.getString("ACTION"))
                        .team(result.getString("TEAM_NAME"))
                        .teamId(result.getInt("TEAM_ID"))
                        .account(result.getString("TEAM_NAME"))
                        .accountId(result.getInt("TEAM_ID"))
                        .categoryaccount(result.getString("TEAM_NAME"))
                        .categoryaccountId(result.getInt("TEAM_ID"))
                        .reason(result.getString("REASON"))
                        .usuario(result.getString("USUARIO"))
                        .actionDate(ConversionUtils.formatISODateTimeAsRegularDateTime(
                                result.getString("DATE_CREATED").replace(" ", "T"))
                        )
                        .hoursTempo(Double.valueOf(df.format(result.getDouble("SUBMITTED_TIME")/3600).replaceAll(",", ".")))
                        .build();
                dto.setPeriod(result.getString("PERIOD"));
                dto.setPeriodStartDate(ConversionUtils.formatISODateAsRegularDate(result.getDate("DATE_FROM").toLocalDate().format(
                    DateTimeFormatter.ISO_DATE)));
                dto.setPeriodEndDate(ConversionUtils.formatISODateAsRegularDate(result.getDate("DATE_TO").toLocalDate().minusDays(1).format(
                    DateTimeFormatter.ISO_DATE)));
                dto.setPeriodStartDateISO(result.getDate("DATE_FROM").toLocalDate().format(
                    DateTimeFormatter.ISO_DATE));
                dto.setPeriodEndDateISO(result.getDate("DATE_TO").toLocalDate().minusDays(1).format(
                    DateTimeFormatter.ISO_DATE));
                dto.setEmail(result.getString("EMAIL"));
                dto.setEmailPonto(result.getString("PONTO_EMAIL"));
                dto.setEmailInformado(result.getString("INFORMED_EMAIL"));
                dto.setJiraEmail(result.getString("JIRA_EMAIL"));
                dto.setWorkerKey(result.getString("USER_KEY"));
                list.add(dto);
                }
            }
        } catch (Exception e) {
            log.error("DAO: getTimesheetsApproved error: " + e.getMessage(), e);
        }
        return list;
    }

    public List<TimesheetReportsDTO> getTimesheetsSubmitted(String startDate, String endDate) {
        log.info("getTimesheetsSubmitted: startDate = " + startDate + ", endDate = " + endDate);
        List<TimesheetReportsDTO> list = new ArrayList<>();
        try (LegacySQLProcessor sqlProcessor = this.createSQLProcessor()){
            sqlProcessor.prepareStatement(SQL_REPORT_SUBMITTED_TIMESHEETS);
            sqlProcessor.setValue(startDate);
            sqlProcessor.setValue(endDate);
            ResultSet result = sqlProcessor.executeQuery();
            DecimalFormat df = new DecimalFormat("#.##");
            while (result.next()) {
                if (conditionsHelper.hasUserTempoTeamLeadOrViewTimesheetPermissions(result.getInt("TEAM_ID")) || conditionsHelper.hasUserTempoAdminPermissions()) {
                TimesheetReportsDTO dto = TimesheetReportsDTO.builder()
                        .id(result.getInt("ID"))
                        .approver(result.getString("APPROVER"))
                        .origin(result.getString("ORIGIN"))
                        .status(result.getString("ACTION"))
                        .team(result.getString("TEAM_NAME"))
                        .teamId(result.getInt("TEAM_ID"))
                        .account(result.getString("TEAM_NAME"))
                        .accountId(result.getInt("TEAM_ID"))
                        .categoryaccount(result.getString("TEAM_NAME"))
                        .categoryaccountId(result.getInt("TEAM_ID"))
                        .usuario(result.getString("USUARIO"))
                        .reason(result.getString("REASON"))
                        .actionDate(ConversionUtils.formatISODateTimeAsRegularDateTime(
                                result.getString("DATE_CREATED").replace(" ", "T"))
                        )
                        .hoursTempo(Double.valueOf(df.format(result.getDouble("SUBMITTED_TIME")/3600).replaceAll(",", ".")))
                        .build();
                dto.setPeriod(result.getString("PERIOD"));
                dto.setPeriodStartDate(ConversionUtils.formatISODateAsRegularDate(result.getDate("DATE_FROM").toLocalDate().format(
                    DateTimeFormatter.ISO_DATE)));
                dto.setPeriodEndDate(ConversionUtils.formatISODateAsRegularDate(result.getDate("DATE_TO").toLocalDate().minusDays(1).format(
                    DateTimeFormatter.ISO_DATE)));
                dto.setPeriodStartDateISO(result.getDate("DATE_FROM").toLocalDate().format(
                    DateTimeFormatter.ISO_DATE));
                dto.setPeriodEndDateISO(result.getDate("DATE_TO").toLocalDate().minusDays(1).format(
                    DateTimeFormatter.ISO_DATE));
                dto.setEmail(result.getString("EMAIL"));
                dto.setEmailPonto(result.getString("PONTO_EMAIL"));
                dto.setEmailInformado(result.getString("INFORMED_EMAIL"));
                dto.setJiraEmail(result.getString("JIRA_EMAIL"));
                dto.setWorkerKey(result.getString("USER_KEY"));
                list.add(dto);
                }
            }
        } catch (Exception e) {
            log.error("DAO: getTimesheetsSubmitted error: " + e.getMessage(), e);
        }
        return list;
    }

    public List<UnsubmittedHoursDTO> getUnsubmittedHours(String periodStartDate, String periodEndDate) {
        log.info("getUnsubmittedHours: periodStartDate = " + periodStartDate + ", periodEndDate = " + periodEndDate);
        List<UnsubmittedHoursDTO> list = new ArrayList<>();
        try (LegacySQLProcessor sqlProcessor = this.createSQLProcessor()){
            String startDate = periodStartDate + " 00:00:00";
            String endDate = periodEndDate + " 23:59:59";
            String sql = SQL_UNSUBMITTEDHOURS.replaceAll("%%START_DATE%%", startDate).replaceAll("%%END_DATE%%", endDate);
            sqlProcessor.prepareStatement(sql);
            ResultSet result = sqlProcessor.executeQuery();
            DecimalFormat df = new DecimalFormat("#.##");
            while (result.next()) {
                if (conditionsHelper.hasUserTempoTeamLeadOrViewTimesheetPermissions(result.getInt("TEAM_ID")) || conditionsHelper.hasUserTempoAdminPermissions()) {
                UnsubmittedHoursDTO dto = UnsubmittedHoursDTO.builder()
                        .approver(result.getString("LEAD"))
                        .team(result.getString("TEAM_NAME"))
                        .teamId(result.getInt("TEAM_ID"))
                        .totalHours(Double.valueOf(df.format(result.getDouble("TOTAL_HOURS")/3600).replaceAll(",", ".")))
                        .usuario(result.getString("USUARIO"))
                        .build();
                dto.setPeriod(periodStartDate.split("-")[1] + periodStartDate.split("-")[0].substring(2));
                dto.setPeriodStartDate(ConversionUtils.formatISODateAsRegularDate(periodStartDate));
                dto.setPeriodEndDate(ConversionUtils.formatISODateAsRegularDate(periodEndDate));
                dto.setPeriodStartDateISO(periodStartDate);
                dto.setPeriodEndDateISO(periodEndDate);
                dto.setEmail(result.getString("EMAIL"));
                dto.setEmailPonto(result.getString("PONTO_EMAIL"));
                dto.setEmailInformado(result.getString("INFORMED_EMAIL"));
                dto.setJiraEmail(result.getString("JIRA_EMAIL"));
                dto.setPeriodStartDateISO(periodStartDate);
                dto.setPeriodEndDateISO(periodEndDate);
                dto.setWorkerKey(result.getString("USER_KEY"));
                if(dto != null) {
                    list.add(dto);
                }
            }
        }
        } catch (Exception e) {
            log.error("DAO: getUnsubmittedHours error: " + e.getMessage(), e);
        }
        return list;
    }

    public List<PartialTimesheetReportsDTO> getTimesheetsSubmittedPartialHours(String startDate, String endDate) {
        log.info("getTimesheetsSubmittedPartialHours: startDate = " + startDate + ", endDate = " + endDate);
        List<PartialTimesheetReportsDTO> list = new ArrayList<>();
        try (LegacySQLProcessor sqlProcessor = this.createSQLProcessor()){
            sqlProcessor.prepareStatement(SQL_UNDERSUBMITTED_HOURS);
            sqlProcessor.setValue(startDate);
            sqlProcessor.setValue(endDate);
            ResultSet result = sqlProcessor.executeQuery();
            DecimalFormat df = new DecimalFormat("#.##");
            while (result.next()) {
                if (conditionsHelper.hasUserTempoTeamLeadOrViewTimesheetPermissions(result.getInt("TEAM_ID")) || conditionsHelper.hasUserTempoAdminPermissions()) {
                PartialTimesheetReportsDTO dto = PartialTimesheetReportsDTO.builder()
                                .id(result.getInt("ID"))
                                .approver(result.getString("APPROVER"))
                                .team(result.getString("TEAM_NAME"))
                                .teamId(result.getInt("TEAM_ID"))
                                .account(result.getString("TEAM_NAME"))
                                .accountId(result.getInt("TEAM_ID"))
                                .categoryaccount(result.getString("TEAM_NAME"))
                                .categoryaccountId(result.getInt("TEAM_ID"))
                                .usuario(result.getString("USUARIO"))
                                .reason(result.getString("REASON"))
                                .actionDate(ConversionUtils.formatISODateTimeAsRegularDateTime(
                                        result.getString("DATE_CREATED").replace(" ", "T"))
                                )
                                .requiredHours(Double.valueOf(df.format(result.getDouble("REQUIRED_TIME")/3600).replaceAll(",", ".")))
                                .submittedHours(Double.valueOf(df.format(result.getDouble("SUBMITTED_TIME")/3600).replaceAll(",", ".")))
                                .build();
                dto.setPeriod(result.getString("PERIOD"));
                dto.setPeriodStartDate(ConversionUtils.formatISODateAsRegularDate(result.getDate("DATE_FROM").toLocalDate().format(
                    DateTimeFormatter.ISO_DATE)));
                dto.setPeriodEndDate(ConversionUtils.formatISODateAsRegularDate(result.getDate("DATE_TO").toLocalDate().minusDays(1).format(
                    DateTimeFormatter.ISO_DATE)));
                dto.setPeriodStartDateISO(result.getDate("DATE_FROM").toLocalDate().format(
                    DateTimeFormatter.ISO_DATE));
                dto.setPeriodEndDateISO(result.getDate("DATE_TO").toLocalDate().minusDays(1).format(
                    DateTimeFormatter.ISO_DATE));
                dto.setEmail(result.getString("EMAIL"));
                dto.setEmailPonto(result.getString("PONTO_EMAIL"));
                dto.setEmailInformado(result.getString("INFORMED_EMAIL"));
                dto.setJiraEmail(result.getString("JIRA_EMAIL"));
                dto.setWorkerKey(result.getString("USER_KEY"));
                list.add(dto);
            }
        }
        } catch (Exception e) {
            log.error("DAO: getTimesheetsSubmittedPartialHours error: " + e.getMessage(), e);
        }
        return list;
    }

    public List<AccountTimesheetReportsDTO> getAccountHours(String startDate, String endDate) {
        log.error("SQL do ACCOUNT executando");
        List<AccountTimesheetReportsDTO> list = new ArrayList<>();
        try (LegacySQLProcessor sqlProcessor = this.createSQLProcessor()){
            sqlProcessor.prepareStatement(SQL_ACCOUNT_HOURS);
            //sqlProcessor.setValue(startDate);
            //sqlProcessor.setValue(endDate);
            ResultSet result = sqlProcessor.executeQuery();
            log.error("Resultado SQL Account: "+result);
            DecimalFormat df = new DecimalFormat("#.##");
            while (result.next()) {
                log.error("Algo passou no SQL do Account");
                if (conditionsHelper.hasUserTempoTeamLeadOrViewTimesheetPermissions(result.getInt("TEAM_ID")) || conditionsHelper.hasUserTempoAdminPermissions()) {
                AccountTimesheetReportsDTO dto = AccountTimesheetReportsDTO.builder()
                        .id(result.getInt("ID"))
                        .leader(result.getString("USUARIO"))
                        .status(result.getString("STATUS"))
                        .account(result.getString("ACCOUNT_NAME"))
                        .accountId(result.getInt("ID"))
                        .keyaccount(result.getString("KEY_ACCOUNT"))
                        .categoryaccount(result.getString("CATEGORY_NAME"))
                        .categoryaccountId(result.getInt("CATEGORY_TYPE_ID"))
                        .usuario(result.getString("USUARIO"))
                        .totalHours(result.getInt("Horas"))
                        .build();
                //dto.setPeriod(result.getString("PERIOD"));
                //dto.setPeriodStartDate(ConversionUtils.formatISODateAsRegularDate(result.getDate("DATE_FROM").toLocalDate().format(
                //    DateTimeFormatter.ISO_DATE)));
                //dto.setPeriodEndDate(ConversionUtils.formatISODateAsRegularDate(result.getDate("DATE_TO").toLocalDate().minusDays(1).format(
                //    DateTimeFormatter.ISO_DATE)));
                //dto.setPeriodStartDateISO(result.getDate("DATE_FROM").toLocalDate().format(
                //    DateTimeFormatter.ISO_DATE));
                //dto.setPeriodEndDateISO(result.getDate("DATE_TO").toLocalDate().minusDays(1).format(
                //    DateTimeFormatter.ISO_DATE));
                dto.setEmail(result.getString("EMAIL"));
                dto.setEmailPonto(result.getString("PONTO_EMAIL"));
                dto.setEmailInformado(result.getString("INFORMED_EMAIL"));
                dto.setJiraEmail(result.getString("JIRA_EMAIL"));
                dto.setWorkerKey(result.getString("USER_KEY"));
                list.add(dto);
                log.error("Executou SQL: "+list);
            }
        }
        } catch (Exception e) {
            log.error("DAO: getUnsubmittedHours error: " + e.getMessage(), e);
        }
        return list;
    }

    public List<TeamDTO> getAllTeams() {
        List<TeamDTO> list = new ArrayList<>();
        try (LegacySQLProcessor sqlProcessor = this.createSQLProcessor()){
            sqlProcessor.prepareStatement(SQL_ALL_TEAMS);
            ResultSet result = sqlProcessor.executeQuery();
            while (result.next()) {
                if (conditionsHelper.hasUserTempoTeamLeadOrViewTimesheetPermissions(result.getInt("TEAM_ID")) || conditionsHelper.hasUserTempoAdminPermissions()) {
                TeamDTO dto = TeamDTO.builder()
                        .teamId(result.getInt("TEAM_ID"))
                        .teamName(result.getString("TEAM_NAME"))
                        .build();
                list.add(dto);
                }
            }
        } catch (Exception e) {
            log.error("DAO: getAllTeams error: " + e.getMessage(), e);
        }
        return list;
    }

    public List<AccountDTO> getAllAccounts() {
        List<AccountDTO> list = new ArrayList<>();
        try (LegacySQLProcessor sqlProcessor = this.createSQLProcessor()){
            sqlProcessor.prepareStatement(SQL_ALL_ACCOUNTS);
            ResultSet result = sqlProcessor.executeQuery();
            while (result.next()) {
                AccountDTO dto = AccountDTO.builder()
                        .accountId(result.getInt("ID"))
                        .accountName(result.getString("NAME"))
                        .build();
                list.add(dto);
            }
        } catch (Exception e) {
            log.error("DAO: getAllAccounts error: " + e.getMessage(), e);
        }
        return list;
    }

    public List<CategoryAccountDTO> getAllCategoryAccounts() {
        List<CategoryAccountDTO> list = new ArrayList<>();
        try (LegacySQLProcessor sqlProcessor = this.createSQLProcessor()){
            sqlProcessor.prepareStatement(SQL_ALL_CATEGORY_ACCOUNTS);
            ResultSet result = sqlProcessor.executeQuery();
            while (result.next()) {
                CategoryAccountDTO dto = CategoryAccountDTO.builder()
                        .categoryaccountId(result.getInt("CATEGORY_TYPE_ID"))
                        .categoryaccountName(result.getString("NAME"))
                        .build();
                list.add(dto);

            }
        } catch (Exception e) {
            log.error("DAO: getAllAccountsCategory error: " + e.getMessage(), e);
        }
        return list;
    }

}


