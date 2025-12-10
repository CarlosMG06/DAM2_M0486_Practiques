package cat.iesesteveterradas.dao;

import cat.iesesteveterradas.model.Personatge;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonatgeDAO {
    private final Connection conn;

    public PersonatgeDAO(Connection conn) {
        this.conn = conn;
    }

    public List<Personatge> getAll() throws SQLException {
        List<Personatge> llista = new ArrayList<>();
        String sql = "SELECT * FROM Personatge";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                llista.add(new Personatge(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getDouble("atac"),
                        rs.getDouble("defensa"),
                        rs.getInt("idFaccio")
                ));
            }
        }
        return llista;
    }

    // Altres mètodes CRUD segons necessitats...
}
