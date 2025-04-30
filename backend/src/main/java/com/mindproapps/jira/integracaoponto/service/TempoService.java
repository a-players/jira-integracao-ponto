package com.mindproapps.jira.integracaoponto.service;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import java.io.IOException;

@Named
public class TempoService {

    private static final Logger logger = LoggerFactory.getLogger(TempoService.class);

    private static final String BASE_URL = "https://api.tempo.io/4/";
    private static final String ACCESS_TOKEN = "SEU_ACCESS_TOKEN_AQUI"; // Trocar pelo Bearer token real
    private static final String TEMPO_API_TOKEN = "1aedbdff-377c-43d8-b265-e1b150e4b270"; // Novo token obrigatório

    private String executeGet(String endpoint) throws IOException {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            String finalUrl = BASE_URL + endpoint;

            if (finalUrl.contains("?")) {
                finalUrl += "&tempoApiToken=" + TEMPO_API_TOKEN;
            } else {
                finalUrl += "?tempoApiToken=" + TEMPO_API_TOKEN;
            }

            HttpGet request = new HttpGet(finalUrl);
            request.setHeader("Accept", "application/json"); // Retira o Authorization

            HttpResponse response = client.execute(request);
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            logger.error("Erro ao executar chamada GET para Tempo API: {}", endpoint, e);
            throw e;
        }
    }

    public String buscarWorklogs(String fromDate, String toDate, String accountId) throws IOException {
        String endpoint = "worklogs?from=" + fromDate + "&to=" + toDate + "&accountId=" + accountId;
        return executeGet(endpoint);
    }

    public String listarEquipes() throws IOException {
        String endpoint = "teams";
        return executeGet(endpoint);
    }
}
