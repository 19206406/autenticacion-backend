package com.udea.tecnica.autenticacion_backend.dto;

import com.udea.tecnica.autenticacion_backend.model.Usuario;

public record UsuarioResponse(Long id, String nombreCompleto, String correo) {
    public static UsuarioResponse desde(Usuario usuario) {
        return new UsuarioResponse(usuario.getId(), usuario.getNombreCompleto(), usuario.getCorreo());
    }
}
