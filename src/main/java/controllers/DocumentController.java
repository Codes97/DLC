package controllers;

import entityClasses.Document;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.*;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

@ApplicationScoped
@Transactional
public class DocumentController implements Serializable {

    @PersistenceContext(name = "dlc")
    private EntityManager em;

    public void create(Document document) {
        em.persist(document);
    }

    public ArrayList<Document> findDocumentsById(Integer[] values) {
        TypedQuery<Document> tq = em.createQuery("SELECT d FROM Document d WHERE d.idDocument IN (:idDocList)", Document.class);
        ArrayList<Document> documents = null;
        try {
            documents = new ArrayList<>(tq.setParameter("idDocList", Arrays.asList(values)).getResultList());
        } catch (NoResultException ex) {
        }
        return documents;
    }

    public int getMaxId() {
        Query q = em.createNativeQuery("SELECT MAX(d.idDocument) FROM Document d");
        return (int) q.getSingleResult();
    }

    public long countDocuments() {
        Query q = em.createQuery("SELECT COUNT(d.idDocument) FROM Document d");
        return (long) q.getSingleResult();
    }

    public void flush() {
        em.flush();
        em.clear();
    }
}
