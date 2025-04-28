package com.mindproapps.jira.integracaoponto.model.config;

import com.mindproapps.jira.integracaoponto.model.base.Entity;
import net.java.ao.Preload;
import net.java.ao.schema.NotNull;

@Preload
public interface Config extends Entity {

    @NotNull
    String getUrlPonto();
    String getUrlLogin();
    String getApiKey();

    void setUrlPonto(String urlPonto);
    void setUrlLogin(String urlLogin);
    void setApiKey(String apiKey);
    
}
