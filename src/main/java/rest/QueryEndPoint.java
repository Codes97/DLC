package rest;

import services.QueryService;
import entityClasses.Document;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/search")
public class QueryEndPoint {
    @Inject
    QueryService q;

    @GET
    @Path("{string}")
    @Produces("application/json")
    public Response getQuery(@PathParam("string") String Nombre) {
        if (q.search(Nombre)) {
            Document[] d = q.getSortedDocuments();
            return Response.ok(d).build();
        }
        return Response.ok(false).build();
    }
}
