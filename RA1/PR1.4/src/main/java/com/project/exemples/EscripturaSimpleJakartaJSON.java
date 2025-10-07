package com.project.exemples;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonWriter;

import java.io.FileWriter;
import java.io.IOException;

public class EscripturaSimpleJakartaJSON {
    public static void main(String[] args) {
        // Definir el contingut JSON a escriure
        JsonObject jsonObject = Json.createObjectBuilder()
                .add("nom", "Pau López")
                .add("edat", 30)
                .add("ocupacio", "Dissenyador gràfic")
                .add("correu", "pau.lopez@example.com")
                .add("direccio", Json.createObjectBuilder()
                        .add("carrer", "Carrer de la Llum")
                        .add("ciutat", "València")
                        .add("codi_postal", "46001")
                )
                .add("telefon", Json.createArrayBuilder()
                        .add(Json.createObjectBuilder()
                                .add("tipus", "casa")
                                .add("numero", "963456789"))
                        .add(Json.createObjectBuilder()
                                .add("tipus", "mòbil")
                                .add("numero", "612345678"))
                )
                .build();

        // Defineix la ruta del fitxer on es guardarà el JSON
        String filePath = "data/exemples/json_escriptura_simple_output_jakarta.json";

        // Escriu el JSON en el fitxer especificat
        try (JsonWriter jsonWriter = Json.createWriter(new FileWriter(filePath))) {
            jsonWriter.writeObject(jsonObject);
            System.out.println("JSON escrit correctament a " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
