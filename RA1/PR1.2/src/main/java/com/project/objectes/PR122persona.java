package com.project.objectes;

import java.io.Serializable;

public class PR122persona implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String nom;
    private String cognom;
    private int edat;

    // Constructor
    public PR122persona(String nom, String cognom, int edat) {
        this.nom = nom;
        this.cognom = cognom;
        this.edat = edat;
    }

    // Getters i Setters
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCognom() {
        return cognom;
    }

    public void setCognom(String cognom) {
        this.cognom = cognom;
    }

    public int getEdat() {
        return edat;
    }

    public void setEdat(int edat) {
        this.edat = edat;
    }

    // toString() per a la correcta visualització de l'objecte
    @Override
    public String toString() {
        return nom + " " + cognom + ", " + edat + " anys";
    }
}
