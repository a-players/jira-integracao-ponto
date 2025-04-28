package com.mindproapps.jira.integracaoponto.service.user.impl;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.user.ApplicationUser;

import com.mindproapps.jira.integracaoponto.dao.user.depara.DeParaUserDAO;
import com.mindproapps.jira.integracaoponto.dao.user.jira.JiraUserDAO;
import com.mindproapps.jira.integracaoponto.exception.NoLoggedUserException;
import com.mindproapps.jira.integracaoponto.model.dto.ponto.PontoDTO;
import com.mindproapps.jira.integracaoponto.model.dto.user.AuditDeParaUserDTO;
import com.mindproapps.jira.integracaoponto.model.dto.user.DeParaUserDTO;
import com.mindproapps.jira.integracaoponto.model.dto.user.DeParaUserListDTO;
import com.mindproapps.jira.integracaoponto.model.dto.user.DeParaUserListRequestDTO;
import com.mindproapps.jira.integracaoponto.model.dto.user.GroupNameDTO;
import com.mindproapps.jira.integracaoponto.model.dto.user.UsernameUserKeyDTO;
import com.mindproapps.jira.integracaoponto.service.ponto.PontoService;
import com.mindproapps.jira.integracaoponto.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j
@Service
public class UserServiceImpl implements UserService {
    private JiraUserDAO jiraUserDAO;
    private DeParaUserDAO deParaUserDAO;
    private PontoService pontoService;

    @Autowired
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

        if(requestDTO.getDeParaType() == null) {
            requestDTO.setDeParaType(1);
        }
        deParaUserListDTO.setTotalCount(jiraUserDAO.getTotalJiraUsers(requestDTO.getDeParaType(), requestDTO.getGroups()));
        if(requestDTO.getPrevious() != null && requestDTO.getPrevious() != requestDTO.getDeParaType()) {
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
        String[] userKeys = list.stream().map(DeParaUserDTO::getUserKey).toArray(size -> new String[size]);
        List<AuditDeParaUserDTO> auditDeParaUserList = deParaUserDAO.getAuditHistory(
            userKeys
        );
        list.parallelStream().forEach(deParaUserDTO ->
            deParaUserDTO.setHistory(
                    auditDeParaUserList.stream()
                            .filter(auditDeParaUser ->
                                    auditDeParaUser.getUserKey().equalsIgnoreCase(deParaUserDTO.getUserKey())
                            ).collect(Collectors.toList()))
            );
    }

    @Override
    public void updateDeParaUsers(List<DeParaUserDTO> deParaUserDTOList) {
        log.info("updateDeParaUsers: deParaUserDTOList = " + deParaUserDTOList);
        if(deParaUserDTOList != null) {
            ApplicationUser user = ComponentAccessor.getJiraAuthenticationContext().getLoggedInUser();
            if (user == null || !user.isActive()) {
                throw new NoLoggedUserException();
            }
            String key = user.getKey();
            deParaUserDTOList.parallelStream().forEach(deParaUserDTO -> deParaUserDAO.updateDeParaUser(deParaUserDTO, key));
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
        DeParaUserListDTO deParaUserListDTO = new DeParaUserListDTO();
        DeParaUserListRequestDTO requestDTO = new DeParaUserListRequestDTO();
        List<PontoDTO> allData = pontoService.getAllData(); 
        this.updateDeParaUsersList(allData, key);
        requestDTO.setDeParaType(3);
        deParaUserListDTO.setTotalCount(1);
        List<DeParaUserDTO> list = jiraUserDAO.listJiraUsersByKey(userKey);

        this.setHistory(list);
        deParaUserListDTO.setList(list);
        deParaUserListDTO.setDeParaTypeOnRequest(requestDTO.getDeParaType());
        deParaUserListDTO.setPagesMap(
                calculatePages(
                        list.size(),
                        30,
                        1
                )
        );
        return deParaUserListDTO;
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
        Integer withoutDaPara = jiraUserDAO.getTotalJiraUsers(1 /* Sem De/Para */, null /* Todos os Grupos */);
        if (withoutDaPara > 0) {
            List<DeParaUserDTO> deParaUserDTOList = jiraUserDAO.listJiraUsers(1, withoutDaPara, 1, null);

            if(deParaUserDTOList != null && !deParaUserDTOList.isEmpty()) {
                deParaUserDTOList.parallelStream().forEach(dto -> {
                    if (dto.getJiraEmail() == null) {
                        String email = pontoService.emailExists(allData, dto.getEmail());
                        if (email != null) {
                            dto.setPontoEmail(email);
                        }
                        deParaUserDAO.insertNewDeParaUser(dto, actorKey);
                        log.warn("Inserindo novo usuário: " + dto.getEmail());
                    } else if (dto.getPontoEmail() == null) {
                        String email = pontoService.emailExists(allData, dto.getEmail());
                        if (email != null) {
                            dto.setPontoEmail(email);
                            deParaUserDAO.updateDeParaUser(dto, actorKey);
                            log.warn("Atualizado e-mail ponto: " + dto.getEmail());
                        }
                    }
                });
            }
        }
    }

    private List<Integer[]> calculatePages(Integer totalRecords, Integer recordsPerPage, Integer currentRecord) {
        List<Integer[]> list = new ArrayList<>();
        final Integer maxNumPages = 10;
        Integer numberOfPages = (int) Math.ceil((double) totalRecords / recordsPerPage);
        Integer currentPage = ((currentRecord -1) / recordsPerPage) +1;
        Integer firstRecordInPage = 1;
        Integer firstPageDisplayed = currentPage -4;
        if (firstPageDisplayed < 1) firstPageDisplayed = 1;
        if (firstPageDisplayed + maxNumPages > numberOfPages) firstPageDisplayed = numberOfPages - maxNumPages; 
        
        for(Integer i = 1; i <= numberOfPages; i++) {
            if ((i == 1) || (i == numberOfPages) || (firstPageDisplayed < i) && (i < (firstPageDisplayed + maxNumPages))) {
                Integer[] page = new Integer[2];
                page[0] = i;
                page[1] = firstRecordInPage;
                list.add(page);
            }
            firstRecordInPage += recordsPerPage;
        }
        if (firstPageDisplayed > 1) {
            Integer[] page = new Integer[2];
            page[0] = -1;
            page[1] = -1;
            list.add(1, page);
        }
        if (firstPageDisplayed + maxNumPages < numberOfPages) {
            Integer[] page = new Integer[2];
            page[0] = -1;
            page[1] = -1;
            list.add(list.size() -1, page);
        }
        return list;
    }

}
