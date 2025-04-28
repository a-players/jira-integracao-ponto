package com.mindproapps.jira.integracaoponto.dao.config;

import com.atlassian.activeobjects.external.ActiveObjects;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.mindproapps.jira.integracaoponto.dao.base.BaseDAO;
import com.mindproapps.jira.integracaoponto.model.config.Config;
import lombok.val;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Log4j
public class ConfigDAO extends BaseDAO {
    @ComponentImport
    private ActiveObjects ao;

    public ConfigDAO(ActiveObjects ao) {
        this.ao = ao;
    }

    public String getUrlPonto() {
        log.info("getUrlPonto");
        Config[] config = ao.find(Config.class);
        if(config != null && config.length > 0) {
            return config[0].getUrlPonto();
        }
        return "https://tisistemas-hml.linx.com.br/HorasTrabalhadasPontoNorberAPI/api/horastrabalhadas";
    }

    public String getUrlLogin() {
        log.info("getUrlLogin: ");
        Config[] config = ao.find(Config.class);
        if(config != null && config.length > 0) {
            return config[0].getUrlLogin();
        }
        return "https://tisistemas-hml.linx.com.br/HorasTrabalhadasPontoNorberAPI/Login";
    }

    public String getApiKey() {
        log.info("getApiKey: ");
        Config[] config = ao.find(Config.class);
        if(config != null && config.length > 0) {
            return config[0].getApiKey();
        }
        return "{ \"api_key\": \"F9F66E7A-10EE-4E23-85DD-33E796FFB730\","+
               " \"api_secret\": \"413A122D-162B-455B-8B05-E3A41BC06FC4\"}";
    }

    public void setApiKey(String apiKey) {
        log.info("setApiKey: apiKey = " + apiKey);
        Config[] config = ao.find(Config.class);
        if(config != null && config.length > 0) {
            config[0].setApiKey(apiKey);
            config[0].save();
        } else {
            val map = new HashMap<String, Object>();
            map.put("API_KEY", apiKey);
            Config newConfig = ao.create(Config.class, map);
            newConfig.save();
        }
    }

    public void setUrlLogin(String urlLogin){
        log.info("setUrlLogin: urlLogin = " + urlLogin);
        Config[] config = ao.find(Config.class);
        if(config != null && config.length > 0) {
            config[0].setUrlLogin(urlLogin);
            config[0].save();
        } else {
            val map = new HashMap<String, Object>();
            map.put("URL_LOGIN", urlLogin);
            Config newConfig = ao.create(Config.class, map);
            newConfig.save();
        }

    }
    
    public void setUrlPonto(String urlPonto) {
        log.info("setUrlPonto: urlPonto = " + urlPonto);
        Config[] config = ao.find(Config.class);
        if(config != null && config.length > 0) {
            config[0].setUrlPonto(urlPonto);
            config[0].save();
        } else {
            val map = new HashMap<String, Object>();
            map.put("URL_PONTO", urlPonto);
            Config newConfig = ao.create(Config.class, map);
            newConfig.save();
        }
    }
}
