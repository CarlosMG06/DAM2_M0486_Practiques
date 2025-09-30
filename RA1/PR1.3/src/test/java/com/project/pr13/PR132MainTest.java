package com.project.pr13;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PR132MainTest {

    private static final String XML_CONTENT = """
            <?xml version="1.0" encoding="UTF-8"?>
            <cursos>
                <curs id="AMS2">
                    <tutor>LARA, Francesc</tutor>
                    <alumnes>
                        <alumne>ALVAREZ, Tomas</alumne>
                        <alumne>CAMACHO, David</alumne>
                    </alumnes>
                    <moduls>
                        <modul id="M06">
                            <titol>Accés a dades</titol>
                        </modul>
                    </moduls>
                </curs>
                <curs id="AWS1">
                    <tutor>Julian Fuentes</tutor>
                    <alumnes>
                        <alumne>FERNANDEZ, Ruben</alumne>
                        <alumne>JANSSEN, Gerard</alumne>
                    </alumnes>
                </curs>
            </cursos>
            """;

    @TempDir
    File tempDir;  // Directori temporal proporcionat per JUnit

    private Path tempFilePath;
    private PR132Main app;

    @BeforeEach
    void setup() throws IOException {
        // Crear el fitxer temporal "cursos.xml" dins del directori temporal
        File tempFile = new File(tempDir, "cursos.xml");
        tempFilePath = tempFile.toPath();

        // Escriure el contingut de base al fitxer "cursos.xml"
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(XML_CONTENT);
        }

        // Inicialitzar l'objecte PR132Main amb el fitxer temporal "cursos.xml"
        app = new PR132Main(tempFilePath);
    }

    @Test
    void testLlistarCursos() {
        // Comprovar que la llista de cursos és correcta
        List<List<String>> cursos = app.llistarCursos();
        assertEquals(2, cursos.size(), "Hauria d'haver-hi dos cursos.");

        // Comprovar detalls dels cursos
        assertEquals("AMS2", cursos.get(0).get(0), "El primer curs hauria de tenir ID 'AMS2'.");
        assertEquals("AWS1", cursos.get(1).get(0), "El segon curs hauria de tenir ID 'AWS1'.");
    }

    @Test
    void testMostrarModuls() {
        // Comprovar que el curs AMS2 té un mòdul amb ID M06
        List<List<String>> moduls = app.mostrarModuls("AMS2");
        assertEquals(1, moduls.size(), "El curs AMS2 hauria de tenir un mòdul.");
        assertEquals("M06", moduls.get(0).get(0), "El mòdul del curs AMS2 hauria de tenir ID 'M06'.");
    }

    @Test
    void testLlistarAlumnes() {
        // Comprovar que la llista d'alumnes del curs AMS2 és correcta
        List<String> alumnes = app.llistarAlumnes("AMS2");
        assertTrue(alumnes.contains("ALVAREZ, Tomas"), "L'alumne ALVAREZ, Tomas hauria de ser al curs AMS2.");
        assertTrue(alumnes.contains("CAMACHO, David"), "L'alumne CAMACHO, David hauria de ser al curs AMS2.");
    }

    @Test
    void testAfegirAlumne() {
        // Afegir un nou alumne al curs AWS1
        app.afegirAlumne("AWS1", "NOU, Alumne");

        // Comprovar que l'alumne s'ha afegit correctament
        List<String> alumnes = app.llistarAlumnes("AWS1");
        assertTrue(alumnes.contains("NOU, Alumne"), "L'alumne hauria d'haver estat afegit.");
    }

    @Test
    void testEliminarAlumne() {
        // Eliminar l'alumne CAMACHO, David del curs AMS2
        app.eliminarAlumne("AMS2", "CAMACHO, David");

        // Comprovar que l'alumne s'ha eliminat correctament
        List<String> alumnes = app.llistarAlumnes("AMS2");
        assertTrue(!alumnes.contains("CAMACHO, David"), "L'alumne CAMACHO, David hauria d'haver estat eliminat.");
    }
}
