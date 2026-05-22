package infrastructure.persistence.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Factory responsible for creating and configuring
 * the SQLite database connection.
 */
public class DatabaseConnectionFactory {

    private final String url;

    public DatabaseConnectionFactory(String url) {
        this.url = url;
    }

    /**
     * Creates a new database connection and initializes the schema.
     *
     * @return configured JDBC connection
     */
    public Connection createConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(url);
            conn.setAutoCommit(true);
            initSchema(conn);
            return conn;
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Failed to connect to database: " + url, e);
        }
    }

    /**
     * Creates the transactions table if it does not already exist.
     */
    private void initSchema(Connection conn) throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS transactions (
                    id       INTEGER PRIMARY KEY AUTOINCREMENT,
                    amount   REAL    NOT NULL,
                    type     TEXT    NOT NULL,
                    category TEXT    NOT NULL,
                    date     TEXT    NOT NULL,
                    note     TEXT
                );
                """;
        try (Statement st = conn.createStatement()) {
            st.execute(sql);
        }
    }
}