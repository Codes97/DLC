package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

public class Indexador implements Runnable {

    //Lista de archivos que va a procesar este indexador
    private ArrayList<File> files;
    private int offset;
    private Hashtable<String, Integer> diccionario;

    public Indexador(ArrayList<File> files, int offset){
        this.files = new ArrayList<File>(files);
        this.offset = offset;
        this.diccionario = new Hashtable<String, Integer>();
    }

    //Metodo que ejecuta el thread
    @Override
    public void run() {
        for (File file : files) {
            cargarDiccionario(file);
        }
        return;
    }


    //Metodo para cargar archivos
    private void cargarDiccionario(File file){
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = null;
            //Insert del libro
            while ((line = br.readLine()) != null) {
                String[] str = line.split(" ");
                for(int i = 0; i < str.length; i++){
                    str[i] = checkPalabra(str[i]);
                    if(!str[i].equals(" ")&&!str[i].isEmpty()){
                        diccionario.merge(str[i], 1, Integer::sum);
                    }
                }
            }
            flushDiccionario();
        }
        catch(Exception e){
            //Nos dice el error
            e.printStackTrace();
        }
    }
    private void flushDiccionario(){
        //Mete el diccionario en la db y lo limpia, asi puede seguir el proximo libro con el diccionario limpio.
        Set<String> set = diccionario.keySet();
        int i = 0;
        for(String key : set){
            /*
            * id = offset+i
            * if(!palabraExiste){
            *   InsertarPalabra
            * }
            * Insertar Postlist
            */
        }
        diccionario.clear();
    }

    //Checkear si es palabra valida
    private String checkPalabra(String palabra){
        palabra = palabra.replaceAll("([.,\\-\"()'°ª:;¿?_*|~€¬&=!¡<>\\[\\]#@«»$%]|[0-9])+", "");
        return palabra;
    }
}
