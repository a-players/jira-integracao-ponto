package com.mindproapps.jira.integracaoponto.conditions;

import com.atlassian.plugin.web.Condition;
import lombok.extern.log4j.Log4j;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

@Log4j
@Named
public abstract class BaseCondition implements Condition {

    @Inject
    protected ConditionsHelper conditionsHelper;

    @Override
    public void init(Map<String, String> params) {
        log.info("Inicializando condição do plugin.");
    }

    @Override
    public boolean shouldDisplay(Map<String, Object> context) {
        log.info("Verificando se deve exibir a condição.");
        return hasPermissions();
    }

    protected abstract Boolean hasPermissions();
}
