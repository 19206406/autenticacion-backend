package com.udea.tecnica.authentication_backend.repository;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import com.udea.tecnica.authentication_backend.model.User;

@Repository
public class InMemoryUserRepository implements UserRepository{
    private final Map<String, User> data = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    @Override
    public Optional<User> findByEmail(String normalizedEmail){
        return Optional.ofNullable( data.get(normalizedEmail));
    }

    @Override
    public Optional<User> saveIfNotExists(User user){
        String key = user.getEmail().strip().toLowerCase(Locale.ROOT);

        User existing = data.get(key);
        if (existing != null) {
            return Optional.of(existing);
        }

        User withId = new User(
                key,
                sequence.incrementAndGet(),
                user.getFullName(),
                user.getPasswordHash()
        );
        User saved = data.putIfAbsent(key, withId);
        if (saved == null) {
            return Optional.of(withId);
        } else {
            return Optional.of(saved);
        }
    }
}
