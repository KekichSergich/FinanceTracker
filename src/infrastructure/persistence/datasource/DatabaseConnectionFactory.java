package infrastructure.persistence.datasource;

import java.sql.Connection;

public class DatabaseConnectionFactory {

    private final String url;

    public DatabaseConnectionFactory(String url) {
        this.url = url;
    }

    public Connection createConnection() {
        // TODO
        return null;
    }
}