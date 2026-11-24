package com.udea.tecnica.autenticacion_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI apiAutenticacion() {
        return new OpenAPI().info(new Info()
                .title("API de Autenticación")
                .description("Registro, inicio y cierre de sesión; usuarios almacenados en memoria")
                .version("1.0.0"));
    }
}
