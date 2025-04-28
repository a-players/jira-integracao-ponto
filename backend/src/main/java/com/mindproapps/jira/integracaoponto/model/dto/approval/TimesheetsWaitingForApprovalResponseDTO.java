package com.mindproapps.jira.integracaoponto.model.dto.approval;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonAutoDetect
public class TimesheetsWaitingForApprovalResponseDTO {
    List<TimesheetsWaitingForApprovalDTO> submittedTimesheets;
    List<TimesheetsWaitingForApprovalDTO> readyToSubmitTimesheets;
    List<TimesheetsWaitingForApprovalDTO> approvedTimesheets;
    List<TimesheetsWaitingForApprovalDTO> openTimesheets;
}
