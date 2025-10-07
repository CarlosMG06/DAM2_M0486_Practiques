package com.project.pr14;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import com.project.objectes.Llibre;

/**
 * Classe de tests per a PR14GestioLlibreriaMain.
 */
public class PR14GestioLlibreriaJacksonMainTest {

    private static final String JSON_CONTENT = """
            [
                {"id": 1, "titol": "Els vents del nord", "autor": "Clara Rojas", "any": 1990},
                {"id": 2, "titol": "Muntanyes màgiques", "autor": "Joan Peris", "any": 2005},
                {"id": 3, "titol": "El secret del mar", "autor": "Laura Vidal", "any": 2010}
            ]
            """;

    @TempDir
    File tempDir;

    private File tempFile;
    private PR14GestioLlibreriaJacksonMain app;

    @BeforeEach
    void setup() throws IOException {
        // Crea un fitxer temporal "llibres_input.json" dins del directori temporal creat per JUnit
        tempFile = new File(tempDir, "llibres_input.json");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(JSON_CONTENT);
        }
        // Inicialitza l'objecte PR14GestioLlibreriaMain amb el fitxer temporal
        app = new PR14GestioLlibreriaJacksonMain(tempFile);
    }

    @Test
    void testCarregarLlibres() {
        List<Llibre> llibres = app.carregarLlibres();
        assertNotNull(llibres, "La llista de llibres no hauria de ser nul.");
        assertEquals(3, llibres.size(), "Hauria d'haver-hi tres llibres al document JSON.");
    }

    @Test
    void testModificarAnyPublicacio() {
        List<Llibre> llibres = app.carregarLlibres();
        app.modificarAnyPublicacio(llibres, 1, 1995);
        assertEquals(1995, llibres.get(0).getAny(), "L'any del llibre amb id 1 hauria de ser 1995.");
    }

    @Test
    void testAfegirNouLlibre() {
        List<Llibre> llibres = app.carregarLlibres();
        app.afegirNouLlibre(llibres, new Llibre(4, "Històries de la ciutat", "Miquel Soler", 2022));
        assertEquals(4, llibres.size(), "Hauria d'haver-hi quatre llibres després d'afegir-ne un nou.");
        assertEquals("Històries de la ciutat", llibres.get(3).getTitol(), "El títol del quart llibre hauria de ser 'Històries de la ciutat'.");
    }

    @Test
    void testEsborrarLlibre() {
        List<Llibre> llibres = app.carregarLlibres();
        app.esborrarLlibre(llibres, 2);
        assertEquals(2, llibres.size(), "Hauria d'haver-hi dos llibres després d'esborrar el llibre amb id 2.");
        assertTrue(llibres.stream().noneMatch(llibre -> llibre.getId() == 2), "El llibre amb id 2 hauria d'haver estat esborrat.");
    }

    @Test
    void testGuardarLlibres() throws IOException {
        List<Llibre> llibres = app.carregarLlibres();
        app.modificarAnyPublicacio(llibres, 1, 1995);
        app.afegirNouLlibre(llibres, new Llibre(4, "Històries de la ciutat", "Miquel Soler", 2022));
        app.esborrarLlibre(llibres, 2);
        app.guardarLlibres(llibres);

        // Llegeix el fitxer de sortida i verifica el contingut
        File outputFile = new File(tempFile.getParent(), "llibres_output_jackson.json");
        ObjectMapper mapper = new ObjectMapper();
        List<Llibre> llibresGuardats = mapper.readValue(outputFile, new TypeReference<List<Llibre>>() {});

        assertEquals(3, llibresGuardats.size(), "Hauria d'haver-hi tres llibres al fitxer de sortida.");
        assertEquals(1995, llibresGuardats.get(0).getAny(), "L'any del llibre amb id 1 hauria de ser 1995 al fitxer de sortida.");
        assertEquals("Històries de la ciutat", llibresGuardats.get(2).getTitol(), "El títol del tercer llibre hauria de ser 'Històries de la ciutat' al fitxer de sortida.");
    }
}