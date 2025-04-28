package com.mindproapps.jira.integracaoponto.model.dto.timesheetconfig;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.DayOfWeek;

@Getter
@Setter
@ToString
public class TimesheetConfigDTO {
    private DayOfWeek weekStart;
    private String viewType;
}
