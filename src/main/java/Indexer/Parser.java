package Indexer;

import entityClasses.Document;
import services.GoogleService;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;

@ApplicationScoped
public class Parser implements Runnable {
    /*
     * Indica si se terminaron de parsear todos los documentos.
     * */
    private static boolean finished = false;

    /*
     *ArrayList que contiene los documentos procesados por el parser (diccionarios).
     * */
    private static ArrayList<Dictionary> dictionaries = new ArrayList<Dictionary>();

    /*
     * @return true si termino de parsear los documentos y no quedan diccionarios
     *           para bajar a la base de datos.
     * */
    public static boolean hasFinished() {
        return (finished && dictionaries.isEmpty());
    }

    /*
     * @return retorna el ultimo diccionario de la lista para ser bajado a la base
     *           de datos. Si no quedan mas retorna null.
     * */
    public static Dictionary getNext() {
        return dictionaries.size() != 0 ? dictionaries.remove(dictionaries.size() - 1) : null;
    }

    /*
     * Este metodo se encarga de reemplazar todos símbolos que no sean letras
     * @param word es la palabra a chequear.
     * @return word palabra chequeada.
     * */
    public static String checkWord(String word) {
        word = word.replaceAll("([.,\\-\"()'°ª:;¿?_*|~€¬&=!¡<>\\[\\]#@«»$%]|[0-9])+", "");
        return word;
    }

    /*
     * Este metodo se encarga de parsear un documento en palabras individuales,
     * cuenta ademas la frecuencia de aparición de cada una. Finalmente agrega
     * el diccionario a la lista.
     * @param file documento a ser parseado.
     * */
    private void parseFile(Document file) {
        Dictionary d = new Dictionary(file);
        String[] str = file.getFile().split("[^\\p{Alpha}]");//("[(?U)\\P{L}+\\s]");
        for (int i = 0; i < str.length; i++) {
            str[i] = checkWord(str[i]);
            if (!str[i].equals(" ") && !str[i].isEmpty() && !str[i].equals("")) {
                d.merge(str[i].toLowerCase(), 1, Integer::sum);
            }
        }
        dictionaries.add(d);
    }

    @Override
    public void run() {
        Document temp;
        while (true) {
            if (GoogleService.hasFinished()) break;
            if ((temp = GoogleService.getNext()) == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                parseFile(temp);
            }
        }
        finished = true;
    }
}
