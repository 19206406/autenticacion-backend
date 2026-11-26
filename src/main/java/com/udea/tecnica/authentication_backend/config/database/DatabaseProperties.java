package com.udea.tecnica.authentication_backend.config.database;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("app.database")
public record DatabaseProperties(
        String host,
        int port,
        String name,
        String maintenanceName,
        boolean autoCreate) {
}
