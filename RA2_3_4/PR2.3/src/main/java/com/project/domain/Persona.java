package com.project.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "persona")
public class Persona implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="personaId", unique=true, nullable=false)
    private Long personaId;

    private String dni;
    private String nom;
    private String telefon;
    private String email;

    @OneToMany(mappedBy = "persona", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Prestec> prestecs = new HashSet<>();

    public Persona() {}

    public Persona(String dni, String nom, String telefon, String email) {
        this.dni = dni;
        this.nom = nom;
        this.telefon = telefon;
        this.email = email;
    }

    public Long getPersonaId() { return personaId; }
    public void setPersonaId(Long personaId) { this.personaId = personaId; }
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Set<Prestec> getPrestecs() { return prestecs; }
    public void setPrestecs(Set<Prestec> prestecs) { this.prestecs = prestecs; }

    @Override
    public String toString() {
        return "Persona{id=" + personaId + ", dni='" + dni + "', nom='" + nom + "'}";
    }
}