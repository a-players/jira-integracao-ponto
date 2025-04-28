package com.mindproapps.jira.integracaoponto.service.config.impl;

import com.mindproapps.jira.integracaoponto.dao.config.ConfigDAO;
import com.mindproapps.jira.integracaoponto.model.dto.config.ConfigDTO;
import com.mindproapps.jira.integracaoponto.service.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigServiceImpl implements ConfigService {
    private ConfigDAO configDAO;

    @Autowired
    public ConfigServiceImpl(ConfigDAO configDAO) {
        this.configDAO = configDAO;
    }
    @Override
    public ConfigDTO getConfig() {
        return ConfigDTO.builder()
                .urlPonto(configDAO.getUrlPonto())
                .urlLogin(configDAO.getUrlLogin())
                .apiKey(configDAO.getApiKey())
                .build();
    }

    @Override
    public void setConfig(ConfigDTO configDTO) {
        configDAO.setUrlPonto(configDTO.getUrlPonto());
        configDAO.setUrlLogin(configDTO.getUrlLogin());
        configDAO.setApiKey(configDTO.getApiKey());
    }
}
