package com.project.exemples;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class AccesPropietatsJacksonJSON {
    public static void main(String[] args) {
        // Defineix la ruta del fitxer JSON
        String filePath = "data/exemples/json_lectura_simple_input.json";

        // Crear ObjectMapper per llegir el fitxer JSON
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Llegeix el fitxer JSON i el converteix en un JsonNode
            JsonNode rootNode = objectMapper.readTree(new File(filePath));

            // Accedeix a les propietats simples
            String nom = rootNode.get("nom").asText();
            int edat = rootNode.get("edat").asInt();
            String ocupacio = rootNode.get("ocupacio").asText();
            String correu = rootNode.get("correu").asText();

            // Accedeix a l'objecte aniuat 'direccio'
            JsonNode direccioNode = rootNode.get("direccio");
            String carrer = direccioNode.get("carrer").asText();
            String ciutat = direccioNode.get("ciutat").asText();
            String codiPostal = direccioNode.get("codi_postal").asText();

            // Accedeix a l'array 'telefon'
            JsonNode telefonArray = rootNode.get("telefon");

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
            Iterator<JsonNode> elements = telefonArray.elements();
            while (elements.hasNext()) {
                JsonNode telefon = elements.next();
                String tipus = telefon.get("tipus").asText();
                String numero = telefon.get("numero").asText();
                System.out.println("  " + tipus + ": " + numero);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
