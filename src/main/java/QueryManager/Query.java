package QueryManager;

import Indexer.Parser;
import controllers.DocumentJpaController;
import controllers.PostlistJpaController;
import controllers.WordJpaController;
import entityClasses.Document;
import entityClasses.Postlist;
import entityClasses.Word;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Hashtable;

@ApplicationScoped
public class Query {
    @Inject
    WordJpaController wordCon;
    @Inject
    DocumentJpaController docCon;
    @Inject
    PostlistJpaController plCon;
    private Hashtable<Integer, Word> words;
    private Hashtable<Integer, Document> documents;
    private ArrayList<Postlist> postlists;

    public void setWords(String q) {
        words = new Hashtable<Integer, Word>();
        String[] w = q.split("(?U)[^\\p{IsAlphabetic}']+");
        for (int i = 0; i <= w.length; i++) {
            w[i] = Parser.checkWord(w[i]);
        }
        ArrayList<Word> arrayWords = wordCon.findWordsByValue(w);
        for (Word a : arrayWords) {
            if (!words.containsKey(a.getIdWord()))
                words.put(a.getIdWord(), a);
        }
    }

    private Integer[] getIdWords() {
        return words.keySet().toArray(new Integer[words.size()]);
    }

    private void setPlCon() {
        postlists = plCon.findPostlistByWords(getIdWords());
    }

    private Integer[] getIdDocuments() {
        ArrayList<Integer> idDoc = new ArrayList<Integer>();
        for (Postlist pl : postlists) {
            if (!idDoc.contains(pl.getIdDocument())) idDoc.add(pl.getIdDocument());
        }
        return idDoc.toArray(new Integer[idDoc.size()]);
    }

    private void setDocuments() {

    }
}
