package com.project.exemples;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.IOException;

public class AccesPropietatsGsonJSON {
    public static void main(String[] args) {
        // Defineix la ruta del fitxer JSON
        String filePath = "data/exemples/json_lectura_simple_input.json";

        try (FileReader reader = new FileReader(filePath)) {
            // Llegeix el fitxer i converteix-lo en un JsonObject
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

            // Accedeix a les propietats simples
            String nom = jsonObject.get("nom").getAsString();
            int edat = jsonObject.get("edat").getAsInt();
            String ocupacio = jsonObject.get("ocupacio").getAsString();
            String correu = jsonObject.get("correu").getAsString();

            // Accedeix a l'objecte aniuat 'direccio'
            JsonObject direccioObject = jsonObject.get("direccio").getAsJsonObject();
            String carrer = direccioObject.get("carrer").getAsString();
            String ciutat = direccioObject.get("ciutat").getAsString();
            String codiPostal = direccioObject.get("codi_postal").getAsString();

            // Accedeix a l'array 'telefon'
            JsonArray telefonArray = jsonObject.get("telefon").getAsJsonArray();

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
            for (int i = 0; i < telefonArray.size(); i++) {
                JsonObject telefon = telefonArray.get(i).getAsJsonObject();
                String tipus = telefon.get("tipus").getAsString();
                String numero = telefon.get("numero").getAsString();
                System.out.println("  " + tipus + ": " + numero);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
