package com.mindproapps.jira.integracaoponto.dao.ponto;

import com.mindproapps.jira.integracaoponto.model.dto.ponto.PontoDTO;
import com.mindproapps.jira.integracaoponto.model.dto.ponto.PontoRequestDTO;
import com.mindproapps.jira.integracaoponto.rest.client.PontoRestClient;
import lombok.extern.log4j.Log4j;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Named
@Log4j
public class PontoDAO {

    private final PontoRestClient pontoRestClient;

    @Inject
    public PontoDAO(PontoRestClient pontoRestClient) {
        log.info("PontoDAO: pontoRestClient = " + pontoRestClient);
        this.pontoRestClient = pontoRestClient;
    }

    public List<PontoDTO> getAllData() {
        return pontoRestClient.getList();
    }

    public List<PontoDTO> getAllData(String email, LocalDate dtStart, LocalDate dtEnd) {
        log.info("getAllData: email = " + email + ", dtStart = " + dtStart + ", dtEnd = " + dtEnd);
        return pontoRestClient.getList(email, dtStart, dtEnd);
    }

    public List<PontoDTO> getAllData(List<String> emails, LocalDate dtStart, LocalDate dtEnd) {
        log.info("getAllData: emails = " + emails + ", dtStart = " + dtStart + ", dtEnd = " + dtEnd);
        List<PontoRequestDTO> lstPontoRequest = new ArrayList<>();
        for (String email : emails) {
            if (email != null && !email.trim().isEmpty()) {
                lstPontoRequest.add(PontoRequestDTO.builder()
                        .email(email)
                        .datainicio(dtStart.toString())
                        .datafim(dtEnd.toString())
                        .build());
            } else {
                log.warn("Existe um e-mail ponto não encontrado, ignorando na requisição...");
            }
        }
        return pontoRestClient.getList(lstPontoRequest);
    }
}
