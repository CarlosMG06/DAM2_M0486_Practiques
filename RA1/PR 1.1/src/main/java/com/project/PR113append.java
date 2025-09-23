package com.project;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class PR113append {

    public static void main(String[] args) {
        // Definir el camí del fitxer dins del directori "data"
        String camiBase = System.getProperty("user.dir") + "/data/";
        String camiFitxer = camiBase + "frasesMatrix.txt";

        // Crida al mètode que afegeix les frases al fitxer
        afegirFrases(camiFitxer);
    }

    // Mètode que afegeix les frases al fitxer amb UTF-8 i línia en blanc final
    public static void afegirFrases(String camiFitxer) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(camiFitxer), StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write("I can only show you the door");
            writer.newLine();
            writer.write("You're the one that has to walk through it");
            writer.newLine();
        } catch (Exception e) {
            System.out.println("Error en escriure en el fitxer: " + camiFitxer);
            e.printStackTrace();
        }
    }
}
