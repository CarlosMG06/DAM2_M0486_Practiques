package com.project;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

public class PR114linies {

    public static void main(String[] args) {
        // Definir el camí del fitxer dins del directori "data"
        String camiBase = System.getProperty("user.dir") + "/data/";
        String camiFitxer = camiBase + "numeros.txt";

        // Crida al mètode que genera i escriu els números aleatoris
        generarNumerosAleatoris(camiFitxer);
    }

    // Mètode per generar 10 números aleatoris i escriure'ls al fitxer
    public static void generarNumerosAleatoris(String camiFitxer) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(camiFitxer), StandardCharsets.UTF_8)) {
            for (int i = 0; i < 10; i++) {
                int numeroAleatori = (int) (Math.random() * 100); // entre 0 i 99
                writer.write(String.valueOf(numeroAleatori));
                if (i < 9) {
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error en escriure en el fitxer: " + camiFitxer);
            e.printStackTrace();
        }
    }
}
