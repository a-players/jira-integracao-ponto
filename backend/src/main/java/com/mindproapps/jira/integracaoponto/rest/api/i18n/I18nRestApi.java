package com.mindproapps.jira.integracaoponto.rest.api.i18n;

import com.mindproapps.jira.integracaoponto.service.i18n.I18nService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@Log4j
@Path("/i18n")
public class I18nRestApi {
    @Autowired
    private I18nService i18nService;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getList(@QueryParam("page") String page) {
       return Response.ok(i18nService.getTextsForPage(page)).build();
    }

}
