package com.mindproapps.jira.integracaoponto.model.dto.config;

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
public class ConfigDTO {
    private String urlPonto;
    private String urlLogin;
    private String apiKey;
}
