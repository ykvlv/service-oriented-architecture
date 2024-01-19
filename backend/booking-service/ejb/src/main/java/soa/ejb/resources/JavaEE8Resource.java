package soa.ejb.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;


@Path("javaee8")
public class JavaEE8Resource {
    
    @GET
    @Path("ping")
    public Response ping(){
        return Response
                .ok("ping")
                .build();
    }
}
