package com.oceanviewresort.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oceanviewresort.model.User;
import com.oceanviewresort.service.UserService;
import com.oceanviewresort.service.ActivityService;
import com.oceanviewresort.validation.ValidationUtils;

/**
 * Controller for handling authentication at Ocean View Resort.
 * Manages login, logout, and user session management.
 */
@WebServlet("/auth")
public class AuthController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserService userService;
    private ActivityService activityService;

    /**
     * Initializes the controller
     */
    public void init() throws ServletException {
        userService = UserService.getInstance();
        activityService = new ActivityService();

        try {
            activityService.initialize();
        } catch (Exception e) {
            System.err.println("Failed to initialize activity system: " + e.getMessage());
        }
    }

    /**
     * Handles GET requests for login, logout
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null || action.equals("login")) {
            showLoginForm(request, response);
        } else if (action.equals("logout")) {
            logout(request, response);
        } else if (action.equals("profile")) {
            showProfile(request, response);
        } else {
            showLoginForm(request, response);
        }
    }

    /**
     * Handles POST requests for login and profile updates
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action.equals("login")) {
            login(request, response);
        } else if (action.equals("updateProfile")) {
            updateProfile(request, response);
        } else if (action.equals("changePassword")) {
            changePassword(request, response);
        }
    }

    /**
     * Shows the login form
     */
    private void showLoginForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // If already logged in, redirect to dashboard
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            response.sendRedirect("dashboard");
            return;
        }
        request.getRequestDispatcher("WEB-INF/view/auth/login.jsp").forward(request, response);
    }

    /**
     * Shows the user profile
     */
    private void showProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("auth?action=login");
            return;
        }
        request.getRequestDispatcher("WEB-INF/view/auth/profile.jsp").forward(request, response);
    }

    /**
     * Processes login request
     */
    private void login(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Sanitize inputs
        username = ValidationUtils.sanitizeString(username);
        password = ValidationUtils.sanitizeString(password);

        // Validate login inputs
        Map<String, String> validationErrors = ValidationUtils.validateLogin(username, password);

        if (!validationErrors.isEmpty()) {
            request.setAttribute("fieldErrors", validationErrors);
            request.setAttribute("username", username);
            request.getRequestDispatcher("WEB-INF/view/auth/login.jsp").forward(request, response);
            return;
        }

        try {
            User user = userService.authenticate(username, password);

            if (user != null) {
                // Authentication successful
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("username", user.getUsername());
                session.setAttribute("fullName", user.getDisplayName());
                session.setAttribute("role", user.getRole());
                session.setAttribute("userId", user.getUserId());
                session.setMaxInactiveInterval(30 * 60); // 30 minutes timeout

                // Log login activity
                try {
                    activityService.logUserLogin(user.getUsername());
                } catch (SQLException e) {
                    System.err.println("Failed to log login activity: " + e.getMessage());
                }

                response.sendRedirect("dashboard");
            } else {
                // Authentication failed
                request.setAttribute("error", "Invalid username or password");
                request.setAttribute("username", username);
                request.getRequestDispatcher("WEB-INF/view/auth/login.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("WEB-INF/view/auth/login.jsp").forward(request, response);
        }
    }

    /**
     * Processes logout request
     */
    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String username = (String) session.getAttribute("username");
            
            // Log logout activity
            if (username != null) {
                try {
                    activityService.logUserLogout(username);
                } catch (SQLException e) {
                    System.err.println("Failed to log logout activity: " + e.getMessage());
                }
            }

            session.invalidate();
        }
        response.sendRedirect("auth?action=login");
    }

    /**
     * Updates user profile
     */
    private void updateProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("auth?action=login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        String email = ValidationUtils.sanitizeString(request.getParameter("email"));
        String fullName = ValidationUtils.sanitizeString(request.getParameter("fullName"));

        try {
            currentUser.setEmail(email);
            currentUser.setFullName(fullName);

            boolean success = userService.updateUser(currentUser);
            if (success) {
                session.setAttribute("user", currentUser);
                session.setAttribute("fullName", currentUser.getDisplayName());
                request.setAttribute("success", "Profile updated successfully");
            } else {
                request.setAttribute("error", "Failed to update profile");
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Database error: " + e.getMessage());
        }

        request.getRequestDispatcher("WEB-INF/view/auth/profile.jsp").forward(request, response);
    }

    /**
     * Changes user password
     */
    private void changePassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("auth?action=login");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        // Validate
        if (!ValidationUtils.isNotEmpty(currentPassword) || !ValidationUtils.isNotEmpty(newPassword)) {
            request.setAttribute("error", "All password fields are required");
            request.getRequestDispatcher("WEB-INF/view/auth/profile.jsp").forward(request, response);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "New passwords do not match");
            request.getRequestDispatcher("WEB-INF/view/auth/profile.jsp").forward(request, response);
            return;
        }

        if (!ValidationUtils.hasMinLength(newPassword, 6)) {
            request.setAttribute("error", "New password must be at least 6 characters");
            request.getRequestDispatcher("WEB-INF/view/auth/profile.jsp").forward(request, response);
            return;
        }

        try {
            boolean success = userService.changePassword(currentUser.getUserId(), currentPassword, newPassword);
            if (success) {
                request.setAttribute("success", "Password changed successfully");
            } else {
                request.setAttribute("error", "Current password is incorrect");
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Database error: " + e.getMessage());
        }

        request.getRequestDispatcher("WEB-INF/view/auth/profile.jsp").forward(request, response);
    }
}