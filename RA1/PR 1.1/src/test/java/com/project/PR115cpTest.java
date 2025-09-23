package com.project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PR115cpTest {

    @TempDir
    Path directoriTemporal;

    @Test
    void testCopiarArxiuAmbLiniesEnBlanc() throws IOException {
        // Crear un fitxer origen dins del directori temporal
        File fitxerOrigen = new File(directoriTemporal.toFile(), "origen.txt");

        // Escriure contingut al fitxer d'origen, incloent línies en blanc
        List<String> contingutOrigen = new ArrayList<>();
        contingutOrigen.add("Primera línia");
        contingutOrigen.add(""); // Línia en blanc
        contingutOrigen.add("Segona línia");
        contingutOrigen.add(""); // Línia en blanc
        contingutOrigen.add("Tercera línia");

        Files.write(fitxerOrigen.toPath(), contingutOrigen, StandardCharsets.UTF_8);

        // Definir la ruta del fitxer de destí dins del directori temporal
        File fitxerDesti = new File(directoriTemporal.toFile(), "desti.txt");

        // Executar el mètode que copia l'arxiu
        PR115cp.copiarArxiu(fitxerOrigen.getPath(), fitxerDesti.getPath());

        // Comprovar que el fitxer de destí existeix
        assertTrue(fitxerDesti.exists(), "El fitxer de destí hauria d'existir");

        // Llegir el contingut del fitxer de destí i comprovar que és igual al contingut del fitxer origen
        try (BufferedReader reader = new BufferedReader(new FileReader(fitxerDesti, StandardCharsets.UTF_8))) {
            List<String> contingutDesti = new ArrayList<>();
            String linia;
            while ((linia = reader.readLine()) != null) {
                contingutDesti.add(linia);
            }

            assertEquals(contingutOrigen, contingutDesti, "El contingut del fitxer de destí hauria de ser igual al contingut d'origen, incloent les línies en blanc.");
        }
    }

    @Test
    void testCopiarArxiuAmbLiniaEnBlancFinal() throws IOException {
        // Crear un fitxer origen dins del directori temporal
        File fitxerOrigen = new File(directoriTemporal.toFile(), "origen.txt");
    
        // Escriure contingut al fitxer d'origen, incloent línies en blanc i una línia en blanc al final
        List<String> contingutOrigen = new ArrayList<>();
        contingutOrigen.add("Primera línia");
        contingutOrigen.add(""); // Línia en blanc
        contingutOrigen.add("Segona línia");
        contingutOrigen.add(""); // Línia en blanc
        contingutOrigen.add("Tercera línia");
    
        // Aquesta línia en blanc no serà detectada amb BufferedReader.readLine() perquè és l'última
        // Això comprova si el fitxer acaba amb un salt de línia
        Files.write(fitxerOrigen.toPath(), contingutOrigen, StandardCharsets.UTF_8);
    
        // Definir la ruta del fitxer de destí dins del directori temporal
        File fitxerDesti = new File(directoriTemporal.toFile(), "desti.txt");
    
        // Executar el mètode que copia l'arxiu
        PR115cp.copiarArxiu(fitxerOrigen.getPath(), fitxerDesti.getPath());
    
        // Comprovar que el fitxer de destí existeix
        assertTrue(fitxerDesti.exists(), "El fitxer de destí hauria d'existir");
    
        // Llegir tot el contingut del fitxer de destí
        List<String> contingutDesti = Files.readAllLines(fitxerDesti.toPath(), StandardCharsets.UTF_8);
    
        // Comprovar que el contingut és el mateix incloent les línies en blanc
        assertEquals(contingutOrigen, contingutDesti, "El contingut del fitxer de destí hauria de ser igual al contingut d'origen.");
    
        // Comprovar que l'arxiu acaba amb una línia en blanc (salt de línia)
        try (BufferedReader reader = new BufferedReader(new FileReader(fitxerDesti, StandardCharsets.UTF_8))) {
            int lastChar = reader.read();
            while (reader.ready()) {
                lastChar = reader.read();
            }
            assertEquals('\n', lastChar, "El fitxer hauria d'acabar amb un salt de línia.");
        }
    }
        

    @Test
    void testArxiuNoExisteix() {
        // Definir la ruta d'un fitxer origen que no existeix
        File fitxerInexistent = new File(directoriTemporal.toFile(), "inexistent.txt");

        // Definir la ruta del fitxer de destí dins del directori temporal
        File fitxerDesti = new File(directoriTemporal.toFile(), "desti.txt");

        // Executar el mètode que intenta copiar un fitxer inexistent
        PR115cp.copiarArxiu(fitxerInexistent.getPath(), fitxerDesti.getPath());

        // Comprovar que el fitxer de destí no s'ha creat
        assertFalse(fitxerDesti.exists(), "El fitxer de destí no hauria de crear-se si l'origen no existeix.");
    }
}
