package com.project.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "exemplar")
public class Exemplar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="exemplar_id", unique=true, nullable=false)
    private Long exemplarId;

    @Column(name="codi_barres", unique=true, nullable=false)
    private String codiBarres;

    private boolean disponible;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "llibre_id")
    private Llibre llibre;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "biblioteca_id")
    private Biblioteca biblioteca;

    @OneToMany(mappedBy = "exemplar", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Prestec> historialPrestecs = new HashSet<>();

    public Exemplar() {}

    public Exemplar(String codiBarres, Llibre llibre, Biblioteca biblioteca) {
        this.codiBarres = codiBarres;
        this.llibre = llibre;
        this.biblioteca = biblioteca;
        this.disponible = true; // Per defecte disponible
    }

    public Long getExemplarId() { return exemplarId; }
    public void setExemplarId(Long exemplarId) { this.exemplarId = exemplarId; }
    public String getCodiBarres() { return codiBarres; }
    public void setCodiBarres(String codiBarres) { this.codiBarres = codiBarres; }
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
    public Llibre getLlibre() { return llibre; }
    public void setLlibre(Llibre llibre) { this.llibre = llibre; }
    public Biblioteca getBiblioteca() { return biblioteca; }
    public void setBiblioteca(Biblioteca biblioteca) { this.biblioteca = biblioteca; }
    public Set<Prestec> getHistorialPrestecs() { return historialPrestecs; }
    public void setHistorialPrestecs(Set<Prestec> historialPrestecs) { this.historialPrestecs = historialPrestecs; }

    @Override
    public String toString() {
        // Accés segur al títol del llibre
        String titol = (llibre != null) ? llibre.getTitol() : "Desconegut";
        return "Exemplar{id=" + exemplarId + ", codi='" + codiBarres + "', llibre='" + titol + "', disponible=" + disponible + "}";
    }
}