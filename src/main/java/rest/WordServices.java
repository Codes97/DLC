package rest;

import entityClasses.Word;
import entityControllers.WordController;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/words")
public class WordServices {
    @Inject
    WordController controller;

    @GET
    @Path("add/{id}/{word}/{fr}/{docs}")
    @Produces("application/json")
    public Response add(@PathParam("id") Integer id,
                        @PathParam("word") String word,
                        @PathParam("fr") Integer fr,
                        @PathParam("docs") Integer docs) {
        Word alahuakbar = controller.add(id, word, fr, docs);
        return Response.ok(alahuakbar).build();
    }
}
