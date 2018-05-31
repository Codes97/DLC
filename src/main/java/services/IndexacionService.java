package services;

import controllers.DocumentController;
import controllers.WordController;
import indexer.Flusher;
import indexer.Parser;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.util.Hashtable;

@ApplicationScoped
public class IndexacionService implements Serializable {
    public static final int BATCH_SIZE = 10000;
    public static int DOC_ID;
    public static int WORD_ID;
    public static Hashtable<String, Integer> vocabulary;
    public static boolean canIndex = true;
    @Inject
    private DocumentController documentController;
    @Inject
    private WordController wordController;
    @Inject
    Flusher flusher;
    @Inject
    GoogleService googleS;
    @Inject
    Parser parser;

    public IndexacionService() {
    }

    public void startIndexing(String id) {
        if (canIndex) {
            setValues();
            Thread flusherT = new Thread(flusher);
            Thread parserT = new Thread(parser);
            flusherT.start();
            parserT.start();
            try {
                googleS.downloadFiles(id); //1rBcT8Awu45P6b1HRYBdHlRiRMCMNJTI9, 0B_R7SeoAotsmUUtYendIX04zRjA
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Not finished yet!");
        }

    }


    private void setValues() {
        canIndex = false;
        DOC_ID = documentController.getMaxId() + 1;
        WORD_ID = wordController.getMaxId() + 1;
        vocabulary = wordController.getVocabulary();
    }


}
