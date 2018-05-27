package services;

import controllers.DocumentController;
import controllers.PostlistController;
import controllers.WordController;
import entityClasses.Document;
import entityClasses.Postlist;
import entityClasses.Word;
import Indexer.Parser;

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

    /**
     * Este metodo divide la consulta en palabras individuales y las agrega a
     * words, si es que existen en el vocabulario.
     *
     * @param q es la cadena que representa la consulta del usuario.
     */
    public void setWords(String q) {
        words = new Hashtable<>();
        String[] w = Parser.splitString(q);
        ArrayList<Word> arrayWords = wordCon.findWordsByValue(w);
        for (Word a : arrayWords) {
            if (!words.containsKey(a.getIdWord()))
                words.put(a.getIdWord(), a);
        }
    }

    /**
     * Este metodo retorna un Array con los ids de las palabras en la consulta.
     *
     * @return Array con id de palabras.
     */
    private Integer[] getIdWords() {
        return words.keySet().toArray(new Integer[words.size()]);
    }

    /**
     * Busca todas las entradas en la postlist talque el id de la palabra pertenezca
     * al conjunto de palabras de la consulta y las agrega a postlist.
     */
    private void setPlist() {
        postlists = plCon.findPostlistByWords(getIdWords());
    }

    /**
     * Retorna un Array con todos los id de los documentos que tengan relacion con
     * las palabras de la consulta.
     *
     * @return Array con el id de los documentos relevantes a la consulta.
     */
    private Integer[] getIdDocuments() {
        ArrayList<Integer> idDoc = new ArrayList<>();
        for (Postlist pl : postlists) {
            if (!idDoc.contains(pl.getIdDocument())) idDoc.add(pl.getIdDocument());
        }
        return idDoc.toArray(new Integer[idDoc.size()]);
    }

    /**
     * Este metodo busca en la base de datos todos los documentos relevantes encontrados
     * en la postlist y los agrega a documents.
     */
    private void setDocuments() {
        documents = new Hashtable<>();
        ArrayList<Document> docs = docCon.findDocumentsById(getIdDocuments());
        for (Document d : docs) {
            documents.put(d.getIdDocument(), d);
        }
    }

    /**
     * Por cada documento relevante recorre las entradas de la postlist buscando
     * las frecuencias de las palabras que en el se encuentran y que pertenecen a la consulta
     * Con las frecuencias de cada palabra por cada documento se puede, calcular
     * el peso individual de cada palabra con su documento y sumando estos pesos
     * se obtiene el peso total del documento (ranking).
     * Al finalizar agrega el documento a la lista que será retornada como resultado.
     */
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

    /**
     * Este metodo calcula el peso individual de una palabra determinada con un documento.
     *
     * @param maxDocuments es la cantidad de documentos en los que aparece la palabra.
     * @param frequency    es la frecuencia de la palabra en el documento.
     * @return el peso de la palabra en el documento.
     */
    private float calculateIndividualRanking(Integer maxDocuments, Integer frequency) {
        return (float) frequency * (float) Math.log((float) NUMBER_OF_DOCUMENTS / (float) maxDocuments);
    }

    /**
     * Este metodo suma los pesos individuales y retorna el ranking del documento.
     *
     * @param idDoc    es el id del documento a calcular el ranking.
     * @param rankTemp es la variable que usa para acumular el ranking.
     * @return el valor del ranking del documento.
     */
    private float calculateDocumentRanking(int idDoc, float rankTemp) {
        rankTemp = 0;
        for (Postlist pl : postlists) {
            if (pl.getIdDocument() == idDoc) {
                rankTemp += calculateIndividualRanking(words.get(pl.getIdWord()).getMaxDocuments(), pl.getFrequency());
            }
        }
        return rankTemp;
    }
}
