package com.udea.tecnica.authentication_backend.service;

import java.nio.charset.StandardCharsets;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import com.udea.tecnica.authentication_backend.dto.LoginRequest;
import com.udea.tecnica.authentication_backend.dto.RegisterRequest;
import com.udea.tecnica.authentication_backend.dto.UserResponse;
import com.udea.tecnica.authentication_backend.exception.DuplicateEmailException;
import com.udea.tecnica.authentication_backend.exception.PasswordsDoNotMatchException;
import com.udea.tecnica.authentication_backend.exception.UserNotFoundException;
import com.udea.tecnica.authentication_backend.model.User;
import com.udea.tecnica.authentication_backend.repository.IUserRepository;
import com.udea.tecnica.authentication_backend.util.EmailNormalizer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthenticationService implements IAuthenticationService {
    private static final int BCRYPT_MAX_BYTES = 72;

    private final IUserRepository repository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticator;
    private final SecurityContextRepository contextRepository;

    public AuthenticationService(IUserRepository repository, PasswordEncoder encoder,
            AuthenticationManager authenticator, SecurityContextRepository contextRepository) {
        this.repository = repository;
        this.encoder = encoder;
        this.authenticator = authenticator;
        this.contextRepository = contextRepository;
    }

    @Override
    public UserResponse register(RegisterRequest request) {
        validatePasswords(request);

        User newUser = new User(request.fullName().strip(),
                EmailNormalizer.normalize(request.email()), encoder.encode(request.password()));

        return repository.saveIfNotExists(newUser)
                .map(UserResponse::from)
                .orElseThrow(DuplicateEmailException::new);
    }

    @Override
    public UserResponse login(LoginRequest credentials,
            HttpServletRequest request, HttpServletResponse response) {
        String email = EmailNormalizer.normalize(credentials.email());
        Authentication authenticated = authenticator.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(email, credentials.password()));

        startSession(authenticated, request, response);

        return repository.findByEmail(email)
                .map(UserResponse::from)
                .orElseThrow(UserNotFoundException::new);
    }

    private void validatePasswords(RegisterRequest request) {
        if (!request.password().equals(request.confirmPassword())) {
            throw new PasswordsDoNotMatchException();
        }
        if (request.password().getBytes(StandardCharsets.UTF_8).length > BCRYPT_MAX_BYTES) {
            throw new IllegalArgumentException("La contraseña es demasiado larga");
        }
    }

    private void startSession(Authentication authenticated,
            HttpServletRequest request, HttpServletResponse response) {
        if (request.getSession(false) != null) {
            request.changeSessionId();
        }
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authenticated);
        SecurityContextHolder.setContext(context);
        contextRepository.saveContext(context, request, response);
    }
}
