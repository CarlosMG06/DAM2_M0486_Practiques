package com.project.exemples;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class LecturaSimpleJacksonJSON {
    public static void main(String[] args) {
        // Defineix la ruta del fitxer JSON
        File file = new File("data/exemples/json_lectura_simple_input.json");

        // Crea un objecte ObjectMapper per treballar amb Jackson
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Llegeix el fitxer JSON i el converteix en un JsonNode
            JsonNode jsonNode = objectMapper.readTree(file);

            // Mostra el contingut del JSON a la consola
            System.out.println(jsonNode.toPrettyString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
