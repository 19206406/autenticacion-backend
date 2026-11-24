package com.udea.tecnica.authentication_backend.service;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.udea.tecnica.authentication_backend.dto.RegisterRequest;
import com.udea.tecnica.authentication_backend.dto.UserResponse;
import com.udea.tecnica.authentication_backend.exception.PasswordsDoNotMatchException;
import com.udea.tecnica.authentication_backend.exception.DuplicateEmailException;
import com.udea.tecnica.authentication_backend.exception.UserNotFoundException;
import com.udea.tecnica.authentication_backend.model.User;
import com.udea.tecnica.authentication_backend.repository.UserRepository;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public AuthenticationServiceImpl(PasswordEncoder encoder, UserRepository repository) {
        this.encoder = encoder;
        this.repository = repository;
    }

    @Override
    public UserResponse register(RegisterRequest request) {
        if (!request.password().equals(request.confirmPassword())) 
            throw new PasswordsDoNotMatchException();
        
        if (request.password().getBytes(StandardCharsets.UTF_8).length > 72) 
            throw new IllegalArgumentException("La contraseña es demasiado larga para BCrypt");

        String email = request.email().strip().toLowerCase(Locale.ROOT);

        User newUser = new User(email, null, request.fullName().strip(),
                encoder.encode(request.password()));

        User registered = repository.saveIfNotExists(newUser)
                .orElseThrow(DuplicateEmailException::new);

        return UserResponse.from(registered);
    }

    @Override
    public UserResponse getCurrentUser(String email) {
        String normalized = email.strip().toLowerCase(Locale.ROOT);
        return repository.findByEmail(normalized)
                .map(UserResponse::from)
                .orElseThrow(UserNotFoundException::new);
    }
}
