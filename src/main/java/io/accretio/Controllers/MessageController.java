package io.accretio.Controllers;


import io.accretio.Errors.NotFoundException;
import io.accretio.Minio.MinioFileService;
import io.accretio.Models.Message;
import io.accretio.Models.Room;
import io.accretio.Services.MessageService;
import io.accretio.Services.RoomService;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
@Path("/msg")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MessageController {


    @Inject
    MessageService messageService;
    @Inject
    RoomService roomService;


    @Inject
    MinioFileService minioFileService;

    @POST
    @Produces("application/json")
    @Transactional
    public Response addMessage(Message message)  {


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


    @Path("/files/metaData/{fileId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String,String> getFileMetaData(@PathParam String fileId){

        Map<String,String> metadata;
        try{
            metadata= minioFileService.getFileMetaData(fileId);
        }catch(Exception e){
            metadata=new HashMap<>();
            e.printStackTrace();
        }
        return metadata;

    }



}
