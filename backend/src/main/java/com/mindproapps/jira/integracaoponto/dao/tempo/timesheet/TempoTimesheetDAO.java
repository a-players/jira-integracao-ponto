package com.mindproapps.jira.integracaoponto.dao.tempo.timesheet;

import com.mindproapps.jira.integracaoponto.conditions.ConditionsHelper;
import com.mindproapps.jira.integracaoponto.dao.base.BaseDAO;
import com.mindproapps.jira.integracaoponto.dao.tempo.team.TempoTeamDAO;
import com.mindproapps.jira.integracaoponto.model.dto.approval.TimesheetApprovalDTO;
import com.mindproapps.jira.integracaoponto.model.dto.approval.TimesheetsWaitingForApprovalDTO;
import com.mindproapps.jira.integracaoponto.util.LegacySQLProcessor;

import lombok.Cleanup;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Log4j
@Component
public class TempoTimesheetDAO extends BaseDAO {

    @Autowired
    private ConditionsHelper conditionsHelper;

    @Autowired
    private TempoTeamDAO tempoTeamDAO;

    private static final String SQL = "[SUA QUERY SQL COMPLETA AQUI]";

    public List<TimesheetsWaitingForApprovalDTO> getTimesheetsWaitingForApprovalList(String startDate, String endDate, String period, String userKey) {
        log.info("getTimesheetsWaitingForApprovalList: startDate=" + startDate + ", endDate=" + endDate + ", period=" + period + ", userKey=" + userKey);
        List<TimesheetsWaitingForApprovalDTO> list = new ArrayList<>();

        try (LegacySQLProcessor sqlProcessor = this.createSQLProcessor()){
            String stDate = startDate + " 00:00:00";
            String edDate = endDate + " 23:59:59";

            sqlProcessor.prepareStatement(SQL.replaceAll("%%START_DATE%%", stDate).replaceAll("%%END_DATE%%", edDate));
            sqlProcessor.setValue(period);
            sqlProcessor.setValue(period);

            ResultSet result = sqlProcessor.executeQuery();
            DecimalFormat df = new DecimalFormat("#.##");

            while (result.next()) {
                if (conditionsHelper.hasUserTempoTeamLeadOrViewTimesheetPermissions(result.getInt("TEAM_ID"))) {
                    double requiredTime = Double.parseDouble(df.format((result.getDouble("REQUIRED_TIME") < 0 ? tempoTeamDAO.userWorkLoad(result.getString("USER_KEY"), startDate, endDate) : result.getDouble("REQUIRED_TIME")) / 3600).replace(",", "."));
                    TimesheetsWaitingForApprovalDTO dto = TimesheetsWaitingForApprovalDTO.builder()
                            .status(result.getString("STATUS"))
                            .horasTempo(Double.parseDouble(df.format(result.getDouble("SUBMITTED_TIME") / 3600).replace(",", ".")))
                            .requiredTime(requiredTime)
                            .timeSheetApprovalId(result.getInt("ID"))
                            .username(result.getString("USER_NAME"))
                            .origin(result.getInt("ORIGIN"))
                            .build();

                    dto.setPeriod(period);
                    dto.setWorkerKey(result.getString("USER_KEY"));
                    dto.setEmail(result.getString("EMAIL"));
                    dto.setEmailPonto(result.getString("PONTO_EMAIL"));
                    dto.setEmailInformado(result.getString("INFORMED_EMAIL"));
                    dto.setJiraEmail(result.getString("JIRA_EMAIL"));
                    dto.setTeamId(result.getInt("TEAM_ID"));
                    dto.setTeam(result.getString("TEAM_NAME"));
                    dto.setCanApprove(conditionsHelper.hasUserTempoTeamLeadOrApproveTimesheetPermissions(result.getInt("TEAM_ID")));

                    list.add(dto);
                }
            }
        } catch (Exception e) {
            log.error("DAO: getTimesheetsWaitingForApprovalList error: " + e.getMessage(), e);
        }
        return list;
    }

    public TimesheetApprovalDTO getTimesheetApprovalById(Integer timeSheetApprovalId) {
        log.info("getTimesheetApprovalById: timeSheetApprovalId=" + timeSheetApprovalId);

        try (LegacySQLProcessor sqlProcessor = this.createSQLProcessor()){
            String sql = "SELECT * FROM \"AO_86ED1B_TIMESHEET_APPROVAL\" WHERE \"ID\" = ?";
            sqlProcessor.prepareStatement(sql);
            sqlProcessor.setValue(timeSheetApprovalId);

            ResultSet result = sqlProcessor.executeQuery();
            if (result.next()) {
                return TimesheetApprovalDTO.builder()
                        .dateFrom(result.getDate("DATE_FROM").toLocalDate())
                        .dateTo(result.getDate("DATE_TO").toLocalDate())
                        .period(result.getString("PERIOD"))
                        .periodType(result.getString("PERIOD_TYPE"))
                        .periodView(result.getString("PERIOD_VIEW"))
                        .requiredTime(result.getLong("REQUIRED_TIME"))
                        .reviewerKey(result.getString("REVIEWER_KEY"))
                        .submittedTime(result.getLong("SUBMITTED_TIME"))
                        .userKey(result.getString("USER_KEY"))
                        .build();
            }
        } catch (Exception e) {
            log.error("DAO: getTimesheetApprovalById error: " + e.getMessage(), e);
        }
        return null;
    }

    public Integer saveTimesheetApproval(TimesheetApprovalDTO dto) {
        log.info("saveTimesheetApproval: timesheetApprovalDTO=" + dto);
        Integer resultId = -1;
        try (LegacySQLProcessor sqlProcessor = this.createSQLProcessor()){
            String sql = "INSERT INTO public.\"AO_86ED1B_TIMESHEET_APPROVAL\"" +
                    "(\"ACTION\", \"ACTOR_KEY\", \"DATE_CREATED\", \"DATE_FROM\", \"DATE_TO\", \"PERIOD\", " +
                    "\"PERIOD_TYPE\", \"PERIOD_VIEW\", \"REASON\", \"REQUIRED_TIME\", \"REVIEWER_KEY\", " +
                    "\"STATUS\", \"SUBMITTED_TIME\", \"USER_KEY\") " +
                    "VALUES (?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            sqlProcessor.prepareStatement(sql);
            sqlProcessor.setValue(dto.getAction());
            sqlProcessor.setValue(dto.getActorKey());
            sqlProcessor.setValue(dto.getDateFrom());
            sqlProcessor.setValue(dto.getDateTo());
            sqlProcessor.setValue(dto.getPeriod());
            sqlProcessor.setValue(dto.getPeriodType());
            sqlProcessor.setValue(dto.getPeriodView());
            sqlProcessor.setValue(dto.getReason());
            sqlProcessor.setValue(dto.getRequiredTime());
            sqlProcessor.setValue(dto.getReviewerKey());
            sqlProcessor.setValue(dto.getStatus());
            sqlProcessor.setValue(dto.getSubmittedTime());
            sqlProcessor.setValue(dto.getUserKey());

            sqlProcessor.executeUpdate();

            ResultSet result = sqlProcessor.executeQuery("SELECT MAX(\"ID\") FROM \"AO_86ED1B_TIMESHEET_APPROVAL\"");
            if (result.next()) {
                resultId = result.getInt(1);
            }
        } catch (Exception e) {
            log.error("DAO: saveTimesheetApproval error: " + e.getMessage(), e);
        }
        return resultId;
    }
}
