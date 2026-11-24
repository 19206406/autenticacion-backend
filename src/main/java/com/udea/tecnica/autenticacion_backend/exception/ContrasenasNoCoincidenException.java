package com.udea.tecnica.autenticacion_backend.exception;

public class ContrasenasNoCoincidenException extends RuntimeException {
    public ContrasenasNoCoincidenException() {
        super("La contraseña y su confirmación no coinciden");
    }
}
