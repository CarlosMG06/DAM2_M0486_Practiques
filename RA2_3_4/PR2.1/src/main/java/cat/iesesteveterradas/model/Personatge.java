package cat.iesesteveterradas.model;

public class Personatge {
    private int id;
    private String nom;
    private double atac;
    private double defensa;
    private int idFaccio;

    public Personatge(int id, String nom, double atac, double defensa, int idFaccio) {
        this.id = id;
        this.nom = nom;
        this.atac = atac;
        this.defensa = defensa;
        this.idFaccio = idFaccio;
    }

    public Personatge(String nom, double atac, double defensa, int idFaccio) {
        this(0, nom, atac, defensa, idFaccio);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public double getAtac() { return atac; }
    public void setAtac(double atac) { this.atac = atac; }
    public double getDefensa() { return defensa; }
    public void setDefensa(double defensa) { this.defensa = defensa; }
    public int getIdFaccio() { return idFaccio; }
    public void setIdFaccio(int idFaccio) { this.idFaccio = idFaccio; }

    @Override
    public String toString() {
        return "Personatge{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", atac=" + atac +
                ", defensa=" + defensa +
                ", idFaccio=" + idFaccio +
                '}';
    }
}
