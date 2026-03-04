package com.oceanviewresort.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oceanviewresort.model.Product;
import com.oceanviewresort.service.ProductService;
import com.oceanviewresort.service.ActivityService;
import com.oceanviewresort.validation.ValidationUtils;
import com.oceanviewresort.util.PaginationUtil;
import com.oceanviewresort.util.PaginationUtil.PaginationData;

@WebServlet("/product")
public class ProductController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ProductService productService;
	private ActivityService activityService;

	public void init() throws ServletException {
		productService = ProductService.getInstance();
		activityService = new ActivityService();

		// Initialize activity system
		try {
			activityService.initialize();
		} catch (Exception e) {
			// Log error but don't fail initialization
			System.err.println("Failed to initialize activity system: " + e.getMessage());
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");
		if (action == null || action.isEmpty()) {
			action = "list";
		}

		switch (action) {
		case "list":
			listProducts(request, response);
			break;
		case "add":
			showAddForm(request, response);
			break;
		case "view":
			viewProduct(request, response);
			break;
		case "edit":
			showEditForm(request, response);
			break;
		case "delete":
			deleteProduct(request, response);
			break;
		default:
			listProducts(request, response);
			break;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");
		if (action.equals("add")) {
			addProduct(request, response);
		} else if (action.equals("update")) {
			updateProduct(request, response);
		}
	}

	private void listProducts(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			// Parse pagination parameters
			int page = PaginationUtil.parsePageNumber(request.getParameter("page"));
			int pageSize = PaginationUtil.parsePageSize(request.getParameter("pageSize"));
			String searchTerm = request.getParameter("search");

			List<Product> productList;
			int totalItems;

			if (searchTerm != null && !searchTerm.trim().isEmpty()) {
				// Search products with pagination
				int offset = PaginationUtil.calculateOffset(page, pageSize);
				productList = productService.searchProductsPaginated(searchTerm.trim(), offset, pageSize);
				totalItems = productService.getProductSearchCount(searchTerm.trim());
			} else {
				// Get all products with pagination
				int offset = PaginationUtil.calculateOffset(page, pageSize);
				productList = productService.getProductsPaginated(offset, pageSize);
				totalItems = productService.getProductCount();
			}

			// Create pagination data
			PaginationData paginationData = PaginationUtil.createPaginationData(productList, page, pageSize,
					totalItems);

			// Set attributes for JSP
			request.setAttribute("products", productList);
			request.setAttribute("pagination", paginationData);
			request.setAttribute("searchTerm", searchTerm != null ? searchTerm.trim() : "");

		} catch (SQLException e) {
			request.setAttribute("errorMessage", e.getMessage());
			request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
			return;
		}

		request.getRequestDispatcher("WEB-INF/view/product/listProducts.jsp").forward(request, response);
	}

	private void showAddForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("WEB-INF/view/product/addProduct.jsp").forward(request, response);
	}

	private void addProduct(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String name = request.getParameter("name");
		String priceStr = request.getParameter("price");
		String quantityStr = request.getParameter("quantity");
		String description = request.getParameter("description");

		// Sanitize inputs
		name = ValidationUtils.sanitizeString(name);
		description = ValidationUtils.sanitizeString(description);

		// Validate input
		Map<String, String> validationErrors = ValidationUtils.validateProduct(name, description, priceStr, quantityStr);

		if (!validationErrors.isEmpty()) {
			// Validation failed - show errors
			request.setAttribute("fieldErrors", validationErrors);
			request.setAttribute("name", name);
			request.setAttribute("price", priceStr);
			request.setAttribute("quantity", quantityStr);
			request.setAttribute("description", description);
			request.getRequestDispatcher("WEB-INF/view/product/addProduct.jsp").forward(request, response);
			return;
		}

		// Parse price and quantity
		double price = Double.parseDouble(priceStr);
		int quantity = Integer.parseInt(quantityStr);

		Product product = new Product();
		product.setName(name);
		product.setPrice(price);
		product.setQuantity(quantity);
		product.setDescription(description);

		try {
			productService.addProduct(product);

			// Log product addition activity
			try {
				HttpSession session = request.getSession(false);
				String username = session != null ? (String) session.getAttribute("username") : "system";
				activityService.logProductAdded(product.getProductId(), product.getName(), username);
			} catch (Exception e) {
				// Log error but don't fail product addition
				System.err.println("Failed to log product addition activity: " + e.getMessage());
			}

			response.sendRedirect("product?action=list");
		} catch (SQLException e) {
			request.setAttribute("error", "Database error: " + e.getMessage());
			request.setAttribute("name", name);
			request.setAttribute("price", priceStr);
			request.setAttribute("quantity", quantityStr);
			request.setAttribute("description", description);
			request.getRequestDispatcher("WEB-INF/view/product/addProduct.jsp").forward(request, response);
		}
	}

	private void viewProduct(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String productIdStr = request.getParameter("id");
		if (productIdStr == null || productIdStr.trim().isEmpty()) {
			request.setAttribute("errorMessage", "Product ID is required");
			request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
			return;
		}

		try {
			int productId = Integer.parseInt(productIdStr);
			Product product = productService.getProductById(productId);

			if (product == null) {
				request.setAttribute("errorMessage", "Product not found");
				request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
				return;
			}

			request.setAttribute("product", product);
			request.getRequestDispatcher("WEB-INF/view/product/viewProduct.jsp").forward(request, response);
		} catch (NumberFormatException e) {
			request.setAttribute("errorMessage", "Invalid product ID");
			request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
		} catch (SQLException e) {
			request.setAttribute("errorMessage", "Database error: " + e.getMessage());
			request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
		}
	}

	private void showEditForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String productIdStr = request.getParameter("id");
		if (productIdStr == null || productIdStr.trim().isEmpty()) {
			request.setAttribute("errorMessage", "Product ID is required");
			request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
			return;
		}

		try {
			int productId = Integer.parseInt(productIdStr);
			Product product = productService.getProductById(productId);

			if (product == null) {
				request.setAttribute("errorMessage", "Product not found");
				request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
				return;
			}

			request.setAttribute("product", product);
			request.getRequestDispatcher("WEB-INF/view/product/editProduct.jsp").forward(request, response);
		} catch (NumberFormatException e) {
			request.setAttribute("errorMessage", "Invalid product ID");
			request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
		} catch (SQLException e) {
			request.setAttribute("errorMessage", "Database error: " + e.getMessage());
			request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
		}
	}

	private void updateProduct(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String productIdStr = request.getParameter("id");
		if (productIdStr == null || productIdStr.trim().isEmpty()) {
			request.setAttribute("errorMessage", "Product ID is required");
			request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
			return;
		}

		int productId;
		try {
			productId = Integer.parseInt(productIdStr);
		} catch (NumberFormatException e) {
			request.setAttribute("errorMessage", "Invalid product ID");
			request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
			return;
		}
		String name = request.getParameter("name");
		String priceStr = request.getParameter("price");
		String quantityStr = request.getParameter("quantity");
		String description = request.getParameter("description");

		// Sanitize inputs
		name = ValidationUtils.sanitizeString(name);
		description = ValidationUtils.sanitizeString(description);

		// Validate input
		Map<String, String> validationErrors = ValidationUtils.validateProduct(name, description, priceStr, quantityStr);

		if (!validationErrors.isEmpty()) {
			// Validation failed - show errors
			request.setAttribute("fieldErrors", validationErrors);
			request.setAttribute("productId", productId);
			request.setAttribute("name", name);
			request.setAttribute("price", priceStr);
			request.setAttribute("quantity", quantityStr);
			request.setAttribute("description", description);
			request.getRequestDispatcher("WEB-INF/view/product/editProduct.jsp").forward(request, response);
			return;
		}

		// Parse price and quantity
		double price = Double.parseDouble(priceStr);
		int quantity = Integer.parseInt(quantityStr);

		Product product = new Product(productId, name, description, price, quantity);

		try {
			productService.updateProduct(product);
			response.sendRedirect("product?action=list");
		} catch (SQLException e) {
			request.setAttribute("error", "Database error: " + e.getMessage());
			request.setAttribute("productId", productId);
			request.setAttribute("name", name);
			request.setAttribute("price", priceStr);
			request.setAttribute("quantity", quantityStr);
			request.setAttribute("description", description);
			request.getRequestDispatcher("WEB-INF/view/product/editProduct.jsp").forward(request, response);
		}
	}

	private void deleteProduct(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String productIdStr = request.getParameter("id");
		if (productIdStr == null || productIdStr.trim().isEmpty()) {
			request.setAttribute("errorMessage", "Product ID is required");
			request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
			return;
		}

		try {
			int productId = Integer.parseInt(productIdStr);
			productService.deleteProduct(productId);
			response.sendRedirect("product?action=list");
		} catch (NumberFormatException e) {
			request.setAttribute("errorMessage", "Invalid product ID");
			request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
		} catch (SQLException e) {
			request.setAttribute("errorMessage", "Database error: " + e.getMessage());
			request.getRequestDispatcher("WEB-INF/view/error.jsp").forward(request, response);
		}
	}
}
