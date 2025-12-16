package com.project;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

// @Entity: Marca aquesta classe com una entitat JPA mapejada a una taula de la BBDD.
@Entity
@Table(name = "ciutats")
public class Ciutat implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="ciutatId", unique=true, nullable=false)
    private long ciutatId;

    private String nom;
    private String pais;
    private int poblacio;
    
    @OneToMany(mappedBy = "ciutat", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Ciutada> ciutadans = new HashSet<>();

    // UUID: Identificador únic generat ABANS de guardar a la BBDD.
    // Útil per equals/hashCode ja que cartId és null fins que es persisteix.
    @Column(name = "uuid", nullable = false, updatable = false, unique = true)
    private String uuid = UUID.randomUUID().toString();

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
        this.ciutadans.clear();
        if (ciutadans != null) {
            ciutadans.forEach(this::addCiutada);
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
                .map(ciutada -> ciutada.getNom() + " " + ciutada.getCognom())
                .collect(Collectors.joining(" | ", "[", "]"));
        }

        return String.format("%d: %s (%s), Població: %d, Ciutadans: %s", ciutatId, nom, pais, poblacio, llistaCiutadans);
    }

    // EQUALS i HASHCODE basats en UUID:
    // Garanteix consistència abans i després de persistir l'entitat.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ciutat)) return false;
        Ciutat ciutat = (Ciutat) o;
        return Objects.equals(uuid, ciutat.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }    

}