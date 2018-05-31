package rest;


import services.IndexacionService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

// Esta clase recibe los pedidos de la web

@Path("/indexar")
public class IndexacionEndpoint {

    @Inject
    IndexacionService indexService;

    //Metodo de prueba, no hace nada
    @GET
    @Produces("application/json")
    @Path("/{id}")
    public Response doGet(@PathParam("id") String id) {
        indexService.startIndexing(id);
        return Response.ok("Sea muy feliz!").build();
    }

}
