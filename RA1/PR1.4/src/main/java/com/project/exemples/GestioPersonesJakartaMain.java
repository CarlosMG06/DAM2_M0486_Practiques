package com.project.exemples;

import jakarta.json.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.project.objectes.Persona;

/**
 * Classe principal que gestiona la lectura i el processament de fitxers JSON per obtenir dades de persones.
 */
public class GestioPersonesJakartaMain {

    private final File dataFile;

    /**
     * Constructor de la classe PR14GestioPersonesJakartaMain.
     *
     * @param dataFile Fitxer on es troben les persones.
     */
    public GestioPersonesJakartaMain(File dataFile) {
        this.dataFile = dataFile;
    }

    public static void main(String[] args) {
        // Defineix la ruta del fitxer de dades d'entrada (persones_input.json)
        File dataFile = new File(System.getProperty("user.dir"), "data/exemples" + File.separator + "persones_input.json");
        GestioPersonesJakartaMain app = new GestioPersonesJakartaMain(dataFile);
        app.processarFitxer();
    }

    /**
     * Processa el fitxer JSON per carregar, modificar, afegir, esborrar i guardar les dades de les persones.
     */
    public void processarFitxer() {
        List<Persona> persones = carregarPersones();
        if (persones != null) {
            modificarEdat(persones, 1, 35);
            afegirNovaPersona(persones, new Persona(4, "Miquel Soler", 30));
            esborrarPersona(persones, 2);
            guardarPersones(persones);
        }
    }

    /**
     * Carrega les persones des del fitxer JSON utilitzant Jakarta JSON.
     *
     * @return Llista de persones o null si hi ha hagut un error en la lectura.
     */
    public List<Persona> carregarPersones() {
        List<Persona> persones = new ArrayList<>();
        try (JsonReader jsonReader = Json.createReader(new FileReader(dataFile))) {
            JsonArray jsonArray = jsonReader.readArray();
            for (JsonObject jsonObject : jsonArray.getValuesAs(JsonObject.class)) {
                int id = jsonObject.getInt("id");
                String nom = jsonObject.getString("nom");
                int edat = jsonObject.getInt("edat");
                persones.add(new Persona(id, nom, edat));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return persones;
    }

    /**
     * Modifica l'edat d'una persona amb un id específic.
     *
     * @param persones Llista de persones.
     * @param id Identificador de la persona a modificar.
     * @param novaEdat Nova edat.
     */
    public void modificarEdat(List<Persona> persones, int id, int novaEdat) {
        for (Persona persona : persones) {
            if (persona.getId() == id) {
                persona.setEdat(novaEdat);
            }
        }
    }

    /**
     * Afegeix una nova persona a la llista de persones.
     *
     * @param persones Llista de persones.
     * @param novaPersona Nova persona a afegir.
     */
    public void afegirNovaPersona(List<Persona> persones, Persona novaPersona) {
        persones.add(novaPersona);
    }

    /**
     * Esborra una persona amb un id específic de la llista de persones.
     *
     * @param persones Llista de persones.
     * @param id Identificador de la persona a esborrar.
     */
    public void esborrarPersona(List<Persona> persones, int id) {
        Iterator<Persona> iterator = persones.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getId() == id) {
                iterator.remove();
            }
        }
    }

    /**
     * Guarda la llista de persones en un fitxer nou utilitzant Jakarta JSON.
     *
     * @param persones Llista de persones a guardar.
     */
    public void guardarPersones(List<Persona> persones) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Persona persona : persones) {
            JsonObject personaJson = Json.createObjectBuilder()
                .add("id", persona.getId())
                .add("nom", persona.getNom())
                .add("edat", persona.getEdat())
                .build();
            arrayBuilder.add(personaJson);
        }

        JsonArray jsonArray = arrayBuilder.build();
        try (JsonWriter jsonWriter = Json.createWriter(Files.newBufferedWriter(Paths.get(dataFile.getParent(), "persones_output_jakarta.json")))) {
            jsonWriter.writeArray(jsonArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
