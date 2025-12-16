package com.project;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

// @Entity: Marca aquesta classe com una entitat JPA mapejada a una taula de la BBDD.
@Entity
@Table(name = "ciutadans")
public class Ciutada implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="ciutadaId", unique=true, nullable=false)
    private Long ciutadaId;

    private String nom;
    private String cognom;
    private int edat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ciutatId")
    private Ciutat ciutat;
    
    
    // UUID: Identificador únic generat al crear l'objecte.
    // Necessari per equals/hashCode quan itemId encara és null (abans de persist).
    @Column(name = "uuid", nullable = false, updatable = false, unique = true)
    private String uuid = UUID.randomUUID().toString();

    public Ciutada() {}

    public Ciutada(String nom, String cognom, int edat) {
        this.nom = nom;
        this.cognom = cognom;
        this.edat = edat;
    }

    public Long getCiutadaId() { return ciutadaId; }
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

    // EQUALS amb instanceof (no getClass()):
    // Hibernate crea PROXIES (subclasses) per lazy loading.
    // Utilitzar getClass() fallaria perquè Proxy.class != Ciutada.class.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ciutada)) return false; 
        Ciutada ciutada = (Ciutada) o;
        return Objects.equals(uuid, ciutada.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }    

}