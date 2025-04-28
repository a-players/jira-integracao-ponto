package com.mindproapps.jira.integracaoponto.service.config;

import com.mindproapps.jira.integracaoponto.model.dto.config.ConfigDTO;

public interface ConfigService {
    ConfigDTO getConfig();
    void setConfig(ConfigDTO configDTO);
}
