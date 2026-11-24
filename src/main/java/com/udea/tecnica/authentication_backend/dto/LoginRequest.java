package com.udea.tecnica.authentication_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Credenciales de inicio de sesión")
public record LoginRequest(
    @Schema(example = "ana@correo.com") String email,
    @Schema(example = "Clave123", accessMode = Schema.AccessMode.WRITE_ONLY) String password
) {}
