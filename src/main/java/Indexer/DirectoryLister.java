package Indexer;

import java.io.File;
import java.util.ArrayList;

public class DirectoryLister {
    private ArrayList<File> files;

    public DirectoryLister() {
    }

    public ArrayList<File> getFiles() {
        return files;
    }

    public void listDirectory() {
        File dir = new File("src/main/resources/Files");
        File[] directoryListing = dir.listFiles();
        for (int i = 0; i < directoryListing.length; i++) {
            //Agregamos al array temporal el archivo
            this.files.add(directoryListing[i]);
        }
    }

}
