package io.accretio.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import io.accretio.Models.Tag;
import io.accretio.Models.Topic;
import io.accretio.Services.TopicService;

import javax.annotation.Nullable;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@ApplicationScoped
@Path("topic")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TopicController {

    @Inject
    TopicService topicService;

    @POST
    @Produces("application/json")
    @Transactional
    public Response addTopic(Topic topic) {
        topicService.addTopic(topic);
        return Response.ok(topic).status(201).build();
    }

    @POST
    @Path("/tag")
    @Produces("application/json")
    public Response getTopicsByTags(@Nullable List<Tag> tags)  {
        List<Topic> topics = topicService.getTopicsByTags(tags);
        return Response.ok(topics).build();
    }

    @GET
    @Transactional
    @Path("{id}")
    @Produces("application/json")
    public Response getTopicById(@PathParam("id") long id)  {
        Topic topic = topicService.getTopicById(id);
        return Response.ok(topic).build();
    }

    @PUT
    @Path("{id}")
    public Response setTopicInactive(@PathParam("id") long id) {
        Topic topic = topicService.getTopicById(id);
        topicService.setInactive(topic);
        return Response.ok(topic).build();
    }

}
