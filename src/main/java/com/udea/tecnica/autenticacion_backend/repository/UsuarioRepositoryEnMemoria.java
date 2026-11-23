package com.udea.tecnica.autenticacion_backend.repository;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;
import com.udea.tecnica.autenticacion_backend.model.Usuario;

@Repository
public class UsuarioRepositoryEnMemoria implements UsuarioRepository{
    private final Map<String, Usuario> datos = new ConcurrentHashMap<>();
    private final AtomicLong secuencia = new AtomicLong(0);

    @Override 
    public Optional<Usuario> buscarPorCorreo(String correoNormalizado){
        return Optional.ofNullable( datos.get(correoNormalizado));
    }

    @Override
    public Optional<Usuario> guardarSiNoExiste(Usuario usuario){
        String clave = usuario.getCorreo().strip().toLowerCase(Locale.ROOT);

        Usuario existente = datos.get(clave);
        if (existente != null) {
            return Optional.of(existente);
        }

        Usuario conId = new Usuario(
                clave,
                secuencia.incrementAndGet(),
                usuario.getNombreCompleto(),
                usuario.getPasswordHash()
        );
        Usuario guardado = datos.putIfAbsent(clave, conId);
        if (guardado == null) {
            return Optional.of(conId); // El correo estaba libre y se guardó
        } else {
            return Optional.of(guardado); // Otro hilo lo guardó primero
        }
    }
}
