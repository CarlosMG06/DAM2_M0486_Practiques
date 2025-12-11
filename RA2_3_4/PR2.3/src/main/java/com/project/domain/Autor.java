package com.project.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

// TODO 1: Afegir anotacions @Entity i @Table
public class Autor implements Serializable {

    private static final long serialVersionUID = 1L;

    // TODO 2: Afegir @Id i @GeneratedValue
    private Long autorId;

    private String nom;

    // TODO 3: Relació ManyToMany. 
    // PISTA: L'enunciat diu que Autor és la part inversa ("mappedBy").
    // Això vol dir que la taula intermèdia la gestiona l'entitat 'Llibre'.
    private Set<Llibre> llibres = new HashSet<>();

    public Autor() {}

    public Autor(String nom) {
        this.nom = nom;
    }

    public Long getAutorId() { return autorId; }
    public void setAutorId(Long autorId) { this.autorId = autorId; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public Set<Llibre> getLlibres() { return llibres; }
    public void setLlibres(Set<Llibre> llibres) { this.llibres = llibres; }

    @Override
    public String toString() {
        return "Autor{id=" + autorId + ", nom='" + nom + "'}";
    }
}