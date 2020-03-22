package io.accretio.Errors;


import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class NotFoundException extends WebApplicationException {

    public static Response NotFoundResponse(String message) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorVm(message)).type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}