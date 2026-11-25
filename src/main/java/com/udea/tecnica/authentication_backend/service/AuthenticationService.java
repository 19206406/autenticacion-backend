package com.udea.tecnica.authentication_backend.service;

import com.udea.tecnica.authentication_backend.dto.LoginRequest;
import com.udea.tecnica.authentication_backend.dto.RegisterRequest;
import com.udea.tecnica.authentication_backend.dto.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthenticationService {
    UserResponse register(RegisterRequest request);

    UserResponse login(LoginRequest credentials, HttpServletRequest request, HttpServletResponse response);
}
