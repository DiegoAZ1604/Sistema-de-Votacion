package com.sysec.votechain.model;

import jakarta.persistence.*;

/**
 * JPA entity for persisting users.
 * @author Diego
 */
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private User.Role role;

    public UserEntity() {}

    public UserEntity(String username, String password, User.Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public User.Role getRole() { return role; }
}
