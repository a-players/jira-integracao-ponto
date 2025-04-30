package com.mindproapps.jira.integracaoponto.rest.api.i18n;

import com.mindproapps.jira.integracaoponto.service.i18n.I18nService;
import lombok.extern.log4j.Log4j;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Named
@Log4j
@Path("/i18n")
public class I18nRestApi {

    private final I18nService i18nService;

    @Inject
    public I18nRestApi(I18nService i18nService) {
        this.i18nService = i18nService;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getList(@QueryParam("page") String page) {
        return Response.ok(i18nService.getTextsForPage(page)).build();
    }
}
