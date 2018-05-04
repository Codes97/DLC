package rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
@Path("/hola")
public class HelloWorldEndpoint {

    @GET
    @Produces("text/html")
    public Response doGet() {
        return Response.ok("<h1>Hola Mundo!</h1>").build();
    }
}
