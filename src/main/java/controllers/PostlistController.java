package controllers;

import entityClasses.Postlist;
import entityClasses.PostlistPK;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ApplicationScoped
@Transactional
public class PostlistController implements Serializable {

    @PersistenceContext(name = "dlc")
    private EntityManager em;

    public ArrayList<Postlist> findPostlistByWords(Integer[] value) {
        TypedQuery<Postlist> tq = em.createQuery("SELECT pl FROM Postlist pl WHERE pl.idWord IN (:idWordList)", Postlist.class);
        ArrayList<Postlist> postlists = null;
        try {
            postlists = new ArrayList<>(tq.setParameter("idWordList", Arrays.asList(value)).getResultList());
        } catch (NoResultException ex) {
        }
        return postlists;
    }
}
