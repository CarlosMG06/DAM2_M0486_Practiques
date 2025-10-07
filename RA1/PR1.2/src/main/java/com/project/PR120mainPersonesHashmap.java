package com.project;

import java.io.*;
import java.util.HashMap;

import com.project.excepcions.IOFitxerExcepcio;

public class PR120mainPersonesHashmap {
    private static String filePath = System.getProperty("user.dir") + "/data/PR120persones.dat";

    public static void main(String[] args) {
        HashMap<String, Integer> persones = new HashMap<>();
        persones.put("Anna", 25);
        persones.put("Bernat", 30);
        persones.put("Carla", 22);
        persones.put("David", 35);
        persones.put("Elena", 28);

        try {
            escriurePersones(persones);
            llegirPersones();
        } catch (IOFitxerExcepcio e) {
            System.err.println("Error en treballar amb el fitxer: " + e.getMessage());
        }
    }

    // Getter per a filePath
    public static String getFilePath() {
        return filePath;
    }

    // Setter per a filePath
    public static void setFilePath(String newFilePath) {
        filePath = newFilePath;
    }

    // Mètode per escriure les persones al fitxer
    public static void escriurePersones(HashMap<String, Integer> persones) throws IOFitxerExcepcio {
       // *************** CODI PRÀCTICA **********************/
       try (FileOutputStream fos = new FileOutputStream(filePath);
            DataOutputStream dos = new DataOutputStream(fos)) {
            for (HashMap.Entry<String, Integer> entry : persones.entrySet()) {
                dos.writeUTF(entry.getKey());
                dos.writeInt(entry.getValue());
            }
            dos.flush();
            dos.close();
       } catch (IOException e) {
            e.printStackTrace();
            throw new IOFitxerExcepcio("Error en escriure les persones al fitxer: " + e.getMessage(), e);
       }
    }

    // Mètode per llegir les persones des del fitxer
    public static void llegirPersones() throws IOFitxerExcepcio {
        // *************** CODI PRÀCTICA **********************/
        try (FileInputStream fis = new FileInputStream(filePath);
            DataInputStream dis = new DataInputStream(fis)) {
            while (dis.available() > 0) {
                String nom = dis.readUTF();
                int edat = dis.readInt();
                System.out.println(nom + ": " + edat + " anys");
            }
            dis.close();
        } catch (IOException e) {
            throw new IOFitxerExcepcio("Error en llegir les persones del fitxer: " + e.getStackTrace(), e);
        }
    }
}
