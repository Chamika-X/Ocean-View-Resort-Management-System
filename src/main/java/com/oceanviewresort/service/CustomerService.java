package com.oceanviewresort.service;

import java.sql.SQLException;
import java.util.List;

import com.oceanviewresort.dao.CustomerDAO;
import com.oceanviewresort.model.Customer;

/**
 * Service class for Customer-related operations. Provides business logic and
 * serves as an intermediary between controllers and DAO.
 */
public class CustomerService {

	private static CustomerService instance;
	private CustomerDAO customerDAO;

	/**
	 * Private constructor for Singleton pattern
	 */
	private CustomerService() {
		this.customerDAO = new CustomerDAO();
	}

	/**
	 * Gets the singleton instance of CustomerService
	 * 
	 * @return The CustomerService instance
	 */
	public static CustomerService getInstance() {
		if (instance == null) {
			synchronized (CustomerService.class) {
				if (instance == null) {
					instance = new CustomerService();
				}
			}
		}
		return instance;
	}

	/**
	 * Adds a new customer
	 * 
	 * @param customer The customer to add
	 * @return true if successful, false otherwise
	 * @throws SQLException if a database error occurs
	 */
	public boolean addCustomer(Customer customer) throws SQLException {
		// Check if account number already exists
		if (customerDAO.getCustomerByAccountNumber(customer.getAccountNumber()) != null) {
			return false;
		}

		return customerDAO.addCustomer(customer);
	}

	/**
	 * Gets a customer by ID
	 * 
	 * @param customerId The customer ID
	 * @return Customer object if found, null otherwise
	 * @throws SQLException if a database error occurs
	 */
	public Customer getCustomerById(int customerId) throws SQLException {
		return customerDAO.getCustomerById(customerId);
	}

	/**
	 * Gets a customer by account number
	 * 
	 * @param accountNumber The account number
	 * @return Customer object if found, null otherwise
	 * @throws SQLException if a database error occurs
	 */
	public Customer getCustomerByAccountNumber(String accountNumber) throws SQLException {
		return customerDAO.getCustomerByAccountNumber(accountNumber);
	}

	/**
	 * Gets all customers
	 * 
	 * @return List of all customers
	 * @throws SQLException if a database error occurs
	 */
	public List<Customer> getAllCustomers() throws SQLException {
		return customerDAO.getAllCustomers();
	}

	/**
	 * Searches customers by name, telephone, or account number
	 * 
	 * @param searchTerm The search term to look for
	 * @return List of matching customers
	 * @throws SQLException if a database error occurs
	 */
	public List<Customer> searchCustomers(String searchTerm) throws SQLException {
		return customerDAO.searchCustomers(searchTerm);
	}

	/**
	 * Gets paginated customers
	 * 
	 * @param offset The offset for pagination
	 * @param limit  The limit for pagination
	 * @return List of customers for the current page
	 * @throws SQLException if a database error occurs
	 */
	public List<Customer> getCustomersPaginated(int offset, int limit) throws SQLException {
		return customerDAO.getCustomersPaginated(offset, limit);
	}

	/**
	 * Searches customers with pagination
	 * 
	 * @param searchTerm The search term to look for
	 * @param offset     The offset for pagination
	 * @param limit      The limit for pagination
	 * @return List of matching customers for the current page
	 * @throws SQLException if a database error occurs
	 */
	public List<Customer> searchCustomersPaginated(String searchTerm, int offset, int limit) throws SQLException {
		return customerDAO.searchCustomersPaginated(searchTerm, offset, limit);
	}

	/**
	 * Gets the total count of customers
	 * 
	 * @return Total number of customers
	 * @throws SQLException if a database error occurs
	 */
	public int getCustomerCount() throws SQLException {
		return customerDAO.getCustomerCount();
	}

	/**
	 * Gets the count of customers matching a search term
	 * 
	 * @param searchTerm The search term to look for
	 * @return Count of matching customers
	 * @throws SQLException if a database error occurs
	 */
	public int getCustomerSearchCount(String searchTerm) throws SQLException {
		return customerDAO.getCustomerSearchCount(searchTerm);
	}

	/**
	 * Updates a customer
	 * 
	 * @param customer The customer to update
	 * @return true if successful, false otherwise
	 * @throws SQLException if a database error occurs
	 */
	public boolean updateCustomer(Customer customer) throws SQLException {
		// Check if account number already exists for a different customer
		Customer existingCustomer = customerDAO.getCustomerByAccountNumber(customer.getAccountNumber());
		if (existingCustomer != null && existingCustomer.getCustomerId() != customer.getCustomerId()) {
			return false;
		}

		return customerDAO.updateCustomer(customer);
	}

	/**
	 * Deletes a customer
	 * 
	 * @param customerId The ID of the customer to delete
	 * @return true if successful, false otherwise
	 * @throws SQLException if a database error occurs
	 */
	public boolean deleteCustomer(int customerId) throws SQLException {
		return customerDAO.deleteCustomer(customerId);
	}

	/**
	 * Generates a unique account number for a new customer
	 * 
	 * @return A unique account number
	 * @throws SQLException if a database error occurs
	 */
	public String generateAccountNumber() throws SQLException {
		// Get all customers to find the highest account number
		List<Customer> customers = getAllCustomers();

		// If no customers exist, start with CUST001
		if (customers.isEmpty()) {
			return "CUST001";
		}

		// Find the highest account number
		int highestNumber = 0;
		for (Customer customer : customers) {
			String accountNumber = customer.getAccountNumber();
			if (accountNumber.startsWith("CUST")) {
				try {
					int number = Integer.parseInt(accountNumber.substring(4));
					if (number > highestNumber) {
						highestNumber = number;
					}
				} catch (NumberFormatException e) {
					// Ignore non-standard account numbers
				}
			}
		}

		// Generate the next account number
		return String.format("CUST%03d", highestNumber + 1);
	}
}