package com.project;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import com.project.excepcions.IOFitxerExcepcio;

import java.io.*;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class PR120mainPersonesHashmapTest {

    @TempDir
    File directoriTemporal;  // JUnit gestiona automàticament la creació i eliminació del directori temporal

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        // Redirigeix la sortida del sistema per capturar els outputs
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        // Restaura la sortida del sistema
        System.setOut(originalOut);
    }

    @Test
    void testEscriureILlegirPersones() throws IOException, IOFitxerExcepcio {
        // Preparació: Utilitzar el directori temporal proporcionat per JUnit
        File fitxerTemporal = new File(directoriTemporal, "PR120persones.dat");

        // Dades de prova
        HashMap<String, Integer> persones = new HashMap<>();
        persones.put("Anna", 25);
        persones.put("Bernat", 30);

        // Reemplaçar la ruta del fitxer per utilitzar el fitxer temporal
        String filePathAnterior = PR120mainPersonesHashmap.getFilePath();
        PR120mainPersonesHashmap.setFilePath(fitxerTemporal.getAbsolutePath());

        // Escriure i llegir les dades
        PR120mainPersonesHashmap.escriurePersones(persones);
        PR120mainPersonesHashmap.llegirPersones();

        // Comprovar els resultats
        String output = outContent.toString();
        assertTrue(output.contains("Anna: 25 anys"));
        assertTrue(output.contains("Bernat: 30 anys"));

        // Restaurar la ruta original
        PR120mainPersonesHashmap.setFilePath(filePathAnterior);
    }

    @Test
    void testLlegirFitxerInexistent() throws IOException {
        // Preparació: Utilitzar el directori temporal proporcionat per JUnit
        File fitxerInexistent = new File(directoriTemporal, "PR120persones.dat");
    
        // Assegura que el fitxer no existeix, si existeix, elimina'l
        if (fitxerInexistent.exists()) {
            assertTrue(fitxerInexistent.delete());
        }
    
        // Verifica que el fitxer no existeix
        assertFalse(fitxerInexistent.exists());
    
        // Reemplaçar la ruta del fitxer per utilitzar el fitxer temporal
        String filePathAnterior = PR120mainPersonesHashmap.getFilePath();
        PR120mainPersonesHashmap.setFilePath(fitxerInexistent.getAbsolutePath());
    
        // Verificar que es llença l'excepció
        IOFitxerExcepcio excepcio = assertThrows(IOFitxerExcepcio.class, () -> {
            PR120mainPersonesHashmap.llegirPersones();
        });
    
        // Comprovar que el missatge d'error és adequat
        assertTrue(excepcio.getMessage().contains("Error en llegir les persones del fitxer"));
    
        // Comprovar que l'excepció original està embolcallada
        assertNotNull(excepcio.getCause());
        assertTrue(excepcio.getCause() instanceof FileNotFoundException);
    
        // Restaurar la ruta original
        PR120mainPersonesHashmap.setFilePath(filePathAnterior);
    }
    
    
    @Test
    void testEscriureFitxerAmbError() throws IOException {
        // Preparació: Utilitzar el directori temporal proporcionat per JUnit
        File fitxerAmbError = new File(directoriTemporal, "PR120persones.dat");
    
        // Assegura que el fitxer existeix i després elimina els permisos d'escriptura
        assertTrue(fitxerAmbError.createNewFile());  // Crea el fitxer temporal
        fitxerAmbError.setWritable(false);  // El fitxer no és escrivible ara
    
        // Dades de prova
        HashMap<String, Integer> persones = new HashMap<>();
        persones.put("Test", 20);
    
        // Reemplaçar la ruta del fitxer per utilitzar el fitxer temporal
        String filePathAnterior = PR120mainPersonesHashmap.getFilePath();
        PR120mainPersonesHashmap.setFilePath(fitxerAmbError.getAbsolutePath());
    
        // Verificar que es llença l'excepció
        IOFitxerExcepcio excepcio = assertThrows(IOFitxerExcepcio.class, () -> {
            PR120mainPersonesHashmap.escriurePersones(persones);
        });
    
        // Comprovar que el missatge d'error és adequat
        assertTrue(excepcio.getMessage().contains("Error en escriure les persones al fitxer"));
    
        // Comprovar que l'excepció original està embolcallada
        assertNotNull(excepcio.getCause());
        assertTrue(excepcio.getCause() instanceof IOException);
    
        // Restaurar la ruta original i permisos del fitxer
        PR120mainPersonesHashmap.setFilePath(filePathAnterior);
        fitxerAmbError.setWritable(true);  // Restaurar el permís d'escriptura
    }

}
