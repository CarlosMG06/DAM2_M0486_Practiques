package com.project.exemples;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ExempleClient {

    public static void main(String[] args) {
        // URL del servidor
        String url = "http://localhost:8080/";

        // Crear l'objecte JSON utilitzant Jakarta JSON
        JsonObject personaJson = Json.createObjectBuilder()
                .add("nom", "Joan Garcia")
                .add("anyNaixement", 1985)
                .build();

        // Convertir l'objecte JSON a String
        String jsonString = personaJson.toString();

        System.out.println("JSON a enviar: " + jsonString);

        // Crear client HTTP amb HttpClient5
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            // Crear la petició POST
            HttpPost postRequest = new HttpPost(url);
            postRequest.setEntity(new StringEntity(jsonString));  // Utilitzar el String del JSON generat
            postRequest.setHeader("Content-Type", "application/json");

            // Executar la petició i obtenir la resposta
            try (CloseableHttpResponse response = client.execute(postRequest)) {
                // Llegir la resposta del servidor
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {
                    StringBuilder resposta = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        resposta.append(line);
                    }
                    System.out.println("Resposta del servidor: " + resposta.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
