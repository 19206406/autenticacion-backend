package com.udea.tecnica.authentication_backend.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import com.udea.tecnica.authentication_backend.model.User;
import com.udea.tecnica.authentication_backend.repository.jpa.UserJpaRepository;

@Repository
public class UserRepository implements IUserRepository {
    private final UserJpaRepository jpaRepository;

    public UserRepository(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<User> findByEmail(String normalizedEmail) {
        return jpaRepository.findByEmail(normalizedEmail);
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<User> findAll() {
        return jpaRepository.findAll(Sort.by("id"));
    }

    @Override
    public Optional<User> saveIfNotExists(User user) {
        if (jpaRepository.existsByEmail(user.getEmail())) {
            return Optional.empty();
        }
        try {
            return Optional.of(jpaRepository.saveAndFlush(user));
        } catch (DataIntegrityViolationException duplicateEmail) {
            return Optional.empty();
        }
    }
}
