package com.project;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class Main {
   public static void main(String[] args) {
        // Creem el directori data si no existeix
        String basePath = System.getProperty("user.dir") + "/data/";
        File dir = new File(basePath);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                System.out.println("Error creating 'data' folder");
            }
        }

        // Inicialitzem la connexió amb Hibernate
        Manager.createSessionFactory();

        // CREATE - Creem les ciutats
        Ciutat refCiutat1 = Manager.addCiutat("Vancouver", "Canada", 98661);
        Ciutat refCiutat2 = Manager.addCiutat("Växjö", "Suècia", 35220);
        Ciutat refCiutat3 = Manager.addCiutat("Kyoto", "Japó", 5200461);

        // CREATE - Creem els ciutadans
        Ciutada refCiutada1 = Manager.addCiutada("Tony", "Happy", 20);
        Ciutada refCiutada2 = Manager.addCiutada("Monica", "Mouse", 22);
        Ciutada refCiutada3 = Manager.addCiutada("Eirika", "Erjo", 44);
        Ciutada refCiutada4 = Manager.addCiutada("Ven", "Enrison", 48);
        Ciutada refCiutada5 = Manager.addCiutada("Akira", "Akiko", 62);
        Ciutada refCiutada6 = Manager.addCiutada("Masako", "Kubo", 66);

        // READ - Mostrem tots els elements creats
        printState("Punt 1. Després de la creació inicial d'elements");

        // Creem un set de ciutadans per la primera ciutat
        Set<Ciutada> ciutadansCity1 = new HashSet<Ciutada>();
        ciutadansCity1.add(refCiutada1);
        ciutadansCity1.add(refCiutada2);
        ciutadansCity1.add(refCiutada3);

        // UPDATE - Actualitzem la primera ciutat amb els seus ciutadans
        Manager.updateCiutat(refCiutat1.getCiutatId(), refCiutat1.getNom(), refCiutat1.getPais(), refCiutat1.getPoblacio(), ciutadansCity1);

        // Creem un set de ciutadans per la segona ciutat
        Set<Ciutada> ciutadansCity2 = new HashSet<Ciutada>();
        ciutadansCity2.add(refCiutada4);
        ciutadansCity2.add(refCiutada5);

        // UPDATE - Actualitzem la segona ciutat amb els seus ciutadans
        Manager.updateCiutat(refCiutat2.getCiutatId(), refCiutat2.getNom(), refCiutat2.getPais(), refCiutat2.getPoblacio(), ciutadansCity2);

                // READ - Mostrem l'estat després d'assignar ciutadans a les ciutats
        printState("Punt 2. Després d'actualitzar ciutats");

        // UPDATE - Actualitzem els noms de les ciutats
        Manager.updateCiutat(refCiutat1.getCiutatId(), "Vancouver Updated", refCiutat1.getPais(), refCiutat1.getPoblacio(), ciutadansCity1);
        Manager.updateCiutat(refCiutat2.getCiutatId(), "Växjö Updated", refCiutat2.getPais(), refCiutat2.getPoblacio(), ciutadansCity2);

        // UPDATE - Actualitzem els noms dels ciutadans
        Manager.updateCiutada(refCiutada1.getCiutadaId(), "Tony Updated", refCiutada1.getCognom(), refCiutada1.getEdat());
        Manager.updateCiutada(refCiutada4.getCiutadaId(), "Ven Updated", refCiutada4.getCognom(), refCiutada4.getEdat());

        // READ - Mostrem l'estat després d'actualitzar els noms
        printState("Punt 3: Després d'actualització de noms");

        // DELETE - Esborrem la tercera ciutat i el sisè ciutadà
        Manager.delete(Ciutat.class, refCiutat3.getCiutatId());
        Manager.delete(Ciutada.class, refCiutada6.getCiutadaId());

        // READ - Mostrem l'estat després d'esborrar elements
        printState("Punt 4: després d'esborrat");

        // READ - Recuperació amb lazy loading
        System.out.println("Punt 5: Recuperació de ciutadans d'una ciutat específica");

        Ciutat ciutat = Manager.getCiutatWithCiutadans(refCiutat1.getCiutatId());
        if (ciutat != null) {
            System.out.println("Ciutadans de la ciutat '" + ciutat.getNom() + "':");
            Set<Ciutada> ciutadans = ciutat.getCiutadans();
            if (ciutadans != null && !ciutadans.isEmpty()) {
                // STREAM API: Ordenem i iterem la col·lecció de forma funcional
                ciutadans.stream()
                    .sorted(Comparator.comparing(Ciutada::getCiutadaId))
                    .forEach(ciutada -> System.out.println("- " + ciutada.getNom() + " " + ciutada.getCognom()));
            } else {
                System.out.println("La ciutat no té ciutadans");
            }
        } else {
            System.out.println("No s'ha trobat la ciutat");
        }

        // Tanquem la connexió amb Hibernate
        Manager.close();
   }

   
    // Mètode auxiliar per mostrar l'estat actual de la BBDD.
    private static void printState(String title) {
        System.out.println(title);

        // CIUTATS
        List<Ciutat> ciutats = Manager.findAllCiutatsWithCiutadans();
        ciutats.sort(Comparator.comparing(Ciutat::getCiutatId));

        for (Ciutat ciutat : ciutats) {
            System.out.println(ciutat.toString());
        }

        // CIUTADANS
        List<Ciutada> ciutadans = Manager.findAll(Ciutada.class);
        ciutadans.sort(Comparator.comparing(Ciutada::getCiutadaId));
        
        for (Ciutada ciutada : ciutadans) {
            System.out.println(ciutada.toString());
        }
    }
}