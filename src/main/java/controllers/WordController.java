package controllers;

import entityClasses.Word;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.*;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

@ApplicationScoped
@Transactional
public class WordController implements Serializable {

    @PersistenceContext(name = "dlc")
    private EntityManager em;

    public ArrayList<Word> findWordsByValue(String[] values) {
        TypedQuery<Word> tq = em.createQuery("SELECT w FROM Word w WHERE w.word IN (:sWordList)", Word.class);
        ArrayList<Word> words = null;
        try {
            words = new ArrayList<>(tq.setParameter("sWordList", Arrays.asList(values)).getResultList());
        } catch (NoResultException ex) {
        }
        return words;
    }

    public int getMaxId() {
        Query q = em.createQuery("SELECT COALESCE(MAX(w.idWord), 0) FROM Word w");
        return (int) q.getSingleResult();
    }

    public Hashtable<String, Integer> getVocabulary() {
        TypedQuery<Word> tq = em.createQuery("SELECT w FROM Word w", Word.class);
        ArrayList<Word> words;
        Hashtable<String, Integer> voc = new Hashtable<String, Integer>();
        try {
            words = new ArrayList<>(tq.getResultList());
            for (Word word : words) {
                voc.put(word.getWord(), word.getIdWord());
            }
        } catch (NoResultException ex) {
        }
        return voc;
    }
}
