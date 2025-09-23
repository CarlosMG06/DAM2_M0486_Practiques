package com.project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class PR112catTest {

    @TempDir
    Path directoriTemporal;

    @Test
    void testMostrarContingutArxiuCorrecte() {
        // Preparació: Crear un fitxer de prova dins del directori temporal
        String nomFitxer = "GestioTasques.java";
        File fitxer = directoriTemporal.resolve(nomFitxer).toFile();
        String contingut = """
            public class GestioTasques {
                public static void main(String[] args) {
                    System.out.println("Hola, món!");
                }
            }""";

        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fitxer), StandardCharsets.UTF_8))) {
            writer.println(contingut);
        } catch (IOException e) {
            fail("No s'hauria de produir cap error creant el fitxer de prova.");
        }

        // Redirigir la sortida estàndard per capturar-la
        ByteArrayOutputStream sortidaCapturada = new ByteArrayOutputStream();
        PrintStream sortidaOriginal = System.out;
        System.setOut(new PrintStream(sortidaCapturada));

        // Executar el mètode a provar
        PR112cat.mostrarContingutArxiu(fitxer.getPath());

        // Restaurar la sortida estàndard
        System.setOut(sortidaOriginal);

        // Comprovar que el contingut es mostra correctament
        String sortida = sortidaCapturada.toString().trim();
        assertTrue(sortida.contains("public class GestioTasques"));
        assertTrue(sortida.contains("System.out.println(\"Hola, món!\");"));
    }

    @Test
    void testMostrarMissatgeEsCarpeta() {
        // Preparació: Utilitzar un directori temporal
        File carpeta = directoriTemporal.toFile();

        // Redirigir la sortida estàndard per capturar-la
        ByteArrayOutputStream sortidaCapturada = new ByteArrayOutputStream();
        PrintStream sortidaOriginal = System.out;
        System.setOut(new PrintStream(sortidaCapturada));

        // Executar el mètode a provar amb la carpeta com a path
        PR112cat.mostrarContingutArxiu(carpeta.getPath());

        // Restaurar la sortida estàndard
        System.setOut(sortidaOriginal);

        // Comprovar el missatge de carpeta
        String sortida = sortidaCapturada.toString().trim();
        assertEquals("El path no correspon a un arxiu, sinó a una carpeta.", sortida);
    }

    @Test
    void testMostrarMissatgeFitxerNoExisteix() {
        // Preparació: Definir un fitxer que no existeix dins del directori temporal
        File fitxerInexistent = new File(directoriTemporal.toFile(), "inexistent.txt");

        // Redirigir la sortida estàndard per capturar-la
        ByteArrayOutputStream sortidaCapturada = new ByteArrayOutputStream();
        PrintStream sortidaOriginal = System.out;
        System.setOut(new PrintStream(sortidaCapturada));

        // Executar el mètode a provar amb el fitxer inexistent
        PR112cat.mostrarContingutArxiu(fitxerInexistent.getPath());

        // Restaurar la sortida estàndard
        System.setOut(sortidaOriginal);

        // Comprovar el missatge de fitxer no existent
        String sortida = sortidaCapturada.toString().trim();
        assertEquals("El fitxer no existeix o no és accessible.", sortida);
    }
}
