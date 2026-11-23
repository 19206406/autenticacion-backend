package com.udea.tecnica.autenticacion_backend.repository;

import java.util.Optional;
import com.udea.tecnica.autenticacion_backend.model.Usuario;

public interface UsuarioRepository {

    Optional<Usuario> buscarPorCorreo (String correo);
    Optional<Usuario> guardarSiNoExiste (Usuario usuario);
}
