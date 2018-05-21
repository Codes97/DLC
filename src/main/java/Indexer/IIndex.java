package Indexer;

import controllers.DocumentJpaController;
import controllers.PostlistJpaController;
import controllers.WordJpaController;
import entityClasses.Document;
import entityClasses.Postlist;
import entityClasses.Word;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

@ApplicationScoped
public class IIndex {
    @Inject
    WordJpaController wordCon;
    @Inject
    DocumentJpaController docCon;
    @Inject
    PostlistJpaController plCon;
    private Hashtable<String, Integer> diccionario = new Hashtable<String, Integer>();
    private Hashtable<String, Word> vocabulario = new Hashtable<String, Word>();
    private ArrayList<File> files = new ArrayList<File>();

    private void obtenerArchivos() {
        File dir = new File("src/main/resources/Files");
        File[] directoryListing = dir.listFiles();
        for (int i = 0; i < directoryListing.length; i++) {
            //Agregamos al array temporal el archivo
            this.files.add(directoryListing[i]);
        }
    }

    private void cargarArchivos() {
        for (File f : this.files) {
            if (docCon.fingDocumentByUrl(f.getPath()) == null) cargarDiccionario(f);
            System.out.println(f.getName());
        }
    }

    private void cargarDiccionario(File file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = null;
            //Insert del libro
            while ((line = br.readLine()) != null) {
                String[] str = line.split(" ");
                for (int i = 0; i < str.length; i++) {
                    str[i] = checkPalabra(str[i]);
                    if (!str[i].equals(" ") && !str[i].isEmpty()) {
                        diccionario.merge(str[i].toLowerCase(), 1, Integer::sum);
                    }
                }
            }
            //Mete el diccionario en la db y lo limpia, asi puede seguir el proximo libro con el diccionario limpio.
            flush(file);
            diccionario.clear();
        } catch (Exception e) {
            //Nos dice el error
            e.printStackTrace();
        }
    }

    private void flush(File file) {
        Document doc = new Document();
        doc.setDocName(file.getName());
        doc.setUrl(file.getPath());
        docCon.create(doc);
        int docId = docCon.fingDocumentByUrl(file.getPath()).getIdDocument();

        for (Map.Entry<String, Integer> e : diccionario.entrySet()) {
            Word temp = vocabulario.get(e.getKey());
            if (temp == null) {
                temp = new Word(e.getKey(), e.getValue(), 1);
                wordCon.create(temp);
                vocabulario.put(e.getKey(), wordCon.findWordByValue(e.getKey()));
            } else {
                temp.updateFrequency(e.getValue());
                temp.incrementMaxDocuments();
                wordCon.edit(temp);
            }
            Postlist pl = new Postlist();
            pl.setIdDocument(docId);
            pl.setIdWord(vocabulario.get(e.getKey()).getIdWord());
            pl.setFrequency(e.getValue());
        }
    }

    private String checkPalabra(String palabra) {
        palabra = palabra.replaceAll("([.,\\-\"()'°ª:;¿?_*|~€¬&=!¡<>\\[\\]#@«»$%]|[0-9])+", "");
        return palabra;
    }

    public void index() {
        obtenerArchivos();
        cargarArchivos();
    }

    public ArrayList<File> getFiles() {
        //Array temporal para pasarle a cada indexador los archivos
        ArrayList<File> files = new ArrayList<>();

        //Esto sirve para obtener los libros de la carpeta Resources.
        ClassLoader classLoader = getClass().getClassLoader();
        File dir = new File(classLoader.getResource("Files").getFile());

        //Obtener la lista de archivos de un directorio
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (int i = 0; i < directoryListing.length; i++) {
                //Agregamos al array temporal el archivo
                files.add(directoryListing[i]);
            }
        }
        return files;
    }
}
