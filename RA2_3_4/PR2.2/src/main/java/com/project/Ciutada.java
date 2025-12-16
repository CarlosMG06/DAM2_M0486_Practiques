package com.project;

public class Ciutada {
    private long ciutadaId;
    private String nom;
    private String cognom;
    private int edat;

    private Ciutat ciutat;
    
    public Ciutada() {}

    public Ciutada(String nom, String cognom, int edat) {
        this.nom = nom;
        this.cognom = cognom;
        this.edat = edat;
    }

    public long getCiutadaId() { return ciutadaId; }
    public void setCiutadaId(long ciutadaId) {this.ciutadaId = ciutadaId; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getCognom() { return cognom; }
    public void setCognom(String cognom) { this.cognom = cognom; }

    public int getEdat() { return edat; }
    public void setEdat(int edat) { this.edat = edat; }

    public Ciutat getCiutat() { return ciutat; }
    public void setCiutat(Ciutat ciutat) { this.ciutat = ciutat; }


}