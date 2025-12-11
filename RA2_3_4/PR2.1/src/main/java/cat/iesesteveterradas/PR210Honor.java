package cat.iesesteveterradas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;
import java.util.ArrayList;

public class PR210Honor {

    private static final Logger logger = LoggerFactory.getLogger(PR210Honor.class);

    public static void main(String[] args) {
        DBManager.init();
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
                logger.error("Introdueix un número vàlid.");
                continue;
            }
            System.out.println();
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
                    logger.error("Opció no vàlida.");
            }
        }
        DBManager.disconnect();
        sc.close();
    }

    private static void showTable(Scanner sc) {
        System.out.println("Quina taula vols mostrar?");
        System.out.println("1. Faccions");
        System.out.println("2. Personatges");
        System.out.print("Opció: ");
        int opcio = 0;
        while (true) {
            try {
                opcio = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                logger.error("Introdueix un número vàlid.");
                continue;
            }
            if (opcio != 1 && opcio != 2) {
                logger.error("Opció no vàlida.");
                continue;
            }
            break;
        }
        System.out.println();
        switch (opcio) {
            case 1:
                DBManager.showFactions();
                break;
            case 2:
                DBManager.showCharacters();
                break;
        }
    }

    private static void showCharactersByFaction(Scanner sc) {
        ArrayList<String> factionsList = DBManager.getFactionsList();
        System.out.println("De quina facció els vols mostrar?");
        for (int i = 1; i <= factionsList.size(); i++) {
            System.out.println(i + ". " + factionsList.get(i-1));
        }
        int opcio = 0;
        while (true) {
            try {
                opcio = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                logger.error("Introdueix un número vàlid.");
                continue;
            }
            if (opcio < 1 || opcio > factionsList.size()) {
                logger.error("Opció no vàlida.");
                continue;
            }
            break;
        }
        System.out.println();
        DBManager.showCharactersByFaction(opcio);
    }

    private static void showBestByFaction(Scanner sc, String statName) {
        ArrayList<String> factionsList = DBManager.getFactionsList();
        System.out.println("De quina facció el vols mostrar?");
        for (int i = 1; i <= factionsList.size(); i++) {
            System.out.println(i + ". " + factionsList.get(i-1));
        }
        int opcio = 0;
        while (true) {
            try {
                opcio = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                logger.error("Introdueix un número vàlid.");
                continue;
            }
            if (opcio < 1 || opcio > factionsList.size()) {
                logger.error("Opció no vàlida.");
                continue;
            }
            break;
        }
        System.out.println();
        DBManager.showBestByFaction(opcio, statName);
    }
}
