package com.mindproapps.jira.integracaoponto.service.approval;

import com.mindproapps.jira.integracaoponto.model.dto.approval.TimesheetSubmitRequestDTO;
import com.mindproapps.jira.integracaoponto.model.dto.approval.TimesheetsWaitingForApprovalResponseDTO;
import com.mindproapps.jira.integracaoponto.model.timesheets.TimesheetActionOrigin;

public interface ApproveHoursService {
    TimesheetsWaitingForApprovalResponseDTO getTimesheetsWaitingForApprovalList(String startDate, String endDate);
    Integer approveTimesheet(Integer previousTimesheetActionId);
    Integer rejectTimesheet(Integer previousTimesheetActionId, String reason);
    Integer reopenTimesheet(Integer previousTimesheetActionId, String reason);
    Integer submitTimesheet(TimesheetSubmitRequestDTO timesheetSubmit);
    void saveOriginTrace(Integer timesheetActionId, TimesheetActionOrigin origin);
}
