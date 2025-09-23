import java.util.ArrayList;
import java.util.Scanner;

class Tasca {
    private String nom;
    private boolean completada;

    public Tasca(String nom) {
        this.nom = nom;
        this.completada = false;
    }

    public String getNom() {
        return nom;
    }

    public boolean isCompletada() {
        return completada;
    }

    public void marcarCompletada() {
        this.completada = true;
    }

    @Override
    public String toString() {
        return (completada ? "[X] " : "[ ] ") + nom;
    }
}

public class GestioTasques {
    private static ArrayList<Tasca> tasques = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean sortir = false;

        while (!sortir) {
            mostrarMenu();
            int opcio = Integer.parseInt(scanner.nextLine());

            switch (opcio) {
                case 1:
                    afegirTasca();
                    break;
                case 2:
                    marcarTascaCompletada();
                    break;
                case 3:
                    mostrarTasques();
                    break;
                case 4:
                    sortir = true;
                    System.out.println("Sortint del programa...");
                    break;
                default:
                    System.out.println("Opció no vàlida, intenta-ho de nou.");
            }
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n--- Menú de Gestió de Tasques ---");
        System.out.println("1. Afegir nova tasca");
        System.out.println("2. Marcar tasca com a completada");
        System.out.println("3. Mostrar llista de tasques");
        System.out.println("4. Sortir");
        System.out.print("Selecciona una opció: ");
    }

    private static void afegirTasca() {
        System.out.print("Introdueix el nom de la tasca: ");
        String nom = scanner.nextLine();
        Tasca novaTasca = new Tasca(nom);
        tasques.add(novaTasca);
        System.out.println("Tasca afegida correctament.");
    }

    private static void marcarTascaCompletada() {
        mostrarTasques();
        System.out.print("Introdueix el número de la tasca a marcar com a completada: ");
        int index = Integer.parseInt(scanner.nextLine()) - 1;

        if (index >= 0 && index < tasques.size()) {
            tasques.get(index).marcarCompletada();
            System.out.println("Tasca marcada com a completada.");
        } else {
            System.out.println("Número de tasca no vàlid.");
        }
    }

    private static void mostrarTasques() {
        System.out.println("\n--- Llista de Tasques ---");
        if (tasques.isEmpty()) {
            System.out.println("No hi ha tasques.");
        } else {
            for (int i = 0; i < tasques.size(); i++) {
                System.out.println((i + 1) + ". " + tasques.get(i));
            }
        }
    }
}
