package rest;


import services.IndexacionService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

// Esta clase recibe los pedidos de la web

@Path("/Index")
public class IndexacionRest {

    //Metodo de prueba, no hace nada
    @GET
    @Produces("application/json")
    public Response doGet() {
        return Response.ok("{}").build();
    }
}
