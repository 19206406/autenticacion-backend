package com.udea.tecnica.authentication_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos que el frontend envía para registrar una cuenta")
public record RegisterRequest(@NotBlank(message = "El nombre completo es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
    @Schema(example = "Ana Pérez") String fullName,

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato válido")
    @Schema(example = "ana@correo.com") String email,

    @NotBlank(message = "La contraseña es obligatoria")
    @Pattern(regexp = "(?s)^(?=.*[A-Z])(?=.*[0-9]).{8,}$",
             message = "La contraseña necesita mínimo 8 caracteres, una mayúscula y un número")
    @Schema(example = "Clave123", accessMode = Schema.AccessMode.WRITE_ONLY)
    String password,

    @NotBlank(message = "Debes confirmar la contraseña")
    @Schema(example = "Clave123", accessMode = Schema.AccessMode.WRITE_ONLY)
    String confirmPassword) {

}
