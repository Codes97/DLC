package controllers;

import entityClasses.Postlist;
import entityClasses.PostlistPK;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

@ApplicationScoped
@Transactional
public class PostlistJpaController implements Serializable {

    @PersistenceContext(name = "dlc")
    private EntityManager em;

    public void create(Postlist postlist) {
        em.persist(postlist);
        em.flush();
    }

    public void edit(Postlist postlist) {
        em.merge(postlist);
        em.flush();
    }

    public void destroy(PostlistPK pk) {
        Postlist postlist;
        postlist = em.getReference(Postlist.class, pk);
        em.remove(postlist);
    }

    public List<Postlist> findPostlistEntities() {
        return findPostlistEntities(true, -1, -1);
    }

    public List<Postlist> findPostlistEntities(int maxResults, int firstResult) {
        return findPostlistEntities(false, maxResults, firstResult);
    }

    private List<Postlist> findPostlistEntities(boolean all, int maxResults, int firstResult) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Postlist.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public Postlist findPostlist(PostlistPK pk) {
        return em.find(Postlist.class, pk) != null ? em.find(Postlist.class, pk) : null;
    }
}
