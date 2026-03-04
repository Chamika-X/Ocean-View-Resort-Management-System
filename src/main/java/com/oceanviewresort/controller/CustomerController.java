package com.oceanviewresort.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oceanviewresort.model.Customer;
import com.oceanviewresort.service.CustomerService;
import com.oceanviewresort.service.ActivityService;
import com.oceanviewresort.validation.ValidationUtils;
import com.oceanviewresort.util.PaginationUtil;
import com.oceanviewresort.util.PaginationUtil.PaginationData;

/**
 * Controller for handling customer-related requests. Manages customer listing,
 * creation, editing, and deletion.
 */
@WebServlet("/customer")
public class CustomerController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private CustomerService customerService;
	private ActivityService activityService;

	/**
	 * Initializes the controller
	 */
	public void init() throws ServletException {
		customerService = CustomerService.getInstance();
		activityService = new ActivityService();

		// Initialize activity system
		try {
			activityService.initialize();
		} catch (Exception e) {
			// Log error but don't fail initialization
			System.err.println("Failed to initialize activity system: " + e.getMessage());
		}
	}

	/**
	 * Handles GET requests for customer management
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Check if user is logged in
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("user") == null) {
			response.sendRedirect("auth?action=login");
			return;
		}

		String action = request.getParameter("action");
		if (action == null || action.equals("list")) {
			listCustomers(request, response);
		} else if (action.equals("add")) {
			showAddForm(request, response);
		} else if (action.equals("edit")) {
			showEditForm(request, response);
		} else if (action.equals("delete")) {
			deleteCustomer(request, response);
		} else if (action.equals("view")) {
			viewCustomer(request, response);
		}
	}

	/**
	 * Handles POST requests for customer management
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Check if user is logged in
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("user") == null) {
			response.sendRedirect("auth?action=login");
			return;
		}

		String action = request.getParameter("action");
		if (action.equals("add")) {
			addCustomer(request, response);
		} else if (action.equals("update")) {
			updateCustomer(request, response);
		}
	}

	/**
	 * Lists all customers or searches customers based on search parameter with
	 * pagination
	 */
	private void listCustomers(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// Parse pagination parameters
			int page = PaginationUtil.parsePageNumber(request.getParameter("page"));
			int pageSize = PaginationUtil.parsePageSize(request.getParameter("pageSize"));
			String searchTerm = request.getParameter("search");

			List<Customer> customerList;
			int totalItems;

			if (searchTerm != null && !searchTerm.trim().isEmpty()) {
				// Search customers with pagination
				int offset = PaginationUtil.calculateOffset(page, pageSize);
				customerList = customerService.searchCustomersPaginated(searchTerm.trim(), offset, pageSize);
				totalItems = customerService.getCustomerSearchCount(searchTerm.trim());
			} else {
				// Get all customers with pagination
				int offset = PaginationUtil.calculateOffset(page, pageSize);
				customerList = customerService.getCustomersPaginated(offset, pageSize);
				totalItems = customerService.getCustomerCount();
			}

			// Create pagination data
			PaginationData paginationData = PaginationUtil.createPaginationData(customerList, page, pageSize,
					totalItems);

			// Set attributes for JSP
			request.setAttribute("customers", customerList);
			request.setAttribute("pagination", paginationData);
			request.setAttribute("searchTerm", searchTerm != null ? searchTerm.trim() : "");

		} catch (SQLException e) {
			request.setAttribute("errorMessage", e.getMessage());
			request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
			return;
		}

		request.getRequestDispatcher("WEB-INF/view/customer/listCustomers.jsp").forward(request, response);
	}

	/**
	 * Shows the form for adding a new customer
	 */
	private void showAddForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// Generate a new account number
			String accountNumber = customerService.generateAccountNumber();
			request.setAttribute("accountNumber", accountNumber);
		} catch (SQLException e) {
			request.setAttribute("errorMessage", e.getMessage());
			request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
			return;
		}

		request.getRequestDispatcher("WEB-INF/view/customer/addCustomer.jsp").forward(request, response);
	}

	/**
	 * Processes the addition of a new customer
	 */
	private void addCustomer(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String accountNumber = request.getParameter("accountNumber");
		String name = request.getParameter("name");
		String address = request.getParameter("address");
		String telephone = request.getParameter("telephone");

		// Sanitize inputs
		accountNumber = ValidationUtils.sanitizeString(accountNumber);
		name = ValidationUtils.sanitizeString(name);
		address = ValidationUtils.sanitizeString(address);
		telephone = ValidationUtils.sanitizeString(telephone);

		// Validate input
		Map<String, String> validationErrors = ValidationUtils.validateCustomer(accountNumber, name, address,
				telephone);

		if (!validationErrors.isEmpty()) {
			// Validation failed - show errors
			request.setAttribute("fieldErrors", validationErrors);
			request.setAttribute("accountNumber", accountNumber);
			request.setAttribute("name", name);
			request.setAttribute("address", address);
			request.setAttribute("telephone", telephone);
			request.getRequestDispatcher("WEB-INF/view/customer/addCustomer.jsp").forward(request, response);
			return;
		}

		Customer customer = new Customer(accountNumber, name, address, telephone);

		try {
			boolean success = customerService.addCustomer(customer);

			if (success) {
				// Log customer addition activity
				try {
					HttpSession session = request.getSession(false);
					String username = session != null ? (String) session.getAttribute("username") : "system";
					activityService.logCustomerAdded(customer.getCustomerId(), customer.getName(), username);
				} catch (Exception e) {
					// Log error but don't fail customer addition
					System.err.println("Failed to log customer addition activity: " + e.getMessage());
				}

				// Customer added successfully
				response.sendRedirect("customer?action=list");
			} else {
				// Customer addition failed (likely account number already exists)
				request.setAttribute("error", "Account number already exists");
				request.setAttribute("accountNumber", accountNumber);
				request.setAttribute("name", name);
				request.setAttribute("address", address);
				request.setAttribute("telephone", telephone);
				request.getRequestDispatcher("WEB-INF/view/customer/addCustomer.jsp").forward(request, response);
			}
		} catch (SQLException e) {
			request.setAttribute("error", "Database error: " + e.getMessage());
			request.setAttribute("accountNumber", accountNumber);
			request.setAttribute("name", name);
			request.setAttribute("address", address);
			request.setAttribute("telephone", telephone);
			request.getRequestDispatcher("WEB-INF/view/customer/addCustomer.jsp").forward(request, response);
		}
	}

	/**
	 * Shows the form for editing a customer
	 */
	private void showEditForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String customerIdStr = request.getParameter("id");
		if (customerIdStr == null || customerIdStr.trim().isEmpty()) {
			request.setAttribute("errorMessage", "Customer ID is required");
			request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
			return;
		}

		try {
			int customerId = Integer.parseInt(customerIdStr);
			Customer customer = customerService.getCustomerById(customerId);

			if (customer == null) {
				request.setAttribute("errorMessage", "Customer not found");
				request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
				return;
			}

			request.setAttribute("customer", customer);
			request.getRequestDispatcher("WEB-INF/view/customer/editCustomer.jsp").forward(request, response);
		} catch (NumberFormatException e) {
			request.setAttribute("errorMessage", "Invalid customer ID");
			request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
		} catch (SQLException e) {
			request.setAttribute("errorMessage", "Database error: " + e.getMessage());
			request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
		}
	}

	/**
	 * Processes the update of a customer
	 */
	private void updateCustomer(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int customerId = Integer.parseInt(request.getParameter("id"));
		String accountNumber = request.getParameter("accountNumber");
		String name = request.getParameter("name");
		String address = request.getParameter("address");
		String telephone = request.getParameter("telephone");

		// Sanitize inputs
		accountNumber = ValidationUtils.sanitizeString(accountNumber);
		name = ValidationUtils.sanitizeString(name);
		address = ValidationUtils.sanitizeString(address);
		telephone = ValidationUtils.sanitizeString(telephone);

		// Validate input
		Map<String, String> validationErrors = ValidationUtils.validateCustomer(accountNumber, name, address,
				telephone);

		if (!validationErrors.isEmpty()) {
			// Validation failed - show errors
			request.setAttribute("fieldErrors", validationErrors);
			request.setAttribute("customerId", customerId);
			request.setAttribute("accountNumber", accountNumber);
			request.setAttribute("name", name);
			request.setAttribute("address", address);
			request.setAttribute("telephone", telephone);
			request.getRequestDispatcher("WEB-INF/view/customer/editCustomer.jsp").forward(request, response);
			return;
		}

		Customer customer = new Customer(customerId, accountNumber, name, address, telephone);

		try {
			boolean success = customerService.updateCustomer(customer);

			if (success) {
				// Customer updated successfully
				response.sendRedirect("customer?action=list");
			} else {
				// Customer update failed (likely account number already exists for another
				// customer)
				request.setAttribute("error", "Account number already exists for another customer");
				request.setAttribute("customerId", customerId);
				request.setAttribute("accountNumber", accountNumber);
				request.setAttribute("name", name);
				request.setAttribute("address", address);
				request.setAttribute("telephone", telephone);
				request.getRequestDispatcher("WEB-INF/view/customer/editCustomer.jsp").forward(request, response);
			}
		} catch (SQLException e) {
			request.setAttribute("error", "Database error: " + e.getMessage());
			request.setAttribute("customerId", customerId);
			request.setAttribute("accountNumber", accountNumber);
			request.setAttribute("name", name);
			request.setAttribute("address", address);
			request.setAttribute("telephone", telephone);
			request.getRequestDispatcher("WEB-INF/view/customer/editCustomer.jsp").forward(request, response);
		}
	}

	/**
	 * Processes the deletion of a customer
	 */
	private void deleteCustomer(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int customerId = Integer.parseInt(request.getParameter("id"));
		try {
			customerService.deleteCustomer(customerId);
			response.sendRedirect("customer?action=list");
		} catch (SQLException e) {
			request.setAttribute("errorMessage", e.getMessage());
			request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
		}
	}

	/**
	 * Shows the details of a customer
	 */
	private void viewCustomer(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int customerId = Integer.parseInt(request.getParameter("id"));
		try {
			Customer customer = customerService.getCustomerById(customerId);
			request.setAttribute("customer", customer);
			request.getRequestDispatcher("WEB-INF/view/customer/viewCustomer.jsp").forward(request, response);
		} catch (SQLException e) {
			request.setAttribute("errorMessage", e.getMessage());
			request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
		}
	}
}