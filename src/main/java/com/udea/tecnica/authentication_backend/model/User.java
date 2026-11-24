package com.udea.tecnica.authentication_backend.model;

public class User {
    private final Long id;
    private final String fullName;
    private final String email;
    private final String passwordHash;

    public User(String email, Long id, String fullName, String passwordHash) {
        this.email = email;
        this.id = id;
        this.fullName = fullName;
        this.passwordHash = passwordHash;
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
