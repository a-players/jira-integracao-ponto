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
public class AccountDTO extends PontoHoursContainerDTO {
    private String accountName;
    private Integer accountId;


}
