package com.mindproapps.jira.integracaoponto.model.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeParaUserListRequestDTO {
    private Integer startRecord;
    private Integer numberOfRecords;
    private Integer DeParaType;
    private Integer previous;
    private String groups;
}
