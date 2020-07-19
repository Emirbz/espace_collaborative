package io.accretio.Controllers;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import io.accretio.Models.TopicCategory;
import io.accretio.Services.TopicCategoryService;

@ApplicationScoped
@Path("topicCategory")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TopicCategoryController {

    @Inject
    private TopicCategoryService topicCategoryService;

    @GET
    @Produces("application/json")
    public Response getCategories() throws JsonProcessingException {
        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
        filterProvider.addFilter("replies", SimpleBeanPropertyFilter.serializeAllExcept("replies"));

        List<TopicCategory> categories = topicCategoryService.getCategories();

        categories.forEach(c -> {
            c.setCountTopics(c.getTopics().size());
        });

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setFilterProvider(filterProvider);
        objectMapper.writeValueAsString(categories);
        return Response.ok(categories).build();
    }

}
