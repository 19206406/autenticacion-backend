package com.udea.tecnica.authentication_backend.repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;
import com.udea.tecnica.authentication_backend.model.User;

/** Almacenamiento en memoria: los usuarios se pierden al reiniciar la aplicación. */
@Repository
public class InMemoryUserRepository implements UserRepository {
    private final Map<String, User> usersByEmail = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    @Override
    public Optional<User> findByEmail(String normalizedEmail) {
        return Optional.ofNullable(usersByEmail.get(normalizedEmail));
    }

    @Override
    public Optional<User> findById(Long id) {
        return usersByEmail.values().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        return usersByEmail.values().stream()
                .sorted(Comparator.comparing(User::getId))
                .toList();
    }

    @Override
    public synchronized Optional<User> saveIfNotExists(User user) {
        if (usersByEmail.containsKey(user.getEmail())) {
            return Optional.empty();
        }
        User saved = user.withId(sequence.incrementAndGet());
        usersByEmail.put(saved.getEmail(), saved);
        return Optional.of(saved);
    }
}
