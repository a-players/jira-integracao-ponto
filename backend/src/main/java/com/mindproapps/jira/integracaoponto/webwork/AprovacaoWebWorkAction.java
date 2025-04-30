package com.mindproapps.jira.integracaoponto.webwork;

import com.atlassian.jira.web.action.ActionViewDataMappings;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.mindproapps.jira.integracaoponto.conditions.ConditionsHelper;
import lombok.extern.log4j.Log4j;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

@Named
@Log4j
public class AprovacaoWebWorkAction extends JiraWebActionSupport {

    private final ConditionsHelper conditionsHelper;

    @Inject
    public AprovacaoWebWorkAction(ConditionsHelper conditionsHelper) {
        log.info("AprovacaoWebWorkAction: conditionsHelper = " + conditionsHelper);
        this.conditionsHelper = conditionsHelper;
    }

    @Override
    public String execute() {
        if (conditionsHelper.hasUserTempoTeamLeadOrViewTimesheetPermissions()) {
            return SUCCESS;
        }
        return "unauthorized";
    }

    @Override
    public String doDefault() {
        return "success";
    }

    @ActionViewDataMappings({"success", "unauthorized"})
    public Map<String, Object> getData() {
        return new HashMap<>();
    }
}
