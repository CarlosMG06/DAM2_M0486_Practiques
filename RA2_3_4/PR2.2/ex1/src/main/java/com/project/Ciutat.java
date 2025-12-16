package com.project;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Ciutat implements Serializable {
    private long ciutatId;
    private String nom;
    private String pais;
    private int poblacio;
    
    private Set<Ciutada> ciutadans = new HashSet<>();

    public Ciutat() {}

    public Ciutat(String nom, String pais, int poblacio) {
        this.nom = nom;
        this.pais = pais;
        this.poblacio = poblacio;
    }

    public long getCiutatId() { return ciutatId; }
    public void setCiutatId(long ciutatId) {this.ciutatId = ciutatId; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }

    public int getPoblacio() { return poblacio; }
    public void setPoblacio(int poblacio) { this.poblacio = poblacio; }


    public Set<Ciutada> getCiutadans() {
        return ciutadans;
    }
    public void setCiutadans(Set<Ciutada> ciutadans) {
        this.ciutadans = ciutadans;
        if (this.ciutadans != null) {
            for (Ciutada ciutada : this.ciutadans) {
                ciutada.setCiutat(this);
            }
        }
    }

    public void addCiutada(Ciutada ciutada) {
        if (ciutadans.add(ciutada)) {
            ciutada.setCiutat(this);
        }
    }
    public void removeCiutada(Ciutada ciutada) {
        if (ciutadans.remove(ciutada)) {
            ciutada.setCiutat(null);
        }
    }

    @Override
    public String toString() {
        String llistaCiutadans = "[]";
        
        if (ciutadans != null && !ciutadans.isEmpty()) {
            llistaCiutadans = ciutadans.stream()
                .map(Ciutada::getNom)
                .collect(Collectors.joining(", ", "[", "]"));
        }

        return String.format("Ciutat [ID=%d, Nom=%s, Pais=%s, Poblacio=%d, Ciutadans: %s]", ciutatId, nom, pais, poblacio, llistaCiutadans);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ciutat ciutat = (Ciutat) o;
        if (ciutatId == 0 || ciutat.ciutatId == 0) return this == ciutat;
        return ciutatId == ciutat.ciutatId;
    }

    @Override
    public int hashCode() {
        return (ciutatId > 0) ? Objects.hash(ciutatId) : super.hashCode();
    }    

}