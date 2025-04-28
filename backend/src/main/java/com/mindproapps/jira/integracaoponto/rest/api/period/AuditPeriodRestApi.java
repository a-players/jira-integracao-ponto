package com.mindproapps.jira.integracaoponto.rest.api.period;

import com.mindproapps.jira.integracaoponto.conditions.ConditionsHelper;
import com.mindproapps.jira.integracaoponto.service.period.AuditPeriodService;
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
@Path("/auditperiod")
public class AuditPeriodRestApi {

    @Autowired
    private AuditPeriodService auditPeriodService;

    @Autowired
    ConditionsHelper conditionsHelper;

    @GET
    @Path("/list")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getList(@QueryParam("startDate") String startDate,
                            @QueryParam("endDate") String endDate) {
        if(conditionsHelper.hasUserTempoAdminPermissions()) {
            return Response.ok(auditPeriodService.getListByInterval(startDate, endDate)).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();

    }

}
