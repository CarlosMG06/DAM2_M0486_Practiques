package com.project;

import java.io.Serializable;
import java.util.Objects;

public class Ciutada implements Serializable {
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

    @Override
    public String toString() {
        return String.format("Ciutada [ID=%d, Nom=%s, Cognom=%s, Edat=%d]", ciutadaId, nom, cognom, edat);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ciutada ciutada = (Ciutada) o;
        if (ciutadaId == 0 || ciutada.ciutadaId == 0) return this == ciutada;
        return ciutadaId == ciutada.ciutadaId;
    }

    @Override
    public int hashCode() {
        return (ciutadaId > 0) ? Objects.hash(ciutadaId) : super.hashCode();
    }    

}