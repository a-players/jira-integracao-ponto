package com.mindproapps.jira.integracaoponto.rest.api.config;

import com.mindproapps.jira.integracaoponto.conditions.ConditionsHelper;
import com.mindproapps.jira.integracaoponto.model.dto.config.ConfigDTO;
import com.mindproapps.jira.integracaoponto.service.config.ConfigService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@Log4j
@Path("/config")
public class ConfigRestApi {
    @Autowired
    ConfigService configService;

    @Autowired
    ConditionsHelper conditionsHelper;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getConfig() {
        if(conditionsHelper.hasUserTempoAdminPermissions()) {
            return Response.ok(configService.getConfig()).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @PUT
    public Response updateConfig(ConfigDTO configDTO) {
        if(conditionsHelper.hasUserTempoAdminPermissions()) {
            configService.setConfig(configDTO);
            return Response.ok().build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}
