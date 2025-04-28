package com.mindproapps.jira.integracaoponto.conditions;

import lombok.extern.log4j.Log4j;

@Log4j
public class UserTempoTeamLeadCondition extends BaseCondition {

    @Override
    protected Boolean hasPermissions() {
        log.info("Verificando permissões do usuário para líder de equipe do Tempo ou visualização de planilhas de tempo.");
        boolean hasPermissions = this.conditionsHelper.hasUserTempoTeamLeadOrViewTimesheetPermissions();
        log.info("Permissões do usuário para líder de equipe do Tempo ou visualização de planilhas de tempo: hasPermissions = " + hasPermissions);
        return hasPermissions;
    }
}
