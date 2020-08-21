package io.accretio.Controllers;

import io.accretio.Models.Badge;
import io.accretio.Models.Tag;
import io.accretio.Services.BadgeService;
import io.accretio.Services.TagService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@ApplicationScoped
@Path("badge")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BadgeController {

    @Inject
    BadgeService badgeService;

    @GET
    @Produces("application/json")
    @Transactional
    public Response getAllBadges() {
        List<Badge> badges =badgeService.getAllBadges();
        return Response.ok(badges).status(200).build();
    }


}
