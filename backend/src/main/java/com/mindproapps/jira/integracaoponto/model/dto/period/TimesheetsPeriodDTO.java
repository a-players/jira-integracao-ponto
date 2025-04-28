package com.mindproapps.jira.integracaoponto.model.dto.period;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

@Getter
@Setter
@Builder
@ToString
@JsonAutoDetect
@NoArgsConstructor
@AllArgsConstructor
public class TimesheetsPeriodDTO {
    private String startDate;
    private String endDate;
}
