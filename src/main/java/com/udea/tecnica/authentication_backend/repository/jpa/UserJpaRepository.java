package com.udea.tecnica.authentication_backend.repository.jpa;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.udea.tecnica.authentication_backend.model.User;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
