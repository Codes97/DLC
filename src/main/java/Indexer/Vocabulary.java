package Indexer;

import entityClasses.Word;

import java.util.Hashtable;
import java.util.Map;

public class Vocabulary {
    private Hashtable<String, Word> words;

    public Vocabulary() {
        this.words = new Hashtable<String, Word>();
    }

    public Word getWord(String w) {
        return this.words.get(w);
    }

    public void put(String key, Word value) {
        words.put(key, value);
    }
    public Word remove(String w) {
        return words.remove(w);
    }
}
