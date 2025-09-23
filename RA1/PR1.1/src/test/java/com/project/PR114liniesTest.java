package com.project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PR114liniesTest {

    @TempDir
    Path directoriTemporal;

    @Test
    void testGenerarNumerosAleatoris() throws IOException {
        // Definir el camí del fitxer dins del directori temporal
        File fitxer = new File(directoriTemporal.toFile(), "numeros.txt");

        // Executar el mètode que genera i escriu els números aleatoris
        PR114linies.generarNumerosAleatoris(fitxer.getPath());

        // Comprovar que el fitxer existeix
        assertTrue(fitxer.exists(), "El fitxer hauria d'existir");

        // Llegir el contingut del fitxer amb UTF-8
        try (BufferedReader reader = new BufferedReader(new FileReader(fitxer, StandardCharsets.UTF_8))) {
            List<String> contingut = reader.lines().toList();

            // Comprovar que s'han escrit exactament 10 línies
            assertEquals(10, contingut.size(), "El fitxer hauria de tenir exactament 10 línies.");

            // Comprovar que cada línia conté un número enter
            for (String linia : contingut) {
                try {
                    Integer.parseInt(linia);  // Intentar convertir la línia a un número
                } catch (NumberFormatException e) {
                    fail("Cada línia hauria de contenir un número enter.");
                }
            }
        }

        // Comprovar que l'última línia no acaba amb un salt de línia
        try (BufferedReader reader = new BufferedReader(new FileReader(fitxer, StandardCharsets.UTF_8))) {
            String ultimaLinia = null;
            String linia;
            while ((linia = reader.readLine()) != null) {
                ultimaLinia = linia;
            }
            assertNotNull(ultimaLinia, "L'última línia no hauria de ser nul·la.");
            assertFalse(ultimaLinia.endsWith("\n"), "L'última línia no hauria de tenir un salt de línia al final.");
        }
    }
}
