package model;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;

/**
 * Complete User class for authentication
 * Links to Person and stores login credentials
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    // Properties
    private String userId;
    private String username;
    private String passwordHash;
    private Role role;
    private Person person;
    private boolean isActive;
    private LocalDateTime lastLogin;
    private LocalDateTime createdDate;

    /**
     * Complete constructor
     */
    public User(String username, String password, Role role, Person person) {
        this.userId = generateUserId();
        this.username = username;
        this.passwordHash = hashPassword(password);
        this.role = role;
        this.person = person;
        this.isActive = true;
        this.lastLogin = null;
        this.createdDate = LocalDateTime.now();
    }

    /**
     * Default constructor
     */
    public User() {
        this.userId = generateUserId();
        this.username = "";
        this.passwordHash = "";
        this.role = Role.PATIENT;
        this.person = new Person();
        this.isActive = true;
        this.lastLogin = null;
        this.createdDate = LocalDateTime.now();
    }

    /**
     * Generate unique user ID
     */
    private String generateUserId() {
        return "USR-" + java.util.UUID.randomUUID().toString();
    }

    /**
     * Hash password using SHA-256
     */
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            // Fallback to simple encoding if SHA-256 not available
            return Base64.getEncoder().encodeToString(password.getBytes());
        }
    }

    /**
     * Verify password
     */
    public boolean verifyPassword(String password) {
        String hashedInput = hashPassword(password);
        return this.passwordHash.equals(hashedInput);
    }

    /**
     * Change password
     */
    public void changePassword(String oldPassword, String newPassword) throws Exception {
        if (!verifyPassword(oldPassword)) {
            throw new Exception("Current password is incorrect");
        }

        if (newPassword.length() < 6) {
            throw new Exception("New password must be at least 6 characters");
        }

        this.passwordHash = hashPassword(newPassword);
    }

    /**
     * Update last login timestamp
     */
    public void recordLogin() {
        this.lastLogin = LocalDateTime.now();
    }

    // Getters
    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public Role getRole() {
        return role;
    }

    public Person getPerson() {
        return person;
    }

    public boolean isActive() {
        return isActive;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    // Setters
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    /**
     * Deactivate user account
     */
    public void deactivate() {
        this.isActive = false;
    }

    /**
     * Activate user account
     */
    public void activate() {
        this.isActive = true;
    }

    /**
     * Check if username is valid format
     */
    public static boolean isUsernameValid(String username) {
        if (username == null || username.isEmpty()) {
            return false;
        }
        // Username: 4-20 characters, letters, numbers, underscore only
        return username.matches("^[a-zA-Z0-9_]{4,20}$");
    }

    /**
     * Check if password meets requirements
     */
    public static boolean isPasswordValid(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        // Password: minimum 6 characters
        return password.length() >= 6;
    }

    @Override
    public String toString() {
        return username + " (" + role.getDisplayName() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        User user = (User) obj;
        return userId.equals(user.userId);
    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }
}