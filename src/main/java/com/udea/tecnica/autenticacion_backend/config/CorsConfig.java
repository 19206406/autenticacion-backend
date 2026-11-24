package com.udea.tecnica.autenticacion_backend.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {
    @Bean
    public CorsConfigurationSource corsConfigurationSource(
            @Value("${app.cors.allowed-origins:http://localhost:5500,http://127.0.0.1:5500}")
            String origenesPermitidos) {

        CorsConfiguration configuracion = new CorsConfiguration();
        configuracion.setAllowedOrigins(Arrays.stream(origenesPermitidos.split(","))
                .map(String::strip)
                .filter(valor -> !valor.isEmpty())
                .toList());
        configuracion.setAllowCredentials(true);
        configuracion.setAllowedMethods(Arrays.asList(
                HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.OPTIONS.name()));
        configuracion.setAllowedHeaders(Arrays.asList(
                HttpHeaders.CONTENT_TYPE, "X-CSRF-TOKEN", "X-XSRF-TOKEN"));
        configuracion.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource fuente = new UrlBasedCorsConfigurationSource();
        fuente.registerCorsConfiguration("/api/**", configuracion);
        return fuente;
    }
}
