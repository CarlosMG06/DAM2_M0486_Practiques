package com.project.pr13;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.project.pr13.format.PersonaFormatter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PR130MainTest {

    private static final String XML_CONTENT = """
            <?xml version="1.0" encoding="UTF-8"?>
            <persones>
                <persona>
                    <nom>Maria</nom>
                    <cognom>López</cognom>
                    <edat>36</edat>
                    <ciutat>Barcelona</ciutat>
                </persona>
                <persona>
                    <nom>Gustavo</nom>
                    <cognom>Catadasús</cognom>
                    <edat>15</edat>
                    <ciutat>London</ciutat>
                </persona>
            </persones>
            """;

    @TempDir
    File tempDir;  // Directori temporal creat automàticament per JUnit

    private PR130Main app;
    private File tempFile;

    @BeforeEach
    void setup() throws IOException {
        // Crea un fitxer temporal "persones.xml" dins del directori temporal creat per JUnit
        tempFile = new File(tempDir, "persones.xml");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(XML_CONTENT);
        }

        // Inicialitza l'objecte PR130Main amb el directori temporal
        app = new PR130Main(tempDir);
    }

    @Test
    void testParseXML() {
        // Verifica que el document es parsegi correctament des del fitxer temporal
        Document doc = PR130Main.parseXML(tempFile);
        assertNotNull(doc, "El document no hauria de ser nul després de parsejar.");
    }

    @Test
    void testLlegeixPersones() {
        // Comprova que el document conté dues persones
        Document doc = PR130Main.parseXML(tempFile);
        NodeList persones = doc.getElementsByTagName("persona");
        assertEquals(2, persones.getLength(), "Hauria d'haver-hi dues persones al document XML.");
    }

    @Test
    void testImprimirCapçaleres() {
        String expected = "Nom      Cognom        Edat  Ciutat\n-------- -------------- ----- ---------";
        assertEquals(expected, PersonaFormatter.getCapçaleres(), "Les capçaleres no són correctes.");
    }

    @Test
    void testFormatarPersona() {
        String expected = "Maria    López          36    Barcelona";
        String resultat = PersonaFormatter.formatarPersona("Maria", "López", "36", "Barcelona");
        assertEquals(expected, resultat, "El format de la persona no és correcte.");
    }

    @Test
    void testProcessFile() {
        // Comprova que el programa llegeix correctament el fitxer i processa les persones
        app.processarFitxerXML("persones.xml");
    }
}
