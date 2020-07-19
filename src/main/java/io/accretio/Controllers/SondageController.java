package io.accretio.Controllers;


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
import io.accretio.Services.RoomService;
import io.accretio.Services.SondageService;

@ApplicationScoped
@Path("sondage")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SondageController {


    @Inject
    SondageService sondageService;
    @Inject
    RoomService roomService;

    @POST
    @Produces("application/json")
    @Transactional
    public Response addSondage(Message sondage) {

        Room entity = roomService.getSigneRoom(sondage.getRoom().getId());
        if (entity == null) {
            return NotFoundException.NotFoundResponse("Room with  not found");
        }

        sondageService.addSondage(sondage);
        return Response.ok(sondage).status(201).build();
    }

    @GET
    @Path("/room/{id}")
    @Produces("application/json")
    public Response getSondagesByRoom(@PathParam("id") Integer roomId){
        if (roomId == null) {
            return NotFoundException.NotFoundResponse("room id must be not null");
        }
        Room entity = roomService.getSigneRoom(roomId);
        if (entity == null) {
            return NotFoundException.NotFoundResponse("room with id "+roomId+" not found");
        }

        List<Message> sondages = sondageService.findByRoom(roomId);
        return Response.ok(sondages).build();
    }






}
