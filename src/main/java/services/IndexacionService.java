package services;

import main.Indexador;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class IndexacionService {
    //Cantidad de archivos que va a procesar cada indexador
    public static final int FILES_PER_INDEXER = 50;

    //Array temporal para pasarle a cada indexador los archivos
    ArrayList<File> files = new ArrayList<>();
    public IndexacionService() {
        //Esto sirve para obtener los libros de la carpeta Resources.
        ClassLoader classLoader = getClass().getClassLoader();
        File dir = new File(classLoader.getResource("Files").getFile());

        //Obtener la lista de archivos de un directorio
        File[] directoryListing = dir.listFiles();
        if (directoryListing != null) {
            for (int i = 0; i < directoryListing.length; i++) {
                //Agregamos al array temporal el archivo
                files.add(directoryListing[i]);

                //Cuando llegue a 50 creamos un nuevo hilo de ejecucion con un indexador
                if((i+1) % FILES_PER_INDEXER == 0){
                    System.out.println("Creando nuevo thread en i = " + i);
                    createThread(files);
                }
                //Si hay resto lo metemos en otro hilo con un indexador
                if(i == directoryListing.length-1 && files.size() > 0){
                    System.out.println("Creando nuevo thread en i = " + i);
                    createThread(files);
                }
            }
        }
    }
    private void createThread(ArrayList<File> files){
        //Creamos un indexador
        Indexador indexador = new Indexador(files);
        //Creamos el thread
        Thread t = new Thread(indexador);
        //Lo iniciamos
        t.start();
        //Limpiamos la array temporal
        files.clear();
    }
}
