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
    Parser parser;
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
        Thread flusherT = new Thread(flusher);
        Thread parserT = new Thread(parser);
        flusherT.start();
        parserT.start();
        try {
            googleS.downloadFiles();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

}
