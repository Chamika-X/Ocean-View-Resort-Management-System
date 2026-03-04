package com.oceanviewresort.service;

import java.sql.SQLException;
import java.util.List;

import com.oceanviewresort.dao.UserDAO;
import com.oceanviewresort.model.User;

/**
 * Service class for User-related operations at Ocean View Resort.
 * Implements Singleton pattern and provides business logic for user management.
 */
public class UserService {

    private static UserService instance;
    private UserDAO userDAO;

    /**
     * Private constructor for Singleton pattern
     */
    private UserService() {
        this.userDAO = new UserDAO();
    }

    /**
     * Gets the singleton instance of UserService
     * @return The UserService instance
     */
    public static UserService getInstance() {
        if (instance == null) {
            synchronized (UserService.class) {
                if (instance == null) {
                    instance = new UserService();
                }
            }
        }
        return instance;
    }

    /**
     * Authenticates a user by username and password
     * @param username The username
     * @param password The password
     * @return User object if authentication successful, null otherwise
     * @throws SQLException if a database error occurs
     */
    public User authenticate(String username, String password) throws SQLException {
        return userDAO.authenticate(username, password);
    }

    /**
     * Registers a new user (staff member)
     * @param user The user to register
     * @return true if registration successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean registerUser(User user) throws SQLException {
        // Check if username already exists
        if (userDAO.getUserByUsername(user.getUsername()) != null) {
            return false;
        }
        return userDAO.addUser(user);
    }

    /**
     * Registers a new user with basic info
     * @param username The username
     * @param password The password
     * @param email The email
     * @param fullName The full name
     * @param role The role (admin/staff)
     * @return true if registration successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean registerUser(String username, String password, String email, String fullName, String role) throws SQLException {
        // Check if username already exists
        if (userDAO.getUserByUsername(username) != null) {
            return false;
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setFullName(fullName);
        user.setRole(role != null ? role : User.ROLE_STAFF);
        
        return userDAO.addUser(user);
    }

    /**
     * Registers a new user with username, password and role
     * @param username The username
     * @param password The password
     * @param role The role (admin/staff)
     * @return true if registration successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean registerUser(String username, String password, String role) throws SQLException {
        return registerUser(username, password, null, username, role != null ? role : User.ROLE_STAFF);
    }

    /**
     * Registers a new user with just username and password (for tests)
     * @param username The username
     * @param password The password
     * @return true if registration successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean registerUser(String username, String password) throws SQLException {
        return registerUser(username, password, null, username, "user");
    }

    /**
     * Gets a user by ID
     * @param userId The user ID
     * @return User object if found, null otherwise
     * @throws SQLException if a database error occurs
     */
    public User getUserById(int userId) throws SQLException {
        return userDAO.getUserById(userId);
    }

    /**
     * Gets a user by username
     * @param username The username
     * @return User object if found, null otherwise
     * @throws SQLException if a database error occurs
     */
    public User getUserByUsername(String username) throws SQLException {
        return userDAO.getUserByUsername(username);
    }

    /**
     * Gets all users
     * @return List of all users
     * @throws SQLException if a database error occurs
     */
    public List<User> getAllUsers() throws SQLException {
        return userDAO.getAllUsers();
    }

    /**
     * Gets paginated users
     * @param offset The offset for pagination
     * @param limit The limit for pagination
     * @return List of users for the current page
     * @throws SQLException if a database error occurs
     */
    public List<User> getUsersPaginated(int offset, int limit) throws SQLException {
        return userDAO.getUsersPaginated(offset, limit);
    }

    /**
     * Gets the total count of users
     * @return Total number of users
     * @throws SQLException if a database error occurs
     */
    public int getUserCount() throws SQLException {
        return userDAO.getUserCount();
    }

    /**
     * Searches users with pagination
     * @param searchTerm The search term
     * @param offset The offset for pagination
     * @param limit The limit for pagination
     * @return List of matching users for the current page
     * @throws SQLException if a database error occurs
     */
    public List<User> searchUsersPaginated(String searchTerm, int offset, int limit) throws SQLException {
        return userDAO.searchUsersPaginated(searchTerm, offset, limit);
    }

    /**
     * Gets the count of users matching a search term
     * @param searchTerm The search term
     * @return Count of matching users
     * @throws SQLException if a database error occurs
     */
    public int getUserSearchCount(String searchTerm) throws SQLException {
        return userDAO.getUserSearchCount(searchTerm);
    }

    /**
     * Updates a user
     * @param user The user to update
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean updateUser(User user) throws SQLException {
        // Check if username already exists for a different user
        User existingUser = userDAO.getUserByUsername(user.getUsername());
        if (existingUser != null && existingUser.getUserId() != user.getUserId()) {
            return false;
        }
        return userDAO.updateUser(user);
    }

    /**
     * Updates a user's password
     * @param userId The user ID
     * @param currentPassword The current password for verification
     * @param newPassword The new password
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean changePassword(int userId, String currentPassword, String newPassword) throws SQLException {
        User user = userDAO.getUserById(userId);
        if (user == null || !user.getPassword().equals(currentPassword)) {
            return false;
        }
        return userDAO.updatePassword(userId, newPassword);
    }

    /**
     * Resets a user's password (admin function)
     * @param userId The user ID
     * @param newPassword The new password
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean resetPassword(int userId, String newPassword) throws SQLException {
        return userDAO.updatePassword(userId, newPassword);
    }

    /**
     * Deletes a user
     * @param userId The ID of the user to delete
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean deleteUser(int userId) throws SQLException {
        return userDAO.deleteUser(userId);
    }
}