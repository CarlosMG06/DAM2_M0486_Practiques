package com.project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PR110ReadFileTest {

    @TempDir
    Path directoriTemporal;

    @Test
    void testLlegirIMostrarFitxer() {
        // Preparació: Crear un fitxer de prova amb contingut conegut
        String nomFitxer = "GestioTasques.java";
        Path camiFitxer = directoriTemporal.resolve(nomFitxer);
        List<String> contingutEsperat = List.of(
                "public class GestioTasques {",
                "    public static void main(String[] args) {",
                "        System.out.println(\"Hola, món!\");",
                "    }",
                "}"
        );

        try {
            Files.write(camiFitxer, contingutEsperat, Charset.forName("UTF-8"));

            // Redirigir la sortida estàndard a un stream per capturar-la
            ByteArrayOutputStream sortidaCapturada = new ByteArrayOutputStream();
            PrintStream sortidaOriginal = System.out;
            System.setOut(new PrintStream(sortidaCapturada));

            // Executar el mètode a provar (llegirIMostrarFitxer)
            PR110ReadFile.llegirIMostrarFitxer(camiFitxer.toString());

            // Restaurar la sortida estàndard
            System.setOut(sortidaOriginal);

            // Processar la sortida capturada
            String[] sortida = sortidaCapturada.toString().split(System.lineSeparator());
            assertEquals(contingutEsperat.size(), sortida.length, "El nombre de línies hauria de coincidir");

            // Verificar que cada línia té el format correcte
            for (int i = 0; i < contingutEsperat.size(); i++) {
                String liniaEsperada = String.format("%d: %s", i + 1, contingutEsperat.get(i));
                assertEquals(liniaEsperada, sortida[i], "La línia hauria de coincidir");
            }

        } catch (IOException e) {
            fail("No hauria de fallar amb excepció: " + e.getMessage());
        }
    }
}
