package com.project;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

public class PR113sobreescriu {

    public static void main(String[] args) {
        // Definir el camí del fitxer dins del directori "data"
        String camiBase = System.getProperty("user.dir") + "/data/";
        String camiFitxer = camiBase + "frasesMatrix.txt";

        // Crida al mètode que escriu les frases sobreescrivint el fitxer
        escriureFrases(camiFitxer);
    }

    // Mètode que escriu les frases sobreescrivint el fitxer amb UTF-8 i línia en blanc final
    public static void escriureFrases(String camiFitxer) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(camiFitxer), StandardCharsets.UTF_8)) {
            writer.write("I can only show you the door");
            writer.newLine();
            writer.write("You're the one that has to walk through it");
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error en escriure en el fitxer: " + camiFitxer);
            e.printStackTrace();
        }
    }
}
