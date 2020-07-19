package io.accretio.Controllers;

import io.accretio.Models.Topic;
import io.accretio.Models.TopicCategory;
import io.accretio.Services.TopicCategoryService;
import io.accretio.Services.TopicService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import java.util.List;

@ApplicationScoped
@Path("topic")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TopicController {

    @Inject
    private TopicService topicService;
    @Inject
    private TopicCategoryService topicCategoryService;

    @POST
    @Produces("application/json")
    @Transactional
    public Response addTopic(Topic topic) {
        topicService.addTopic(topic);
        return Response.ok(topic).status(201).build();
    }

    @GET
    @Path("category/{id}")
    @Produces("application/json")
    public Response getTopicsByCategory(@PathParam("id") long id) throws JsonProcessingException {
        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
        filterProvider.addFilter("replies", SimpleBeanPropertyFilter.serializeAllExcept("replies"));

        TopicCategory topicCategory = topicCategoryService.getCategory(id);
        List<Topic> topics = topicService.getTopicsByCategory(topicCategory);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setFilterProvider(filterProvider);
        objectMapper.writeValueAsString(topics);

        return Response.ok(topics).build();
    }

    @GET
    @Path("{id}")
    @Produces("application/json")
    public Response getTopicById(@PathParam("id") long id) throws JsonProcessingException {
        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
        filterProvider.addFilter("replies", SimpleBeanPropertyFilter.serializeAll());
        Topic topic = topicService.getTopicById(id);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setFilterProvider(filterProvider);
        objectMapper.writeValueAsString(topic);
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
