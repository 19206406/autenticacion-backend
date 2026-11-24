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

    public AutenticacionServiceImpl(PasswordEncoder encoder, UsuarioRepository repository) {
        this.encoder = encoder;
        this.repository = repository;
    }

    @Override
    public UsuarioResponse registrar(RegistroRequest solicitud) {
        if (!solicitud.password().equals(solicitud.confirmarPassword())) {
            throw new ContrasenasNoCoincidenException();
        }
        if (solicitud.password().getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new IllegalArgumentException("La contraseña es demasiado larga para BCrypt");
        }
        String correo = solicitud.correo().strip().toLowerCase(Locale.ROOT);
        Usuario nuevo = new Usuario(correo, null, solicitud.nombreCompleto().strip(),
                encoder.encode(solicitud.password()));
        Usuario registrado = repository.guardarSiNoExiste(nuevo)
                .orElseThrow(CorreoDuplicadoException::new);
        return UsuarioResponse.desde(registrado);
    }

    @Override
    public UsuarioResponse obtenerUsuarioActual(String correo) {
        String normalizado = correo.strip().toLowerCase(Locale.ROOT);
        return repository.buscarPorCorreo(normalizado)
                .map(UsuarioResponse::desde)
                .orElseThrow(UsuarioNoEncontradoException::new);
    }
}
