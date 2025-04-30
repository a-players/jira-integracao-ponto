package com.mindproapps.jira.integracaoponto.webwork;

import com.atlassian.jira.web.action.ActionViewDataMappings;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.mindproapps.jira.integracaoponto.conditions.ConditionsHelper;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

@Named
public class SettingsWebWorkAction extends JiraWebActionSupport {

    private final ConditionsHelper conditionsHelper;

    @Inject
    public SettingsWebWorkAction(ConditionsHelper conditionsHelper) {
        this.conditionsHelper = conditionsHelper;
    }

    @Override
    public String execute() throws Exception {
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
        return new HashMap<>();
    }
}
