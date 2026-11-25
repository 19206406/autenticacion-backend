package com.udea.tecnica.authentication_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Credenciales de inicio de sesión")
public record LoginRequest(
    @NotBlank(message = "El correo es obligatorio")
    @Schema(example = "ana@correo.com") String email,

    @NotBlank(message = "La contraseña es obligatoria")
    @Schema(example = "Clave123", accessMode = Schema.AccessMode.WRITE_ONLY) String password
) {}
