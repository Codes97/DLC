package rest;


import services.IndexacionService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/Index")
public class IndexacionRest {
    @GET
    @Produces("application/json")
    public Response doGet() {
        IndexacionService x = new IndexacionService();
        return Response.ok("{}").build();
    }
}
