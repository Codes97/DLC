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
    WordJpaController wordCon;
    @Inject
    DocumentJpaController docCon;
    @Inject
    PostlistJpaController plCon;

    private void flush(Dictionary d) {
        Hashtable<String, Integer> tempWords = d.getDictionary();
        Word temp = null;
        Postlist pl;
        Document doc = d.getFile();
        doc.setIdDocument(IndexacionService.DOC_ID);
        docCon.create(doc);
        docCon.flush();
        for (Map.Entry<String, Integer> e : tempWords.entrySet()) {
            if (!(IndexacionService.vocabulary.isEmpty())) temp = IndexacionService.vocabulary.get(e.getKey());
            if (temp != null) {
                temp.updateFrequency(e.getValue());
                temp.incrementMaxDocuments();
                wordCon.edit(temp);
                IndexacionService.vocabulary.remove(e.getKey());
                IndexacionService.vocabulary.put(e.getKey(), temp);
            } else {
                temp = new Word(IndexacionService.WORD_ID, e.getKey(), e.getValue(), 1);
                wordCon.create(temp);
                IndexacionService.vocabulary.put(e.getKey(), temp);
                IndexacionService.WORD_ID++;
            }
            pl = new Postlist();
            pl.setIdDocument(doc.getIdDocument());
            pl.setIdWord(temp.getIdWord());
            pl.setFrequency(e.getValue());
            plCon.create(pl);
            flushAndClear();
        }
        flushAndClear();
        IndexacionService.DOC_ID++;
    }

    private void flushAndClear() {
        wordCon.flush();
        plCon.flush();
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
                    System.out.println("Nothing to flush"
                            + TimeUnit.SECONDS.convert(System.nanoTime(), TimeUnit.NANOSECONDS));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Starting flush of " + temp.getFile().getDocName());
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
