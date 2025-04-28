package com.mindproapps.jira.integracaoponto.model.dto.reports;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect
public class TimesheetReportsResponseDTO {
    private List<TimesheetReportsDTO> approvedTimesheetsList;
    private List<TimesheetReportsDTO> submittedTimesheetsList;
}
