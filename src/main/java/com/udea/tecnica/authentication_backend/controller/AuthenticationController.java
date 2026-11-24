package com.udea.tecnica.authentication_backend.controller;

import java.util.Locale;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.udea.tecnica.authentication_backend.dto.AuthResponse;
import com.udea.tecnica.authentication_backend.dto.CsrfResponse;
import com.udea.tecnica.authentication_backend.dto.LoginRequest;
import com.udea.tecnica.authentication_backend.dto.RegisterRequest;
import com.udea.tecnica.authentication_backend.dto.UserResponse;
import com.udea.tecnica.authentication_backend.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Registro, login, sesión y logout")
public class AuthenticationController {
    private final AuthenticationService service;
    private final AuthenticationManager authenticator;
    private final SecurityContextRepository contextRepository;

    public AuthenticationController(AuthenticationService service,
            AuthenticationManager authenticator,
            SecurityContextRepository contextRepository) {
        this.service = service;
        this.authenticator = authenticator;
        this.contextRepository = contextRepository;
    }

    @Operation(summary = "Obtener token CSRF", description = "Solicitar antes de enviar registro, login o logout")
    @GetMapping("/csrf")
    public CsrfResponse csrf(CsrfToken token) {
        return new CsrfResponse(token.getToken(), token.getHeaderName());
    }

    @Operation(summary = "Registrar usuario", description = "La contraseña se guarda con BCrypt; no inicia sesión")
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        UserResponse created = service.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponse("Usuario registrado correctamente", created));
    }

    @Operation(summary = "Iniciar sesión", description = "Autentica un JSON y persiste la sesión en una cookie")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest,
            HttpServletRequest request, HttpServletResponse response) {
        if (loginRequest.email() == null || loginRequest.email().isBlank()
                || loginRequest.password() == null || loginRequest.password().isBlank()) {
            throw new BadCredentialsException("Credenciales inválidas");
        }
        String email = loginRequest.email().strip().toLowerCase(Locale.ROOT);

        Authentication authenticated = authenticator.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(email, loginRequest.password()));

        // The session identifier is rotated to mitigate session fixation.
        HttpSession existingSession = request.getSession(false);
        if (existingSession != null) {
            request.changeSessionId();
        }

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authenticated);
        SecurityContextHolder.setContext(context);
        contextRepository.saveContext(context, request, response);

        return ResponseEntity.ok().cacheControl(CacheControl.noStore())
                .body(new AuthResponse("Inicio de sesión exitoso",
                        service.getCurrentUser(authenticated.getName())));
    }

    @Operation(summary = "Usuario autenticado", description = "Sin sesión activa se recibe 401")
    @GetMapping("/me")
    public ResponseEntity<UserResponse> currentUser(Authentication authenticated) {
        return ResponseEntity.ok().cacheControl(CacheControl.noStore())
                .body(service.getCurrentUser(authenticated.getName()));
    }
}
