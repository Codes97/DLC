package controllers;

import services.IndexacionService;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.Hashtable;
import java.util.Map;

@ApplicationScoped
@Transactional
public class BulkInsert {
    /**
     * Formato de INSERT para el nativeQuery.
     */
    private final String WORDS_INSERT_STRING = "INSERT INTO words " +
            "(idWord, word, maxFrequency, maxDocuments) VALUES ";
    private final String ON_DUPLICATE = " ON DUPLICATE KEY UPDATE maxDocuments = maxDocuments + 1;";
    private final String POSTLIST_INSERT_STRING = "INSERT INTO postlist " +
            "(idDocument, idWord, frequency) VALUES ";

    /**
     * EntityManager para crear y ejecutar nativeQuerys.
     */
    @PersistenceContext(name = "dlc")
    private EntityManager em;

    /**
     * StringBuilders para construir el Bulk Insert.
     */
    private StringBuilder words = new StringBuilder(WORDS_INSERT_STRING);
    private StringBuilder postlist = new StringBuilder(POSTLIST_INSERT_STRING);

    /**
     * Contador de palabras insertadas.
     * Cada IndexacionService.BATCH_SIZE hace un INSERT.
     */
    private int insertedWords = 0;

    /**
     * Este metodo se encarga de recorrer cada Entry del diccionario del
     * documento y agregar los VALUES correspondientes a los INSERTS de
     * palabras y postlist y ademas se encarga de mantener el vocabulario
     * en memoria. Si la palabra a agregar ya se encuentra en el vocabulario
     * el id de la misma se para como parametro al metodo appendWordToString,
     * de lo contrario se utiliza el id global IndexacionService.WORD_ID.
     * Al finalizar limpia las variables words, postlist e insertedWords,
     * dejando su valor por defecto.
     *
     * @param tempWords es el diccionario de un documento parseado donde
     *                  K = palabra, V = frecuencia en el documento
     * @param docId     es el id del documento al cual pertenece el diccionario
     */
    public void flushDictionary(Hashtable<String, Integer> tempWords, int docId) {
        for (Map.Entry<String, Integer> e : tempWords.entrySet()) {
            if (IndexacionService.vocabulary.containsKey(e.getKey())) {
                appendWordToString(e.getKey(), IndexacionService.vocabulary.get(e.getKey()), docId, e.getValue());
            } else {
                appendWordToString(e.getKey(), IndexacionService.WORD_ID, docId, e.getValue());
                addWordToVocabulary(e.getKey());
            }
            insertedWords++;
            if (insertedWords >= IndexacionService.BATCH_SIZE) {
                flushAndClear();
            }
        }
        flushAndClear();
        IndexacionService.WORD_ID++;
    }

    /**
     * Este metodo quita el excedente de caracteres de los INSERTS y agrega
     * al final del INSERT correspondiente a las palabras la condicion ON
     * UPDATE (que se encarga de incrementar en 1 la cantidad de documentos
     * en los que aparece la palabra).
     */
    private void fixString() {
        words.setLength(words.length() - 2);
        words.append(ON_DUPLICATE);
        postlist.setLength(postlist.length() - 2);
    }

    /**
     * Limpia las variables words, postlist e insertedWords,dejando su valor por
     * defecto.
     */
    private void clear() {
        words.setLength(0);
        words.append(WORDS_INSERT_STRING);
        postlist.setLength(0);
        postlist.append(POSTLIST_INSERT_STRING);
        insertedWords = 0;
    }

    /**
     * Agrega los VALUES correspondientes a los INSERTS
     *
     * @param word      es un String que representa la palabra a agregar.
     * @param idWord    es el id de la palabra a agregar.
     * @param docId     es el id del documento en el que aparece la palabra.
     * @param frequency es la frecuencia de aparición de la palabra en
     *                  el documento.
     */
    private void appendWordToString(String word, int idWord, int docId, int frequency) {
        words.append("(" + idWord + ", \'" + word + "\', " + 1 + ", " + 1 + "), ");
        postlist.append("(" + docId + ", " + idWord + ", " + frequency + "), ");
    }

    /**
     * En caso de no existir en el vocabulario este metodo agrega la
     * palabra al mismo e incrementa el id global en 1.
     *
     * @param word es un String que representa la palabra a agregar.
     */
    private void addWordToVocabulary(String word) {
        IndexacionService.vocabulary.put(word, IndexacionService.WORD_ID);
        IndexacionService.WORD_ID++;
    }

    /**
     * Este metodo instancia una query y la ejecuta.
     *
     * @param s es un String que representa el statement INSERT de las palabras.
     */
    private void insertWords(String s) {
        Query q = em.createNativeQuery(s);
        q.executeUpdate();
        em.clear();
    }

    /**
     * Este metodo instancia una query y la ejecuta.
     *
     * @param s es un String que representa el statement INSERT de la postlist.
     */
    private void insertPostlist(String s) {
        Query q = em.createNativeQuery(s);
        q.executeUpdate();
        em.clear();
    }

    /**
     * Este metodo llama a los metodos encargados de crear y ejecutar las querys
     * con las palabras y sus postlist
     */
    private void insert() {
        insertWords(words.toString());
        insertPostlist(postlist.toString());
    }

    /**
     * Este metodo se encarga de arreglar los INSERTS, ejecutar las querys y
     * limpiar las variables words, postlist e insertedWords,dejando su valor por
     * defecto.
     */
    private void flushAndClear() {
        fixString();
        insert();
        clear();
    }



}
