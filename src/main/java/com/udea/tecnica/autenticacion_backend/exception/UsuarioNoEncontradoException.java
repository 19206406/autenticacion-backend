package com.udea.tecnica.autenticacion_backend.exception;

public class UsuarioNoEncontradoException extends RuntimeException {
    public UsuarioNoEncontradoException() {
        super("El usuario autenticado ya no está disponible");
    }
}
