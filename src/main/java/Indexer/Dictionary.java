package Indexer;

import entityClasses.Document;

import java.util.Hashtable;
import java.util.function.BiFunction;

public class Dictionary {
    private Hashtable<String, Integer> dictionary;
    private Document file;

    public Dictionary(Document file) {
        this.dictionary = new Hashtable<String, Integer>();
        this.file = file;
    }

    public Document getFile() {
        return file;
    }

    public Hashtable<String, Integer> getDictionary() {
        return dictionary;
    }

    public void setDictionary(Hashtable<String, Integer> dictionary) {
        this.dictionary = dictionary;
    }

    public void clear() {
        dictionary.clear();
    }

    public void merge(String key, Integer value, BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        dictionary.merge(key, value, remappingFunction);
    }
}
