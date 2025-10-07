package com.project.exemples;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import java.io.FileReader;
import java.io.IOException;

public class AccesPropietatsJakartaJSON {
    public static void main(String[] args) {
        // Defineix la ruta del fitxer JSON
        String filePath = "data/exemples/json_lectura_simple_input.json";

        // Llegeix el fitxer JSON i accedeix a les propietats
        try (JsonReader jsonReader = Json.createReader(new FileReader(filePath))) {
            // Llegeix el fitxer JSON com un JsonObject
            JsonObject jsonObject = jsonReader.readObject();

            // Accedeix a les propietats simples
            String nom = jsonObject.getString("nom");
            int edat = jsonObject.getInt("edat");
            String ocupacio = jsonObject.getString("ocupacio");
            String correu = jsonObject.getString("correu");

            // Accedeix a l'objecte aniuat 'direccio'
            JsonObject direccio = jsonObject.getJsonObject("direccio");
            String carrer = direccio.getString("carrer");
            String ciutat = direccio.getString("ciutat");
            String codiPostal = direccio.getString("codi_postal");

            // Accedeix a l'array 'telefon'
            JsonArray telefonArray = jsonObject.getJsonArray("telefon");

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
            for (JsonObject telefon : telefonArray.getValuesAs(JsonObject.class)) {
                String tipus = telefon.getString("tipus");
                String numero = telefon.getString("numero");
                System.out.println("  " + tipus + ": " + numero);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
