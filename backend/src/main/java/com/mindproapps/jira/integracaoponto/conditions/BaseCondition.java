package com.mindproapps.jira.integracaoponto.conditions;

import com.atlassian.plugin.PluginParseException;
import com.atlassian.plugin.web.Condition;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import lombok.extern.log4j.Log4j;

@Log4j
public abstract class BaseCondition implements Condition {

    @Autowired
    protected ConditionsHelper conditionsHelper;

    @Override
    public void init(Map<String, String> map) throws PluginParseException {
        log.info("Inicializando condição do plugin.");
    }

    @Override
    public boolean shouldDisplay(Map<String, Object> map) {
        log.info("Verificando se deve exibir a condição.");
        return hasPermissions();
    }

    protected abstract Boolean hasPermissions();

}
