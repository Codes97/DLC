package Indexer;

import controllers.BulkInsert;
import controllers.DocumentController;
import entityClasses.Document;
import services.IndexacionService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class Flusher implements Runnable {
    @Inject
    private DocumentController documentController;
    @Inject
    private BulkInsert inserter;

    /**
     * Este metodo se encarga de setear un id al documento del diccionario
     * y de insertar el mismo en la base de datos.
     *
     * @param d es una instancia de la clase Dictionary que posee el docuento
     *          y las palabras a insertar en la base de datos.
     */
    private void flush(Dictionary d) {
        Hashtable<String, Integer> tempWords = d.getDictionary();
        Document doc = d.getFile();
        doc.setIdDocument(IndexacionService.DOC_ID);
        insertDocument(doc);
        inserter.flushDictionary(tempWords, doc.getIdDocument());
    }

    /**
     * Este metodo se encarga de insertar el documento correspondiente
     * al diccionario en la base de datos. Al finalizar incrementa el
     * id global IndexacionService.DOC_ID en 1.
     *
     * @param doc es el documento a insertar en la pase de dato.
     */
    private void insertDocument(Document doc) {
        documentController.create(doc);
        documentController.flush();
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
