package com.udea.tecnica.autenticacion_backend.model;

public class Usuario {
    private final Long id;
    private final String nombreCompleto;
    private final String correo;
    private final String passwordHash;

    public Usuario(String correo, Long id, String nombreCompleto, String passwordHash) {
        this.correo = correo;
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.passwordHash = passwordHash;
    }

    public Long getId() {
        return id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getCorreo() {
        return correo;
    }

    public String getPasswordHash() {
        return passwordHash;
    }



}
