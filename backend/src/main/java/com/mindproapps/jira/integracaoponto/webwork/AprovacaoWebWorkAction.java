package com.mindproapps.jira.integracaoponto.webwork;

import com.atlassian.jira.web.action.ActionViewDataMappings;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.mindproapps.jira.integracaoponto.conditions.ConditionsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.log4j.Log4j;

import java.util.HashMap;
import java.util.Map;

@Log4j
public class AprovacaoWebWorkAction extends JiraWebActionSupport {
    private ConditionsHelper conditionsHelper;

    @Autowired
    public AprovacaoWebWorkAction(ConditionsHelper conditionsHelper) {
        log.info("AprovacaoWebWorkAction: conditionsHelper = " + conditionsHelper);
        this.conditionsHelper = conditionsHelper;
    }

    @Override
    public String execute() {
        if(conditionsHelper.hasUserTempoTeamLeadOrViewTimesheetPermissions()) {
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
