package com.udea.tecnica.authentication_backend.exception;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException() {
        super("El correo ya se encuentra registrado");
    }
}
