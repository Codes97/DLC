package main;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;

public class DictionaryFlusher implements Runnable {
    Hashtable<String, Integer> diccionario;
    Integer offset;

    public DictionaryFlusher(Hashtable<String, Integer> dic, int offset){
        diccionario = new Hashtable<String, Integer>(dic);
        this.offset = offset;
    }
    @Override
    public void run() {
        flush();
        return;
    }

    private void flush(){
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
    }
}
