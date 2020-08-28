package io.accretio.Controllers;

import io.accretio.Errors.ForbiddenException;
import io.accretio.Errors.NotFoundException;
import io.accretio.Models.RoomRequest;
import io.accretio.Models.User;
import io.accretio.Services.RoomRequestService;
import io.accretio.Services.UserService;
import io.quarkus.security.identity.SecurityIdentity;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.Principal;
import java.util.List;

@ApplicationScoped
@Path("roomrequest")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomRequestController {
    private User loggedUser;


    @Inject
    UserService userService;

    @Inject
    SecurityIdentity identity;

    @Inject
    RoomRequestService roomRequestService;

    public boolean getUserIdentity() {
        Principal caller = identity.getPrincipal();
        String userName = caller == null ? "none" : caller.getName();
        if (userName.equals("none")) {
            return true;

        }
        loggedUser = userService.findUserByUsername(userName);
        return loggedUser == null;
    }

    @GET
    @Produces("application/json")
    @Transactional
    public Response getMyRequests() {
        if (getUserIdentity()) {
            return ForbiddenException.ForbiddenResponse("Invalid Acces token");
        }

        List<RoomRequest> roomRequests = roomRequestService.getMyRequests(loggedUser);
        return Response.ok(roomRequests).status(200).build();
    }

    @PUT
    @Produces("application/json")
    @Transactional
    @Path("/accept/{id}")
    public Response acceptRequest(@PathParam Integer id) {
        if (getUserIdentity()) {
            return ForbiddenException.ForbiddenResponse("Invalid Acces token");
        }
        RoomRequest roomRequest = roomRequestService.getSingleRoomRequest(id);

        if (roomRequest == null) {
            return NotFoundException.NotFoundResponse("RoomRequest with id " + id + " not found");
        }
        if (!roomRequest.getRoom().getUser().getId().equals(loggedUser.getId())) {
            return ForbiddenException.ForbiddenResponse("You don't own this room");
        }
        roomRequestService.acceptRequest(roomRequest);
        return Response.ok().status(200).build();
    }
    @PUT
    @Produces("application/json")
    @Transactional
    @Path("/reject/{id}")
    public Response RejectRequest(@PathParam Integer id) {
        if (getUserIdentity()) {
            return ForbiddenException.ForbiddenResponse("Invalid Acces token");
        }
        RoomRequest roomRequest = roomRequestService.getSingleRoomRequest(id);

        if (roomRequest == null) {
            return NotFoundException.NotFoundResponse("RoomRequest with id " + id + " not found");
        }
        if (!roomRequest.getRoom().getUser().getId().equals(loggedUser.getId())) {
            return ForbiddenException.ForbiddenResponse("You don't own this room");
        }
        roomRequestService.rejectRequest(roomRequest);
        return Response.ok().status(200).build();
    }


}
