package com.project.exemples;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AccesPropietatsOrgJSON {
    public static void main(String[] args) {
        // Defineix la ruta del fitxer JSON
        String filePath = "data/exemples/json_lectura_simple_input.json";

        try {
            // Llegeix el contingut del fitxer com a String
            String content = new String(Files.readAllBytes(Paths.get(filePath)));

            // Converteix el contingut a un JSONObject
            JSONObject jsonObject = new JSONObject(content);

            // Accedeix a les propietats simples
            String nom = jsonObject.getString("nom");
            int edat = jsonObject.getInt("edat");
            String ocupacio = jsonObject.getString("ocupacio");
            String correu = jsonObject.getString("correu");

            // Accedeix a l'objecte aniuat 'direccio'
            JSONObject direccio = jsonObject.getJSONObject("direccio");
            String carrer = direccio.getString("carrer");
            String ciutat = direccio.getString("ciutat");
            String codiPostal = direccio.getString("codi_postal");

            // Accedeix a l'array 'telefon'
            JSONArray telefonArray = jsonObject.getJSONArray("telefon");

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
            for (int i = 0; i < telefonArray.length(); i++) {
                JSONObject telefon = telefonArray.getJSONObject(i);
                String tipus = telefon.getString("tipus");
                String numero = telefon.getString("numero");
                System.out.println("  " + tipus + ": " + numero);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
