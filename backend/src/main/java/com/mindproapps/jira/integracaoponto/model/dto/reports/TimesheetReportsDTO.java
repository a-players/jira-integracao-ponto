package com.mindproapps.jira.integracaoponto.model.dto.reports;

import com.mindproapps.jira.integracaoponto.model.dto.PontoHoursContainerDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect
public class TimesheetReportsDTO extends PontoHoursContainerDTO {
    private Integer id;
    private String actionDate;
    private String status;
    private String usuario;
    private String team;
    private Integer teamId;
    private String account;
    private Integer accountId;
    private String categoryaccount;
    private Integer categoryaccountId;
    private String origin;
    private String approver;
    private String reason;
    private Double hoursTempo;
}
