package com.udea.tecnica.authentication_backend.repository;

import java.util.Optional;
import com.udea.tecnica.authentication_backend.model.User;

public interface UserRepository {

    Optional<User> findByEmail (String email);
    Optional<User> saveIfNotExists (User user);
}
