package com.sysec.votechain.model;

/**
 * Represents an authenticated user in the system.
 * @author Diego
 */
public class User {

    public enum Role {
        ADMIN, VOTER
    }

    private final String username;
    private final String password;
    private final Role role;

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }
}
