package services;

import controllers.DocumentController;
import controllers.PostlistController;
import controllers.WordController;
import entityClasses.Document;
import entityClasses.Postlist;
import entityClasses.Word;
import indexer.Parser;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Hashtable;

@ApplicationScoped
public class QueryService {
    /**
     * Controladores JPA de las entidades.
     */
    @Inject
    WordController wordCon;
    @Inject
    DocumentController docCon;
    @Inject
    PostlistController plCon;

    /**
     * Cantidad todal de documentos en la base documental.
     */
    private long NUMBER_OF_DOCUMENTS;

    /**
     * Representan las entidades involucradas en la consulta.
     * En el caso de las palabras y los documentos K = idEntidad y
     * V = Entidad.
     */
    private Hashtable<Integer, Word> words;
    private Hashtable<Integer, Document> documents;
    private ArrayList<Postlist> postlists;

    /**
     * Lista de documentos ordenados en función de su ranking.
     */
    private ArrayList<Document> sortedDocuments;

    public QueryService() {
    }

    /**
     * Este metodo se encarga de llamar a todos los metodos necesarios para
     * manejar la consulta. Setea NUMBER_OF_DOCUMENTS, chequea si las palabras
     * de la consulta existen en la base documental. Finalmente retorna true
     * si se pudo realizar la consulta o false en caso contrario.
     *
     * @param s es la cadena que representa la consulta del usuario.
     */
    public boolean search(String s) {
        NUMBER_OF_DOCUMENTS = docCon.countDocuments();
        setWords(s);
        if (words.size() != 0) {
            setPlist();
            setDocuments();
            calculateRanking();
            return true;
        }
        return false;
    }

    public Document[] getSortedDocuments() {
        return sortedDocuments.toArray(new Document[sortedDocuments.size()]);
    }

    public void setWords(String q) {
        words = new Hashtable<>();
        String[] w = Parser.splitString(q);
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
        ArrayList<Integer> idDoc = new ArrayList<>();
        for (Postlist pl : postlists) {
            if (!idDoc.contains(pl.getIdDocument())) idDoc.add(pl.getIdDocument());
        }
        return idDoc.toArray(new Integer[idDoc.size()]);
    }

    private void setDocuments() {
        documents = new Hashtable<>();
        ArrayList<Document> docs = docCon.findDocumentsById(getIdDocuments());
        for (Document d : docs) {
            documents.put(d.getIdDocument(), d);
        }
    }

    private void calculateRanking() {
        sortedDocuments = new ArrayList<>();
        Document docTemp;
        float rankTemp = 0;
        for (Integer idDoc : documents.keySet().toArray(new Integer[documents.size()])) {
            docTemp = documents.get(idDoc);
            rankTemp = calculateDocumentRanking(docTemp.getIdDocument(), rankTemp);
            docTemp.setRanking(rankTemp);
            sortedDocuments.add(docTemp);
        }
    }

    private float calculateIndividualRanking(Integer maxDocuments, Integer frequency) {
        return (float) frequency * (float) Math.log((float) NUMBER_OF_DOCUMENTS / (float) maxDocuments);
    }

    private float calculateDocumentRanking(int idDoc, float rankTemp) {
        for (Postlist pl : postlists) {
            if (pl.getIdDocument() == idDoc) {
                rankTemp += calculateIndividualRanking(words.get(pl.getIdWord()).getMaxDocuments(), pl.getFrequency());
            }
        }
        return rankTemp;
    }
}