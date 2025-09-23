package com.project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class PR111FilesTest {

    @TempDir
    Path directoriTemporal;

    @Test
    void testGestionarArxius() {
        // Preparació: Definir el camí de la carpeta temporal dins del directori temporal proporcionat per JUnit
        File carpeta = new File(directoriTemporal.toFile(), "myFiles");

        // Executar el mètode de gestió d'arxius passant la ruta del directori temporal
        PR111Files.gestionarArxius(directoriTemporal.toString());

        // Comprovar que la carpeta existeix
        assertTrue(carpeta.exists() && carpeta.isDirectory(), "La carpeta hauria d'existir");

        // Comprovar que s'ha creat "renamedFile.txt" i no existeix "file2.txt"
        File renamedFile = new File(carpeta, "renamedFile.txt");
        assertTrue(renamedFile.exists(), "El fitxer hauria d'haver estat renombrat a 'renamedFile.txt'");
        File file2 = new File(carpeta, "file2.txt");
        assertFalse(file2.exists(), "El fitxer 'file2.txt' no hauria d'existir");

        // Comprovar que s'ha eliminat "file1.txt"
        File file1 = new File(carpeta, "file1.txt");
        assertFalse(file1.exists(), "El fitxer 'file1.txt' hauria d'haver estat eliminat");

        // Comprovar el llistat final de la carpeta
        String[] arxiusFinals = carpeta.list();
        assertNotNull(arxiusFinals);
        assertEquals(1, arxiusFinals.length, "Ha de quedar només un fitxer a la carpeta");
        assertEquals("renamedFile.txt", arxiusFinals[0], "L'únic fitxer restant hauria de ser 'renamedFile.txt'");
    }
}
