package controllers;

import entityClasses.Document;

import javax.enterprise.context.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

@SessionScoped
@Transactional
public class DocumentJpaController implements Serializable {

    @PersistenceContext(name = "dlc")
    private EntityManager em;

    public void create(Document document) {
        em.persist(document);
        em.flush();
    }

    public void edit(Document document) {
        em.merge(document);
        em.flush();
    }

    public void destroy(int id) {
        Document document;
        document = em.getReference(Document.class, id);
        em.remove(document);
    }

    public List<Document> findDocumentEntities() {
        return findDocumentEntities(true, -1, -1);
    }

    public List<Document> findDocumentEntities(int maxResults, int firstResult) {
        return findDocumentEntities(false, maxResults, firstResult);
    }

    private List<Document> findDocumentEntities(boolean all, int maxResults, int firstResult) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Document.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public Document findDocument(int id) {
        return em.find(Document.class, id) != null ? em.find(Document.class, id) : null;
    }
}
