package com.mindproapps.jira.integracaoponto.service.ponto.impl;

import com.mindproapps.jira.integracaoponto.dao.ponto.PontoDAO;
import com.mindproapps.jira.integracaoponto.dao.user.depara.DeParaUserDAO;
import com.mindproapps.jira.integracaoponto.model.dto.PontoHoursContainerDTO;
import com.mindproapps.jira.integracaoponto.model.dto.ponto.PontoDTO;
import com.mindproapps.jira.integracaoponto.model.dto.user.DeParaUserDTO;
import com.mindproapps.jira.integracaoponto.service.ponto.PontoService;
import lombok.extern.log4j.Log4j;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Named
@Singleton
@Log4j
public class PontoServiceImpl implements PontoService {

    @Inject
    private PontoDAO pontoDAO;

    @Inject
    private DeParaUserDAO deParaUserDAO;

    @Override
    public List<PontoDTO> getPontoByEmailAndPeriod(List<PontoDTO> allData, String email, LocalDate start, LocalDate end) {
        log.info("getPontoByEmailAndPeriod: email = " + email + ", start = " + start + ", end = " + end);
        if (allData != null) {
            return allData.stream().filter(pontoDTO -> {
                LocalDate datePonto = LocalDateTime.parse(pontoDTO.getDataInformacao(), DateTimeFormatter.ISO_DATE_TIME).toLocalDate();
                return pontoDTO.getEmail().equalsIgnoreCase(email)
                        && !datePonto.isBefore(start)
                        && !datePonto.isAfter(end);
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public String emailExists(List<PontoDTO> allData, String email) {
        log.info("emailExists: email = " + email);
        String login = email.contains("@") ? email.split("@")[0] : email;
        if (allData != null) {
            Optional<PontoDTO> anyDto = allData.stream().filter(pontoDTO ->
                    pontoDTO.getEmail().equalsIgnoreCase(email) ||
                            pontoDTO.getLogin().equalsIgnoreCase(login)
            ).findFirst();
            if (anyDto.isPresent()) {
                return anyDto.get().getEmail();
            }
        }
        return null;
    }

    @Override
    public List<PontoDTO> getAllData(String email, LocalDate dtStart, LocalDate dtEnd) {
        log.info("getAllData: email = " + email + ", dtStart = " + dtStart + ", dtEnd = " + dtEnd);
        return pontoDAO.getAllData(email, dtStart, dtEnd);
    }

    @Override
    public List<PontoDTO> getAllData(List<String> emails, LocalDate dtStart, LocalDate dtEnd) {
        log.info("getAllData: emails = " + emails + ", dtStart = " + dtStart + ", dtEnd = " + dtEnd);
        return pontoDAO.getAllData(emails, dtStart, dtEnd);
    }

    @Override
    public List<PontoDTO> getAllData(String email, LocalDate date) {
        log.info("getAllData: email = " + email + ", date = " + date);
        return pontoDAO.getAllData(email, date, null);
    }

    @Override
    public List<PontoDTO> getAllData(List<String> emails, LocalDate date) {
        log.info("getAllData: emails = " + emails + ", date = " + date);
        return pontoDAO.getAllData(emails, date, null);
    }

    @Override
    public List<PontoDTO> getAllData() {
        log.info("getAllData: all records");
        return pontoDAO.getAllData();
    }

    @Override
    public void setPontoHours(List<PontoDTO> allData, List<? extends PontoHoursContainerDTO> list, String startDate, String endDate, String actorKey) {
        log.info("setPontoHours: setting with startDate = " + startDate + ", endDate = " + endDate);
        List<String> usersVerified = Collections.synchronizedList(new ArrayList<>());
        list.parallelStream().forEach(dto -> setPontoHours(allData, dto, startDate, endDate, usersVerified, actorKey));
    }

    @Override
    public void setPontoHours(List<PontoDTO> allData, List<? extends PontoHoursContainerDTO> list, String actorKey) {
        log.info("setPontoHours: setting without specific dates");
        List<String> usersVerified = Collections.synchronizedList(new ArrayList<>());
        list.parallelStream().forEach(dto -> setPontoHours(allData, dto, usersVerified, actorKey));
    }

    private void setPontoHours(List<PontoDTO> allData, PontoHoursContainerDTO dto, List<String> usersVerified, String actorKey) {
        setPontoHours(allData, dto, dto.getPeriodStartDateISO(), dto.getPeriodEndDateISO(), usersVerified, actorKey);
    }

    private void setPontoHours(List<PontoDTO> allData, PontoHoursContainerDTO dto, String startDate, String endDate, List<String> usersVerified, String actorKey) {
        dto.updateEmailPonto();
        if (dto.getJiraEmail() == null && !usersVerified.contains(dto.getWorkerKey())) {
            usersVerified.add(dto.getWorkerKey());
            DeParaUserDTO deParaUserDTO = new DeParaUserDTO();
            deParaUserDTO.setUserKey(dto.getWorkerKey());
            deParaUserDTO.setEmail(dto.getEmail());
            String email = emailExists(allData, dto.getEmail());
            if (email != null) {
                deParaUserDTO.setPontoEmail(dto.getEmail());
                dto.setEmailPonto(dto.getEmail());
            }
            deParaUserDAO.insertNewDeParaUser(deParaUserDTO, actorKey);
        } else if (dto.getEmailPonto() == null && !usersVerified.contains(dto.getWorkerKey())) {
            usersVerified.add(dto.getWorkerKey());
            DeParaUserDTO deParaUserDTO = new DeParaUserDTO();
            deParaUserDTO.setUserKey(dto.getWorkerKey());
            String email = emailExists(allData, dto.getEmail());
            if (email != null) {
                deParaUserDTO.setPontoEmail(dto.getEmail());
                dto.setEmailPonto(dto.getEmail());
                deParaUserDAO.updateDeParaUser(deParaUserDTO, actorKey);
            }
        }

        if (dto.getEmailPonto() != null) {
            List<PontoDTO> listPonto = getPontoByEmailAndPeriod(
                    allData, dto.getEmailPonto(),
                    LocalDate.parse(startDate),
                    LocalDate.parse(endDate)
            );
            dto.setHorasPonto("0.0");
            if (listPonto != null && !listPonto.isEmpty()) {
                double horasTrabalhadas = listPonto.stream().mapToDouble(PontoDTO::getHorasTrabalhadas).sum();
                double horasExtras = listPonto.stream().mapToDouble(PontoDTO::getHorasExtras).sum();
                double totalHoras = horasExtras + horasTrabalhadas;
                dto.setHorasPonto(String.valueOf(totalHoras));
            }
        } else {
            dto.setHorasPonto("?");
        }
    }
}
