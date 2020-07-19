package io.accretio.Controllers;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.annotations.Form;

import io.accretio.Models.Reply;
import io.accretio.Models.Topic;
import io.accretio.Services.ReplyService;

@ApplicationScoped
@Path("reply")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReplyController {

    @Inject
    private ReplyService replyService;

    @POST
    @Produces("application/json")
    @Transactional
    public Response addreply(Reply reply) {
        replyService.addReply(reply);
        return Response.ok(reply).status(201).build();
    }


    @GET
    @Path("{id}")
    @Produces("application/json")
    public Response getreplyById(@PathParam("id") long id) {
        Reply reply = replyService.getReplyById(id);
        return Response.ok(reply).build();
    }

}