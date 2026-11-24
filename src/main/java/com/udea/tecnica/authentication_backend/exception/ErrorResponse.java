package com.udea.tecnica.authentication_backend.exception;

import java.time.LocalDateTime;

public record ErrorResponse(int status, String message, LocalDateTime date) {
    public static ErrorResponse create(int status, String message) {
        return new ErrorResponse(status, message, LocalDateTime.now());
    }
}
