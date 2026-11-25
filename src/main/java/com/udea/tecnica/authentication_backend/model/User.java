package com.udea.tecnica.authentication_backend.model;

public class User {
    private final Long id;
    private final String fullName;
    private final String email;
    private final String passwordHash;

    public User(Long id, String fullName, String email, String passwordHash) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    /** Devuelve una copia del usuario con el identificador asignado por el repositorio. */
    public User withId(Long newId) {
        return new User(newId, fullName, email, passwordHash);
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
}
