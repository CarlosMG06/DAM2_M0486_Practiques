package com.project.exemples;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import com.project.utilitats.UTF8Utils;

public class RandomAccessFilesVideojocsManager {

    // Constants que defineixen l'estructura d'un registre
    private static final int ID_SIZE = 4; // Número de registre: 4 bytes
    private static final int NAME_MAX_BYTES = 40; // Nom: màxim 20 caràcters (40 bytes en UTF-8)

    // Atribut per al path del fitxer
    private String filePath;

    // Constructor per inicialitzar el path del fitxer
    public RandomAccessFilesVideojocsManager() {
        this.filePath = System.getProperty("user.dir") + "/data/videojocs.dat"; // Valor per defecte
    }

    // Getter per al filePath
    public String getFilePath() {
        return filePath;
    }

    // Setter per al filePath
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public static void main(String[] args) {
        RandomAccessFilesVideojocsManager manager = new RandomAccessFilesVideojocsManager();
        try {
            // Afegir videojocs
            manager.afegirVideojoc(1, "Assassin's Creed");
            manager.afegirVideojoc(2, "The Legend of Zelda");

            // Llistar videojocs
            manager.llistarVideojocs();

            // Consultar videojoc
            manager.consultarVideojoc(1);

            // Actualitzar videojoc
            manager.actualitzarNomVideojoc(1, "Assassin's Creed Valhalla");

            // Llistar videojocs després de l'actualització
            manager.llistarVideojocs();

        } catch (IOException e) {
            System.out.println("Error en la manipulació del fitxer: " + e.getMessage());
        }
    }

    // Mètode per afegir un videojoc
    public void afegirVideojoc(int registre, String nom) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(filePath, "rw")) {
            raf.seek(raf.length()); // Escriure al final del fitxer
            raf.writeInt(registre);
            escriureNom(raf, nom);
            System.out.println("Videojoc afegit correctament: " + nom);
        }
    }

    // Mètode per llistar tots els videojocs
    public void llistarVideojocs() throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("No hi ha videojocs registrats.");
            return;
        }

        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            raf.seek(0);
            while (raf.getFilePointer() < raf.length()) {
                int registre = raf.readInt();
                String nom = llegirNom(raf);
                System.out.println("Registre: " + registre + ", Nom: " + nom);
            }
        }
    }

    // Mètode per consultar un videojoc per registre
    public void consultarVideojoc(int registre) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(filePath, "r")) {
            long posicio = trobarPosicioRegistre(raf, registre);
            if (posicio == -1) {
                System.out.println("No s'ha trobat el videojoc amb registre: " + registre);
            } else {
                raf.seek(posicio);
                raf.readInt(); // Saltar el número de registre
                String nom = llegirNom(raf);
                System.out.println("Registre: " + registre + ", Nom: " + nom);
            }
        }
    }

    // Mètode per actualitzar el nom d'un videojoc
    public void actualitzarNomVideojoc(int registre, String nouNom) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(filePath, "rw")) {
            long posicio = trobarPosicioRegistre(raf, registre);
            if (posicio == -1) {
                System.out.println("No s'ha trobat el videojoc amb registre: " + registre);
            } else {
                raf.seek(posicio + ID_SIZE); // Saltar l'ID per escriure el nou nom
                escriureNom(raf, nouNom);
                System.out.println("Nom actualitzat correctament per al registre: " + registre);
            }
        }
    }

    // Funcions auxiliars per a la lectura i escriptura del nom amb UTF-8
    private String llegirNom(RandomAccessFile raf) throws IOException {
        byte[] nomBytes = new byte[NAME_MAX_BYTES]; // Llegim fins a 40 bytes
        raf.read(nomBytes);

        // Convertim els bytes a cadena utilitzant UTF-8
        return new String(nomBytes, StandardCharsets.UTF_8).trim();
    }

    private void escriureNom(RandomAccessFile raf, String nom) throws IOException {
        // Convertir el nom a bytes en UTF-8
        byte[] nomBytes = nom.getBytes(StandardCharsets.UTF_8);

        // Si el nom ocupa més de 40 bytes, es talla adequadament
        if (nomBytes.length > NAME_MAX_BYTES) {
            byte[] nomTruncat = UTF8Utils.truncar(nomBytes, NAME_MAX_BYTES);
            raf.write(nomTruncat);
        } else {
            raf.write(nomBytes);  // Escriure els bytes
            // Omplir si és necessari fins a 40 bytes
            raf.write(new byte[NAME_MAX_BYTES - nomBytes.length]);
        }
    }

    // Mètode per trobar la posició d'un videojoc al fitxer segons el número de registre
    private long trobarPosicioRegistre(RandomAccessFile raf, int registreBuscat) throws IOException {
        raf.seek(0); // Tornem al principi
        while (raf.getFilePointer() < raf.length()) {
            long posicioActual = raf.getFilePointer();
            int registreActual = raf.readInt();
            raf.skipBytes(NAME_MAX_BYTES); // Saltar el nom

            if (registreActual == registreBuscat) {
                return posicioActual;
            }
        }
        return -1;
    }
}
