package com.udea.tecnica.authentication_backend.exception;

public class PasswordsDoNotMatchException extends RuntimeException {
    public PasswordsDoNotMatchException() {
        super("La contraseña y su confirmación no coinciden");
    }
}
