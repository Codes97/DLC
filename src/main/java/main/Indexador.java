package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Indexador implements Runnable {
    private ArrayList<File> files;

    public Indexador(ArrayList<File> files){
        this.files = new ArrayList(files);
    }
    @Override
    public void run() {
        for (File file : files) {
            cargarArchivo(file);
        }
    }

    private void cargarArchivo(File file){
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = br.readLine()) != null) {
                String[] str = line.split(" ");
                for(int i = 0; i < str.length; i++){
                    str[i] = checkPalabra(str[i]);
                    if(!str[i].equals(" ")&&!str[i].isEmpty()){
                        //System.out.println(str[i]);// Aca se tiene que cargar en la db la palabra |  table.merge(str[i], 1, Integer::sum);
                    }
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private String checkPalabra(String palabra){
        palabra = palabra.replaceAll("([.,\\-\"()'°ª:;¿?_*|~€¬&=!¡<>\\[\\]#@«»$%]|[0-9])+", "");
        return palabra;
    }
}
