package io.accretio.Controllers;

import io.accretio.Errors.ForbiddenException;
import io.accretio.Errors.NotFoundException;
import io.accretio.Models.*;
import io.accretio.Services.*;
import io.quarkus.security.identity.SecurityIdentity;
import io.vertx.core.json.JsonObject;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.annotation.Nullable;
import javax.annotation.security.PermitAll;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.Principal;
import java.util.List;

@ApplicationScoped
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {


    @Inject
    UserService userService;

    @Inject
    TopicService topicService;
    @Inject
    BadgeService badgeService;

    @Inject
    ReplyService replyService;
    @Inject
    RoomService roomService;

    @Inject
    SecurityIdentity identity;

    private User loggedUser;

    @GET
    @Path("/topic")
    @Produces(MediaType.APPLICATION_JSON)
    @NoCache
    public Response myTopics(@Nullable @DefaultValue("") @QueryParam("name") String name) {
        if (getUserIdentity())
        {
            return ForbiddenException.ForbiddenResponse("Invalid Acces token");
        }
        List<Topic> topics = topicService.getMyTopics(loggedUser, name);


        return Response.ok(topics).status(200).build();
    }

    public boolean getUserIdentity() {
        Principal caller = identity.getPrincipal();
        String userName = caller == null ? "none" : caller.getName();
        if (userName.equals("none")) {
            return true;

        }
        loggedUser = userService.findUserByUsername(userName);
        return loggedUser == null;
    }

    @GET
    @Path("/reply")
    @Produces(MediaType.APPLICATION_JSON)
    @NoCache
    public Response myReplies(@Nullable @DefaultValue("") @QueryParam("name") String name) {
        if (getUserIdentity())
        {
            return ForbiddenException.ForbiddenResponse("Invalid Acces token");
        }
        List<Reply> replies = replyService.getMyReplies(loggedUser, name);


        return Response.ok(replies).status(200).build();
    }


    @GET
    @Path("/room")
    @Produces(MediaType.APPLICATION_JSON)
    @NoCache
    public Response myRooms(@Nullable @DefaultValue("") @QueryParam("name") String name) {
        if (getUserIdentity())
        {
            return ForbiddenException.ForbiddenResponse("Invalid Acces token");
        }
        List<Room> rooms = roomService.getMyRooms(loggedUser, name);

        return Response.ok(rooms).status(200).build();
    }

    @GET
    @Path("/room/created")
    @Produces(MediaType.APPLICATION_JSON)
    @NoCache
    public Response myCreatedRooms(@Nullable @DefaultValue("") @QueryParam("name") String name) {
        if (getUserIdentity())
        {
            return ForbiddenException.ForbiddenResponse("Invalid Acces token");
        }
        List<Room> rooms = roomService.getMyCreatedRooms(loggedUser, name);

        return Response.ok(rooms).status(200).build();
    }

    @GET
    @Path("/stats")
    @Produces(MediaType.APPLICATION_JSON)
    @NoCache
    public Response myStats() {
        if (getUserIdentity())
        {
            return ForbiddenException.ForbiddenResponse("Invalid Acces token");
        }

        JsonObject response = userService.getUserStats(loggedUser);

        return Response.ok(response).status(200).build();
    }

    @GET
    @Path("/badge")
    @Produces(MediaType.APPLICATION_JSON)
    @NoCache
    public Response myBadges() {
        if (getUserIdentity())
        {
            return ForbiddenException.ForbiddenResponse("Invalid Acces token");
        }
        List<Badge> badges = badgeService.getMyBadges(loggedUser);

        return Response.ok(badges).status(200).build();
    }

    @GET
    @Path("/me")
    @Produces(MediaType.APPLICATION_JSON)
    @NoCache
    public Response me() {
        if (getUserIdentity())
        {
            return ForbiddenException.ForbiddenResponse("Invalid Acces token");
        }
        return Response.ok(loggedUser).status(200).build();
    }


    @POST
    @Produces("application/json")

    public Response addUser(User user) {
        userService.addUser(user);
        return Response.ok(user).status(201).build();
    }

    @GET
    @Produces("application/json")
    @NoCache
    @PermitAll
    public Response getUsers() {
        List<User> user = userService.getUser();
        return Response.ok(user).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Response updateUser(@PathParam String id, User user) {
        User entity = userService.getSigneUser(id);


        if (entity == null) {
            return NotFoundException.NotFoundResponse("User with id " + id + " not found");
        }
        userService.updateUser(id, user);

        return Response.ok(user).build();


    }


    @GET
    @Path("{id}")
    public Response getSingle(@PathParam String id) {
        User entity = userService.getSigneUser(id);
        if (entity == null) {
            return NotFoundException.NotFoundResponse("User with id " + id + " not found");
        }
        return Response.ok(entity).build();
    }

    @GET
    @Path("/name/{searchToken}")
    public Response getUserByName(@PathParam String searchToken) {
        List<User> userList = userService.findUserByName(searchToken);
        if (userList.isEmpty()) {
            return NotFoundException.NotFoundResponse("User with name " + searchToken + " not found");
        }
        return Response.ok(userList).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response deleteUser(@PathParam String id) {
        User entity = userService.getSigneUser(id);

        if (entity == null) {
            return NotFoundException.NotFoundResponse("User with id " + id + " not found");
        }
        userService.deleteUser(entity);
        return Response.status(204).build();
    }
}


