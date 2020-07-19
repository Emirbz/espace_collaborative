package io.accretio.Controllers;


import java.text.ParseException;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.annotations.jaxrs.PathParam;

import io.accretio.Errors.NotFoundException;
import io.accretio.Models.Message;
import io.accretio.Models.Room;
import io.accretio.Services.MessageService;
import io.accretio.Services.RoomService;

@ApplicationScoped
@Path("/msg")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MessageController {


    @Inject
    MessageService messageService;
    @Inject
    RoomService roomService;

    @POST
    @Produces("application/json")
    @Transactional
    public Response addMessage(Message message) throws ParseException {


        messageService.addMessage(message);
        return Response.ok(message).status(201).build();
    }

    @GET
    @Path("/room/images/{id}")
    @Produces("application/json")
    public Response getImagesByRoom(@PathParam Integer id){
        if (id == null) {
            return NotFoundException.NotFoundResponse("Room id must be not null");
        }
        Room entity = roomService.getSigneRoom(id);
        if (entity == null) {
            return NotFoundException.NotFoundResponse("Room with id "+id+" not found");
        }

        List<Message> messages = messageService.findImagesByRoom(id);
        return Response.ok(messages).build();
    }



    @GET
    @Path("/room/{id}")
    @Produces("application/json")
    public Response getMessagesByRoom(@PathParam Integer id){
        if (id == null) {
            return NotFoundException.NotFoundResponse("Room id must be not null");
        }
        Room entity = roomService.getSigneRoom(id);
        if (entity == null) {
            return NotFoundException.NotFoundResponse("Room with id "+id+" not found");
        }

        List<Message> messages = messageService.findByRoom(id);
        return Response.ok(messages).build();
    }



}
