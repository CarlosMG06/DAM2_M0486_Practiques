package com.project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class PR113FilesTest {

    @TempDir
    Path directoriTemporal;

    @Test
    void testSobreescriureFrases() throws IOException {
        // Definir el camí del fitxer dins del directori temporal
        File fitxer = new File(directoriTemporal.toFile(), "frasesMatrix.txt");

        // Executar el mètode que sobreescriu l'arxiu
        PR113sobreescriu.escriureFrases(fitxer.getPath());

        // Comprovar que el fitxer existeix
        assertTrue(fitxer.exists(), "El fitxer hauria d'existir");

        // Llegir tot el contingut del fitxer com a text complet amb UTF-8
        String contingut = Files.readString(fitxer.toPath(), StandardCharsets.UTF_8);

        // Dividir el contingut per línies, mantenint les línies buides
        String[] linies = contingut.split("\\R", -1);  // "\\R" gestiona qualsevol tipus de salt de línia

        // Comprovar el nombre de línies esperades
        assertEquals(3, linies.length, "El fitxer hauria de tenir tres línies: dues frases i una línia en blanc al final.");
        assertEquals("I can only show you the door", linies[0], "La primera frase hauria de coincidir.");
        assertEquals("You're the one that has to walk through it", linies[1], "La segona frase hauria de coincidir.");
        assertEquals("", linies[2], "L'última línia hauria de ser en blanc.");
    }

    @Test
    void testAfegirFrases() throws IOException {
        // Definir el camí del fitxer dins del directori temporal
        File fitxer = new File(directoriTemporal.toFile(), "frasesMatrix.txt");

        // Executar el mètode que afegeix frases al final dues vegades
        PR113append.afegirFrases(fitxer.getPath());
        PR113append.afegirFrases(fitxer.getPath()); // Executar una segona vegada per afegir més frases

        // Comprovar que el fitxer existeix
        assertTrue(fitxer.exists(), "El fitxer hauria d'existir");

        // Llegir tot el contingut del fitxer com a text complet amb UTF-8
        String contingut = Files.readString(fitxer.toPath(), StandardCharsets.UTF_8);

        // Dividir el contingut per línies, mantenint les línies buides
        String[] linies = contingut.split("\\R", -1);  // "\\R" gestiona qualsevol tipus de salt de línia

        // Comprovar el nombre de línies esperades després de dues escriptures
        assertEquals(5, linies.length, "El fitxer hauria de tenir cinc línies després de dos afegits: quatre frases i una línia en blanc.");
        assertEquals("I can only show you the door", linies[0], "La primera frase hauria de coincidir.");
        assertEquals("You're the one that has to walk through it", linies[1], "La segona frase hauria de coincidir.");
        assertEquals("I can only show you the door", linies[2], "La tercera frase hauria de coincidir després d'afegir.");
        assertEquals("You're the one that has to walk through it", linies[3], "La quarta frase hauria de coincidir després d'afegir.");
        assertEquals("", linies[4], "L'última línia hauria de ser en blanc.");
    }
}
