package com.project.exemples;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;

import static org.junit.jupiter.api.Assertions.*;

class RandomAccessFilesVideojocsManagerTest {

    @TempDir
    File tempDir;

    private RandomAccessFilesVideojocsManager manager;

    @BeforeEach
    void setUp() {
        manager = new RandomAccessFilesVideojocsManager();
        manager.setFilePath(new File(tempDir, "videojocs.dat").getAbsolutePath());
    }

    @Test
    void testAfegirIConsultarVideojoc() throws IOException {
        // Afegir un videojoc
        manager.afegirVideojoc(1, "Assassin's Creed");

        // Consultar el videojoc afegit
        assertDoesNotThrow(() -> manager.consultarVideojoc(1));
    }

    @Test
    void testLlistarVideojocs() throws IOException {
        // Afegir alguns videojocs
        manager.afegirVideojoc(1, "Assassin's Creed");
        manager.afegirVideojoc(2, "The Legend of Zelda");

        // Capturar la sortida estàndard (System.out)
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            // Llistar videojocs
            manager.llistarVideojocs();
        } finally {
            // Restaurar la sortida estàndard
            System.setOut(originalOut);
        }

        // Obtenir la sortida capturada com a String
        String output = outputStream.toString();

        // Verificar que la sortida conté els videojocs esperats
        assertTrue(output.contains("Registre: 1, Nom: Assassin's Creed"));
        assertTrue(output.contains("Registre: 2, Nom: The Legend of Zelda"));
    }

    @Test
    void testConsultarVideojocNoExistent() throws IOException {
        // Afegir alguns videojocs
        manager.afegirVideojoc(1, "Assassin's Creed");
        manager.afegirVideojoc(2, "The Legend of Zelda");

        // Consultar un registre no existent
        assertDoesNotThrow(() -> manager.consultarVideojoc(3));
    }

    @Test
    void testActualitzarNomVideojoc() throws IOException {
        // Afegir un videojoc i actualitzar el seu nom
        manager.afegirVideojoc(1, "Assassin's Creed");
        manager.actualitzarNomVideojoc(1, "Assassin's Creed Valhalla");

        // Consultar per verificar l'actualització
        assertDoesNotThrow(() -> manager.consultarVideojoc(1));
    }

    @Test
    void testMidaRegistreCorrecte() throws IOException {
        // Verificar que la mida de cada registre sigui correcta
        File file = new File(manager.getFilePath());
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            manager.afegirVideojoc(1, "Test Videojoc");
            assertEquals(44, raf.length());  // 4 bytes per ID + 40 bytes per nom en UTF-8
        }
    }

    @Test
    void testTruncamentNomVideojoc() throws IOException {
        // Afegim un videojoc amb un nom molt llarg
        manager.afegirVideojoc(1, "UnNomMoltLlargQueSuperaEls40BytesDeLongitud");

        // Capturar la sortida
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            manager.llistarVideojocs();
        } finally {
            System.setOut(originalOut);
        }

        String output = outputStream.toString();
        // Assegurar que el nom ha estat truncat a una longitud màxima raonable
        assertTrue(output.contains("Registre: 1, Nom: UnNomMoltLlargQueSuperaEls40By"));
    }

    @Test
    void testLecturaMultiplesVideojocs() throws IOException {
        for (int i = 1; i <= 100; i++) {
            manager.afegirVideojoc(i, "Videojoc" + i);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            manager.llistarVideojocs();
        } finally {
            System.setOut(originalOut);
        }

        String output = outputStream.toString();
        for (int i = 1; i <= 100; i++) {
            assertTrue(output.contains("Registre: " + i));
        }
    }

    @Test
    void testActualitzarVideojocNoExistent() throws IOException {
        // Intentem actualitzar un videojoc que no existeix
        manager.actualitzarNomVideojoc(999, "Videojoc Inexistent");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            manager.consultarVideojoc(999);
        } finally {
            System.setOut(originalOut);
        }

        String output = outputStream.toString();
        assertTrue(output.contains("No s'ha trobat el videojoc amb registre: 999"));
    }

    @Test
    void testNomsAmbAccentsICaractersEspecials() throws IOException {
        manager.afegirVideojoc(1, "Final Fantasy");
        manager.afegirVideojoc(2, "Pokémon");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            manager.llistarVideojocs();
        } finally {
            System.setOut(originalOut);
        }

        String output = outputStream.toString();
        assertTrue(output.contains("Final Fantasy"));
        assertTrue(output.contains("Pokémon"));
    }

    @Test
    void testNomsAmbCaractersXinesos() throws IOException {
        manager.afegirVideojoc(1, "王者荣耀");  // Nom comú en xinès
        manager.afegirVideojoc(2, "和平精英");  // Un altre nom comú en xinès

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            manager.llistarVideojocs();
        } finally {
            System.setOut(originalOut);
        }

        String output = outputStream.toString();
        assertTrue(output.contains("王者荣耀"));
        assertTrue(output.contains("和平精英"));
    }
}
