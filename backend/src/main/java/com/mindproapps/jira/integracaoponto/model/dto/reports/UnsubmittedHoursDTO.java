package com.mindproapps.jira.integracaoponto.model.dto.reports;

import com.mindproapps.jira.integracaoponto.model.dto.PontoHoursContainerDTO;
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
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonAutoDetect
public class UnsubmittedHoursDTO extends PontoHoursContainerDTO {
    private Double totalHours;
    private String usuario;
    private String team;
    private Integer teamId;
    private String approver;
}
