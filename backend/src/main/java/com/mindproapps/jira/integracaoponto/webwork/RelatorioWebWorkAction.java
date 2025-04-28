package com.mindproapps.jira.integracaoponto.webwork;

import com.atlassian.jira.web.action.ActionViewDataMappings;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.mindproapps.jira.integracaoponto.conditions.ConditionsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.log4j.Log4j;

import java.util.HashMap;
import java.util.Map;

@Log4j
public class RelatorioWebWorkAction extends JiraWebActionSupport {
    private ConditionsHelper conditionsHelper;

    @Autowired
    public RelatorioWebWorkAction(ConditionsHelper conditionsHelper) {
        log.info("RelatorioWebWorkAction: conditionsHelper = " + conditionsHelper);
        this.conditionsHelper = conditionsHelper;
    }
    @Override
    public String execute() throws Exception {
        if(conditionsHelper.hasUserTempoAdminPermissions() || conditionsHelper.hasUserTempoTeamLeadOrViewTimesheetPermissions()) {
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
