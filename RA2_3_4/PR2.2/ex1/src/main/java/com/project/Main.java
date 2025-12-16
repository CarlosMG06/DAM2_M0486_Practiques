package com.project;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class Main {
   public static void main(String[] args) {
        // PREPARACIÓ: Crea el directori "data" si no existeix i inicialitza Hibernate.
        String basePath = System.getProperty("user.dir") + "/data/";
        File dir = new File(basePath);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                System.out.println("Error creating 'data' folder");
            }
        }

        Manager.createSessionFactory();

        // ───────────────────────────────────────────────────────────────
        // CRUD - CREATE: Creació d'entitats a la BBDD
        // ───────────────────────────────────────────────────────────────

        Ciutat refCiutat1 = Manager.addCiutat("Vancouver", "Canada", 98661);
        Ciutat refCiutat2 = Manager.addCiutat("Växjö", "Suècia", 35220);
        Ciutat refCiutat3 = Manager.addCiutat("Kyoto", "Japó", 5200461);

        Ciutada refCiutada1 = Manager.addCiutada("Tony", "Happy", 20);
        Ciutada refCiutada2 = Manager.addCiutada("Monica", "Mouse", 22);
        Ciutada refCiutada3 = Manager.addCiutada("Eirika", "Erjo", 44);
        Ciutada refCiutada4 = Manager.addCiutada("Ven", "Enrison", 48);
        Ciutada refCiutada5 = Manager.addCiutada("Akira", "Akiko", 62);
        Ciutada refCiutada6 = Manager.addCiutada("Masako", "Kubo", 66);

        printState("1. CREACIÓ", "Després de la creació inicial");

        // ───────────────────────────────────────────────────────────────
        // CRUD - UPDATE: Assignació d'Items a Carts (establir relacions)
        // ───────────────────────────────────────────────────────────────

        Set<Ciutada> ciutadansCity1 = new HashSet<Ciutada>();
        ciutadansCity1.add(refCiutada1);
        ciutadansCity1.add(refCiutada2);
        ciutadansCity1.add(refCiutada3);
        Manager.updateCiutat(refCiutat1.getCiutatId(), refCiutat1.getNom(), refCiutat1.getPais(), refCiutat1.getPoblacio(), ciutadansCity1);

        Set<Ciutada> ciutadansCity2 = new HashSet<Ciutada>();
        ciutadansCity2.add(refCiutada4);
        ciutadansCity2.add(refCiutada5);
        Manager.updateCiutat(refCiutat2.getCiutatId(), refCiutat2.getNom(), refCiutat2.getPais(), refCiutat2.getPoblacio(), ciutadansCity2);

        printState("2. ASSIGNACIÓ D'ITEMS", "Després d'actualitzar ciutats");

        // ───────────────────────────────────────────────────────────────
        // CRUD - UPDATE: Modificació de noms d'entitats existents
        // ───────────────────────────────────────────────────────────────

        Manager.updateCiutat(refCiutat1.getCiutatId(), "Vancouver Updated", refCiutat1.getPais(), refCiutat1.getPoblacio(), ciutadansCity1);
        Manager.updateCiutat(refCiutat2.getCiutatId(), "Växjö Updated", refCiutat2.getPais(), refCiutat2.getPoblacio(), ciutadansCity2);

        Manager.updateCiutada(refCiutada1.getCiutadaId(), "Tony Updated", refCiutada1.getCognom(), refCiutada1.getEdat());
        Manager.updateCiutada(refCiutada4.getCiutadaId(), "Ven Updated", refCiutada4.getCognom(), refCiutada4.getEdat());

        printState("3. ACTUALITZACIÓ DE CAMPS", "Després d'actualitzar noms");

        // ───────────────────────────────────────────────────────────────
        // CRUD - DELETE: Eliminació d'entitats
        // ───────────────────────────────────────────────────────────────

        Manager.delete(Ciutat.class, refCiutat3.getCiutatId());
        Manager.delete(Ciutada.class, refCiutada6.getCiutadaId());

        printState("4. ESBORRAT", "Després d'esborrar");

        // ───────────────────────────────────────────────────────────────
        // CRUD - READ: Recuperació amb LAZY LOADING
        // ───────────────────────────────────────────────────────────────

        System.out.println("--- 5. RECUPERACIÓ EAGER ---");

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

   /**
     * Mètode auxiliar per mostrar l'estat actual de la BBDD.
     * @param title Títol de la secció (ex: "1. CREACIÓ")
     * @param subtitle Descripció (ex: "Després de la creació inicial")
     */
    private static void printState(String title, String subtitle) {
        System.out.println("--- " + title + " ---");
        System.out.println("[" + subtitle + "]");

        // ─── CIUTATS ───
        System.out.println("CIUTATS:");
        List<Ciutat> ciutats = Manager.findAllCiutatsWithCiutadans();
        ciutats.sort(Comparator.comparing(Ciutat::getCiutatId));

        for (Ciutat ciutat : ciutats) {
            System.out.println(ciutat.toString());
        }

        // ─── CIUTADANS ───
        System.out.println("CIUTADANS:");
        List<Ciutada> ciutadans = Manager.findAll(Ciutada.class);
        ciutadans.sort(Comparator.comparing(Ciutada::getCiutadaId));
        
        for (Ciutada ciutada : ciutadans) {
            ciutada.toString();
        }
        
        // Separador visual entre seccions
        System.out.println("------------------------------");
    }
}