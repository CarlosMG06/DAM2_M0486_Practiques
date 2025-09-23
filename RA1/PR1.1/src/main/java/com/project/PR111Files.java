package com.project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.stream.Stream;

public class PR111Files {

    public static void main(String[] args) {
        String camiBase = System.getProperty("user.dir") + "/data/";
        String camiFitxer = camiBase + "pr111";

        gestionarArxius(camiFitxer);
    }

    public static void gestionarArxius(String camiFitxer) {
        // 1
        String nomDirectori = "myFiles";
        String camiDirectori = camiFitxer + "/" + nomDirectori;
        try {
            Files.createDirectory(Paths.get(camiDirectori));
        } catch (IOException e) {
            System.out.println("Error en crear el directori: " + camiDirectori);
            e.printStackTrace();
        }

        // 2
        String nomFitxer1 = "file1.txt";
        String nomFitxer2 = "file2.txt";
        String camiFitxer1 = camiDirectori + "/" + nomFitxer1;
        String camiFitxer2 = camiDirectori + "/" + nomFitxer2;
        try {
            Files.createFile(Paths.get(camiFitxer1));
            Files.createFile(Paths.get(camiFitxer2));
        } catch (IOException e) {
            System.out.println("Error en crear els fitxers: " + camiFitxer1 + " i " + camiFitxer2);
            e.printStackTrace();
        }

        // 3
        String nomFitxerRenombrat = "renamedFile.txt";
        String camiFitxerRenombrat = camiDirectori + "/" + nomFitxerRenombrat;
        try {
            Files.move(Paths.get(camiFitxer2), Paths.get(camiFitxerRenombrat));
        } catch (IOException e) {
            System.out.println("Error en renombrar el fitxer: " + camiFitxer2);
            e.printStackTrace();
        }

        // 4
        llistarArxius(camiDirectori);
        
        // 5
        try {
            Files.delete(Paths.get(camiFitxer1));
        } catch (IOException e) {
            System.out.println("Error en eliminar el fitxer: " + camiFitxer1);
            e.printStackTrace();
        }

        // 6
        llistarArxius(camiDirectori);
    }

    // Mètode auxiliar per llistar els arxius d'un directori
    public static void llistarArxius(String camiDirectori) {
        try (Stream<Path> arxius = Files.list(Paths.get(camiDirectori));) {
            System.out.println("Els arxius de la carpeta són:");
            arxius.forEach(System.out::println);
        } catch (IOException e) {
            System.out.println("Error en llistar els fitxers del directori: " + camiDirectori);
            e.printStackTrace();
        }
    }
}
