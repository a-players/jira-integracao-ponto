package com.mindproapps.jira.integracaoponto.rest.api.approval;

import com.mindproapps.jira.integracaoponto.conditions.ConditionsHelper;
import com.mindproapps.jira.integracaoponto.model.dto.approval.TimesheetRejectRequestDTO;
import com.mindproapps.jira.integracaoponto.model.dto.approval.TimesheetReopenRequestDTO;
import com.mindproapps.jira.integracaoponto.model.dto.approval.TimesheetSubmitRequestDTO;
import com.mindproapps.jira.integracaoponto.service.approval.ApproveHoursService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Component
@Log4j
@Path("/approvehours")
public class ApproveHoursRestApi {

    @Autowired
    ConditionsHelper conditionsHelper;

    @Autowired
    ApproveHoursService approveHoursService;

    @GET
    @Path("/list/{startDate}/{endDate}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getList(@PathParam("startDate") String startDate, @PathParam("endDate") String endDate) {
        if(conditionsHelper.hasUserTempoTeamLeadOrViewTimesheetPermissions()) {
            return Response.ok(approveHoursService.getTimesheetsWaitingForApprovalList(startDate, endDate)).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();

    }

    @POST
    @Path("/approve/{teamId}/{idPreviousTimesheet}")
    public Response approve(@PathParam("teamId") Integer teamId, 
                            @PathParam("idPreviousTimesheet") Integer idPreviousTimesheet) {
        if(conditionsHelper.hasUserTempoTeamLeadOrApproveTimesheetPermissions(teamId)) {
            return Response.ok(approveHoursService.approveTimesheet(idPreviousTimesheet)).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();  
    }

    @POST
    @Path("/reject/{teamId}/{idPreviousTimesheet}")
    public Response reject(@PathParam("teamId") Integer teamId,
                           @PathParam("idPreviousTimesheet") Integer idPreviousTimesheet,
                           TimesheetRejectRequestDTO dto) {
        if(conditionsHelper.hasUserTempoTeamLeadOrApproveTimesheetPermissions(teamId)) {
            return Response.ok(approveHoursService.rejectTimesheet(idPreviousTimesheet, dto.getReason())).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build(); 
    }

    @POST
    @Path("/reopen/{teamId}/{idPreviousTimesheet}")
    public Response reopen(@PathParam("teamId") Integer teamId,
                           @PathParam("idPreviousTimesheet") Integer idPreviousTimesheet,
                           TimesheetReopenRequestDTO dto) {
        if(conditionsHelper.hasUserTempoTeamLeadOrApproveTimesheetPermissions(teamId)) {
            return Response.ok(approveHoursService.reopenTimesheet(idPreviousTimesheet, dto.getReason())).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @POST
    @Path("/submit/{teamId}")
    public Response submit(@PathParam("teamId") Integer teamId, 
                           TimesheetSubmitRequestDTO dto) {
        if(conditionsHelper.hasUserTempoTeamLeadOrApproveTimesheetPermissions(teamId)) {
            return Response.ok(approveHoursService.submitTimesheet(dto)).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();  
    }

/* 
    // Account

    @POST
    @Path("/approve/{accountId}/{idPreviousTimesheet}")
    public Response approve(@PathParam("accountId") Integer accountId, 
                            @PathParam("idPreviousTimesheet") Integer idPreviousTimesheet) {
        if(conditionsHelper.hasUserTempoTeamLeadOrApproveTimesheetPermissions(accountId)) {
            return Response.ok(approveHoursService.approveTimesheet(idPreviousTimesheet)).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();  
    }

    @POST
    @Path("/reject/{accountId}/{idPreviousTimesheet}")
    public Response reject(@PathParam("accountId") Integer accountId,
                           @PathParam("idPreviousTimesheet") Integer idPreviousTimesheet,
                           TimesheetRejectRequestDTO dto) {
        if(conditionsHelper.hasUserTempoTeamLeadOrApproveTimesheetPermissions(accountId)) {
            return Response.ok(approveHoursService.rejectTimesheet(idPreviousTimesheet, dto.getReason())).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build(); 
    }

    @POST
    @Path("/reopen/{accountId}/{idPreviousTimesheet}")
    public Response reopen(@PathParam("accountId") Integer accountId,
                           @PathParam("idPreviousTimesheet") Integer idPreviousTimesheet,
                           TimesheetReopenRequestDTO dto) {
        if(conditionsHelper.hasUserTempoTeamLeadOrApproveTimesheetPermissions(accountId)) {
            return Response.ok(approveHoursService.reopenTimesheet(idPreviousTimesheet, dto.getReason())).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @POST
    @Path("/submit/{accountId}")
    public Response submit(@PathParam("accountId") Integer accountId, 
                           TimesheetSubmitRequestDTO dto) {
        if(conditionsHelper.hasUserTempoTeamLeadOrApproveTimesheetPermissions(accountId)) {
            return Response.ok(approveHoursService.submitTimesheet(dto)).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();  
    }

    //Account Category

    @POST
    @Path("/approve/{categoryaccountId}/{idPreviousTimesheet}")
    public Response approve(@PathParam("categoryaccountId") Integer categoryaccountId, 
                            @PathParam("idPreviousTimesheet") Integer idPreviousTimesheet) {
        if(conditionsHelper.hasUserTempoTeamLeadOrApproveTimesheetPermissions(categoryaccountId)) {
            return Response.ok(approveHoursService.approveTimesheet(idPreviousTimesheet)).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();  
    }

    @POST
    @Path("/reject/{categoryaccountId}/{idPreviousTimesheet}")
    public Response reject(@PathParam("categoryaccountId") Integer categoryaccountId,
                           @PathParam("idPreviousTimesheet") Integer idPreviousTimesheet,
                           TimesheetRejectRequestDTO dto) {
        if(conditionsHelper.hasUserTempoTeamLeadOrApproveTimesheetPermissions(categoryaccountId)) {
            return Response.ok(approveHoursService.rejectTimesheet(idPreviousTimesheet, dto.getReason())).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build(); 
    }

    @POST
    @Path("/reopen/{categoryaccountId}/{idPreviousTimesheet}")
    public Response reopen(@PathParam("categoryaccountId") Integer categoryaccountId,
                           @PathParam("idPreviousTimesheet") Integer idPreviousTimesheet,
                           TimesheetReopenRequestDTO dto) {
        if(conditionsHelper.hasUserTempoTeamLeadOrApproveTimesheetPermissions(categoryaccountId)) {
            return Response.ok(approveHoursService.reopenTimesheet(idPreviousTimesheet, dto.getReason())).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @POST
    @Path("/submit/{categoryaccountId}")
    public Response submit(@PathParam("categoryaccountId") Integer categoryaccountId, 
                           TimesheetSubmitRequestDTO dto) {
        if(conditionsHelper.hasUserTempoTeamLeadOrApproveTimesheetPermissions(categoryaccountId)) {
            return Response.ok(approveHoursService.submitTimesheet(dto)).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();  
    }
*/
}

