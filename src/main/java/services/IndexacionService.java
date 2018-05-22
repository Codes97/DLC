package services;

import Indexer.Flusher;
import Indexer.Parser;
import controllers.DocumentJpaController;
import controllers.WordJpaController;
import entityClasses.Word;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.util.Hashtable;

@ApplicationScoped
public class IndexacionService implements Serializable {
    public static int DOC_ID;
    public static int WORD_ID;
    public static Hashtable<String, Word> vocabulary;/*HASHTABLE*/

    @Inject
    Flusher flusher;
    @Inject
    GoogleService googleS;
    @Inject
    Parser parse;
    @Inject
    DocumentJpaController docCon;
    @Inject
    WordJpaController wordCon;

    public IndexacionService() {
        DOC_ID = 0;
        WORD_ID = 0;
        vocabulary = new Hashtable<String, Word>();

    }

    public void startIndexing() {
        Thread t = new Thread(flusher);
        t.start();
        try {
            parse.parseFiles(googleS.getFiles());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

}
