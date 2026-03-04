package com.oceanviewresort.model;

import java.sql.Timestamp;

/**
 * User model class representing system users at Ocean View Resort.
 * Used for authentication and authorization of staff members.
 */
public class User {
    private int userId;
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String role; // admin, staff
    private Timestamp createdAt;

    // Role constants
    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_STAFF = "staff";

    /**
     * Default constructor
     */
    public User() {
        this.role = ROLE_STAFF;
    }

    /**
     * Constructor with essential fields
     */
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /**
     * Constructor with all fields except createdAt
     */
    public User(int userId, String username, String password, String email, String fullName, String role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
    }

    /**
     * Full constructor
     */
    public User(int userId, String username, String password, String email, 
                String fullName, String role, Timestamp createdAt) {
        this(userId, username, password, email, fullName, role);
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Check if user is an admin
     */
    public boolean isAdmin() {
        return ROLE_ADMIN.equals(this.role);
    }

    /**
     * Get display name (full name or username)
     */
    public String getDisplayName() {
        return (fullName != null && !fullName.isEmpty()) ? fullName : username;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}