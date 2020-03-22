package io.accretio.Controllers;

import io.accretio.Errors.NotFoundException;
import io.accretio.Models.User;
import io.accretio.Services.UserService;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@ApplicationScoped
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {



    @Inject
    private UserService userService;


    @POST
    @Produces("application/json")
    public Response addUser(User user){
        userService.addUser(user);
        return Response.ok(user).status(201).build();
    }
    @GET
    @Produces("application/json")
    public Response getUsers(){
        List<User> user = userService.getUser();
        return Response.ok(user).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response updateUser(@PathParam Integer id, User user) {
        User entity = userService.getSigneUser(id);


        if (entity == null) {
            return NotFoundException.NotFoundResponse("User with id "+id+" not found");
        }
        userService.updateUser(id,user);

        return Response.ok(user).build();


    }


    @GET
    @Path("{id}")
    public Response getSingle(@PathParam Integer id) {
        User entity = userService.getSigneUser(id);
        if (entity == null) {
            return NotFoundException.NotFoundResponse("User with id "+id+" not found");
        }
        return Response.ok(entity).build();
    }
    @GET
    @Path("/name/{searchToken}")
    public Response getUserByName(@PathParam String searchToken) {
        List <User> userList = userService.findUserByName(searchToken);
        if (userList.isEmpty()) {
            return NotFoundException.NotFoundResponse("User with name "+searchToken+" not found");
        }
        return Response.ok(userList).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteUser(@PathParam Integer id) {
        User entity = userService.getSigneUser(id);

        if (entity == null) {
            return NotFoundException.NotFoundResponse("User with id "+id+" not found");
        }
        userService.deleteUser(entity);
        return Response.status(204).build();
    }
}


