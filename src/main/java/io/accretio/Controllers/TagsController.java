package io.accretio.Controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import io.accretio.Models.Tag;
import io.accretio.Models.Topic;
import io.accretio.Services.TagService;
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
@Path("tag")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TagsController {

    @Inject
    TagService tagService;

    @GET
    @Produces("application/json")
    @Transactional
    public Response getAllTags() {
        List<Tag> tags =tagService.getAllTags();
        return Response.ok(tags).status(200).build();
    }

    @GET
    @Produces("application/json")
    @Path("/popular")
    @Transactional
    public Response getPopularTags() {
        List<Tag> tags =tagService.getPopularTags();
        return Response.ok(tags).status(200).build();
    }

    @GET
    @Path("/{name}")
    @Produces("application/json")
    public Response getTagsByName(@PathParam("name") String name) {

        List<Tag> tags = tagService.searchTag(name);

        return Response.ok(tags).status(200).build();
    }

}
