package com.udea.tecnica.authentication_backend.service;

import com.udea.tecnica.authentication_backend.dto.RegisterRequest;
import com.udea.tecnica.authentication_backend.dto.UserResponse;
public interface AuthenticationService {
    UserResponse register(RegisterRequest request);
    UserResponse getCurrentUser(String email);
}
