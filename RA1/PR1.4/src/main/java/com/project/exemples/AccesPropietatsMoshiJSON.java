package com.project.exemples;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class AccesPropietatsMoshiJSON {
    public static void main(String[] args) {
        // Defineix la ruta del fitxer JSON
        String filePath = "data/exemples/json_lectura_simple_input.json";

        // Crear un objecte Moshi
        Moshi moshi = new Moshi.Builder().build();

        // Definir el tipus per a la deserialització (Map amb clau String i valor Object)
        Type type = Types.newParameterizedType(Map.class, String.class, Object.class);
        JsonAdapter<Map<String, Object>> adapter = moshi.adapter(type);

        try {
            // Llegeix el contingut del fitxer com a String
            String jsonContent = new String(Files.readAllBytes(Paths.get(filePath)));

            // Converteix el contingut JSON a un Map
            Map<String, Object> jsonObject = adapter.fromJson(jsonContent);

            if (jsonObject != null) {
                // Accedeix a les propietats simples
                String nom = (String) jsonObject.get("nom");
                int edat = ((Double) jsonObject.get("edat")).intValue();  // Moshi converteix els números a Double per defecte
                String ocupacio = (String) jsonObject.get("ocupacio");
                String correu = (String) jsonObject.get("correu");

                // Accedeix a l'objecte aniuat 'direccio'
                Map<String, String> direccio = (Map<String, String>) jsonObject.get("direccio");
                String carrer = direccio.get("carrer");
                String ciutat = direccio.get("ciutat");
                String codiPostal = direccio.get("codi_postal");

                // Accedeix a l'array 'telefon'
                List<Map<String, String>> telefonArray = (List<Map<String, String>>) jsonObject.get("telefon");

                // Mostra les propietats simples
                System.out.println("Nom: " + nom);
                System.out.println("Edat: " + edat);
                System.out.println("Ocupació: " + ocupacio);
                System.out.println("Correu: " + correu);
                System.out.println("Direcció: ");
                System.out.println("  Carrer: " + carrer);
                System.out.println("  Ciutat: " + ciutat);
                System.out.println("  Codi Postal: " + codiPostal);

                // Recorre l'array 'telefon' i mostra cada número de telèfon
                System.out.println("Telèfons:");
                for (Map<String, String> telefon : telefonArray) {
                    String tipus = telefon.get("tipus");
                    String numero = telefon.get("numero");
                    System.out.println("  " + tipus + ": " + numero);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
