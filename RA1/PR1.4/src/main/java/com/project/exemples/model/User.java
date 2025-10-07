package com.project.exemples.model;

// Aquesta és una classe POJO (Plain Old Java Object). JSON-B mapeja dades JSON
// des de i cap a aquests objectes.
public class User {
    // Camps privats per emmagatzemar les dades de l'usuari.
    private String nom;
    private int edat;

    // JSON-B requereix un constructor sense arguments per a la deserialització.
    public User() {
    }

    // Un constructor amb arguments per crear instàncies fàcilment al nostre codi.
    public User(String nom, int edat) {
        this.nom = nom;
        this.edat = edat;
    }

    // Getter públic per al camp 'nom'. JSON-B l'utilitza durant la serialització.
    public String getNom() {
        return nom;
    }

    // Setter públic per al camp 'nom'. JSON-B l'utilitza durant la deserialització.
    public void setNom(String nom) {
        this.nom = nom;
    }

    // Getter públic per al camp 'edat'.
    public int getEdat() {
        return edat;
    }

    // Setter públic per al camp 'edat'.
    public void setEdat(int edat) {
        this.edat = edat;
    }
}
