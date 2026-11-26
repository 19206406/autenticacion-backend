package com.udea.tecnica.authentication_backend.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.udea.tecnica.authentication_backend.dto.UserResponse;
import com.udea.tecnica.authentication_backend.exception.UserNotFoundException;
import com.udea.tecnica.authentication_backend.repository.IUserRepository;

@Service
public class UserService implements IUserService {
    private final IUserRepository repository;

    public UserService(IUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserResponse findById(Long id) {
        return repository.findById(id)
                .map(UserResponse::from)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public List<UserResponse> findAll() {
        return repository.findAll().stream()
                .map(UserResponse::from)
                .toList();
    }
}
