package com.project;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;

import com.project.excepcions.IOFitxerExcepcio;
import com.project.objectes.PR121hashmap;

import static org.junit.jupiter.api.Assertions.*;

class PR121mainTest {

    @TempDir
    File directoriTemporal;

    @Test
    void testSerialitzarIDeserialitzarHashMap() throws IOException, IOFitxerExcepcio {
        // Preparació: Fitxer temporal
        File fitxerTemporal = new File(directoriTemporal, "PR121HashMapData.ser");

        // Crear l'objecte PR121hashmap i afegir dades
        PR121hashmap hashMap = new PR121hashmap();
        hashMap.getPersones().put("Anna", 25);
        hashMap.getPersones().put("Bernat", 30);

        // Canviar la ruta per utilitzar el fitxer temporal amb el setter
        String filePathAnteriorEscriu = PR121mainEscriu.getFilePath();
        PR121mainEscriu.setFilePath(fitxerTemporal.getAbsolutePath());
        
        String filePathAnteriorLlegeix = PR121mainLlegeix.getFilePath();
        PR121mainLlegeix.setFilePath(fitxerTemporal.getAbsolutePath());

        // Serialitzar i deserialitzar
        PR121mainEscriu.serialitzarHashMap(hashMap);
        PR121hashmap deserialitzat = PR121mainLlegeix.deserialitzarHashMap();

        // Comprovar les dades deserialitzades
        assertEquals(hashMap.getPersones(), deserialitzat.getPersones());

        // Restaurar la ruta original amb el setter
        PR121mainEscriu.setFilePath(filePathAnteriorEscriu);
        PR121mainLlegeix.setFilePath(filePathAnteriorLlegeix);
    }

    @Test
    void testDeserialitzarFitxerInexistent() {
        // Preparació: Fitxer que no existeix
        File fitxerInexistent = new File(directoriTemporal, "PR121HashMapData.ser");

        // Assegura que el fitxer no existeix
        assertFalse(fitxerInexistent.exists());

        // Canviar la ruta per utilitzar el fitxer temporal amb el setter
        String filePathAnterior = PR121mainLlegeix.getFilePath();
        PR121mainLlegeix.setFilePath(fitxerInexistent.getAbsolutePath());

        // Verificar que es llença l'excepció
        IOFitxerExcepcio excepcio = assertThrows(IOFitxerExcepcio.class, () -> {
            PR121mainLlegeix.deserialitzarHashMap();
        });

        // Comprovar que el missatge d'error és adequat
        assertTrue(excepcio.getMessage().contains("Error en deserialitzar l'objecte HashMap"));

        // Restaurar la ruta original amb el setter
        PR121mainLlegeix.setFilePath(filePathAnterior);
    }
}
