package com.udea.tecnica.authentication_backend.config.database;

import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableConfigurationProperties(DatabaseProperties.class)
@ConditionalOnProperty(name = "app.database.auto-create", havingValue = "true")
public class DatabaseConfig {

    @Bean
    public DatabaseCreator databaseCreator(DatabaseProperties databaseProperties,
            DataSourceProperties dataSourceProperties) {
        return new PostgresDatabaseCreator(databaseProperties,
                dataSourceProperties.getUsername(), dataSourceProperties.getPassword());
    }

    @Bean
    public DataSource dataSource(DatabaseCreator databaseCreator, DataSourceProperties dataSourceProperties) {
        databaseCreator.createIfNotExists();
        return dataSourceProperties.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }
}
