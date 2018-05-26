package Indexer;

import entityClasses.Document;
import services.GoogleService;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;

@ApplicationScoped
public class Parser implements Runnable {
    private static boolean finished = false;
    private static ArrayList<Dictionary> dictionaries = new ArrayList<Dictionary>();


    public static boolean hasFinished() {
        return (finished && dictionaries.isEmpty());
    }

    public static Dictionary getNext() {
        return dictionaries.size() != 0 ? dictionaries.remove(dictionaries.size() - 1) : null;
    }


    public void parseFiles(ArrayList<Document> filesToParse) {
        for (Document f : filesToParse) {
            parseFile(f);
        }
    }

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

    public static String checkWord(String word) {
        word = word.replaceAll("([.,\\-\"()'°ª:;¿?_*|~€¬&=!¡<>\\[\\]#@«»$%]|[0-9])+", "");
        return word;
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

    ;
}
