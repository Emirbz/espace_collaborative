package io.accretio.Controllers;

import io.accretio.Errors.ForbiddenException;
import io.accretio.Errors.NotFoundException;
import io.accretio.Models.Room;
import io.accretio.Models.User;
import io.accretio.Services.RoomService;
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
@Path("/room")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomController {

    private User loggedUser;


    @Inject
    RoomService roomService;

    @Inject
    SecurityIdentity identity;

    @Inject
    UserService userService;


    @POST
    @Produces("application/json")
    @Transactional
    public Response addRoom(Room room) {

        roomService.addRoom(room);
        return Response.ok(room).status(201).build();
    }

    @GET
    @Produces("application/json")
    public Response getRooms() {
        List<Room> room = roomService.getRoom();
        return Response.ok(room).build();
    }


    @PUT
    @Path("{id}")
    @Transactional
    public Response updateRoom(@PathParam Integer id, Room room) {
        Room entity = roomService.getSigneRoom(id);

        if (entity == null) {
            return NotFoundException.NotFoundResponse("Room with id " + id + " not found");
        }
        roomService.updateRoom(id, room);

        return Response.ok(room).build();


    }

    @PUT
    @Path("/user/{id}")
    @Transactional
    public Response updateRoomUsers(@PathParam Integer id, User user) {
        Room entity = roomService.getSigneRoom(id);
        if (entity == null) {
            return NotFoundException.NotFoundResponse("Room with id " + id + " not found");
        }

        if (entity.getUsers().stream().anyMatch(user1 -> user1.getId().equals(user.getId()))) {
            return Response.ok(entity).status(204).build();

        }
        Room room = roomService.addUsers(entity, user);
        return Response.ok(room).status(200).build();


    }
    public boolean getUserIdentity() {
        Principal caller = identity.getPrincipal();
        String userName = caller == null ? "none" : caller.getName();
        if (userName.equals("none")) {
            return true;

        }
        loggedUser = userService.findUserByUsername(userName);
        return false;
    }

    @PUT
    @Path("/join/{id}")
    @Transactional
    public Response joinRoom(@PathParam Integer id) {
        Room entity = roomService.getSigneRoom(id);
        if (entity == null) {
            return NotFoundException.NotFoundResponse("Room with id " + id + " not found");
        }
        if (getUserIdentity())
        {
            return ForbiddenException.ForbiddenResponse("Invalid Acces token");
        }
        if (entity.getUsers().stream().anyMatch(user1 -> user1.getId().equals(loggedUser.getId()))) {
            return Response.ok(entity).status(204).build();
        }
        Room room = roomService.addUsers(entity, loggedUser);
        return Response.ok(room).status(200).build();

    }

    @PUT
    @Path("/leav/{id}")
    @Transactional
    public Response leavRoom(@PathParam Integer id) {
        Room entity = roomService.getSigneRoom(id);
        if (entity == null) {
            return NotFoundException.NotFoundResponse("Room with id " + id + " not found");
        }
        if (getUserIdentity())
        {
            return ForbiddenException.ForbiddenResponse("Invalid Acces token");
        }

        if (entity.getUsers().stream().noneMatch(user1 -> user1.getId().equals(loggedUser.getId()))) {
            return ForbiddenException.ForbiddenResponse("User is not part of this room");
        }
        Room room = roomService.removeUser(entity, loggedUser);
        return Response.ok(room).status(200).build();

    }

    @GET
    @Path("{id}")
    public Response getSingle(@PathParam Integer id) {
        Room entity = roomService.getSigneRoom(id);
        if (entity == null) {
            return NotFoundException.NotFoundResponse("Room with id " + id + " not found");
        }
        return Response.ok(entity).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteRoom(@PathParam Integer id) {
        Room entity = roomService.getSigneRoom(id);

        if (entity == null) {
            return NotFoundException.NotFoundResponse("Room with id " + id + " not found");
        }
        roomService.deleteRoom(entity);
        return Response.status(204).build();
    }





}


