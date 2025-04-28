package com.mindproapps.jira.integracaoponto.model.dto.approval;


import com.mindproapps.jira.integracaoponto.model.dto.PontoHoursContainerDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonAutoDetect;


@Builder
@Getter
@Setter
@JsonAutoDetect
@ToString
public class TimesheetsWaitingForApprovalDTO extends PontoHoursContainerDTO {
    private Integer timeSheetApprovalId;
    private String username;
    private String status;
    private Double horasTempo;
    private Double requiredTime;
    private Integer origin;
    private Integer teamId;
    private String team;
    private Integer accountId;
    private String account;
    private Integer categoryaccountId;
    private String categoryaccount;
    private Boolean canApprove;
}
