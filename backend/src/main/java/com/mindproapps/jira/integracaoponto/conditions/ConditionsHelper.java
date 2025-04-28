package com.mindproapps.jira.integracaoponto.conditions;

import com.atlassian.crowd.embedded.api.Group;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.permission.GlobalPermissionKey;
import com.atlassian.jira.security.GlobalPermissionManager;
import com.atlassian.jira.security.groups.GroupManager;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.mindproapps.jira.integracaoponto.dao.tempo.team.TempoTeamDAO;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Log4j
public class ConditionsHelper {
    
    private static final GlobalPermissionKey GLOBAL_TEMPO_ADMINISTRATOR_PERMISSION_KEY =
            GlobalPermissionKey.of("GLOBAL_TEMPO_ADMINISTRATOR");
    @ComponentImport
    GlobalPermissionManager globalPermissionManager;

    private TempoTeamDAO tempoTeamDAO;

    @Autowired
    public ConditionsHelper(GlobalPermissionManager globalPermissionManager, TempoTeamDAO tempoTeamDAO) {
        this.globalPermissionManager = globalPermissionManager;
        this.tempoTeamDAO = tempoTeamDAO;
        log.info("ConditionsHelper: globalPermissionManager = " + globalPermissionManager + ", tempoTeamDAO = " + tempoTeamDAO);
    }

    public boolean hasUserTempoAdminPermissions() {
        log.info("Verificando permissões de administrador do Tempo para o usuário.");
        ApplicationUser user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
        if(user == null || !user.isActive()) {
            return Boolean.FALSE;
        }
        GroupManager groupManager = ComponentAccessor.getGroupManager();
        for(Group group:
                this.globalPermissionManager.getGroupsWithPermission(
                        GLOBAL_TEMPO_ADMINISTRATOR_PERMISSION_KEY)) {
            if(groupManager.isUserInGroup(user, group)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public boolean hasUserTempoTeamLeadOrViewTimesheetPermissions() {
        log.info("Verificando permissões de líder de equipe do Tempo ou permissões para visualizar as planilhas de ponto.");
        return this.hasUserTempoTeamLeadOrViewTimesheetPermissions(null);
    }

    public boolean hasUserTempoTeamLeadOrViewTimesheetPermissions(Integer teamId) {
        log.info("Verificando permissões de líder de equipe do Tempo ou permissões para visualizar as planilhas de ponto para a equipe com ID: " + teamId);
        ApplicationUser user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
        if(user == null || !user.isActive()) {
            return Boolean.FALSE;
        }
        return tempoTeamDAO.isUserKeyTeamLeadOrHasViewTimesheetsPermissions(user.getKey(), teamId);
    }

    public boolean hasUserTempoTeamLeadOrApproveTimesheetPermissions() {
        log.info("Verificando permissões de líder de equipe do Tempo ou permissões para aprovar as planilhas de ponto.");
        return this.hasUserTempoTeamLeadOrApproveTimesheetPermissions(null);
    }

    public boolean hasUserTempoTeamLeadOrApproveTimesheetPermissions(Integer teamId) {
        log.info("hasUserTempoTeamLeadOrApproveTimesheetPermissions: teamId = " + teamId);
        ApplicationUser user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
        if(user == null || !user.isActive()) {
            return Boolean.FALSE;
        }
        return tempoTeamDAO.isUserKeyTeamLeadOrHasApproveTimesheetsPermissions(user.getKey(), teamId);
    }
}
