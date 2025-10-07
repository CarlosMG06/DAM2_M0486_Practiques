package com.project.exemples;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.objectes.Persona;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Classe principal que gestiona la lectura i el processament de fitxers JSON per obtenir dades de persones.
 */
public class GestioPersonesJacksonMain {

    private final File dataFile;

    /**
     * Constructor de la classe PR14GestioPersonesJacksonMain.
     *
     * @param dataFile Fitxer on es troben les persones.
     */
    public GestioPersonesJacksonMain(File dataFile) {
        this.dataFile = dataFile;
    }

    public static void main(String[] args) {
        // Defineix la ruta del fitxer de dades d'entrada (persones_input.json)
        File dataFile = new File(System.getProperty("user.dir"), "data/exemples" + File.separator + "persones_input.json");
        GestioPersonesJacksonMain app = new GestioPersonesJacksonMain(dataFile);
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
     * Carrega les persones des del fitxer JSON.
     *
     * @return Llista de persones o null si hi ha hagut un error en la lectura.
     */
    public List<Persona> carregarPersones() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(dataFile, new TypeReference<List<Persona>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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
     * Guarda la llista de persones en un fitxer nou.
     *
     * @param persones Llista de persones a guardar.
     */
    public void guardarPersones(List<Persona> persones) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File outputFile = new File(dataFile.getParent(), "persones_output_jackson.json");
            mapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, persones);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
