package cat.iesesteveterradas.dao;

import cat.iesesteveterradas.model.Faccio;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FaccioDAO {
    private final Connection conn;

    public FaccioDAO(Connection conn) {
        this.conn = conn;
    }

    public List<Faccio> getAll() throws SQLException {
        List<Faccio> llista = new ArrayList<>();
        String sql = "SELECT * FROM Faccio";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                llista.add(new Faccio(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("resum")
                ));
            }
        }
        return llista;
    }

    // Altres mètodes CRUD segons necessitats...
}
