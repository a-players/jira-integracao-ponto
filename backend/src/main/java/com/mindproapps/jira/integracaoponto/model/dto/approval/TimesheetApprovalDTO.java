package com.mindproapps.jira.integracaoponto.model.dto.approval;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimesheetApprovalDTO {
    private Integer id;
    private String userKey;
    private String actorKey;
    private String reviewerKey;
    private String status;
    private String period;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private String periodType;
    private String periodView;
    private String reason;
    private Long workedTime;
    private Long submittedTime;
    private Long requiredTime;
    private String action;
}
