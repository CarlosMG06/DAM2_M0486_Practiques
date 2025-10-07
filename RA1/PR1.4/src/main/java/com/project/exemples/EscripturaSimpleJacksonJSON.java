package com.project.exemples;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EscripturaSimpleJacksonJSON {
    public static void main(String[] args) {
        // Crear un objecte Map amb les dades fictícies que es volen escriure al fitxer JSON
        Map<String, Object> persona = new HashMap<>();
        persona.put("nom", "Pau López");
        persona.put("edat", 30);
        persona.put("ocupacio", "Dissenyador gràfic");
        persona.put("correu", "pau.lopez@example.com");

        // Crear un altre Map per a la direcció de la persona
        Map<String, String> direccio = new HashMap<>();
        direccio.put("carrer", "Carrer de la Llum");
        direccio.put("ciutat", "València");
        direccio.put("codi_postal", "46001");
        persona.put("direccio", direccio);

        // Crear un array de telèfons (com a List of Maps)
        Map<String, String> telefonCasa = new HashMap<>();
        telefonCasa.put("tipus", "casa");
        telefonCasa.put("numero", "963456789");

        Map<String, String> telefonMobil = new HashMap<>();
        telefonMobil.put("tipus", "mòbil");
        telefonMobil.put("numero", "612345678");

        persona.put("telefon", new Map[]{telefonCasa, telefonMobil});

        // Defineix la ruta del fitxer on es guardarà el JSON
        File file = new File("data/exemples/json_escriptura_simple_output_jackson.json");

        // Crear l'objecte ObjectMapper per gestionar l'escriptura del fitxer JSON
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Escriu el Map com a JSON al fitxer especificat
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, persona);
            System.out.println("JSON escrit correctament a " + file.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
