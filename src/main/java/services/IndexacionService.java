package services;

import main.Indexador;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class IndexacionService {
    public static final int FILES_PER_INDEXER = 50;
    ArrayList<File> files = new ArrayList<>();
    public IndexacionService() {
        ClassLoader classLoader = getClass().getClassLoader();
        File dir = new File(classLoader.getResource("Files").getFile());
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (int i = 0; i < directoryListing.length; i++) {
                files.add(directoryListing[i]);
                if((i+1) % FILES_PER_INDEXER == 0){
                    System.out.println("Creando nuevo thread en i = " + i);
                    createThread(files);
                }
                if(i == directoryListing.length-1 && files.size() > 0){
                    System.out.println("Creando nuevo thread en i = " + i);
                    createThread(files);
                }
            }
        }
    }
    private void createThread(ArrayList<File> files){
        Indexador indexador = new Indexador(files);
        Thread t = new Thread(indexador);
        t.start();
        files.clear();
    }
}
