package com.udea.tecnica.authentication_backend.config.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostgresDatabaseCreator implements DatabaseCreator {
    private static final Logger log = LoggerFactory.getLogger(PostgresDatabaseCreator.class);
    private static final Pattern VALID_NAME = Pattern.compile("^[A-Za-z_][A-Za-z0-9_]{0,62}$");
    private static final String EXISTS_QUERY = "SELECT 1 FROM pg_database WHERE datname = ?";

    private final DatabaseProperties properties;
    private final String username;
    private final String password;

    public PostgresDatabaseCreator(DatabaseProperties properties, String username, String password) {
        this.properties = properties;
        this.username = username;
        this.password = password;
    }

    @Override
    public void createIfNotExists() {
        String databaseName = validatedName(properties.name());
        try (Connection connection = DriverManager.getConnection(maintenanceUrl(), username, password)) {
            if (exists(connection, databaseName)) {
                log.info("La base de datos {} ya existe", databaseName);
                return;
            }
            create(connection, databaseName);
            log.info("Base de datos {} creada", databaseName);
        } catch (SQLException e) {
            throw new IllegalStateException("No se pudo conectar a PostgreSQL en %s:%d para crear la base de datos %s"
                    .formatted(properties.host(), properties.port(), databaseName), e);
        }
    }

    private String maintenanceUrl() {
        return "jdbc:postgresql://%s:%d/%s"
                .formatted(properties.host(), properties.port(), properties.maintenanceName());
    }

    private static String validatedName(String name) {
        if (name == null || !VALID_NAME.matcher(name).matches()) {
            throw new IllegalStateException("Nombre de base de datos inválido: " + name);
        }
        return name;
    }

    private static boolean exists(Connection connection, String databaseName) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(EXISTS_QUERY)) {
            statement.setString(1, databaseName);
            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            }
        }
    }

    private static void create(Connection connection, String databaseName) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE DATABASE \"" + databaseName + "\"");
        }
    }
}
