package indexer;

import entityClasses.Document;

import java.util.Hashtable;
import java.util.function.BiFunction;

public class Dictionary {
    private Hashtable<String, Integer> dictionary;
    private Document file;

    public Dictionary(Document file) {
        this.dictionary = new Hashtable<>();
        this.file = file;
    }

    public Document getFile() {
        return file;
    }

    public Hashtable<String, Integer> getDictionary() {
        return dictionary;
    }

    public void merge(String key,
                      Integer value,
                      BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        dictionary.merge(key, value, remappingFunction);
    }
}
