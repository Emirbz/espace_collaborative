package io.accretio.Errors;


import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ForbiddenException extends WebApplicationException {

    public static Response ForbiddenResponse(String message) {
        return Response.status(Response.Status.FORBIDDEN)
                .entity(new ErrorVm(message)).type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}