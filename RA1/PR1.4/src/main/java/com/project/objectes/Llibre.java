package com.project.objectes;

public class Llibre {
    private int id;
    private String titol;
    private String autor;
    private int any;

    // Constructor per defecte
    public Llibre() {
    }

    // Constructor amb paràmetres
    public Llibre(int id, String titol, String autor, int any) {
        this.id = id;
        this.titol = titol;
        this.autor = autor;
        this.any = any;
    }

    // Getters i setters
    public int getId() {
        return id;
    }

    public String getTitol() {
        return titol;
    }

    public String getAutor() {
        return autor;
    }

    public int getAny() {
        return any;
    }

    public void setAny(int any) {
        this.any = any;
    }
}
