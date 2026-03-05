package com.oceanviewresort.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oceanviewresort.model.User;
import com.oceanviewresort.service.UserService;
import com.oceanviewresort.validation.ValidationUtils;
import com.oceanviewresort.util.PaginationUtil;
import com.oceanviewresort.util.PaginationUtil.PaginationData;

/**
 * Controller for handling user management operations. Manages listing, adding,
 * editing, and deleting users.
 */
@WebServlet("/user-management")
public class UserManagementController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private UserService userService;

	/**
	 * Initializes the controller
	 */
	public void init() throws ServletException {
		userService = UserService.getInstance();
	}

	/**
	 * Handles GET requests for user management operations
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Check if user is logged in and has admin role
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("user") == null) {
			response.sendRedirect("auth?action=login");
			return;
		}

		String role = (String) session.getAttribute("role");
		if (!"admin".equals(role)) {
			request.setAttribute("error", "Access denied. Admin privileges required.");
			request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
			return;
		}

		String action = request.getParameter("action");

		if (action == null) {
			listUsers(request, response);
		} else if (action.equals("list")) {
			listUsers(request, response);
		} else if (action.equals("add")) {
			showAddUserForm(request, response);
		} else if (action.equals("edit")) {
			showEditUserForm(request, response);
		} else if (action.equals("view")) {
			viewUser(request, response);
		} else if (action.equals("delete")) {
			deleteUser(request, response);
		} else {
			listUsers(request, response);
		}
	}

	/**
	 * Handles POST requests for user management operations
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Check if user is logged in and has admin role
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("user") == null) {
			response.sendRedirect("auth?action=login");
			return;
		}

		String role = (String) session.getAttribute("role");
		if (!"admin".equals(role)) {
			request.setAttribute("error", "Access denied. Admin privileges required.");
			request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
			return;
		}

		String action = request.getParameter("action");

		if (action.equals("add")) {
			addUser(request, response);
		} else if (action.equals("update")) {
			updateUser(request, response);
		} else {
			listUsers(request, response);
		}
	}

	/**
	 * Lists all users or searches users based on search parameter with pagination
	 */
	private void listUsers(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// Parse pagination parameters
			int page = PaginationUtil.parsePageNumber(request.getParameter("page"));
			int pageSize = PaginationUtil.parsePageSize(request.getParameter("pageSize"));
			String searchTerm = request.getParameter("search");

			List<User> users;
			int totalItems;

			if (searchTerm != null && !searchTerm.trim().isEmpty()) {
				// Search users with pagination
				int offset = PaginationUtil.calculateOffset(page, pageSize);
				users = userService.searchUsersPaginated(searchTerm.trim(), offset, pageSize);
				totalItems = userService.getUserSearchCount(searchTerm.trim());
			} else {
				// Get all users with pagination
				int offset = PaginationUtil.calculateOffset(page, pageSize);
				users = userService.getUsersPaginated(offset, pageSize);
				totalItems = userService.getUserCount();
			}

			// Create pagination data
			PaginationData paginationData = PaginationUtil.createPaginationData(users, page, pageSize, totalItems);

			// Set attributes for JSP
			request.setAttribute("users", users);
			request.setAttribute("pagination", paginationData);
			request.setAttribute("searchTerm", searchTerm != null ? searchTerm.trim() : "");

			request.getRequestDispatcher("WEB-INF/view/user-management/listUsers.jsp").forward(request, response);
		} catch (SQLException e) {
			request.setAttribute("error", "Error loading users: " + e.getMessage());
			request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
		}
	}

	/**
	 * Shows the add user form
	 */
	private void showAddUserForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("WEB-INF/view/user-management/addUser.jsp").forward(request, response);
	}

	/**
	 * Shows the edit user form
	 */
	private void showEditUserForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userIdStr = request.getParameter("id");
		if (userIdStr == null || userIdStr.trim().isEmpty()) {
			request.setAttribute("error", "User ID is required");
			listUsers(request, response);
			return;
		}

		try {
			int userId = Integer.parseInt(userIdStr);
			User user = userService.getUserById(userId);

			if (user == null) {
				request.setAttribute("error", "User not found");
				listUsers(request, response);
				return;
			}

			request.setAttribute("user", user);
			request.getRequestDispatcher("WEB-INF/view/user-management/editUser.jsp").forward(request, response);
		} catch (NumberFormatException e) {
			request.setAttribute("error", "Invalid user ID");
			listUsers(request, response);
		} catch (SQLException e) {
			request.setAttribute("error", "Error loading user: " + e.getMessage());
			listUsers(request, response);
		}
	}

	/**
	 * Shows user details
	 */
	private void viewUser(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userIdStr = request.getParameter("id");
		if (userIdStr == null || userIdStr.trim().isEmpty()) {
			request.setAttribute("error", "User ID is required");
			listUsers(request, response);
			return;
		}

		try {
			int userId = Integer.parseInt(userIdStr);
			User user = userService.getUserById(userId);

			if (user == null) {
				request.setAttribute("error", "User not found");
				listUsers(request, response);
				return;
			}

			request.setAttribute("user", user);
			request.getRequestDispatcher("WEB-INF/view/user-management/viewUser.jsp").forward(request, response);
		} catch (NumberFormatException e) {
			request.setAttribute("error", "Invalid user ID");
			listUsers(request, response);
		} catch (SQLException e) {
			request.setAttribute("error", "Error loading user: " + e.getMessage());
			listUsers(request, response);
		}
	}

	/**
	 * Adds a new user
	 */
	private void addUser(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String confirmPassword = request.getParameter("confirmPassword");
		String role = request.getParameter("role");

		// Sanitize inputs
		username = ValidationUtils.sanitizeString(username);
		password = ValidationUtils.sanitizeString(password);
		confirmPassword = ValidationUtils.sanitizeString(confirmPassword);
		role = ValidationUtils.sanitizeString(role);

		// Set default role if empty
		if (role == null || role.trim().isEmpty()) {
			role = "staff";
		}

		// Validate input
		Map<String, String> validationErrors = ValidationUtils.validateUserWithRole(username, password, role);

		// Add custom validation for confirm password
		if (password != null && !password.equals(confirmPassword)) {
			validationErrors.put("confirmPassword", "Passwords do not match");
		}

		if (!validationErrors.isEmpty()) {
			// Validation failed - show errors
			request.setAttribute("fieldErrors", validationErrors);
			request.setAttribute("username", username);
			request.setAttribute("role", role);
			request.getRequestDispatcher("WEB-INF/view/user-management/addUser.jsp").forward(request, response);
			return;
		}

		try {
			boolean success = userService.registerUser(username, password, role);

			if (success) {
				request.setAttribute("success", "User added successfully");
				response.sendRedirect("user-management?action=list");
			} else {
				request.setAttribute("error", "Username already exists");
				request.setAttribute("username", username);
				request.setAttribute("role", role);
				request.getRequestDispatcher("WEB-INF/view/user-management/addUser.jsp").forward(request, response);
			}
		} catch (SQLException e) {
			request.setAttribute("error", "Error adding user: " + e.getMessage());
			request.setAttribute("username", username);
			request.setAttribute("role", role);
			request.getRequestDispatcher("WEB-INF/view/user-management/addUser.jsp").forward(request, response);
		}
	}

	/**
	 * Updates an existing user
	 */
	private void updateUser(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userIdStr = request.getParameter("userId");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String confirmPassword = request.getParameter("confirmPassword");
		String role = request.getParameter("role");

		// Sanitize inputs
		username = ValidationUtils.sanitizeString(username);
		password = ValidationUtils.sanitizeString(password);
		confirmPassword = ValidationUtils.sanitizeString(confirmPassword);
		role = ValidationUtils.sanitizeString(role);

		// Validate input
		if (userIdStr == null || userIdStr.trim().isEmpty()) {
			request.setAttribute("error", "User ID is required");
			listUsers(request, response);
			return;
		}

		// Set default role if empty
		if (role == null || role.trim().isEmpty()) {
			role = "staff";
		}

		// Validate username and role
		Map<String, String> validationErrors = ValidationUtils.validateUserWithRole(username,
				password != null && !password.trim().isEmpty() ? password : "dummy123", role);

		// Remove password validation if password is not being updated
		if (password == null || password.trim().isEmpty()) {
			validationErrors.remove("password");
		} else {
			// Add custom validation for confirm password
			if (!password.equals(confirmPassword)) {
				validationErrors.put("confirmPassword", "Passwords do not match");
			}
		}

		if (!validationErrors.isEmpty()) {
			// Validation failed - show errors
			request.setAttribute("fieldErrors", validationErrors);
			request.setAttribute("userId", userIdStr);
			request.setAttribute("username", username);
			request.setAttribute("role", role);
			request.getRequestDispatcher("WEB-INF/view/user-management/editUser.jsp").forward(request, response);
			return;
		}

		try {
			int userId = Integer.parseInt(userIdStr);
			User user = userService.getUserById(userId);

			if (user == null) {
				request.setAttribute("error", "User not found");
				listUsers(request, response);
				return;
			}

			// Update user fields
			user.setUsername(username);
			if (password != null && !password.trim().isEmpty()) {
				user.setPassword(password);
			}
			user.setRole(role);

			boolean success = userService.updateUser(user);

			if (success) {
				request.setAttribute("success", "User updated successfully");
				response.sendRedirect("user-management?action=list");
			} else {
				request.setAttribute("error", "Failed to update user");
				request.setAttribute("userId", userIdStr);
				request.setAttribute("username", username);
				request.setAttribute("role", role);
				request.getRequestDispatcher("WEB-INF/view/user-management/editUser.jsp").forward(request, response);
			}
		} catch (NumberFormatException e) {
			request.setAttribute("error", "Invalid user ID");
			listUsers(request, response);
		} catch (SQLException e) {
			request.setAttribute("error", "Error updating user: " + e.getMessage());
			request.setAttribute("userId", userIdStr);
			request.setAttribute("username", username);
			request.setAttribute("role", role);
			request.getRequestDispatcher("WEB-INF/view/user-management/editUser.jsp").forward(request, response);
		}
	}

	/**
	 * Deletes a user
	 */
	private void deleteUser(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userIdStr = request.getParameter("id");
		if (userIdStr == null || userIdStr.trim().isEmpty()) {
			request.setAttribute("error", "User ID is required");
			listUsers(request, response);
			return;
		}

		try {
			int userId = Integer.parseInt(userIdStr);

			// Check if user exists
			User user = userService.getUserById(userId);
			if (user == null) {
				request.setAttribute("error", "User not found");
				listUsers(request, response);
				return;
			}

			// Prevent deleting the current user
			HttpSession session = request.getSession(false);
			User currentUser = (User) session.getAttribute("user");
			if (currentUser != null && currentUser.getUserId() == userId) {
				request.setAttribute("error", "Cannot delete your own account");
				listUsers(request, response);
				return;
			}

			boolean success = userService.deleteUser(userId);

			if (success) {
				request.setAttribute("success", "User deleted successfully");
			} else {
				request.setAttribute("error", "Failed to delete user");
			}

			listUsers(request, response);
		} catch (NumberFormatException e) {
			request.setAttribute("error", "Invalid user ID");
			listUsers(request, response);
		} catch (SQLException e) {
			request.setAttribute("error", "Error deleting user: " + e.getMessage());
			listUsers(request, response);
		}
	}
}
