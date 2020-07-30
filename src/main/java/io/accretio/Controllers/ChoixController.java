package io.accretio.Controllers;


import io.accretio.Models.Choix;
import io.accretio.Models.Message;
import io.accretio.Models.User;
import io.accretio.Services.ChoixService;
import io.accretio.Services.SondageService;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@ApplicationScoped
@Path("/vote")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ChoixController {


    @Inject
    ChoixService choixService;
    @Inject
    SondageService sondageService;

    @POST
    @Produces("application/json")
    @Path("{choixId}")
    @Transactional
    public Response addChoix(@PathParam("choixId") Integer choixId, User user) {


        Choix choix = choixService.getSingleChoix(choixId);
        Message sondage = sondageService.getSigneSondage(choix.getMessage().getId());
        Set<Choix> choixSet = sondage.getChoix();
        System.out.println("one" + Arrays.toString(choixSet.toArray()));
        AtomicReference<User> userVoted = new AtomicReference<>();

        choixSet.forEach(c -> {
            c.getUsers().forEach(u -> {
                if (u.getId().equals(user.getId())) {
                    System.out.println("two" + u.toString());
                    userVoted.set(u);
                }
            });
            if (userVoted.get() != null) {
                c.getUsers().remove(userVoted.get());
            }

        });

        choix.getUsers().add(user);
        return Response.ok(sondage).status(201).build();
    }


 /*   @GET
    @Path("/room/{id}")
    @Produces("application/json")
    public Response getChoixsBySondage(@PathParam("id") Integer sondageId) {
        if (sondageId == null) {
            return NotFoundException.NotFoundResponse("sondage id must be not null");
        }
        Sondage entity = sondageService.getSigneSondage(sondageId);
        if (entity == null) {
            return NotFoundException.NotFoundResponse("sondage with id " + sondageId + " not found");
        }

        List<Choix> choixs = choixService.getChoixBySondage(sondageId);
        return Response.ok(choixs).build();
    }
*/

}
