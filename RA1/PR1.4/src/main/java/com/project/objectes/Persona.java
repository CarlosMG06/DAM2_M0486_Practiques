package com.project.objectes;

public class Persona {
    private int id;
    private String nom;
    private int edat;

    // Constructor per defecte
    public Persona() {
    }

    // Constructor amb paràmetres
    public Persona(int id, String nom, int edat) {
        this.id = id;
        this.nom = nom;
        this.edat = edat;
    }

    // Getters i setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getEdat() {
        return edat;
    }

    public void setEdat(int edat) {
        this.edat = edat;
    }

    // Sobreescriptura del mètode toString per a una representació més clara de l'objecte
    @Override
    public String toString() {
        return "Persona{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", edat=" + edat +
                '}';
    }
}
