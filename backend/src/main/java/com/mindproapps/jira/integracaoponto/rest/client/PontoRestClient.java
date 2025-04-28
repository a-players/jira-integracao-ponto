package com.mindproapps.jira.integracaoponto.rest.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mindproapps.jira.integracaoponto.dao.config.ConfigDAO;
import com.mindproapps.jira.integracaoponto.exception.CantLoginAPIException;
import com.mindproapps.jira.integracaoponto.model.dto.ponto.PontoDTO;
import com.mindproapps.jira.integracaoponto.model.dto.ponto.PontoRequestDTO;

import lombok.extern.log4j.Log4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Log4j
public class PontoRestClient {
    private ConfigDAO configDAO;
    private static String token = null;

    @Autowired
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

        URL url = null;
        HttpURLConnection connection = null;
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

                url = new URL(strUrl +
                    getParameters(email, dateIni, dateFin));
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("accept", "application/json");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", token);
                
                if (connection.getResponseCode() == 200) {
                    break;
                } 
                else if (connection.getResponseCode() == 401)  {
                    token = null;
                }
            }
            InputStream responseStream = connection.getInputStream();
            BufferedReader responsReader = new BufferedReader(new InputStreamReader(responseStream));

            Gson gson = new Gson();
            List<PontoDTO> list = gson.fromJson(responsReader, new TypeToken<List<PontoDTO>>(){}.getType());
            
            connection.disconnect();
            return list;
        } catch (Exception e) {
            log.error("Ponto Rest Client: getList error: Email " + email + " NOT FOUND");
            log.error("Ponto Rest Client: getList error: " +  e.getMessage(), e);
        }
        return null;
    }

    public List<PontoDTO> getList(List<PontoRequestDTO> pontoRequest) {
        String strUrl = configDAO.getUrlPonto();
        String pathLoginURL = configDAO.getUrlLogin();
        String apiKey = configDAO.getApiKey();

        URL url = null;
        HttpURLConnection connection = null;
        Gson gson = new Gson();
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

                url = new URL(strUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("accept", "application/json");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", token);
                connection.setRequestMethod("POST"); 
                connection.setDoOutput(true);
                
                byte[] out = gson.toJson(pontoRequest).getBytes(StandardCharsets.UTF_8);
                OutputStream os = connection.getOutputStream();
                os.write(out);

                if (connection.getResponseCode() == 200) {
                    break;
                } 
                else if (connection.getResponseCode() == 401)  {
                    token = null;
                }
            }
            InputStream responseStream = connection.getInputStream();
            BufferedReader responsReader = new BufferedReader(new InputStreamReader(responseStream));
            List<PontoDTO> list = gson.fromJson(responsReader, new TypeToken<List<PontoDTO>>(){}.getType());

            connection.disconnect();
            return list;
        } catch (Exception e) {
            log.error("Ponto Rest Client: getList POST error: " +  e.getMessage(), e);
        }
        return null;
    }

    private String getParameters(String email, LocalDate dateIni, LocalDate dateFin) {
        StringBuilder builder = new StringBuilder();
        if(email != null) {
            builder.append("?email=").append(email);
            if((dateIni != null) && (dateFin != null)) {
                builder.append("&dateini=").append(dateIni);
                builder.append("&datefim=").append(dateFin);
            } else if(dateIni != null) {
                builder.append("&dateini=").append(dateIni);
                builder.append("&datefim=").append(dateIni);
            }
        }
        return builder.toString();
    }

    private static String getToken(String pathUrl, String apikey) {
        HttpURLConnection connection = null;
        String token = "";
        try {
            log.info(" ======== API de retorno - Buscando token URL(" + pathUrl + ") Chave(" + apikey + ")  ======== ");  
            connection = (HttpURLConnection) (new URL(pathUrl)).openConnection();
            connection.setRequestProperty("accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            log.info(" ======== API de retorno - enviando requisição ======== ");   
            OutputStreamWriter outStreamWriter = new OutputStreamWriter(connection.getOutputStream());
            outStreamWriter.write(apikey);
            outStreamWriter.flush();
            log.info(" ======== API de retorno - recebendo retorno ======== ");
            switch (connection.getResponseCode()){
                case 200:
                case 201:
                    token = processResponse(connection);   
                    String token_mensage  = (token.isEmpty()) ? "ERRO TOKEN INVALIDO" : token.substring(0,10);
                    log.info(" ======== API de retorno ok- Token processado: " + token_mensage +
                             " - status code: "+ connection.getResponseCode() + " ======== ");     
                    break;
                default:
                    break;
            }
            return token;
        } catch (Exception e) {
           log.error("Ponto Rest Client: getToken error:ao acessar o token");
           log.error("Error: " + e.getMessage(), e);
        }
        finally{
            connection.disconnect();
        }
        return token;
    }

    private static String processResponse (HttpURLConnection connection) throws UnsupportedEncodingException, IOException {
        BufferedReader responsReader = new BufferedReader( new InputStreamReader(connection.getInputStream(), "utf-8"));
        String responseLine;
        StringBuilder response = new StringBuilder();
        while ((responseLine = responsReader.readLine()) != null) {
            response.append(responseLine.trim());
        }
        
        return extractToken(response.toString());
    }

    private static String extractToken(String response){

        Map<String, String> hMapData = new HashMap<String, String>();
        response = response.replace("\"", "").replace("{", "").replace("}", "") ;
        String parts[] = response.split(",");
       
        for(String part : parts){
             String empdata[] = part.split(":");
             String strId = empdata[0].trim();
             String strName = empdata[1].trim();
             hMapData.put(strId, strName);
         }
         return hMapData.get("token").toString();
    }
}
