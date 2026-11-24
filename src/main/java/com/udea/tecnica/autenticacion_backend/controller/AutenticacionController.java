package com.udea.tecnica.autenticacion_backend.controller;

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
import com.udea.tecnica.autenticacion_backend.dto.AuthResponse;
import com.udea.tecnica.autenticacion_backend.dto.CsrfResponse;
import com.udea.tecnica.autenticacion_backend.dto.LoginRequest;
import com.udea.tecnica.autenticacion_backend.dto.RegistroRequest;
import com.udea.tecnica.autenticacion_backend.dto.UsuarioResponse;
import com.udea.tecnica.autenticacion_backend.service.AutenticacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Registro, login, sesión y logout")
public class AutenticacionController {
    private final AutenticacionService service;
    private final AuthenticationManager autenticador;
    private final SecurityContextRepository repositorioContexto;

    public AutenticacionController(AutenticacionService service,
            AuthenticationManager autenticador,
            SecurityContextRepository repositorioContexto) {
        this.service = service;
        this.autenticador = autenticador;
        this.repositorioContexto = repositorioContexto;
    }

    @Operation(summary = "Obtener token CSRF", description = "Solicitar antes de enviar registro, login o logout")
    @GetMapping("/csrf")
    public CsrfResponse csrf(CsrfToken token) {
        return new CsrfResponse(token.getToken(), token.getHeaderName());
    }

    @Operation(summary = "Registrar usuario", description = "La contraseña se guarda con BCrypt; no inicia sesión")
    @PostMapping("/registro")
    public ResponseEntity<AuthResponse> registrar(@Valid @RequestBody RegistroRequest solicitud) {
        UsuarioResponse creado = service.registrar(solicitud);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponse("Usuario registrado correctamente", creado));
    }

    @Operation(summary = "Iniciar sesión", description = "Autentica un JSON y persiste la sesión en una cookie")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest solicitud,
            HttpServletRequest request, HttpServletResponse response) {
        if (solicitud.correo() == null || solicitud.correo().isBlank()
                || solicitud.password() == null || solicitud.password().isBlank()) {
            throw new BadCredentialsException("Credenciales inválidas");
        }
        String correo = solicitud.correo().strip().toLowerCase(Locale.ROOT);
        Authentication autenticado = autenticador.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(correo, solicitud.password()));

        // Se rota el identificador de sesión para mitigar fijación de sesión.
        HttpSession sesionExistente = request.getSession(false);
        if (sesionExistente != null) {
            request.changeSessionId();
        }

        SecurityContext contexto = SecurityContextHolder.createEmptyContext();
        contexto.setAuthentication(autenticado);
        SecurityContextHolder.setContext(contexto);
        repositorioContexto.saveContext(contexto, request, response);

        return ResponseEntity.ok().cacheControl(CacheControl.noStore())
                .body(new AuthResponse("Inicio de sesión exitoso",
                        service.obtenerUsuarioActual(autenticado.getName())));
    }

    @Operation(summary = "Usuario autenticado", description = "Sin sesión activa se recibe 401")
    @GetMapping("/yo")
    public ResponseEntity<UsuarioResponse> usuarioActual(Authentication autenticado) {
        return ResponseEntity.ok().cacheControl(CacheControl.noStore())
                .body(service.obtenerUsuarioActual(autenticado.getName()));
    }
}
