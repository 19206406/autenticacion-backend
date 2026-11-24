package com.udea.tecnica.autenticacion_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ManejadorGlobalErrores {
    @ExceptionHandler(CorreoDuplicadoException.class)
    public ResponseEntity<ErrorRespuesta> correoDuplicado(CorreoDuplicadoException ex) {
        return responder(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(ContrasenasNoCoincidenException.class)
    public ResponseEntity<ErrorRespuesta> contrasenasDistintas(ContrasenasNoCoincidenException ex) {
        return responder(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public ResponseEntity<ErrorRespuesta> usuarioAusente(UsuarioNoEncontradoException ex) {
        return responder(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorRespuesta> credencialesIncorrectas(AuthenticationException ex) {
        return responder(HttpStatus.UNAUTHORIZED, "Correo o contraseña inválidos");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorRespuesta> datosInvalidos(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .filter(m -> m != null && !m.isBlank())
                .findFirst()
                .orElse("Los datos no son válidos");
        return responder(HttpStatus.BAD_REQUEST, mensaje);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorRespuesta> jsonInvalido(HttpMessageNotReadableException ex) {
        return responder(HttpStatus.BAD_REQUEST, "El JSON enviado no es válido");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorRespuesta> argumentoInvalido(IllegalArgumentException ex) {
        return responder(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    private ResponseEntity<ErrorRespuesta> responder(HttpStatus estado, String mensaje) {
        return ResponseEntity.status(estado)
                .body(ErrorRespuesta.crear(estado.value(), mensaje));
    }
}
