package com.oceanviewresort.controller;

import com.oceanviewresort.model.Bill;
import com.oceanviewresort.model.BillItem;
import com.oceanviewresort.model.Customer;
import com.oceanviewresort.model.Product;
import com.oceanviewresort.service.BillService;
import com.oceanviewresort.service.CustomerService;
import com.oceanviewresort.service.ProductService;
import com.oceanviewresort.service.ActivityService;
import com.oceanviewresort.service.EmailService;
import com.oceanviewresort.validation.ValidationUtils;
import com.oceanviewresort.util.PaginationUtil;
import com.oceanviewresort.util.PaginationUtil.PaginationData;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/bill")
public class BillController extends HttpServlet {

	private BillService billService;
	private CustomerService customerService;
	private ProductService productService;
	private ActivityService activityService;
	private EmailService emailService;

	@Override
	public void init() throws ServletException {
		billService = new BillService();
		customerService = CustomerService.getInstance();
		productService = ProductService.getInstance();
		activityService = new ActivityService();
		emailService = new EmailService();

		// Initialize activity system
		try {
			activityService.initialize();
		} catch (Exception e) {
			// Log error but don't fail initialization
			System.err.println("Failed to initialize activity system: " + e.getMessage());
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");

		if (action == null) {
			action = "list";
		}

		switch (action) {
		case "list":
			listBills(request, response);
			break;
		case "view":
			viewBill(request, response);
			break;
		case "create":
			showCreateBillForm(request, response);
			break;
		case "print":
			printBill(request, response);
			break;
		case "email":
			sendBillEmail(request, response);
			break;
		case "delete":
			deleteBill(request, response);
			break;
		default:
			listBills(request, response);
			break;
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");

		switch (action) {
		case "create":
			createBill(request, response);
			break;
		case "update":
			updateBill(request, response);
			break;
		default:
			response.sendRedirect("bill?action=list");
			break;
		}
	}

	private void listBills(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			// Parse pagination parameters
			int page = PaginationUtil.parsePageNumber(request.getParameter("page"));
			int pageSize = PaginationUtil.parsePageSize(request.getParameter("pageSize"));
			String searchTerm = request.getParameter("search");

			List<Bill> bills;
			int totalItems;

			if (searchTerm != null && !searchTerm.trim().isEmpty()) {
				// Search bills with pagination
				int offset = PaginationUtil.calculateOffset(page, pageSize);
				bills = billService.searchBillsPaginated(searchTerm.trim(), offset, pageSize);
				totalItems = billService.getBillSearchCount(searchTerm.trim());
			} else {
				// Get all bills with pagination
				int offset = PaginationUtil.calculateOffset(page, pageSize);
				bills = billService.getBillsPaginated(offset, pageSize);
				totalItems = billService.getBillCount();
			}

			// Create pagination data
			PaginationData paginationData = PaginationUtil.createPaginationData(bills, page, pageSize, totalItems);

			// Set attributes for JSP
			request.setAttribute("bills", bills);
			request.setAttribute("pagination", paginationData);
			request.setAttribute("searchTerm", searchTerm != null ? searchTerm.trim() : "");

		} catch (Exception e) {
			request.setAttribute("error", "Error loading bills: " + e.getMessage());
		}

		request.getRequestDispatcher("/WEB-INF/view/bill/listBills.jsp").forward(request, response);
	}

	private void viewBill(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String billIdStr = request.getParameter("id");
		if (billIdStr != null && !billIdStr.trim().isEmpty()) {
			try {
				int billId = Integer.parseInt(billIdStr);
				Bill bill = billService.getBillById(billId);

				if (bill != null) {
					request.setAttribute("bill", bill);
					request.getRequestDispatcher("/WEB-INF/view/bill/viewBill.jsp").forward(request, response);
				} else {
					request.setAttribute("error", "Bill not found");
					response.sendRedirect("bill?action=list");
				}
			} catch (NumberFormatException e) {
				request.setAttribute("error", "Invalid bill ID");
				response.sendRedirect("bill?action=list");
			} catch (Exception e) {
				request.setAttribute("error", "Error loading bill: " + e.getMessage());
				response.sendRedirect("bill?action=list");
			}
		} else {
			response.sendRedirect("bill?action=list");
		}
	}

	private void showCreateBillForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			// Get all customers for dropdown
			List<Customer> customers = customerService.getAllCustomers();
			request.setAttribute("customers", customers);

			// Get all products for dropdown
			List<Product> products = productService.getAllProducts();
			request.setAttribute("products", products);

		} catch (Exception e) {
			request.setAttribute("error", "Error loading form data: " + e.getMessage());
		}

		request.getRequestDispatcher("/WEB-INF/view/bill/createBill.jsp").forward(request, response);
	}

	private void createBill(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			// Get and sanitize customer ID
			String customerIdStr = ValidationUtils.sanitizeString(request.getParameter("customerId"));

			// Get items from request
			String[] productIds = request.getParameterValues("productId");
			String[] quantities = request.getParameterValues("quantity");
			String[] unitPrices = request.getParameterValues("unitPrice");

			// Get bill-level discount
			String billDiscountStr = request.getParameter("billDiscount");

			// Handle customer ID - allow empty for walk-in customers
			Integer customerId = null;
			if (customerIdStr != null && !customerIdStr.trim().isEmpty()) {
				try {
					customerId = Integer.parseInt(customerIdStr);
				} catch (NumberFormatException e) {
					request.setAttribute("error", "Invalid customer selected");
					showCreateBillForm(request, response);
					return;
				}
			}
			// If customerId is null, this will be treated as a walk-in customer

			if (productIds == null || quantities == null || unitPrices == null || productIds.length == 0) {
				request.setAttribute("error", "Please add at least one item to the invoice");
				showCreateBillForm(request, response);
				return;
			}

			List<BillItem> items = new ArrayList<>();
			Map<String, String> validationErrors = new java.util.HashMap<>();

			// First pass: validate all items and check stock
			for (int i = 0; i < productIds.length; i++) {
				if (productIds[i] != null && !productIds[i].trim().isEmpty() && quantities[i] != null
						&& !quantities[i].trim().isEmpty() && unitPrices[i] != null
						&& !unitPrices[i].trim().isEmpty()) {

					// Validate bill item
					Map<String, String> itemErrors = ValidationUtils.validateBillItem(productIds[i], quantities[i],
							unitPrices[i]);

					if (!itemErrors.isEmpty()) {
						validationErrors.putAll(itemErrors);
					} else {
						try {
							int productId = Integer.parseInt(productIds[i]);
							int quantity = Integer.parseInt(quantities[i]);
							double unitPrice = Double.parseDouble(unitPrices[i]);

							Product product = productService.getProductById(productId);
							if (product != null) {
								// Check stock availability
								if (product.getQuantity() < quantity) {
									validationErrors.put("stock_" + i, "Insufficient stock for " + product.getName()
											+ ". Available: " + product.getQuantity() + ", Requested: " + quantity);
								} else {
									BillItem item = new BillItem(productId, product.getName(), quantity,
											BigDecimal.valueOf(unitPrice));
									item.calculateSubtotal();
									items.add(item);
								}
							} else {
								validationErrors.put("productId", "Invalid product selected");
							}
						} catch (NumberFormatException e) {
							validationErrors.put("quantity", "Invalid quantity or price value");
						}
					}
				}
			}

			if (!validationErrors.isEmpty()) {
				request.setAttribute("fieldErrors", validationErrors);
				showCreateBillForm(request, response);
				return;
			}

			if (items.isEmpty()) {
				request.setAttribute("error", "Please add at least one valid item to the invoice");
				showCreateBillForm(request, response);
				return;
			}

			// Validate bill discount
			double billDiscount = 0.0;
			if (billDiscountStr != null && !billDiscountStr.trim().isEmpty()) {
				try {
					billDiscount = Double.parseDouble(billDiscountStr);
					if (billDiscount < 0 || billDiscount > 100) {
						validationErrors.put("billDiscount", "Bill discount must be between 0 and 100 percent");
					}
				} catch (NumberFormatException e) {
					validationErrors.put("billDiscount", "Please enter a valid discount percentage");
				}
			}

			if (!validationErrors.isEmpty()) {
				request.setAttribute("fieldErrors", validationErrors);
				showCreateBillForm(request, response);
				return;
			}

			// Create bill (use -1 for walk-in customers)
			int customerIdForBill = (customerId != null) ? customerId : -1;
			Bill bill = billService.createBillFromItems(customerIdForBill, items, BigDecimal.valueOf(billDiscount));

			if (bill != null) {
				// Update stock quantities
				boolean stockUpdateSuccess = true;
				String stockUpdateError = "";
				
				try {
					for (BillItem item : items) {
						boolean updated = productService.updateStockQuantity(item.getProductId(), item.getQuantity());
						if (!updated) {
							stockUpdateSuccess = false;
							stockUpdateError = "Failed to update stock for product ID: " + item.getProductId();
							break;
						}
					}
				} catch (Exception e) {
					stockUpdateSuccess = false;
					stockUpdateError = "Error updating stock quantities: " + e.getMessage();
					System.err.println("Failed to update stock quantities: " + e.getMessage());
				}
				
				if (!stockUpdateSuccess) {
					// If stock update failed, we should ideally rollback the bill creation
					// For now, we'll log the error and show a warning to the user
					System.err.println("WARNING: Bill created but stock update failed: " + stockUpdateError);
					request.setAttribute("warning", "Bill created successfully but there was an issue updating product stock. Please check stock levels manually.");
				}

				// Log bill creation activity
				try {
					HttpSession session = request.getSession(false);
					String username = session != null ? (String) session.getAttribute("username") : "system";
					activityService.logBillCreated(bill.getBillId(), bill.getCustomerName(), username);
				} catch (Exception e) {
					// Log error but don't fail bill creation
					System.err.println("Failed to log bill creation activity: " + e.getMessage());
				}

				request.setAttribute("success", "Bill created successfully");
				response.sendRedirect("bill?action=view&id=" + bill.getBillId());
			} else {
				request.setAttribute("error", "Failed to create bill");
				showCreateBillForm(request, response);
			}

		} catch (Exception e) {
			request.setAttribute("error", "An error occurred: " + e.getMessage());
			showCreateBillForm(request, response);
		}
	}

	private void updateBill(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String billIdStr = request.getParameter("billId");
		String status = request.getParameter("status");

		// Sanitize inputs
		billIdStr = ValidationUtils.sanitizeString(billIdStr);
		status = ValidationUtils.sanitizeString(status);

		// Validate input
		Map<String, String> validationErrors = ValidationUtils.validateBillStatusUpdate(billIdStr, status);

		if (!validationErrors.isEmpty()) {
			// Validation failed - show errors
			request.setAttribute("fieldErrors", validationErrors);
			request.setAttribute("billId", billIdStr);
			request.setAttribute("status", status);

			// Redirect back to the bill view page with errors
			if (billIdStr != null) {
				try {
					int billId = Integer.parseInt(billIdStr);
					response.sendRedirect("bill?action=view&id=" + billId);
				} catch (NumberFormatException e) {
					response.sendRedirect("bill?action=list");
				}
			} else {
				response.sendRedirect("bill?action=list");
			}
			return;
		}

		try {
			int billId = Integer.parseInt(billIdStr);
			
			// Get current bill status before update
			Bill currentBill = billService.getBillById(billId);
			String previousStatus = currentBill != null ? currentBill.getStatus() : null;
			
			boolean updated = billService.updateBillStatus(billId, status);

			if (updated) {
				// Handle stock restoration if bill is cancelled
				if ("Cancelled".equalsIgnoreCase(status) && currentBill != null && currentBill.getItems() != null) {
					try {
						boolean stockRestoreSuccess = true;
						String stockRestoreError = "";
						
						for (BillItem item : currentBill.getItems()) {
							boolean restored = productService.restoreStockQuantity(item.getProductId(), item.getQuantity());
							if (!restored) {
								stockRestoreSuccess = false;
								stockRestoreError = "Failed to restore stock for product ID: " + item.getProductId();
								break;
							}
						}
						
						if (!stockRestoreSuccess) {
							System.err.println("WARNING: Bill cancelled but stock restoration failed: " + stockRestoreError);
							request.setAttribute("warning", "Bill status updated successfully but there was an issue restoring product stock. Please check stock levels manually.");
						}
					} catch (Exception e) {
						System.err.println("Error restoring stock quantities: " + e.getMessage());
						request.setAttribute("warning", "Bill status updated successfully but there was an issue restoring product stock. Please check stock levels manually.");
					}
				}
				
				request.setAttribute("success", "Bill status updated successfully");
			} else {
				request.setAttribute("error", "Failed to update bill status");
			}

			response.sendRedirect("bill?action=view&id=" + billId);

		} catch (NumberFormatException e) {
			request.setAttribute("error", "Invalid bill ID");
			response.sendRedirect("bill?action=list");
		} catch (Exception e) {
			request.setAttribute("error", "Error updating bill: " + e.getMessage());
			response.sendRedirect("bill?action=list");
		}
	}

	private void deleteBill(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String billIdStr = request.getParameter("id");

		if (billIdStr != null) {
			try {
				int billId = Integer.parseInt(billIdStr);
				
				// Get bill details before deletion to restore stock
				Bill bill = billService.getBillById(billId);
				List<BillItem> billItems = null;
				
				if (bill != null) {
					billItems = bill.getItems();
				}
				
				boolean deleted = billService.deleteBill(billId);

				if (deleted) {
					// Restore stock quantities if bill had items
					if (billItems != null && !billItems.isEmpty()) {
						try {
							boolean stockRestoreSuccess = true;
							String stockRestoreError = "";
							
							for (BillItem item : billItems) {
								boolean restored = productService.restoreStockQuantity(item.getProductId(), item.getQuantity());
								if (!restored) {
									stockRestoreSuccess = false;
									stockRestoreError = "Failed to restore stock for product ID: " + item.getProductId();
									break;
								}
							}
							
							if (!stockRestoreSuccess) {
								System.err.println("WARNING: Bill deleted but stock restoration failed: " + stockRestoreError);
								request.setAttribute("warning", "Bill deleted successfully but there was an issue restoring product stock. Please check stock levels manually.");
							}
						} catch (Exception e) {
							System.err.println("Error restoring stock quantities: " + e.getMessage());
							request.setAttribute("warning", "Bill deleted successfully but there was an issue restoring product stock. Please check stock levels manually.");
						}
					}
					
					request.setAttribute("success", "Bill deleted successfully");
				} else {
					request.setAttribute("error", "Failed to delete bill");
				}

			} catch (NumberFormatException e) {
				request.setAttribute("error", "Invalid bill ID");
			} catch (Exception e) {
				request.setAttribute("error", "Error deleting bill: " + e.getMessage());
			}
		}

		response.sendRedirect("bill?action=list");
	}

	private void printBill(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String billIdStr = request.getParameter("id");

		if (billIdStr != null) {
			try {
				int billId = Integer.parseInt(billIdStr);
				Bill bill = billService.getBillById(billId);

				if (bill != null) {
					request.setAttribute("bill", bill);
					request.getRequestDispatcher("/WEB-INF/view/bill/printBill.jsp").forward(request, response);
				} else {
					request.setAttribute("error", "Bill not found");
					response.sendRedirect("bill?action=list");
				}
			} catch (NumberFormatException e) {
				request.setAttribute("error", "Invalid bill ID");
				response.sendRedirect("bill?action=list");
			} catch (Exception e) {
				request.setAttribute("error", "Error loading bill for printing: " + e.getMessage());
				response.sendRedirect("bill?action=list");
			}
		} else {
			response.sendRedirect("bill?action=list");
		}
	}

	private void sendBillEmail(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String billIdStr = request.getParameter("id");
		String recipientEmail = request.getParameter("email");

		// Validate email parameter
		if (recipientEmail == null || recipientEmail.trim().isEmpty()) {
			request.setAttribute("error", "Email address is required");
			response.sendRedirect("bill?action=view&id=" + billIdStr);
			return;
		}

		// Basic email validation
		if (!recipientEmail.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
			request.setAttribute("error", "Please enter a valid email address");
			response.sendRedirect("bill?action=view&id=" + billIdStr);
			return;
		}

		if (billIdStr != null) {
			try {
				int billId = Integer.parseInt(billIdStr);
				Bill bill = billService.getBillById(billId);

				if (bill != null) {
					// Handle both regular customers and walk-in customers
					boolean emailSent;
					
					if (bill.getCustomerId() == -1) {
						// Walk-in customer - send email using bill information only
						emailSent = emailService.sendBillEmailForWalkIn(bill, recipientEmail.trim());
					} else {
						// Regular customer - get customer details and send email
						Customer customer = customerService.getCustomerById(bill.getCustomerId());
						
						if (customer != null) {
							emailSent = emailService.sendBillEmail(bill, customer, recipientEmail.trim());
						} else {
							request.setAttribute("error", "Customer information not found");
							return;
						}
					}
					
					if (emailSent) {
						// Log email activity
						try {
							HttpSession session = request.getSession(false);
							String username = session != null ? (String) session.getAttribute("username") : "system";
							activityService.logBillEmailSent(bill.getBillId(), bill.getCustomerName(), username, recipientEmail);
						} catch (Exception e) {
							// Log error but don't fail email sending
							System.err.println("Failed to log email activity: " + e.getMessage());
						}
						
						request.setAttribute("success", "Bill sent successfully to " + recipientEmail);
					} else {
						request.setAttribute("error", "Failed to send email. Please check your email configuration.");
					}
				} else {
					request.setAttribute("error", "Bill not found");
				}
			} catch (NumberFormatException e) {
				request.setAttribute("error", "Invalid bill ID");
			} catch (Exception e) {
				request.setAttribute("error", "Error sending email: " + e.getMessage());
			}
		}

		response.sendRedirect("bill?action=view&id=" + billIdStr);
	}
}