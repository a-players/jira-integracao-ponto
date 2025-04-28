package com.mindproapps.jira.integracaoponto.model.dto.ponto;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

@Getter
@Setter
@Builder
@JsonAutoDetect
public class PontoRequestDTO {
    protected String email;
    protected String datainicio;
    protected String datafim;
}
