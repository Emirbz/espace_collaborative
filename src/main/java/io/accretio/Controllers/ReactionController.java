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
import io.accretio.Models.Reaction;
import io.accretio.Services.MessageService;
import io.accretio.Services.ReactionService;

@ApplicationScoped
@Path("/reaction")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReactionController {


    @Inject
    ReactionService reactionService;
    @Inject
    MessageService messageService;

    @POST
    @Produces("application/json")
    @Transactional
    public Response addReaction(Reaction reaction) throws ParseException {

        Message entity = messageService.getSigneMessage(reaction.getMessage().getId());
        if (entity == null) {
            return NotFoundException.NotFoundResponse("Message with  not found");
        }


        reactionService.addReaction(reaction);
        return Response.ok(reaction).status(201).build();
    }

    @GET
    @Path("/msg/{id}")
    @Produces("application/json")
    public Response getReactionsByMessage(@PathParam Integer id){
        if (id == null) {
            return NotFoundException.NotFoundResponse("Message id must be not null");
        }
        Message entity = messageService.getSigneMessage(id);
        if (entity == null) {
            return NotFoundException.NotFoundResponse("Message with id "+id+" not found");
        }

        List<Reaction> reactions = reactionService.findByMessage(id);
        return Response.ok(reactions).build();
    }






}
