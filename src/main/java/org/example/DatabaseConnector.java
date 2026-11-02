package org.example;

import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnector implements AutoCloseable {
    private Connection connection;

    public Connection getConnection() {
        if (this.connection == null) {
            this.connect();
        }

        return this.connection;
    }

    private void connect() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();

        DBConfig dbConfig = DBConfigReader.getDBConfig();

        dataSource.setUrl(dbConfig.url());
        dataSource.setUser(dbConfig.username());
        dataSource.setPassword(dbConfig.password());

        try {
            this.connection = dataSource.getConnection();
            System.out.println("Connected");
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to connect to database", ex);
        }
    }

    @Override
    public void close() {
        if (this.connection != null) {
            try {
                this.connection.close();
                this.connection = null;
            } catch (SQLException ex) {
                throw new RuntimeException("Failed to close database connection", ex);
            }
        }
    }
}
