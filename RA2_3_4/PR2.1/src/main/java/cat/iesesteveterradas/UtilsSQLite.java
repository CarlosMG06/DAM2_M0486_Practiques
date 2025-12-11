package cat.iesesteveterradas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtilsSQLite {

    private static final Logger logger = LoggerFactory.getLogger(UtilsSQLite.class);

    public static Connection connect(String filePath) throws SQLException {
        String url = "jdbc:sqlite:" + filePath;
        Connection conn = DriverManager.getConnection(url);
        
        // S'ha substituït System.out.println per logger.info
        logger.info("BBDD SQLite connectada a {}", filePath);
        DatabaseMetaData meta = conn.getMetaData();
        logger.info("BBDD driver: {}", meta.getDriverName());
        
        return conn;
    }

    public static void disconnect(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                logger.info("DDBB SQLite desconnectada");
            } catch (SQLException e) {
                // S'ha substituït System.err.println per logger.error
                logger.error("Error en tancar la connexió: {}", e.getMessage());
            }
        }
    }

    public static int queryUpdatePS(Connection conn, String sql, Object... params) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            int affectedRows = ps.executeUpdate();
            logger.debug("Executada queryUpdatePS. Files afectades: {}. SQL: {}", affectedRows, sql);
            return affectedRows;
        }
    }

    public static ResultSet querySelectPS(Connection conn, String sql, Object... params) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(sql);
        try {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            logger.debug("Executant querySelectPS. SQL: {}", sql);
            return ps.executeQuery();
        } catch (SQLException e) {
            ps.close();
            throw e;
        }
    }
}