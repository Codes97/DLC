package rest;

import controllers.DocumentJpaController;
import controllers.WordJpaController;
import entityClasses.Word;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/testing")
public class TestEndPoint {
    @Inject
    private WordJpaController wordCon;
    @Inject
    private DocumentJpaController docCon;

    @GET
    @Path("/add")
    @Produces("application/json")
    public Response adding() {
        Word w = new Word(1, "one", 1, 1);
        wordCon.create(w);
/*        Document a = new Document();
        a.setDocName("a");
        a.setUrl("a");
        docCon.create(a);*/
        return Response.ok(w).build();
    }

    @GET
    @Path("/remove")
    @Produces("application/json")
    public Response removing() {
        Word w = new Word(1, "one", 1, 1);
        wordCon.destroy(1);
        return Response.accepted().build();
    }
}
