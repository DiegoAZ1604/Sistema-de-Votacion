package com.sysec.votechain.security;

import com.sysec.votechain.model.User;
import com.sysec.votechain.model.UserEntity;
import com.sysec.votechain.repository.UserJpaRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * User store backed by PostgreSQL via JPA.
 * @author Diego
 */
@Component
public class UserRepository {

    private final UserJpaRepository jpa;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserRepository(UserJpaRepository jpa) {
        this.jpa = jpa;
        seedDefaultUsers();
    }

    // Seed admin and a test voter on first run — skipped if they already exist
    private void seedDefaultUsers() {
        if (!jpa.existsByUsername("admin")) {
            jpa.save(new UserEntity("admin", encoder.encode("admin123"), User.Role.ADMIN));
        }
        if (!jpa.existsByUsername("voter1")) {
            jpa.save(new UserEntity("voter1", encoder.encode("pass123"), User.Role.VOTER));
        }
    }

    public Optional<UserEntity> findByUsername(String username) {
        return jpa.findByUsername(username);
    }

    public boolean existsByUsername(String username) {
        return jpa.existsByUsername(username);
    }

    public void save(String username, String rawPassword, User.Role role) {
        jpa.save(new UserEntity(username, encoder.encode(rawPassword), role));
    }

    public boolean checkPassword(UserEntity user, String rawPassword) {
        return encoder.matches(rawPassword, user.getPassword());
    }
}
