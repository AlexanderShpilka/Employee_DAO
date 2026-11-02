package org.example;

import java.sql.SQLException;
import java.sql.Statement;

public class SchemaInitializer {
    private final DatabaseConnector databaseConnector;

    public SchemaInitializer(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    public void createEmployeesTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS employees (
                id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                name TEXT NOT NULL,
                age INT NOT NULL CHECK (age BETWEEN 0 AND 120),
                position TEXT NOT NULL,
                salary NUMERIC(10, 2) NOT NULL CHECK (salary >= 0)
            )
        """;

        try (Statement statement = this.databaseConnector.getConnection().createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException ex) {
            throw new RuntimeException("Failed to create `employees` table", ex);
        }
    }
}
