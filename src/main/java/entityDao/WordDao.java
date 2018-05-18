package entityDao;

import entityClasses.Word;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@ApplicationScoped
@Transactional
public class WordDao {
    @PersistenceContext(name = "dlc")
    private EntityManager em;

     public void add(Word w) {
         em.persist(w);
         em.flush();
     }

}
