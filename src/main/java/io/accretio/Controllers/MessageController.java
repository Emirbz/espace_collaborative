package io.accretio.Controllers;


import io.accretio.Models.Message;
import io.accretio.Models.Room;
import io.accretio.Services.MessageService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@ApplicationScoped
@Path("/message")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MessageController {


    @Inject
    MessageService messageService;

    @POST
    @Produces("application/json")
    @Transactional
    public Response addMessage(Message message){
        messageService.addMessage(message);
        return Response.ok(message).status(201).build();
    }
    @GET
    @Produces("application/json")
    public Response getMessages(){
        List<Message> messages = messageService.getMessages();
        return Response.ok(messages).build();
    }

}
