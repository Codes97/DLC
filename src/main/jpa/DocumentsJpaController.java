/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;

import com.mycompany.mavenproject1.exceptions.NonexistentEntityException;
import com.mycompany.mavenproject1.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author John
 */
public class DocumentsJpaController implements Serializable {

    public DocumentsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Documents documents) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(documents);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDocuments(documents.getIdDocuments()) != null) {
                throw new PreexistingEntityException("Documents " + documents + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Documents documents) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            documents = em.merge(documents);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = documents.getIdDocuments();
                if (findDocuments(id) == null) {
                    throw new NonexistentEntityException("The documents with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Documents documents;
            try {
                documents = em.getReference(Documents.class, id);
                documents.getIdDocuments();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The documents with id " + id + " no longer exists.", enfe);
            }
            em.remove(documents);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Documents> findDocumentsEntities() {
        return findDocumentsEntities(true, -1, -1);
    }

    public List<Documents> findDocumentsEntities(int maxResults, int firstResult) {
        return findDocumentsEntities(false, maxResults, firstResult);
    }

    private List<Documents> findDocumentsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Documents.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Documents findDocuments(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Documents.class, id);
        } finally {
            em.close();
        }
    }

    public int getDocumentsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Documents> rt = cq.from(Documents.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
