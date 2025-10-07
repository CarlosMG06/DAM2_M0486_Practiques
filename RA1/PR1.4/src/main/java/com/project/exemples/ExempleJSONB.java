package com.project.exemples;

import com.project.exemples.model.User;

// Importem les classes necessàries de l'API Jakarta JSON Binding.
// Aquest és el paquet modern en lloc de l'antic 'javax.json.bind'.
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

public class ExempleJSONB {
    public static void main(String[] args) {
        // Creem una instància de Jsonb amb el seu constructor. Aquest objecte s'encarregarà de les conversions.
        Jsonb jsonb = JsonbBuilder.create();

        // --- Serialització: Objecte Java a String JSON ---
        System.out.println("--- Serialització ---");

        // 1. Creem una instància del nostre objecte User.
        User user = new User("Anna", 28);

        // 2. Utilitzem el mètode toJson() per convertir l'objecte User a un string JSON.
        String json = jsonb.toJson(user);

        // 3. Imprimim el resultat.
        // Sortida esperada: {"edat":28,"nom":"Anna"}
        System.out.println(json);
        System.out.println();


        // --- Deserialització: String JSON a Objecte Java ---
        System.out.println("--- Deserialització ---");

        // 1. Utilitzem el mètode fromJson() per convertir el string JSON de nou a un objecte User.
        // Cal especificar la classe de destinació (User.class).
        User userDeserialized = jsonb.fromJson(json, User.class);

        // 2. Accedim a les dades de l'objecte nou per verificar que ha funcionat.
        // Sortida esperada: Anna
        System.out.println(userDeserialized.getNom());
    }
}