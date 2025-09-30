package com.project.pr13.format;

public class PersonaFormatter {

    public static String getCapçaleres() {
        return "Nom      Cognom        Edat  Ciutat\n-------- -------------- ----- ---------";
    }

    public static String formatarPersona(String nom, String cognom, String edat, String ciutat) {
        return String.format("%-8s %-14s %-5s %-9s", nom, cognom, edat, ciutat);
    }
}
