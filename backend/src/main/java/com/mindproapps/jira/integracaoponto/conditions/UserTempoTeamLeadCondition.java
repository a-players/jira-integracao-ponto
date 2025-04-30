package com.mindproapps.jira.integracaoponto.conditions;

import lombok.extern.log4j.Log4j;
import javax.inject.Named;


@Named
public class UserTempoTeamLeadCondition extends BaseCondition {

    @Override
    protected Boolean hasPermissions() {
        return conditionsHelper.hasUserTempoTeamLeadOrViewTimesheetPermissions();
    }
}

