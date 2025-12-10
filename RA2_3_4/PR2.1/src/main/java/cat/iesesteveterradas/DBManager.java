package cat.iesesteveterradas;

import cat.iesesteveterradas.utils.UtilsSQLite;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.io.File;
import java.util.ArrayList;

public class DBManager {
    private static final String DB_PATH = "forhonor.db";
    private static Connection conn;

    public static void init() {
        File dbFile = new File(DB_PATH);
        boolean exists = dbFile.exists();
        try (conn = UtilsSQLite.connect(DB_PATH)) {
            if (!exists) {
                createTables(conn);
                insertInitialData(conn);
            }
        } catch (SQLException e) {
            System.err.println("Error inicialitzant la base de dades: " + e.getMessage());
        }
    }

    private static void createTables(Connection conn) throws SQLException {
        String createFaccio = "CREATE TABLE IF NOT EXISTS Faccio (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nom VARCHAR(15) NOT NULL," +
                "resum VARCHAR(500));";
        String createPersonatge = "CREATE TABLE IF NOT EXISTS Personatge (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nom VARCHAR(15) NOT NULL," +
                "atac REAL," +
                "defensa REAL," +
                "idFaccio INTEGER," +
                "FOREIGN KEY (idFaccio) REFERENCES Faccio(id));";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createFaccio);
            stmt.execute(createPersonatge);
        }
    }

    private static void insertInitialData(Connection conn) throws SQLException {
        // Inserció de faccions
        UtilsSQLite.queryUpdatePS(conn,
            "INSERT INTO Faccio (nom, resum) VALUES (?, ?)",
            "Cavallers", "Though seen as a single group, the Knights are hardly unified. There are many Legions in Ashfeld, the most prominent being The Iron Legion."
        );
        UtilsSQLite.queryUpdatePS(conn,
            "INSERT INTO Faccio (nom, resum) VALUES (?, ?)",
            "Vikings", "The Vikings are a loose coalition of hundreds of clans and tribes, the most powerful being The Warborn."
        );
        UtilsSQLite.queryUpdatePS(conn,
            "INSERT INTO Faccio (nom, resum) VALUES (?, ?)",
            "Samurais", "The Samurai are the most unified of the three factions, though this does not say much as the Daimyos were often battling each other for dominance."
        );

        // Inserció de personatges
        UtilsSQLite.queryUpdatePS(conn,
            "INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES (?, ?, ?, ?)",
            "Warden", 1, 3, 1
        );
        UtilsSQLite.queryUpdatePS(conn,
            "INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES (?, ?, ?, ?)",
            "Conqueror", 2, 2, 1
        );
        UtilsSQLite.queryUpdatePS(conn,
            "INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES (?, ?, ?, ?)",
            "Peacekeep", 2, 3, 1
        );
        UtilsSQLite.queryUpdatePS(conn,
            "INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES (?, ?, ?, ?)",
            "Raider", 3, 3, 2
        );
        UtilsSQLite.queryUpdatePS(conn,
            "INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES (?, ?, ?, ?)",
            "Warlord", 2, 2, 2
        );
        UtilsSQLite.queryUpdatePS(conn,
            "INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES (?, ?, ?, ?)",
            "Berserker", 1, 1, 2
        );
        UtilsSQLite.queryUpdatePS(conn,
            "INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES (?, ?, ?, ?)",
            "Kensei", 3, 2, 3
        );
        UtilsSQLite.queryUpdatePS(conn,
            "INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES (?, ?, ?, ?)",
            "Shugoki", 2, 1, 3
        );
        UtilsSQLite.queryUpdatePS(conn,
            "INSERT INTO Personatge (nom, atac, defensa, idFaccio) VALUES (?, ?, ?, ?)",
            "Orochi", 3, 2, 3
        );
    }

    public static void showFactions() {
        try {
            ResultSet rs = UtilsSQLite.querySelectPS(conn, "SELECT * FROM Faccio");
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String resum = rs.getString("resum");
                System.out.println(id + " | " + nom + " | " + resum);
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("Error mostrant la taula de faccions: " + e.getMessage());
        }
    }

    public static void showCharacters() {
        try {
            ResultSet rs = UtilsSQLite.querySelectPS(conn, "SELECT * FROM Personatge");
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                double atac = rs.getDouble("atac");
                double defensa = rs.getDouble("defensa");
                int idFaccio = rs.getInt("idFaccio");
                System.out.println(id + " | " + nom + " | " + atac + " | " + defensa + " | " + idFaccio);
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("Error mostrant la taula de personatges: " + e.getMessage());
        }
    }

    public static ArrayList<String> getFactionsList() {
        ArrayList<String> factionsList = new ArrayList<String>();
        try {
            ResultSet rs = UtilsSQLite.querySelectPS(conn, "SELECT nom FROM Faccio")
            while (rs.next()) {
                String nom = rs.getString("nom");
                factionsList.add(nom);
            }
        } catch (SQLException e) {
            System.err.println("Error obtenint llista de faccions: " + e.getMessage());
        }
        return factionsList;
    }

    public static void showCharactersByFaction(int factionId) {
        try {
            ResultSet rs = UtilsSQLite.querySelectPS(conn, 
                "SELECT p.id AS personatge_id, p.nom AS personatge_nom, f.id AS faccio_id, f.nom AS faccio_nom" +
                "FROM Personatge p JOIN Faccio f ON p.idFaccio = f.id" + 
                "WHERE idFaccio = ?", factionId);
            while (rs.next()) {
                int personatgeId = rs.getInt("personatge_id");
                String personatgeNom = rs.getString("personatge_nom");
                int faccioId = rs.getInt("faccio_id");
                String faccioNom = rs.getString("faccio_nom");
                System.out.println(personatgeId + " | " + personatgeNom + " | " + faccioId + " | " + faccioNom);
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("Error mostrant personatges per facció: " + e.getMessage());
        }
    }

    public static void showBestByFaction(int factionId, String statName) {
        try {
            ResultSet rs = UtilsSQLite.querySelectPS(conn, 
                "SELECT id, nom, " + statName + " FROM Personatge WHERE idFaccio = ?" + 
                "ORDER BY " + statName + " DESC LIMIT 1", factionId);
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getInt("nom");
                double stat = rs.getDouble(statName);
                System.out.println(id + " | " + nom + " | " + stat);
            }
        }
    }


    public static String getDbPath() {
        return DB_PATH;
    }
}
