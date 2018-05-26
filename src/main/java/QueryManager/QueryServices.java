package QueryManager;

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
public class QueryServices {
    @Inject
    WordJpaController wordCon;
    @Inject
    DocumentJpaController docCon;
    @Inject
    PostlistJpaController plCon;
    private Integer NUMBER_OF_DOCUMENTS = 593;
    private Hashtable<Integer, Word> words;
    private Hashtable<Integer, Document> documents;
    private ArrayList<Document> sortedDocuments;
    private ArrayList<Postlist> postlists;

    public QueryServices() {
    }

    public void setParams(String s) {
        setWords(s);
        setPlist();
        setDocuments();
        calculateRanking();
    }

    public Document[] getSortedDocuments() {
        return sortedDocuments.toArray(new Document[sortedDocuments.size()]);
    }

    public void setWords(String q) {
        words = new Hashtable<Integer, Word>();
        String[] w = q.split("(?U)[^\\p{IsAlphabetic}']+");
        for (int i = 0; i < w.length; i++) {
            w[i] = w[i].replaceAll("([.,\\-\"()'°ª:;¿?_*|~€¬&=!¡<>\\[\\]#@«»$%]|[0-9])+", "");
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

    private void setPlist() {
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
        documents = new Hashtable<Integer, Document>();
        ArrayList<Document> docs = docCon.findDocumentsById(getIdDocuments());
        for (Document d : docs) {
            documents.put(d.getIdDocument(), d);
        }
    }

    private void calculateRanking() {
        sortedDocuments = new ArrayList<Document>();
        Document docTemp;
        float rankTemp;
        for (Integer idDoc : documents.keySet().toArray(new Integer[documents.size()])) {
            docTemp = documents.get(idDoc);
            rankTemp = 0;
            for (Postlist pl : postlists) {
                if (pl.getIdDocument() == idDoc) {
                    rankTemp += calculateW(words.get(pl.getIdWord()).getMaxDocuments(), pl.getFrequency());
                }
            }
            docTemp.setRanking(rankTemp);
            sortedDocuments.add(docTemp);
        }
    }

    private float calculateW(Integer maxDocuments, Integer frequency) {
        return frequency * (float) Math.log(NUMBER_OF_DOCUMENTS / maxDocuments);
    }
}
