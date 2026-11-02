package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DBConfigReader {
    public static DBConfig getDBConfig() {
        Properties properties = new Properties();

        try (InputStream input = DBConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IllegalStateException("config.properties file does not exist");
            }

            properties.load(input);

            String dbUrl = getDBPropertyValue(properties, "db.url");
            String dbUser = getDBPropertyValue(properties, "db.user");
            String dbPassword = getDBPropertyValue(properties, "db.password");

            return new DBConfig(dbUrl, dbUser, dbPassword);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to read db config properties", ex);
        }
    }

    private static String getDBPropertyValue(Properties properties, String propertyName) {
        String value = properties.getProperty(propertyName);

        if (value == null) {
            throw new IllegalStateException("db config property does not exist: " + propertyName);
        }

        return value;
    }
}
