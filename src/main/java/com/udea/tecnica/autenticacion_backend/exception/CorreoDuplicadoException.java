package com.udea.tecnica.autenticacion_backend.exception;

public class CorreoDuplicadoException extends RuntimeException {
    public CorreoDuplicadoException() {
        super("El correo ya se encuentra registrado");
    }
}
