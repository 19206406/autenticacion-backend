package com.udea.tecnica.autenticacion_backend.service;

import com.udea.tecnica.autenticacion_backend.dto.RegistroRequest;
import com.udea.tecnica.autenticacion_backend.dto.UsuarioResponse;
public interface AutenticacionService {
    UsuarioResponse registrar(RegistroRequest solicitud);
    UsuarioResponse obtenerUsuarioActual(String correo);
}
