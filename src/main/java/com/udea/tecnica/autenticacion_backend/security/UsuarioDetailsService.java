package com.udea.tecnica.autenticacion_backend.security;

import java.util.Locale;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.udea.tecnica.autenticacion_backend.repository.UsuarioRepository;

@Service 
public class UsuarioDetailsService implements UserDetailsService{
    private final UsuarioRepository repository;

    public UsuarioDetailsService(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String correo) {
        String normalizado = correo.strip().toLowerCase(Locale.ROOT);
        return repository.buscarPorCorreo(normalizado)
                .map(usuario -> User.withUsername(usuario.getCorreo())
                        .password(usuario.getPasswordHash())
                        .roles("USER")
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }
}
