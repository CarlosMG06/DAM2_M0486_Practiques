package cat.iesesteveterradas;

import java.util.Scanner;
import java.util.ArrayList;

public class PR210Honor {
    public static void main(String[] args) {
        DBInitializer.init();
        Scanner sc = new Scanner(System.in);
        boolean sortir = false;
        while (!sortir) {
            System.out.println("\n--- MENÚ FOR HONOR ---");
            System.out.println("1. Mostrar una taula");
            System.out.println("2. Mostrar personatges per facció");
            System.out.println("3. Mostrar millor atacant per facció");
            System.out.println("4. Mostrar millor defensor per facció");
            System.out.println("5. Sortir");
            System.out.print("Opció: ");
            int opcio = 0;
            try {
                opcio = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Introdueix un número vàlid.");
                continue;
            }
            switch (opcio) {
                case 1:
                    showTable(sc);
                    break;
                case 2:
                    showCharactersByFaction(sc);
                    break;
                case 3:
                    showBestByFaction(sc, "atac");
                    break;
                case 4:
                    showBestByFaction(sc, "defensa");
                    break;
                case 5:
                    sortir = true;
                    break;
                default:
                    System.out.println("Opció no vàlida.");
            }
        }
        sc.close();
    }

    private static void showTable(Scanner sc) {
        System.out.println("Quina taula vols mostrar?");
        System.out.println("1. Faccions");
        System.out.println("2. Personatges");
        System.out.print("Opció: ")
        int opcio = 0;
        while (true) {
            try {
                opcio = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Introdueix un número vàlid.");
                continue;
            }
            if (opcio != 1 && opcio != 2) {
                System.out.println("Opció no vàlida.");
                continue;
            }
            break;
        }
        switch (opcio) {
            case 1:
                DBManager.showFactions();
            case 2:
                DBManager.showCharacters();
        }
    }

    private static showCharactersByFaction(Scanner sc) {
        ArrayList<String> factionsList = getFactionsList();
        System.out.println("De quina facció en vols mostrar?");
        for (i = 0; i < factionsList.size(); i++) {
            System.out.println(i + ". " + factionsList.get(i));
        }
        int opcio = 0;
        while (true) {
            try {
                opcio = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Introdueix un número vàlid.");
                continue;
            }
            if (opcio < 0 || opcio >= factionsList.size()) {
                System.out.println("Opció no vàlida.");
                continue;
            }
            break;
        }
        DBManager.showCharactersByFaction(opcio);
    }

    private static showBestByFaction(Scanner sc, String statName) {
        ArrayList<String> factionsList = getFactionsList();
        System.out.println("De quina facció ho vols mostrar?");
        for (i = 0; i < factionsList.size(); i++) {
            System.out.println(i + ". " + factionsList.get(i));
        }
        int opcio = 0;
        while (true) {
            try {
                opcio = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Introdueix un número vàlid.");
                continue;
            }
            if (opcio < 0 || opcio >= factionsList.size()) {
                System.out.println("Opció no vàlida.");
                continue;
            }
            break;
        }
        DBManager.showBestByFaction(opcio, statName);
    }
}
