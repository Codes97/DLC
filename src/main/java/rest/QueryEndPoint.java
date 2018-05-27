package rest;

import services.QueryService;
import entityClasses.Document;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/search")
public class QueryEndPoint {
    @Inject
    QueryService q;

    @GET
    @Path("{string}")
    @Produces("application/json")
    public Response getQuery(@PathParam("string") String s) {
        if (q.search(s)) {
            Document[] d = q.getSortedDocuments();
            return Response.ok(d).build();
        }
        return Response.ok(false).build();
    }
}
