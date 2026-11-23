package com.udea.tecnica.autenticacion_backend.service;

import com.udea.tecnica.autenticacion_backend.dto.RegistroRequest;
import com.udea.tecnica.autenticacion_backend.dto.UsuarioResponse;
public interface AutenticacionService {
    UsuarioRequest registro(RegistroRequest solicitud);
    UsuarioRequest obtenerUsuarioActual( String correo);
}
