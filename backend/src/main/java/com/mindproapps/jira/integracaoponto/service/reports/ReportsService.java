package com.mindproapps.jira.integracaoponto.service.reports;

import com.mindproapps.jira.integracaoponto.model.dto.period.TimesheetsPeriodDTO;
import com.mindproapps.jira.integracaoponto.model.dto.reports.HoursReportsResponseDTO;
import com.mindproapps.jira.integracaoponto.model.dto.reports.TeamDTO;
import com.mindproapps.jira.integracaoponto.model.dto.reports.AccountDTO;
import com.mindproapps.jira.integracaoponto.model.dto.reports.CategoryAccountDTO;
import com.mindproapps.jira.integracaoponto.model.dto.reports.TimesheetReportsResponseDTO;

import java.util.List;
//editar aqui
public interface ReportsService {
    TimesheetReportsResponseDTO getTimesheetsApproved(String startDate, String endDate, Integer teamId);
    TimesheetReportsResponseDTO getTimesheetsSubmitted(String startDate, String endDate, Integer teamId);
    HoursReportsResponseDTO getUndersubmittedHours(String startDate, String endDate, Integer teamId);
    HoursReportsResponseDTO getUnsubmittedHours(List<TimesheetsPeriodDTO> periods, Integer teamId);
    HoursReportsResponseDTO getAccountHours(String startDate, String endDate, Integer accountId, Integer categoryaccountId);
    List<TeamDTO> getAllTeams();
    List<AccountDTO> getAllAccounts();
    List<CategoryAccountDTO> getAllCategoryAccounts();
}
