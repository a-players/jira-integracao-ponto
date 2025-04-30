package com.mindproapps.jira.integracaoponto.service.user.impl;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;

import com.mindproapps.jira.integracaoponto.dao.user.depara.DeParaUserDAO;
import com.mindproapps.jira.integracaoponto.dao.user.jira.JiraUserDAO;
import com.mindproapps.jira.integracaoponto.exception.NoLoggedUserException;
import com.mindproapps.jira.integracaoponto.model.dto.ponto.PontoDTO;
import com.mindproapps.jira.integracaoponto.model.dto.user.*;
import com.mindproapps.jira.integracaoponto.service.ponto.PontoService;
import com.mindproapps.jira.integracaoponto.service.user.UserService;

import lombok.extern.log4j.Log4j;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j
@Named
public class UserServiceImpl implements UserService {

    private final JiraUserDAO jiraUserDAO;
    private final DeParaUserDAO deParaUserDAO;
    private final PontoService pontoService;

    @Inject
    public UserServiceImpl(JiraUserDAO jiraUserDAO, DeParaUserDAO deParaUserDAO, PontoService pontoService) {
        log.info("UserServiceImpl: jiraUserDAO = " + jiraUserDAO + ", deParaUserDAO = " + deParaUserDAO + ", pontoService = " + pontoService);
        this.jiraUserDAO = jiraUserDAO;
        this.deParaUserDAO = deParaUserDAO;
        this.pontoService = pontoService;
    }

    @Override
    public DeParaUserListDTO getUsersList(DeParaUserListRequestDTO requestDTO) {
        log.info("getUsersList: requestDTO = " + requestDTO);
        ApplicationUser user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
        if (user == null || !user.isActive()) {
            throw new NoLoggedUserException();
        }
        String key = user.getKey();
        DeParaUserListDTO deParaUserListDTO = new DeParaUserListDTO();
        List<PontoDTO> allData = pontoService.getAllData();
        this.updateDeParaUsersList(allData, key);

        if (requestDTO.getDeParaType() == null) {
            requestDTO.setDeParaType(1);
        }

        deParaUserListDTO.setTotalCount(jiraUserDAO.getTotalJiraUsers(requestDTO.getDeParaType(), requestDTO.getGroups()));

        if (requestDTO.getPrevious() != null && !requestDTO.getPrevious().equals(requestDTO.getDeParaType())) {
            requestDTO.setStartRecord(1);
            if (requestDTO.getNumberOfRecords() == null || requestDTO.getNumberOfRecords() < 1) {
                requestDTO.setNumberOfRecords(50);
            }
        } else {
            if (requestDTO.getStartRecord() == null || requestDTO.getStartRecord() < 1) {
                requestDTO.setStartRecord(1);
            }
            if (requestDTO.getNumberOfRecords() == null || requestDTO.getNumberOfRecords() < 1) {
                requestDTO.setNumberOfRecords(50);
            }
        }

        List<DeParaUserDTO> list = jiraUserDAO.listJiraUsers(
                requestDTO.getStartRecord(),
                requestDTO.getNumberOfRecords(),
                requestDTO.getDeParaType(),
                requestDTO.getGroups()
        );

        this.setHistory(list);

        deParaUserListDTO.setList(list);
        deParaUserListDTO.setDeParaTypeOnRequest(requestDTO.getDeParaType());
        deParaUserListDTO.setPagesMap(
                calculatePages(
                        deParaUserListDTO.getTotalCount(),
                        requestDTO.getNumberOfRecords(),
                        requestDTO.getStartRecord()
                )
        );

        return deParaUserListDTO;
    }

    private void setHistory(List<DeParaUserDTO> list) {
        String[] userKeys = list.stream().map(DeParaUserDTO::getUserKey).toArray(String[]::new);
        List<AuditDeParaUserDTO> auditDeParaUserList = deParaUserDAO.getAuditHistory(userKeys);

        list.parallelStream().forEach(deParaUserDTO ->
                deParaUserDTO.setHistory(
                        auditDeParaUserList.stream()
                                .filter(audit -> audit.getUserKey().equalsIgnoreCase(deParaUserDTO.getUserKey()))
                                .collect(Collectors.toList())
                )
        );
    }

    @Override
    public void updateDeParaUsers(List<DeParaUserDTO> deParaUserDTOList) {
        log.info("updateDeParaUsers: deParaUserDTOList = " + deParaUserDTOList);
        if (deParaUserDTOList != null) {
            ApplicationUser user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
            if (user == null || !user.isActive()) {
                throw new NoLoggedUserException();
            }
            String key = user.getKey();
            deParaUserDTOList.parallelStream().forEach(dto -> deParaUserDAO.updateDeParaUser(dto, key));
        }
    }

    @Override
    public DeParaUserListDTO getListByKey(String userKey) {
        log.info("getListByKey: userKey = " + userKey);
        ApplicationUser user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
        if (user == null || !user.isActive()) {
            throw new NoLoggedUserException();
        }
        String key = user.getKey();
        List<PontoDTO> allData = pontoService.getAllData();
        this.updateDeParaUsersList(allData, key);

        List<DeParaUserDTO> list = jiraUserDAO.listJiraUsersByKey(userKey);
        this.setHistory(list);

        DeParaUserListDTO result = new DeParaUserListDTO();
        result.setTotalCount(1);
        result.setList(list);
        result.setDeParaTypeOnRequest(3);
        result.setPagesMap(calculatePages(1, 30, 1));
        return result;
    }

    @Override
    public List<UsernameUserKeyDTO> getAllUsernameUserKeyList() {
        return jiraUserDAO.listAllJiraUsernames();
    }

    @Override
    public List<GroupNameDTO> getAllGroups() {
        return jiraUserDAO.listAllJiraAccessGroups();
    }

    private void updateDeParaUsersList(List<PontoDTO> allData, String actorKey) {
        Integer withoutDePara = jiraUserDAO.getTotalJiraUsers(1, null);
        if (withoutDePara > 0) {
            List<DeParaUserDTO> deParaUserDTOList = jiraUserDAO.listJiraUsers(1, withoutDePara, 1, null);
            if (deParaUserDTOList != null && !deParaUserDTOList.isEmpty()) {
                deParaUserDTOList.parallelStream().forEach(dto -> {
                    String pontoEmail = pontoService.emailExists(allData, dto.getEmail());
                    if (dto.getJiraEmail() == null && pontoEmail != null) {
                        dto.setPontoEmail(pontoEmail);
                        deParaUserDAO.insertNewDeParaUser(dto, actorKey);
                        log.warn("Inserindo novo usuário: " + dto.getEmail());
                    } else if (dto.getPontoEmail() == null && pontoEmail != null) {
                        dto.setPontoEmail(pontoEmail);
                        deParaUserDAO.updateDeParaUser(dto, actorKey);
                        log.warn("Atualizado e-mail ponto: " + dto.getEmail());
                    }
                });
            }
        }
    }

    private List<Integer[]> calculatePages(Integer totalRecords, Integer recordsPerPage, Integer currentRecord) {
        List<Integer[]> list = new ArrayList<>();
        final Integer maxPages = 10;
        Integer totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);
        Integer currentPage = ((currentRecord - 1) / recordsPerPage) + 1;
        Integer firstRecord = 1;
        Integer firstPage = Math.max(currentPage - 4, 1);
        if (firstPage + maxPages > totalPages) firstPage = Math.max(totalPages - maxPages, 1);

        for (Integer i = 1; i <= totalPages; i++) {
            if (i == 1 || i == totalPages || (i >= firstPage && i < firstPage + maxPages)) {
                list.add(new Integer[]{i, firstRecord});
            }
            firstRecord += recordsPerPage;
        }

        if (firstPage > 1) list.add(1, new Integer[]{-1, -1});
        if (firstPage + maxPages < totalPages) list.add(list.size() - 1, new Integer[]{-1, -1});

        return list;
    }
}
