package com.mindproapps.jira.integracaoponto.webwork;

import com.atlassian.jira.web.action.ActionViewDataMappings;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.mindproapps.jira.integracaoponto.conditions.ConditionsHelper;
import lombok.extern.log4j.Log4j;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

@Log4j
@Named
public class CadastroWebWorkAction extends JiraWebActionSupport {

    private final ConditionsHelper conditionsHelper;

    @Inject
    public CadastroWebWorkAction(ConditionsHelper conditionsHelper) {
        log.info("CadastroWebWorkAction: conditionsHelper = " + conditionsHelper);
        this.conditionsHelper = conditionsHelper;
    }

    @Override
    public String execute() {
        if (conditionsHelper.hasUserTempoAdminPermissions()) {
            return SUCCESS;
        }
        return "unauthorized";
    }

    public String doDefault() {
        return "success";
    }

    @ActionViewDataMappings({"success", "unauthorized"})
    public Map<String, Object> getData() {
        Map<String, Object> map = new HashMap<>();
        return map;
    }
}
