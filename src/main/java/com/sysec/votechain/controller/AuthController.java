package com.sysec.votechain.controller;

import com.sysec.votechain.model.User;
import com.sysec.votechain.security.JwtUtil;
import com.sysec.votechain.security.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

/**
 * Handles user registration and login.
 * @author Diego
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    // Register a new voter — POST /api/auth/register
    // Body: { "username": "voter2", "password": "pass123" }
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        if (username == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "username and password are required"
            ));
        }

        if (userRepository.existsByUsername(username)) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Username already taken"
            ));
        }

        // New registrations are always VOTER — ADMIN is seeded manually
        userRepository.save(new User(username, password, User.Role.VOTER));

        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Voter registered successfully"
        ));
    }

    // Login — POST /api/auth/login
    // Body: { "username": "voter1", "password": "pass123" }
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty() || !userOpt.get().getPassword().equals(password)) {
            return ResponseEntity.status(401).body(Map.of(
                "success", false,
                "message", "Invalid credentials"
            ));
        }

        User user = userOpt.get();
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

        return ResponseEntity.ok(Map.of(
            "success", true,
            "token", token,
            "role", user.getRole().name()
        ));
    }
}
