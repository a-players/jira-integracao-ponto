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
public class AccountTimesheetReportsDTO extends PontoHoursContainerDTO {
    private Integer totalHours;
    private String usuario;
    private String account;
    private Integer accountId;
    private Integer id;
    private String categoryaccount;
    private Integer categoryaccountId;
    private String approver;
    private String leader;
    private String keyaccount;
    private String status;
}
