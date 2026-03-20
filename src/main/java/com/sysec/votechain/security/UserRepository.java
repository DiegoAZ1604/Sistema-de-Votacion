package com.sysec.votechain.security;

import com.sysec.votechain.model.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * In-memory user store. Replaced by PostgreSQL in Phase 4.
 * @author Diego
 */
@Component
public class UserRepository {

    private final Map<String, User> users = new HashMap<>();

    public UserRepository() {
        // Seed users for development — replaced by DB registration in Phase 4
        users.put("admin", new User("admin", "admin123", User.Role.ADMIN));
        users.put("voter1", new User("voter1", "pass123", User.Role.VOTER));
    }

    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(users.get(username));
    }

    public boolean existsByUsername(String username) {
        return users.containsKey(username);
    }

    public void save(User user) {
        users.put(user.getUsername(), user);
    }
}
