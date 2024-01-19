package soa.ejb.exception;

import soa.ejb.model.ErrorDTO;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;

public class ClientException extends WebApplicationException {
   public ClientException(String message) {
       super(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(
                new ErrorDTO("500", "Client is unreachable because of:\n" + message)
               ).type(MediaType.APPLICATION_JSON).build());
   }
}
