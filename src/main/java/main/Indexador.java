package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Indexador implements Runnable {

    //Lista de archivos que va a procesar este indexador
    private ArrayList<File> files;

    public Indexador(ArrayList<File> files){
        this.files = new ArrayList(files);
    }

    //Metodo que ejecuta el thread
    @Override
    public void run() {
        for (File file : files) {
            cargarArchivo(file);
        }
    }


    //Metodo para cargar archivos
    private void cargarArchivo(File file){
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] str = line.split(" ");
                for(int i = 0; i < str.length; i++){
                    str[i] = checkPalabra(str[i]);
                    if(!str[i].equals(" ")&&!str[i].isEmpty()){
                        // Aca se tiene que cargar en la db la palabra |  table.merge(str[i], 1, Integer::sum);
                    }
                }
            }
        }
        catch(Exception e){
            //Nos dice el error
            e.printStackTrace();
        }
    }

    //Checkear si es palabra valida
    private String checkPalabra(String palabra){
        palabra = palabra.replaceAll("([.,\\-\"()'°ª:;¿?_*|~€¬&=!¡<>\\[\\]#@«»$%]|[0-9])+", "");
        return palabra;
    }
}
