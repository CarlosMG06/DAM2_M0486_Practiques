package com.project;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;

import static org.junit.jupiter.api.Assertions.*;

class PR124mainTest {

    @TempDir
    File tempDir;

    private PR124main gestor;

    @BeforeEach
    void setUp() {
        gestor = new PR124main();
        gestor.setFilePath(new File(tempDir, "PR124estudiants.dat").getAbsolutePath());
    }

    @Test
    void testAfegirIConsultarEstudiant() throws IOException {
        // Afegir un estudiant
        gestor.afegirEstudiantFitxer(1, "Estudiant Test", 8.5f);
        
        // Consultar l'estudiant afegit
        assertDoesNotThrow(() -> gestor.consultarNotaFitxer(1));
    }

    @Test
    void testLlistarEstudiants() throws IOException {
        // Afegir alguns estudiants
        gestor.afegirEstudiantFitxer(1, "Joan", 7.0f);
        gestor.afegirEstudiantFitxer(2, "Marta", 9.0f);

        // Capturar la sortida estàndard (System.out)
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        try {
            // Llistar estudiants
            gestor.llistarEstudiantsFitxer();
        } finally {
            // Restaurar la sortida estàndard
            System.setOut(originalOut);
        }

        // Obtenir la sortida capturada com a String
        String output = outputStream.toString();

        // Verificar que la sortida conté els estudiants esperats
        assertTrue(output.contains("Registre: 1, Nom: Joan, Nota: 7.0"));
        assertTrue(output.contains("Registre: 2, Nom: Marta, Nota: 9.0"));
    }

    @Test
    void testConsultarEstudiantNoExistent() throws IOException {
        // Intentar consultar un estudiant que no existeix
        gestor.afegirEstudiantFitxer(1, "Joan", 7.0f);
        gestor.afegirEstudiantFitxer(2, "Marta", 9.0f);
        
        // Consultar un registre no existent
        assertDoesNotThrow(() -> gestor.consultarNotaFitxer(3));
    }

    @Test
    void testActualitzarNotaEstudiant() throws IOException {
        // Afegir un estudiant i actualitzar la seva nota
        gestor.afegirEstudiantFitxer(1, "Anna", 6.0f);
        gestor.actualitzarNotaFitxer(1, 9.5f);
        
        // Consultar per verificar l'actualització
        assertDoesNotThrow(() -> gestor.consultarNotaFitxer(1));
    }
    
    @Test
    void testMidaRegistreCorrecte() throws IOException {
        // Verificar que la mida de cada registre sigui correcta (48 bytes)
        File file = new File(gestor.getFilePath());
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            gestor.afegirEstudiantFitxer(1, "Test", 5.0f);
            assertEquals(48, raf.length());  // Verifica la mida del fitxer
        }
    }

    @Test
    void testTruncamentNom() throws IOException {
        // Afegim un estudiant amb un nom molt llarg
        gestor.afegirEstudiantFitxer(1, "UnNomRealmentMoltLlargQueSuperaEls40Bytes", 7.5f);
    
        // Capturar la sortida
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    
        try {
            gestor.llistarEstudiantsFitxer();
        } finally {
            System.setOut(originalOut);
        }
    
        String output = outputStream.toString();
        // Assegurar que el nom ha estat truncat a una longitud màxima raonable
        assertTrue(output.contains("Registre: 1, Nom: UnNomRealmentMoltLlargQueS"));
    }
 
    @Test
    void testLecturaMultiplesRegistres() throws IOException {
        for (int i = 1; i <= 100; i++) {
            gestor.afegirEstudiantFitxer(i, "Estudiant" + i, i % 10);
        }
    
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    
        try {
            gestor.llistarEstudiantsFitxer();
        } finally {
            System.setOut(originalOut);
        }
    
        String output = outputStream.toString();
        for (int i = 1; i <= 100; i++) {
            assertTrue(output.contains("Registre: " + i));
        }
    }

    @Test
    void testActualitzarRegistreNoExistent() throws IOException {
        // Intentem actualitzar un estudiant que no existeix
        gestor.actualitzarNotaFitxer(999, 5.0f);
    
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    
        try {
            gestor.consultarNotaFitxer(999);
        } finally {
            System.setOut(originalOut);
        }
    
        String output = outputStream.toString();
        assertTrue(output.contains("No s'ha trobat l'estudiant amb registre: 999"));
    }

    @Test
    void testNomsAmbAccentsICaractersEspecials() throws IOException {
        gestor.afegirEstudiantFitxer(1, "José García", 8.5f);
        gestor.afegirEstudiantFitxer(2, "Renée O'Connor", 7.5f);
    
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    
        try {
            gestor.llistarEstudiantsFitxer();
        } finally {
            System.setOut(originalOut);
        }
    
        String output = outputStream.toString();
        assertTrue(output.contains("José García"));
        assertTrue(output.contains("Renée O'Connor"));
    }

    @Test
    void testNomsAmbCaractersXinesos() throws IOException {
        gestor.afegirEstudiantFitxer(1, "张伟", 9.0f);  // Nom comú en xinès
        gestor.afegirEstudiantFitxer(2, "王芳", 8.0f);  // Un altre nom comú en xinès
    
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    
        try {
            gestor.llistarEstudiantsFitxer();
        } finally {
            System.setOut(originalOut);
        }
    
        String output = outputStream.toString();
        assertTrue(output.contains("张伟"));
        assertTrue(output.contains("王芳"));
    }
}
