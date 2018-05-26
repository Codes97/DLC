package controllers;

import entityClasses.Word;
import services.IndexacionService;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.*;

@ApplicationScoped
@Transactional
public class WordJpaController implements Serializable {

    @PersistenceContext(name = "dlc")
    private EntityManager em;

    private String wordInsertString = "INSERT INTO words " +
            "(idWord, word, maxFrequency, maxDocuments) VALUES ";

    private String wordInsertStringEnd = " ON DUPLICATE KEY UPDATE maxDocuments = maxDocuments + 1;";

    private String plistInsertString = "INSERT INTO postlist " +
            "(idDocument, idWord, frequency) VALUES ";

    public void create(Word word) {
        em.persist(word);
    }

    public void edit(Word word) {
        em.merge(word);
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

    public void flush() {
        em.flush();
        em.clear();
    }

    public ArrayList<Word> findWordsByValue(String[] values) {
        TypedQuery<Word> tq = em.createQuery("FROM Word WHERE word IN (:sWordList)", Word.class);
        ArrayList<Word> words = null;
        try {
            words = new ArrayList<Word>(tq.setParameter("sWordList", Arrays.asList(values)).getResultList());
        } catch (NoResultException ex) {
        }
        return words;
    }

    public void bulkInsertStringBuilder(Hashtable<String, Integer> tempWords, int docId) {
        int counter = 0;
        StringBuilder words = new StringBuilder(wordInsertString);
        StringBuilder postlist = new StringBuilder(plistInsertString);
        for (Map.Entry<String, Integer> e : tempWords.entrySet()) {
            if (IndexacionService.vocabulary.containsKey(e.getKey())) {
                postlist.append("(" + docId + ", " + IndexacionService.vocabulary.get(e.getKey()) + ", " + e.getValue() + "), ");
                words.append("(" + IndexacionService.vocabulary.get(e.getKey()) + ", \'" + e.getKey() + "\', " + 1 + ", " + 1 + "), ");
            } else {
                words.append("(" + IndexacionService.WORD_ID + ", \'" + e.getKey() + "\', " + 1 + ", " + 1 + "), ");
                postlist.append("(" + docId + ", " + IndexacionService.WORD_ID + ", " + e.getValue() + "), ");
                IndexacionService.vocabulary.put(e.getKey(), IndexacionService.WORD_ID);
                IndexacionService.WORD_ID++;
            }
            counter++;
            if (counter >= IndexacionService.BATCH_SIZE) {
                words.setLength(words.length() - 2);
                words.append(wordInsertStringEnd);
                postlist.setLength(postlist.length() - 2);
                insertWords(words.toString());
                insertPostlist(postlist.toString());
                words = new StringBuilder(wordInsertString);
                postlist = new StringBuilder(plistInsertString);
                counter = 0;
            }
        }
        words.setLength(words.length() - 2);
        words.append(wordInsertStringEnd);
        postlist.setLength(postlist.length() - 2);
        insertWords(words.toString());
        insertPostlist(postlist.toString());
        IndexacionService.WORD_ID++;
    }

    private void insertWords(String s) {
        Query q = em.createNativeQuery(s);
        q.executeUpdate();
        em.clear();
    }

    private void insertPostlist(String s) {
        Query q = em.createNativeQuery(s);
        q.executeUpdate();
        em.clear();
    }
}
