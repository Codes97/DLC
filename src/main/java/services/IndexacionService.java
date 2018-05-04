package services;

import main.Indexador;

import javax.enterprise.context.ApplicationScoped;
import java.io.File;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.ArrayList;

@ApplicationScoped
public class IndexacionService implements Serializable {
    //Cantidad de archivos que va a procesar cada indexador
    public static final int FILES_PER_INDEXER = 20;
    public static final int OFFSET_PER_THREAD = 50000;



    public IndexacionService() {
    }

    public void indexResources(){
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

                //Cuando llegue a 50 creamos un nuevo hilo de ejecucion con un indexador
                if((i+1) % FILES_PER_INDEXER == 0){
                    System.out.println("Creando nuevo thread en i = " + i);
                    createThread(files,i*OFFSET_PER_THREAD);
                }
                //Si hay resto lo metemos en otro hilo con un indexador
                if(i == directoryListing.length-1 && files.size() > 0){
                    System.out.println("Creando nuevo thread en i = " + i);
                    createThread(files, i*OFFSET_PER_THREAD);
                }
            }
        }
    }

    private void createThread(ArrayList<File> files,int offset){
        //Creamos un indexador
        Indexador indexador = new Indexador(files, offset);
        //Creamos el thread
        Thread t = new Thread(indexador);
        //Lo iniciamos
        t.start();
        //Limpiamos la array temporal
        files.clear();
    }
}
