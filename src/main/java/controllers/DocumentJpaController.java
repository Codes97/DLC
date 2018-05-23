package controllers;

import entityClasses.Document;
import entityClasses.Postlist;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@Transactional
public class DocumentJpaController implements Serializable {

    @PersistenceContext(name = "dlc")
    private EntityManager em;

    public void create(Document document) {
        em.persist(document);
    }

    public void edit(Document document) {
        em.merge(document);
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
        return em.find(Document.class, id);
    }

    public Document findDocumentByUrl(String value) {
        TypedQuery<Document> tq = em.createQuery("FROM Document WHERE url=?", Document.class);
        Document doc = null;
        try {
            doc = tq.setParameter(1, value).getSingleResult();
        } catch (NoResultException ex) {
        }
        return doc;
    }

    public ArrayList<Postlist> findDocumentsByWords(Integer[] value) {
        TypedQuery<Postlist> tq = em.createQuery("FROM Postlist WHERE idword IN (:idWordList)", Postlist.class);
        ArrayList<Postlist> postlists = null;
        try {
            postlists = new ArrayList<Postlist>(tq.setParameter("idWordList", value).getResultList());
        } catch (NoResultException ex) {
        }
        return postlists;
    }

    public void flush() {
        em.flush();
        em.clear();
    }

//    public int getMaxId() {
//
//    }
}
