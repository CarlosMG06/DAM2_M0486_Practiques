package com.project.utilitats;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the UtilsCSV class.
 * This class tests all public methods, including file I/O operations
 * using a temporary directory to ensure test isolation.
 */
@DisplayName("Tests per a la classe UtilsCSV")
class UtilsCSVTest {

    // Injects a temporary directory before each test
    @TempDir
    Path tempDir;

    private List<String> sampleCsvLines;

    /**
     * Sets up common test data before each test method runs.
     */
    @BeforeEach
    void setUp() {
        sampleCsvLines = new ArrayList<>();
        sampleCsvLines.add("Id,Nom,Departament,Salari");
        sampleCsvLines.add("123,Nicolás,2,1000.00");
        sampleCsvLines.add("435,Xavi,2,1800.50");
        sampleCsvLines.add("876,Daniel,6,700.30");
    }

    @Test
    @DisplayName("Llegeix i escriu un fitxer CSV correctament")
    void testEscriureILlegir() throws IOException {
        Path csvPath = tempDir.resolve("test.csv");

        // Test writing
        UtilsCSV.escriure(csvPath.toString(), sampleCsvLines);
        assertTrue(Files.exists(csvPath), "El fitxer CSV hauria d'haver estat creat.");

        // Test reading
        List<String> llegit = UtilsCSV.llegir(csvPath.toString());

        assertNotNull(llegit, "La llista llegida no hauria de ser nul·la.");
        assertEquals(sampleCsvLines.size(), llegit.size(), "Les llistes haurien de tenir la mateixa mida.");
        assertLinesMatch(sampleCsvLines, llegit);
    }
    
    @Test
    @DisplayName("Llegir un fitxer inexistent ha de retornar nul")
    void testLlegirFitxerInexistent() {
        Path csvPath = tempDir.resolve("noexisteix.csv");
        List<String> resultat = UtilsCSV.llegir(csvPath.toString());
        assertNull(resultat, "Hauria de retornar nul si el fitxer no existeix.");
    }

    @Test
    @DisplayName("Obtenir array a partir d'una línia")
    void testObtenirArrayLinia() {
        String linia = "123,Nicolás,2,1000.00";
        String[] esperat = {"123", "Nicolás", "2", "1000.00"};
        String[] resultat = UtilsCSV.obtenirArrayLinia(linia);
        assertArrayEquals(esperat, resultat, "L'array generat ha de coincidir amb l'esperat.");
    }

    @Test
    @DisplayName("Obtenir les claus (capçalera) del CSV")
    void testObtenirClaus() {
        String[] esperat = {"Id", "Nom", "Departament", "Salari"};
        String[] resultat = UtilsCSV.obtenirClaus(sampleCsvLines);
        assertArrayEquals(esperat, resultat, "Les claus han de ser les de la primera línia.");
    }
    
    @Test
    @DisplayName("Obtenir la posició d'una columna existent")
    void testObtenirPosicioColumnaExistent() {
        assertEquals(0, UtilsCSV.obtenirPosicioColumna(sampleCsvLines, "Id"));
        assertEquals(1, UtilsCSV.obtenirPosicioColumna(sampleCsvLines, "Nom"));
        assertEquals(3, UtilsCSV.obtenirPosicioColumna(sampleCsvLines, "Salari"));
    }

    @Test
    @DisplayName("Obtenir posició d'una columna inexistent ha de retornar -1")
    void testObtenirPosicioColumnaInexistent() {
        assertEquals(-1, UtilsCSV.obtenirPosicioColumna(sampleCsvLines, "Cognom"), "Hauria de retornar -1 per a una columna que no existeix.");
    }

    @Test
    @DisplayName("Obtenir totes les dades d'una columna")
    void testObtenirDadesColumna() {
        String[] esperat = {"Nom", "Nicolás", "Xavi", "Daniel"};
        String[] resultat = UtilsCSV.obtenirDadesColumna(sampleCsvLines, "Nom");
        assertArrayEquals(esperat, resultat, "Les dades de la columna 'Nom' han de ser correctes.");
    }
    
    @Test
    @DisplayName("Obtenir dades d'una columna ha de llançar excepció si la columna no existeix")
    void testObtenirDadesColumnaInexistent() {
        // This method will throw an exception because the position will be -1
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            UtilsCSV.obtenirDadesColumna(sampleCsvLines, "Inexistent");
        }, "S'espera una excepció si la columna no existeix.");
    }
    
    @Test
    @DisplayName("Obtenir el número de línia per un valor i columna")
    void testObtenirNumLiniaExistent() {
        assertEquals(2, UtilsCSV.obtenirNumLinia(sampleCsvLines, "Nom", "Xavi"));
        assertEquals(3, UtilsCSV.obtenirNumLinia(sampleCsvLines, "Id", "876"));
    }

    @Test
    @DisplayName("Obtenir número de línia per un valor inexistent ha de retornar -1")
    void testObtenirNumLiniaInexistent() {
        assertEquals(-1, UtilsCSV.obtenirNumLinia(sampleCsvLines, "Nom", "Laura"), "Hauria de retornar -1 si el valor no es troba.");
    }

    @Test
    @DisplayName("Actualitzar una línia correctament")
    void testActualitzarLinia() {
        int liniaPerActualitzar = 2; // La línia de "Xavi"
        String columna = "Salari";
        String nouValor = "2500.75";
        
        String liniaOriginal = sampleCsvLines.get(liniaPerActualitzar);
        
        UtilsCSV.actualitzarLinia(sampleCsvLines, liniaPerActualitzar, columna, nouValor);
        
        String liniaModificada = sampleCsvLines.get(liniaPerActualitzar);
        
        assertNotEquals(liniaOriginal, liniaModificada, "La línia hauria d'haver canviat.");
        assertTrue(liniaModificada.contains(nouValor), "La línia modificada ha de contenir el nou valor.");
        assertEquals("435,Xavi,2,2500.75", liniaModificada, "La línia completa ha de ser correcta.");
    }
}