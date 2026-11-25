package com.udea.tecnica.authentication_backend.repository;

import java.util.List;
import java.util.Optional;
import com.udea.tecnica.authentication_backend.model.User;

public interface UserRepository {

    Optional<User> findByEmail(String normalizedEmail);

    Optional<User> findById(Long id);

    List<User> findAll();

    Optional<User> saveIfNotExists(User user);
}
