package com.project;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class PR115cp {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Error: Has d'indicar dues rutes d'arxiu.");
            System.out.println("Ús: PR115cp <origen> <destinació>");
            return;
        }

        // Ruta de l'arxiu origen
        String rutaOrigen = args[0];
        // Ruta de l'arxiu destinació
        String rutaDesti = args[1];

        // Crida al mètode per copiar l'arxiu
        copiarArxiu(rutaOrigen, rutaDesti);
    }

    // Mètode per copiar un arxiu de text de l'origen al destí
    public static void copiarArxiu(String rutaOrigen, String rutaDesti) {
        if (Files.notExists(Paths.get(rutaOrigen))) {
            System.out.println("El fitxer d'origen no existeix.");
        } else if (Files.isDirectory(Paths.get(rutaOrigen))) {
            System.out.println("El path no correspon a un arxiu, sinó a una carpeta.");
            return;
        } else {
            try {
                Files.createFile(Paths.get(rutaDesti));
            }  catch (IOException e) {
                System.out.println("Error en crear el fitxer de destinació.");
                e.printStackTrace();
                return;
            }
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(rutaDesti), StandardCharsets.UTF_8)) {
                List<String> linies = Files.readAllLines(Paths.get(rutaOrigen), StandardCharsets.UTF_8);
                for (String linia : linies) {
                    writer.write(linia);
                    writer.newLine();
                }
            } catch (IOException e) {
                System.out.println("Error en copiar el fitxer línia a línia.");
                e.printStackTrace();
            }
        }

    }
}
