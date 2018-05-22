package Indexer;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
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
        Document doc = d.getFile();
        doc.setIdDocument(IndexacionService.DOC_ID);
        docCon.create(doc);
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
            Postlist pl = new Postlist();
            pl.setIdDocument(doc.getIdDocument());
            pl.setIdWord(temp.getIdWord());
            pl.setFrequency(e.getValue());
            plCon.create(pl);
        }
        IndexacionService.DOC_ID++;
    }

    @Override
    public void run() {
        Dictionary temp;
        float init, end;
        while (true) {
            if (Parser.hasFinished()) break;
            if ((temp = Parser.getNext()) == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Starting");
                init = System.nanoTime();
                flush(temp);
                end = System.nanoTime();
                System.out.println("Time spent to flush " + temp.getFile().getDocName() + ": " + (end - init));
            }
        }
    }
}
