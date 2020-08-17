package io.accretio.Controllers;

import io.accretio.Errors.ForbiddenException;
import io.accretio.Errors.NotFoundException;
import io.accretio.Models.Reply;
import io.accretio.Models.User;
import io.accretio.Services.ReplyService;
import io.accretio.Services.UserService;
import io.quarkus.security.identity.SecurityIdentity;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.Principal;
import java.util.List;

@ApplicationScoped
@Path("reply")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReplyController {

    @Inject
    ReplyService replyService;
    @Inject
    SecurityIdentity identity;
    @Inject
    UserService userService;

    @POST
    @Produces("application/json")
    @Transactional
    public Response addreply(Reply reply) {
        replyService.addReply(reply);
        return Response.ok(reply).status(201).build();
    }


   /* @GET
    @Path("{id}")
    @Produces("application/json")
    public Response getreplyById(@PathParam("id") long id) {
        Reply reply = replyService.getReplyById(id);
        return Response.ok(reply).build();
    }*/


    @GET
    @Path("/topic/{id}")
    @Produces("application/json")
    public Response getreplyByTopic(@PathParam("id") Integer id) {
        List<Reply> replies = replyService.getRepliesByTopic(id);
        return Response.ok(replies).status(200).build();
    }

    @PUT
    @Path("/like/{id}")
    @Transactional
    public Response likeReply(@org.jboss.resteasy.annotations.jaxrs.PathParam Integer id) {
        Principal caller = identity.getPrincipal();
        String userName = caller == null ? "none" : caller.getName();
        if (userName.equals("none")) {
            return ForbiddenException.ForbiddenResponse("Invalid Acces token");
        }
        Reply reply =replyService.likeReply(id,userName);
        return Response.ok(reply).build();




    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteReply(@org.jboss.resteasy.annotations.jaxrs.PathParam Integer id) {
        Reply reply = replyService.getReplyById(id);

        if (reply == null) {
            return NotFoundException.NotFoundResponse("Topic with id "+id+" not found");
        }
        replyService.deleteReply(reply);
        return Response.status(204).build();
    }
    @PUT
    @Transactional
    @Path("{id}")
    public Response setReplyUseful(@PathParam("id") long id) {
        Principal caller = identity.getPrincipal();
        String userName = caller == null ? "none" : caller.getName();
        if (userName.equals("none")) {
            return ForbiddenException.ForbiddenResponse("Invalid Acces token");
        }
        Reply reply = replyService.getReplyById(id);
        if (reply == null) {
            return NotFoundException.NotFoundResponse("Reply with id "+id+" not found");
        }
        User user = userService.findUserByUsername(userName);
        if (!reply.getTopic().getUser().getId().equals(user.getId())) {
            return ForbiddenException.ForbiddenResponse("You don't own this topic");
        }

        replyService.setUseful(reply);
        reply.setUseful(!reply.isUseful());
        return Response.ok(reply).build();
    }


}
