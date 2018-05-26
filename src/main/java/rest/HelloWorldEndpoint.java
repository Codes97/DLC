package rest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/hola")
@Transactional
public class HelloWorldEndpoint {
    @PersistenceContext(name = "dlc")
    private EntityManager entityManager;

    @GET
    @Produces("application/json")
    public Response doGet() {
        StringBuilder s = new StringBuilder("INSERT INTO words " +
                "(idWord, word, maxFrequency, maxDocuments) VALUES ");
        for (int i = 0; i < 5000; i++) {
            s.append("(1,\'hola\',1,1), ");
        }
        s.setLength(s.length()-2);
        s.append(" ON DUPLICATE KEY UPDATE maxDocuments = maxDocuments + 1;");
        Query q = entityManager.createNativeQuery(s.toString());
        System.out.println(s.toString());
        long i = System.nanoTime();
        q.executeUpdate();
        long f = System.nanoTime();
        return Response.ok(i-f).build();
    }
}
