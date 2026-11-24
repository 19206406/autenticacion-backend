package com.udea.tecnica.autenticacion_backend.exception;

import java.time.LocalDateTime;

public record ErrorRespuesta(int estado, String mensaje, LocalDateTime fecha) {
    public static ErrorRespuesta crear(int estado, String mensaje) {
        return new ErrorRespuesta(estado, mensaje, LocalDateTime.now());
    }
}
