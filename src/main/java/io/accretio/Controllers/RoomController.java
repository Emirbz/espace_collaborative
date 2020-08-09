package io.accretio.Controllers;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.annotations.jaxrs.PathParam;

import io.accretio.Errors.ForbiddenException;
import io.accretio.Errors.NotFoundException;
import io.accretio.Models.Room;
import io.accretio.Models.User;
import io.accretio.Services.RoomService;

@ApplicationScoped
@Path("/room")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomController {



    @Inject
    RoomService roomService;




    @POST
    @Produces("application/json")
    @Transactional
    public Response addRoom(Room room){

        roomService.addRoom(room);
        return Response.ok(room).status(201).build();
    }

    @GET
    @Produces("application/json")
    public Response getRooms(){
        List<Room> room = roomService.getRoom();
        return Response.ok(room).build();
    }


    @PUT
    @Path("{id}")
    @Transactional
    public Response updateRoom(@PathParam Integer id, Room room) {
        Room entity = roomService.getSigneRoom(id);

        if (entity == null) {
            return NotFoundException.NotFoundResponse("Room with id "+id+" not found");
        }
        roomService.updateRoom(id,room);

        return Response.ok(room).build();


    }
    @PUT
    @Path("/user/{id}")
    @Transactional
    public Response updateRoomUsers(@PathParam Integer id, User user) {
        Room entity = roomService.getSigneRoom(id);

        if (entity.getUsers().stream().anyMatch(user1 -> user1.getId().equals(user.getId())))

        {

                return ForbiddenException.ForbiddenResponse("User Already exists");


        }
        entity.getUsers().add(user);
        Room.persist(entity);


      /*  if (entity == null) {
            return NotFoundException.NotFoundResponse("Room with id "+id+" not found");
        }
        roomService.updateRoom(id,entity);*/

        return Response.ok(entity).build();


    }


    @GET
    @Path("{id}")
    public Response getSingle(@PathParam Integer id) {
        Room entity = roomService.getSigneRoom(id);
        if (entity == null) {
            return NotFoundException.NotFoundResponse("Room with id "+id+" not found");
        }
        return Response.ok(entity).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteRoom(@PathParam Integer id) {
        Room entity = roomService.getSigneRoom(id);

        if (entity == null) {
            return NotFoundException.NotFoundResponse("Room with id "+id+" not found");
        }
        roomService.deleteRoom(entity);
        return Response.status(204).build();
    }

}


