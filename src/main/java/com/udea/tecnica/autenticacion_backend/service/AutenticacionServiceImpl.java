package com.udea.tecnica.autenticacion_backend.service;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.udea.tecnica.autenticacion_backend.dto.RegistroRequest;
import com.udea.tecnica.autenticacion_backend.dto.UsuarioResponse;
import com.udea.tecnica.autenticacion_backend.exception.ContrasenasNoCoincidenException;
import com.udea.tecnica.autenticacion_backend.exception.CorreoDuplicadoException;
import com.udea.tecnica.autenticacion_backend.exception.UsuarioNoEncontradoException;
import com.udea.tecnica.autenticacion_backend.model.Usuario;
import com.udea.tecnica.autenticacion_backend.repository.UsuarioRepository;

@Service
public class AutenticacionServiceImpl implements AutenticacionService {
    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;

    
}
