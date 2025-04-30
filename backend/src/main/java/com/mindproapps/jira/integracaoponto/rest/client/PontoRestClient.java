package com.mindproapps.jira.integracaoponto.rest.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mindproapps.jira.integracaoponto.dao.config.ConfigDAO;
import com.mindproapps.jira.integracaoponto.exception.CantLoginAPIException;
import com.mindproapps.jira.integracaoponto.model.dto.ponto.PontoDTO;
import com.mindproapps.jira.integracaoponto.model.dto.ponto.PontoRequestDTO;
import lombok.extern.log4j.Log4j;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Named
@Log4j
public class PontoRestClient {

    private final ConfigDAO configDAO;
    private static String token = null;

    @Inject
    public PontoRestClient(ConfigDAO configDAO) {
        this.configDAO = configDAO;
    }

    public List<PontoDTO> getList() {
        return getList(null, null, null);
    }

    public List<PontoDTO> getList(String email) {
        return getList(email, null, null);
    }

    public List<PontoDTO> getList(String email, LocalDate date) {
        return getList(email, date, null);
    }

    public List<PontoDTO> getList(String email, LocalDate dateIni, LocalDate dateFin) {
        String strUrl = configDAO.getUrlPonto();
        String pathLoginURL = configDAO.getUrlLogin();
        String apiKey = configDAO.getApiKey();

        try {
            for (int i = 0; i < 2; i++) {
                if (token == null) {
                    String tokenApi = getToken(pathLoginURL, apiKey);
                    if (tokenApi.isEmpty()) {
                        throw new CantLoginAPIException();
                    } else {
                        token = "Bearer " + tokenApi;
                    }
                }

                URL url = new URL(strUrl + getParameters(email, dateIni, dateFin));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("accept", "application/json");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", token);

                if (connection.getResponseCode() == 200) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        return new Gson().fromJson(reader, new TypeToken<List<PontoDTO>>() {}.getType());
                    }
                } else if (connection.getResponseCode() == 401) {
                    token = null;
                }
            }
        } catch (Exception e) {
            log.error("PontoRestClient: getList error: Email " + email + " NOT FOUND");
            log.error("PontoRestClient: getList error: " + e.getMessage(), e);
        }

        return null;
    }

    public List<PontoDTO> getList(List<PontoRequestDTO> pontoRequest) {
        String strUrl = configDAO.getUrlPonto();
        String pathLoginURL = configDAO.getUrlLogin();
        String apiKey = configDAO.getApiKey();

        try {
            for (int i = 0; i < 2; i++) {
                if (token == null) {
                    String tokenApi = getToken(pathLoginURL, apiKey);
                    if (tokenApi.isEmpty()) {
                        throw new CantLoginAPIException();
                    } else {
                        token = "Bearer " + tokenApi;
                    }
                }

                URL url = new URL(strUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("accept", "application/json");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", token);
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                try (OutputStream os = connection.getOutputStream()) {
                    byte[] out = new Gson().toJson(pontoRequest).getBytes(StandardCharsets.UTF_8);
                    os.write(out);
                }

                if (connection.getResponseCode() == 200) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        return new Gson().fromJson(reader, new TypeToken<List<PontoDTO>>() {}.getType());
                    }
                } else if (connection.getResponseCode() == 401) {
                    token = null;
                }
            }
        } catch (Exception e) {
            log.error("PontoRestClient: getList POST error: " + e.getMessage(), e);
        }

        return null;
    }

    private String getParameters(String email, LocalDate dateIni, LocalDate dateFin) {
        StringBuilder builder = new StringBuilder();
        if (email != null) {
            builder.append("?email=").append(email);
            if (dateIni != null && dateFin != null) {
                builder.append("&dateini=").append(dateIni);
                builder.append("&datefim=").append(dateFin);
            } else if (dateIni != null) {
                builder.append("&dateini=").append(dateIni);
                builder.append("&datefim=").append(dateIni);
            }
        }
        return builder.toString();
    }

    private static String getToken(String pathUrl, String apiKey) {
        HttpURLConnection connection = null;
        String token = "";

        try {
            connection = (HttpURLConnection) new URL(pathUrl).openConnection();
            connection.setRequestProperty("accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())) {
                writer.write(apiKey);
                writer.flush();
            }

            if (connection.getResponseCode() == 200 || connection.getResponseCode() == 201) {
                token = processResponse(connection);
                String display = token.isEmpty() ? "ERRO TOKEN INVALIDO" : token.substring(0, 10);
                log.info("Token processado: " + display);
            }

        } catch (Exception e) {
            log.error("PontoRestClient: getToken error ao acessar token: " + e.getMessage(), e);
        } finally {
            if (connection != null) connection.disconnect();
        }

        return token;
    }

    private static String processResponse(HttpURLConnection connection) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line.trim());
            }
            return extractToken(response.toString());
        }
    }

    private static String extractToken(String response) {
        Map<String, String> tokenMap = new HashMap<>();
        response = response.replace("\"", "").replace("{", "").replace("}", "");
        for (String part : response.split(",")) {
            String[] keyValue = part.split(":");
            if (keyValue.length == 2) {
                tokenMap.put(keyValue[0].trim(), keyValue[1].trim());
            }
        }
        return tokenMap.getOrDefault("token", "");
    }
}
