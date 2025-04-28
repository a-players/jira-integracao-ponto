package com.mindproapps.jira.integracaoponto.service.ponto;

import com.mindproapps.jira.integracaoponto.model.dto.PontoHoursContainerDTO;
import com.mindproapps.jira.integracaoponto.model.dto.ponto.PontoDTO;

import java.time.LocalDate;
import java.util.List;

public interface PontoService {
    List<PontoDTO> getPontoByEmailAndPeriod(List<PontoDTO> allData, String email, LocalDate start, LocalDate end);
    String emailExists(List<PontoDTO> allData, String email);
    List<PontoDTO> getAllData(String email, LocalDate dtStart, LocalDate dtEnd);
    List<PontoDTO> getAllData(List<String> emails, LocalDate dtStart, LocalDate dtEnd);
    List<PontoDTO> getAllData(String email, LocalDate date);
    List<PontoDTO> getAllData(List<String> emails, LocalDate date);
    List<PontoDTO> getAllData();
    void setPontoHours(List<PontoDTO> allData, List<? extends PontoHoursContainerDTO> list, String actorKey);
    void setPontoHours(List<PontoDTO> allData, List<? extends PontoHoursContainerDTO> list, String startDate, String endDate, String actorKey);
}
