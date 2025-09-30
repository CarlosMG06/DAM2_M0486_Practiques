package com.project.pr13;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PR131MainTest {

    @TempDir
    File tempDir;  // Directori temporal proporcionat per JUnit

    private PR131Main app;

    @BeforeEach
    void setup() {
        // Inicialitza l'objecte PR131Main amb el directori temporal creat per JUnit
        app = new PR131Main(tempDir);
    }

    @Test
    void testProcessDocumentCreatesFile() {
        // Comprova que el fitxer biblioteca.xml es crea correctament
        app.processarFitxerXML("biblioteca.xml");

        // Comprova que el fitxer s'ha creat dins del directori temporal
        File outputFile = new File(tempDir, "biblioteca.xml");
        assertTrue(outputFile.exists(), "El fitxer biblioteca.xml hauria d'existir.");
    }

    @Test
    void testDocumentContent() {
        // Executa la generació del fitxer XML
        app.processarFitxerXML("biblioteca.xml");

        // Verifica que el contingut del fitxer és correcte
        File outputFile = new File(tempDir, "biblioteca.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(outputFile);
            doc.getDocumentElement().normalize();

            // Comprova els elements del document
            Element biblioteca = (Element) doc.getElementsByTagName("biblioteca").item(0);
            Element llibre = (Element) biblioteca.getElementsByTagName("llibre").item(0);
            String id = llibre.getAttribute("id");
            assertEquals("001", id, "L'ID del llibre hauria de ser '001'.");

            // Comprova el contingut dels elements
            String titol = llibre.getElementsByTagName("titol").item(0).getTextContent();
            assertEquals("El viatge dels venturons", titol, "El títol hauria de ser 'El viatge dels venturons'.");

            String autor = llibre.getElementsByTagName("autor").item(0).getTextContent();
            assertEquals("Joan Pla", autor, "L'autor hauria de ser 'Joan Pla'.");

            String anyPublicacio = llibre.getElementsByTagName("anyPublicacio").item(0).getTextContent();
            assertEquals("1998", anyPublicacio, "L'any de publicació hauria de ser '1998'.");

            String editorial = llibre.getElementsByTagName("editorial").item(0).getTextContent();
            assertEquals("Edicions Mar", editorial, "L'editorial hauria de ser 'Edicions Mar'.");

            String genere = llibre.getElementsByTagName("genere").item(0).getTextContent();
            assertEquals("Aventura", genere, "El gènere hauria de ser 'Aventura'.");

            String pagines = llibre.getElementsByTagName("pagines").item(0).getTextContent();
            assertEquals("320", pagines, "El nombre de pàgines hauria de ser '320'.");

            String disponible = llibre.getElementsByTagName("disponible").item(0).getTextContent();
            assertEquals("true", disponible, "El valor 'disponible' hauria de ser 'true'.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testSetAndGetDataDir() {
        // Comprova el getter i el setter de dataDir
        File newDataDir = new File(tempDir, "newDir");
        app.setDataDir(newDataDir);
        assertEquals(newDataDir, app.getDataDir(), "El getter hauria de retornar el nou directori assignat.");
    }
}
