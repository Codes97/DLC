package Indexer;

import entityClasses.Document;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;

@ApplicationScoped
public class Parser {
    private static boolean finished = false;
    private static ArrayList<Dictionary> dictionaries = new ArrayList<Dictionary>();


    public static boolean hasFinished() {
        return finished;
    }

    public static Dictionary getNext() {
        return dictionaries.size() != 0 ? dictionaries.remove(dictionaries.size() - 1) : null;
    }

    public void parseFiles(ArrayList<Document> filesToParse) {
        for (Document f : filesToParse) {
            parseFile(f);
        }
        finished = true;
    }

    private void parseFile(Document file) {
        Dictionary d = new Dictionary(file);
        String[] str = file.getFile().split(" ");
        for (int i = 0; i < str.length; i++) {
            str[i] = checkWord(str[i]);
            if (!str[i].equals(" ") && !str[i].isEmpty()) {
                d.merge(str[i].toLowerCase(), 1, Integer::sum);
            }
        }
        dictionaries.add(d);
    }

    private String checkWord(String word) {
        word = word.replaceAll("([.,\\-\"()'°ª:;¿?_*|~€¬&=!¡<>\\[\\]#@«»$%]|[0-9])+", "");
        return word;
    }

    ;
}
