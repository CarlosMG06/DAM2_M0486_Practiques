package com.project;

import java.nio.file.Files;
import java.nio.file.Paths;

public class PR112cat {

    public static void main(String[] args) {
        // Comprovar que s'ha proporcionat una ruta com a paràmetre
        if (args.length == 0) {
            System.out.println("No s'ha proporcionat cap ruta d'arxiu.");
            return;
        }

        // Obtenir la ruta del fitxer des dels paràmetres
        String rutaArxiu = args[0];
        mostrarContingutArxiu(rutaArxiu);
    }

    // Funció per mostrar el contingut de l'arxiu o el missatge d'error corresponent
    public static void mostrarContingutArxiu(String rutaArxiu) {
        // Comprovar que el fitxer existeix i no és una carpeta
        if (Files.notExists(Paths.get(rutaArxiu)))
            System.out.println("El fitxer no existeix o no és accessible.");
        else if (Files.isDirectory(Paths.get(rutaArxiu))) {
            System.out.println("El path no correspon a un arxiu, sinó a una carpeta.");
        } else {
            // Llegir i mostrar el contingut del fitxer
            try {
                Files.lines(Paths.get(rutaArxiu)).forEach(System.out::println);
            } catch (Exception e) {
                System.out.println("Error en llegir el fitxer: " + rutaArxiu);
                e.printStackTrace();
            }
        }
    }
}
