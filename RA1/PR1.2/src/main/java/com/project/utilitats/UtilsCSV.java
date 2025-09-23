package com.project.utilitats;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class UtilsCSV {

    // Llegeix un fitxer CSV i el retorna com una llista de línies
    public static List<String> llegir(String camiFitxer) {
        List<String> resultat = null;
        try {
            resultat = Files.readAllLines(Paths.get(camiFitxer), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Error en llegir el fitxer CSV: " + e.getMessage());
        }
        return resultat;
    }

    // Escriu una llista de línies en un fitxer CSV
    public static void escriure(String camiFitxer, List<String> csvLinies) {
        Path sortida = Paths.get(camiFitxer);
        try {
            Files.write(sortida, csvLinies, Charset.defaultCharset());
        } catch (IOException e) {
            System.err.println("Error en escriure al fitxer CSV: " + e.getMessage());
        }
    }

    // Transforma una línia separada per comes en un array
    public static String[] obtenirArrayLinia(String linia) {
        return linia.split(",");
    }

    // Retorna les columnes (claus) de la primera línia del CSV
    public static String[] obtenirClaus(List<String> csvLinies) {
        return obtenirArrayLinia(csvLinies.get(0));
    }

    // Obté la posició d'una columna específica
    public static int obtenirPosicioColumna(List<String> csvLinies, String columna) {
        String[] claus = obtenirClaus(csvLinies);
        return Arrays.asList(claus).indexOf(columna);
    }

    // Obté totes les dades d'una columna específica
    public static String[] obtenirDadesColumna(List<String> csvLinies, String columna) {
        String[] resultat = new String[csvLinies.size()];
        int posicioColumna = obtenirPosicioColumna(csvLinies, columna);
        for (int i = 0; i < csvLinies.size(); i++) {
            String[] arrayLinia = obtenirArrayLinia(csvLinies.get(i));
            resultat[i] = arrayLinia[posicioColumna];
        }
        return resultat;
    }

    // Obté el número de línia que conté una combinació de columna i valor
    public static int obtenirNumLinia(List<String> csvLinies, String columna, String valor) {
        int posicioColumna = obtenirPosicioColumna(csvLinies, columna);
        for (int i = 0; i < csvLinies.size(); i++) {
            String[] arrayLinia = obtenirArrayLinia(csvLinies.get(i));
            if (arrayLinia[posicioColumna].equals(valor)) {
                return i;
            }
        }
        return -1; // No s'ha trobat la línia
    }

    // Actualitza una línia del CSV amb un nou valor a una columna específica
    public static void actualitzarLinia(List<String> csvLinies, int linia, String columna, String valor) {
        String[] arrayLinia = obtenirArrayLinia(csvLinies.get(linia));
        int posicioColumna = obtenirPosicioColumna(csvLinies, columna);
        arrayLinia[posicioColumna] = valor;
        csvLinies.set(linia, String.join(",", arrayLinia));
    }

    // Mostra les línies d'un fitxer CSV
    public static void llistar(List<String> csvLinies) {
        csvLinies.forEach(System.out::println);
    }
}
