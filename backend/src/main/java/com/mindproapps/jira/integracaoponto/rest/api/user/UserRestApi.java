package com.mindproapps.jira.integracaoponto.rest.api.user;

import com.mindproapps.jira.integracaoponto.conditions.ConditionsHelper;
import com.mindproapps.jira.integracaoponto.model.dto.user.DeParaUserDTO;
import com.mindproapps.jira.integracaoponto.model.dto.user.DeParaUserListRequestDTO;
import com.mindproapps.jira.integracaoponto.rest.client.PontoRestClient;
import com.mindproapps.jira.integracaoponto.service.user.UserService;
import lombok.extern.log4j.Log4j;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Named
@Log4j
@Path("/user")
public class UserRestApi {

    private final ConditionsHelper conditionsHelper;
    private final UserService userService;
    private final PontoRestClient pontoRestClient;

    @Inject
    public UserRestApi(ConditionsHelper conditionsHelper,
                       UserService userService,
                       PontoRestClient pontoRestClient) {
        this.conditionsHelper = conditionsHelper;
        this.userService = userService;
        this.pontoRestClient = pontoRestClient;
    }

    @GET
    @Path("/list")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getList(@QueryParam("startRecord") Integer startRecord,
                            @QueryParam("records") Integer records,
                            @QueryParam("DPType") Integer DPType,
                            @QueryParam("previous") Integer previous,
                            @QueryParam("groups") String groups) {
        if (conditionsHelper.hasUserTempoAdminPermissions() || conditionsHelper.hasUserTempoTeamLeadOrViewTimesheetPermissions()) {
            DeParaUserListRequestDTO requestDTO = new DeParaUserListRequestDTO();
            requestDTO.setDeParaType(DPType);
            requestDTO.setNumberOfRecords(records);
            requestDTO.setStartRecord(startRecord);
            requestDTO.setPrevious(previous);
            requestDTO.setGroups(groups);
            return Response.ok(userService.getUsersList(requestDTO)).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @PUT
    public Response update(List<DeParaUserDTO> deParaUserDTOList) {
        if (conditionsHelper.hasUserTempoAdminPermissions()) {
            userService.updateDeParaUsers(deParaUserDTOList);
            return Response.ok().build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @GET
    @Path("/list/{userKey}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getListByKey(@PathParam("userKey") String userKey) {
        if (conditionsHelper.hasUserTempoAdminPermissions()) {
            return Response.ok(userService.getListByKey(userKey)).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @GET
    @Path("/list/username/all")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getUsernameUserKeyList() {
        if (conditionsHelper.hasUserTempoAdminPermissions()) {
            return Response.ok(userService.getAllUsernameUserKeyList()).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @GET
    @Path("/list/groups/all")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getGroups() {
        if (conditionsHelper.hasUserTempoAdminPermissions()) {
            return Response.ok(userService.getAllGroups()).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}
