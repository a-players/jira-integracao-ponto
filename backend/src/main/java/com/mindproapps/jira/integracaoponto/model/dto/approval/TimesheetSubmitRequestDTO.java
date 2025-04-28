package com.mindproapps.jira.integracaoponto.model.dto.approval;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimesheetSubmitRequestDTO {
    private String userKey;
    private String period;
    private Long workedTime;
    private Long submittedTime;
    private Long requiredTime;
}
