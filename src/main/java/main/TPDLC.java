package main;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.wildfly.swarm.Swarm;
import org.wildfly.swarm.jaxrs.JAXRSArchive;

import java.io.BufferedReader;
import java.util.Hashtable;
import java.util.Map;

public class TPDLC {

    public static void main(String[] args) {
        String s = "https://drive.google.com/drive/folders/1ScfWYwyaKaEPzde8vmvoryMdqjSAWT5v";
        String[] a = s.split("^https://drive.google.com/drive/folders/");
        for(String x:a) System.out.println(x);
    }

}
