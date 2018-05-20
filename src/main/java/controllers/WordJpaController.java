package controllers;

import entityClasses.Word;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

@ApplicationScoped
@Transactional
public class WordJpaController implements Serializable {

    @PersistenceContext(name = "dlc")
    private EntityManager em;

    public void create(Word word) {
        em.persist(word);
        em.flush();
    }

    public void edit(Word word) {
        em.merge(word);
        em.flush();
    }

    public void destroy(int id) {
        Word word;
        word = em.getReference(Word.class, id);
        em.remove(word);
    }

    public List<Word> findWordEntities() {
        return findWordEntities(true, -1, -1);
    }

    public List<Word> findWordEntities(int maxResults, int firstResult) {
        return findWordEntities(false, maxResults, firstResult);
    }

    private List<Word> findWordEntities(boolean all, int maxResults, int firstResult) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Word.class));
        Query q = em.createQuery(cq);
        if (!all) {
            q.setMaxResults(maxResults);
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }

    public Word findWord(int id) {
        return em.find(Word.class, id) != null ? em.find(Word.class, id) : null;
    }

    public Word findWordByValue(String value) {
        TypedQuery<Word> tq = em.createQuery("FROM Word WHERE word=?", Word.class);
        Word w = null;
        try {
            w = tq.setParameter(1, value).getSingleResult();
        } catch (NoResultException ex) {
        }
        return w;
    }

}
