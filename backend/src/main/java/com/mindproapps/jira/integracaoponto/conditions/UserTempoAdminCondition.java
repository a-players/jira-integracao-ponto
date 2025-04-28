package com.mindproapps.jira.integracaoponto.conditions;

public class UserTempoAdminCondition extends BaseCondition {

    @Override
    protected Boolean hasPermissions() {
        return this.conditionsHelper.hasUserTempoAdminPermissions();
    }
}
