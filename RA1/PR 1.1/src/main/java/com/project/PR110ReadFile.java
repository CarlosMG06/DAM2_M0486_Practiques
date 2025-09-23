package com.project;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class PR110ReadFile {

    public static void main(String[] args) {
        String camiBase = System.getProperty("user.dir") + "/data/";
        String nomFitxer = "GestioTasques.java";
        String camiFitxer = camiBase + nomFitxer;

        llegirIMostrarFitxer(camiFitxer);  // Només cridem a la funció amb la ruta del fitxer
    }

    // Funció que llegeix el fitxer i mostra les línies amb numeració
    public static void llegirIMostrarFitxer(String camiFitxer) {
        try {
            List<String> linies = Files.readAllLines(Paths.get(camiFitxer), StandardCharsets.UTF_8);
            for (int i = 0; i < linies.size(); i++) {
                String liniaNumerada = (i + 1) + ": " + linies.get(i);
                System.out.println(liniaNumerada);
            }
        } catch (IOException e) {
            System.out.println("Error en llegir el fitxer: " + camiFitxer);
            e.printStackTrace();
        }
    }
}
