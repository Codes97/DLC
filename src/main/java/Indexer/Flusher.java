package Indexer;

import controllers.DocumentJpaController;
import controllers.PostlistJpaController;
import controllers.WordJpaController;
import entityClasses.Document;
import entityClasses.Postlist;
import entityClasses.Word;
import services.IndexacionService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class Flusher implements Runnable {
    @Inject
    DocumentJpaController docCon;
    @Inject
    WordJpaController wordCon;

    private void flush(Dictionary d) {
        Hashtable<String, Integer> tempWords = d.getDictionary();
        Document doc = d.getFile();
        doc.setIdDocument(IndexacionService.DOC_ID);
        docCon.create(doc);
        docCon.flush();
        wordCon.bulkInsertStringBuilder(tempWords, doc.getIdDocument());
        IndexacionService.DOC_ID++;
    }

    @Override
    public void run() {
        Dictionary temp;
        long init, end, total = 0;
        while (true) {
            if (Parser.hasFinished()) break;
            if ((temp = Parser.getNext()) == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                init = System.nanoTime();
                flush(temp);
                end = System.nanoTime();
                total += (end - init);
                System.out.println("Time spent to flush " + temp.getFile().getDocName() + ": " +
                        TimeUnit.SECONDS.convert((end - init), TimeUnit.NANOSECONDS));
                System.out.println("Total time elapsed: " +
                        TimeUnit.SECONDS.convert(total, TimeUnit.NANOSECONDS));
            }
        }
    }
}
