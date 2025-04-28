package com.mindproapps.jira.integracaoponto.dao.ponto;

import java.time.LocalDate;
import com.mindproapps.jira.integracaoponto.model.dto.ponto.PontoDTO;
import com.mindproapps.jira.integracaoponto.model.dto.ponto.PontoRequestDTO;
import com.mindproapps.jira.integracaoponto.rest.client.PontoRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j;

import java.util.ArrayList;
import java.util.List;

@Component
@Log4j
public class PontoDAO {
    private PontoRestClient pontoRestClient;

    @Autowired
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
        List<PontoRequestDTO> lstPontoRequest = new ArrayList<PontoRequestDTO>();
        for (String email : emails) {
            if ((email != null) && (!email.trim().equals(""))) {
                lstPontoRequest.add(PontoRequestDTO.builder()
                    .email(email)
                    .datainicio(dtStart.toString())
                    .datafim(dtEnd.toString()).build());
            } else {
                log.warn("Existe um e-mail ponto não encontrado, ignorando na requisição..."); 
            }
        }

        return pontoRestClient.getList(lstPontoRequest);
    }
}
