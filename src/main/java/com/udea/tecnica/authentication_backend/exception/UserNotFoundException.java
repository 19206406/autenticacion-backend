package com.udea.tecnica.authentication_backend.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("El usuario autenticado ya no está disponible");
    }
}
