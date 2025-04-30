package com.mindproapps.jira.integracaoponto.rest.api.reports;

import com.mindproapps.jira.integracaoponto.conditions.ConditionsHelper;
import com.mindproapps.jira.integracaoponto.model.dto.reports.HoursReportsRequestDTO;
import com.mindproapps.jira.integracaoponto.service.reports.ReportsService;
import lombok.extern.log4j.Log4j;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Named
@Log4j
@Path("/reports")
public class ReportsRestApi {

    private final ReportsService reportsService;
    private final ConditionsHelper conditionsHelper;

    @Inject
    public ReportsRestApi(ReportsService reportsService, ConditionsHelper conditionsHelper) {
        this.reportsService = reportsService;
        this.conditionsHelper = conditionsHelper;
    }

    @GET
    @Path("/timesheets/approved")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getApprovedTimesheetsList(@QueryParam("startDate") String startDate,
                                              @QueryParam("endDate") String endDate,
                                              @QueryParam("teamId") Integer teamId) {
        if (conditionsHelper.hasUserTempoAdminPermissions() || conditionsHelper.hasUserTempoTeamLeadOrViewTimesheetPermissions()) {
            return Response.ok(reportsService.getTimesheetsApproved(startDate, endDate, teamId)).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @GET
    @Path("/timesheets/submitted")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getSubmittedTimesheetsList(@QueryParam("startDate") String startDate,
                                               @QueryParam("endDate") String endDate,
                                               @QueryParam("teamId") Integer teamId) {
        if (conditionsHelper.hasUserTempoAdminPermissions() || conditionsHelper.hasUserTempoTeamLeadOrViewTimesheetPermissions()) {
            return Response.ok(reportsService.getTimesheetsSubmitted(startDate, endDate, teamId)).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @POST
    @Path("/hours/undersubmitted")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getUndersubmittedHoursList(HoursReportsRequestDTO dto) {
        if (conditionsHelper.hasUserTempoAdminPermissions() || conditionsHelper.hasUserTempoTeamLeadOrViewTimesheetPermissions()) {
            return Response.ok(reportsService.getUndersubmittedHours(dto.getStartDate(), dto.getEndDate(), dto.getTeamId())).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @POST
    @Path("/hours/unsubmitted")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getUnsubmittedHoursList(HoursReportsRequestDTO dto) {
        if (conditionsHelper.hasUserTempoAdminPermissions() || conditionsHelper.hasUserTempoTeamLeadOrViewTimesheetPermissions()) {
            return Response.ok(reportsService.getUnsubmittedHours(dto.getPeriods(), dto.getTeamId())).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @POST
    @Path("/hours/accounts")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAccountHoursList(HoursReportsRequestDTO dto) {
        if (conditionsHelper.hasUserTempoAdminPermissions() || conditionsHelper.hasUserTempoTeamLeadOrViewTimesheetPermissions()) {
            log.info("Chamada rest realizada");
            return Response.ok(reportsService.getAccountHours(dto.getStartDate(), dto.getEndDate(), dto.getAccountId(), dto.getCategoryaccountId())).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @GET
    @Path("/teams")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllTeams() {
        if (conditionsHelper.hasUserTempoAdminPermissions() || conditionsHelper.hasUserTempoTeamLeadOrViewTimesheetPermissions()) {
            return Response.ok(reportsService.getAllTeams()).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @GET
    @Path("/accounts")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllAccounts() {
        if (conditionsHelper.hasUserTempoAdminPermissions() || conditionsHelper.hasUserTempoTeamLeadOrViewTimesheetPermissions()) {
            return Response.ok(reportsService.getAllAccounts()).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @GET
    @Path("/categoryaccounts")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllCategoryAccounts() {
        if (conditionsHelper.hasUserTempoAdminPermissions() || conditionsHelper.hasUserTempoTeamLeadOrViewTimesheetPermissions()) {
            return Response.ok(reportsService.getAllCategoryAccounts()).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}
