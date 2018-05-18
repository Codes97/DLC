package main;

import entityClasses.Document;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.File;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;

public class DictionaryFlusher implements Runnable {
    Hashtable<String, Integer> diccionario;
    Integer offset;
    File file;

    @PersistenceContext(name="dlc")
    EntityManager em;

    public DictionaryFlusher(File file, Hashtable<String, Integer> dic, int offset){
        diccionario = new Hashtable<String, Integer>(dic);
        this.offset = offset;
        this.file = file;
    }
    @Override
    public void run() {
        flush();
        return;
    }

    private void flush(){
        Document doc = new Document();
        doc.setDocName(file.getName());
        doc.setUrl(file.getPath());
        em.persist(doc);
        //Mete el diccionario en la db y lo limpia, asi puede seguir el proximo libro con el diccionario limpio.
        Set<String> set = diccionario.keySet();
        int id = offset;
        for(String key : set){
            //Insertamos la palabra
            Query q = em.createQuery("insertWord");
            q.setParameter(1, id);
            q.setParameter(2, key);
            q.setParameter(3, diccionario.get(key));
            q.setParameter(4, 1);
            q.setParameter(5, doc.getIdDocument());
            q.executeUpdate();
            //Insertamos la postlist

            id++;
        }
    }
}
