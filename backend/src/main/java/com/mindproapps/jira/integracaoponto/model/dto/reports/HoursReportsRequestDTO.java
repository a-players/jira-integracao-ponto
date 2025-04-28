package com.mindproapps.jira.integracaoponto.model.dto.reports;

import com.mindproapps.jira.integracaoponto.model.dto.period.TimesheetsPeriodDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect
public class HoursReportsRequestDTO {
    private String startDate;
    private String endDate;
    private List<TimesheetsPeriodDTO> periods;
    private Integer teamId;
    private Integer accountId;
    private Integer categoryaccountId;
}
