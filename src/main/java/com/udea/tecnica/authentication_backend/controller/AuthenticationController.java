package com.udea.tecnica.authentication_backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.udea.tecnica.authentication_backend.dto.AuthResponse;
import com.udea.tecnica.authentication_backend.dto.LoginRequest;
import com.udea.tecnica.authentication_backend.dto.RegisterRequest;
import com.udea.tecnica.authentication_backend.dto.UserResponse;
import com.udea.tecnica.authentication_backend.service.IAuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Registro, login y logout (POST /api/auth/logout)")
public class AuthenticationController {
    private final IAuthenticationService service;

    public AuthenticationController(IAuthenticationService service) {
        this.service = service;
    }

    @Operation(summary = "Registrar usuario", description = "La contraseña se guarda con BCrypt; no inicia sesión")
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        UserResponse created = service.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponse("Usuario registrado correctamente", created));
    }

    @Operation(summary = "Iniciar sesión", description = "Valida credenciales y crea la sesión (cookie JSESSIONID)")
    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest loginRequest,
            HttpServletRequest request, HttpServletResponse response) {
        UserResponse user = service.login(loginRequest, request, response);
        return new AuthResponse("Inicio de sesión exitoso", user);
    }
}
