package io.accretio.Errors;


import javax.ws.rs.core.Response;
import java.util.Optional;

/**
 * Utility class for Response creation.
 */
public interface ResponseUtil {


    static <X> Response wrapOrNotFound(Optional<X> maybeResponse ) {
        return maybeResponse.map(Response::ok)
                .orElse(Response.status(Response.Status.NOT_FOUND)).build();
    }
}