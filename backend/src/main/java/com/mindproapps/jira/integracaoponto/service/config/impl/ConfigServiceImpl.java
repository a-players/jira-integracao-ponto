package com.mindproapps.jira.integracaoponto.service.config.impl;

import com.mindproapps.jira.integracaoponto.dao.config.ConfigDAO;
import com.mindproapps.jira.integracaoponto.model.dto.config.ConfigDTO;
import com.mindproapps.jira.integracaoponto.service.config.ConfigService;
import lombok.extern.log4j.Log4j;

import javax.inject.Inject;
import javax.inject.Named;

@Named
@Log4j
public class ConfigServiceImpl implements ConfigService {

    private final ConfigDAO configDAO;

    @Inject
    public ConfigServiceImpl(ConfigDAO configDAO) {
        this.configDAO = configDAO;
    }

    @Override
    public ConfigDTO getConfig() {
        log.info("Fetching configuration");
        return ConfigDTO.builder()
                .urlPonto(configDAO.getUrlPonto())
                .urlLogin(configDAO.getUrlLogin())
                .apiKey(configDAO.getApiKey())
                .build();
    }

    @Override
    public void setConfig(ConfigDTO configDTO) {
        log.info("Updating configuration");
        configDAO.setUrlPonto(configDTO.getUrlPonto());
        configDAO.setUrlLogin(configDTO.getUrlLogin());
        configDAO.setApiKey(configDTO.getApiKey());
    }
}
