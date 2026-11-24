package com.udea.tecnica.authentication_backend.dto;

import com.udea.tecnica.authentication_backend.model.User;

public record UserResponse(Long id, String fullName, String email) {
    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getFullName(), user.getEmail());
    }
}
