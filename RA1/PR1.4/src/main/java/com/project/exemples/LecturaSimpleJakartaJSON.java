package com.project.exemples;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import java.io.FileReader;
import java.io.IOException;

public class LecturaSimpleJakartaJSON {
    public static void main(String[] args) {
        try (JsonReader jsonReader = Json.createReader(new FileReader("data/exemples/json_lectura_simple_input.json"))) {
            JsonObject jsonObject = jsonReader.readObject();
            System.out.println(jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
