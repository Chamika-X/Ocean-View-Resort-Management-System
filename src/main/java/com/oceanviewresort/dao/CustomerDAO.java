package com.oceanviewresort.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.oceanviewresort.model.Customer;

/**
 * Data Access Object for Customer entity. Handles all database operations
 * related to customers.
 */
public class CustomerDAO {

	/**
	 * Adds a new customer to the database
	 * 
	 * @param customer The customer to add
	 * @return true if successful, false otherwise
	 * @throws SQLException if a database error occurs
	 */
	public boolean addCustomer(Customer customer) throws SQLException {
		String query = "INSERT INTO Customer (account_number, name, address, telephone) VALUES (?, ?, ?, ?)";

		Connection connection = DBConnectionFactory.getConnection();
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setString(1, customer.getAccountNumber());
		statement.setString(2, customer.getName());
		statement.setString(3, customer.getAddress());
		statement.setString(4, customer.getTelephone());

		int rowsInserted = statement.executeUpdate();
		return rowsInserted > 0;
	}

	/**
	 * Gets a customer by ID
	 * 
	 * @param customerId The customer ID
	 * @return Customer object if found, null otherwise
	 * @throws SQLException if a database error occurs
	 */
	public Customer getCustomerById(int customerId) throws SQLException {
		String query = "SELECT * FROM Customer WHERE customer_id = ?";
		Customer customer = null;

		Connection connection = DBConnectionFactory.getConnection();
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, customerId);
		ResultSet resultSet = statement.executeQuery();

		if (resultSet.next()) {
			customer = new Customer();
			customer.setCustomerId(resultSet.getInt("customer_id"));
			customer.setAccountNumber(resultSet.getString("account_number"));
			customer.setName(resultSet.getString("name"));
			customer.setAddress(resultSet.getString("address"));
			customer.setTelephone(resultSet.getString("telephone"));
			customer.setCreatedAt(resultSet.getTimestamp("created_at"));
			customer.setUpdatedAt(resultSet.getTimestamp("updated_at"));
		}

		return customer;
	}

	/**
	 * Gets a customer by account number
	 * 
	 * @param accountNumber The account number
	 * @return Customer object if found, null otherwise
	 * @throws SQLException if a database error occurs
	 */
	public Customer getCustomerByAccountNumber(String accountNumber) throws SQLException {
		String query = "SELECT * FROM Customer WHERE account_number = ?";
		Customer customer = null;

		Connection connection = DBConnectionFactory.getConnection();
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setString(1, accountNumber);
		ResultSet resultSet = statement.executeQuery();

		if (resultSet.next()) {
			customer = new Customer();
			customer.setCustomerId(resultSet.getInt("customer_id"));
			customer.setAccountNumber(resultSet.getString("account_number"));
			customer.setName(resultSet.getString("name"));
			customer.setAddress(resultSet.getString("address"));
			customer.setTelephone(resultSet.getString("telephone"));
			customer.setCreatedAt(resultSet.getTimestamp("created_at"));
			customer.setUpdatedAt(resultSet.getTimestamp("updated_at"));
		}

		return customer;
	}

	/**
	 * Gets all customers from the database
	 * 
	 * @return List of all customers
	 * @throws SQLException if a database error occurs
	 */
	public List<Customer> getAllCustomers() throws SQLException {
		List<Customer> customers = new ArrayList<>();
		String query = "SELECT * FROM Customer ORDER BY name";

		Connection connection = DBConnectionFactory.getConnection();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(query);

		while (resultSet.next()) {
			Customer customer = new Customer();
			customer.setCustomerId(resultSet.getInt("customer_id"));
			customer.setAccountNumber(resultSet.getString("account_number"));
			customer.setName(resultSet.getString("name"));
			customer.setAddress(resultSet.getString("address"));
			customer.setTelephone(resultSet.getString("telephone"));
			customer.setCreatedAt(resultSet.getTimestamp("created_at"));
			customer.setUpdatedAt(resultSet.getTimestamp("updated_at"));
			customers.add(customer);
		}

		return customers;
	}

	/**
	 * Gets paginated customers from the database
	 * 
	 * @param offset The offset for pagination
	 * @param limit  The limit for pagination
	 * @return List of customers for the current page
	 * @throws SQLException if a database error occurs
	 */
	public List<Customer> getCustomersPaginated(int offset, int limit) throws SQLException {
		List<Customer> customers = new ArrayList<>();
		String query = "SELECT * FROM Customer ORDER BY name LIMIT ? OFFSET ?";

		Connection connection = DBConnectionFactory.getConnection();
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, limit);
		statement.setInt(2, offset);
		ResultSet resultSet = statement.executeQuery();

		while (resultSet.next()) {
			Customer customer = new Customer();
			customer.setCustomerId(resultSet.getInt("customer_id"));
			customer.setAccountNumber(resultSet.getString("account_number"));
			customer.setName(resultSet.getString("name"));
			customer.setAddress(resultSet.getString("address"));
			customer.setTelephone(resultSet.getString("telephone"));
			customer.setCreatedAt(resultSet.getTimestamp("created_at"));
			customer.setUpdatedAt(resultSet.getTimestamp("updated_at"));
			customers.add(customer);
		}

		return customers;
	}

	/**
	 * Gets the total count of customers
	 * 
	 * @return Total number of customers
	 * @throws SQLException if a database error occurs
	 */
	public int getCustomerCount() throws SQLException {
		String query = "SELECT COUNT(*) FROM Customer";

		Connection connection = DBConnectionFactory.getConnection();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(query);

		if (resultSet.next()) {
			return resultSet.getInt(1);
		}

		return 0;
	}

	/**
	 * Searches customers by name or telephone
	 * 
	 * @param searchTerm The search term to look for
	 * @return List of matching customers
	 * @throws SQLException if a database error occurs
	 */
	public List<Customer> searchCustomers(String searchTerm) throws SQLException {
		List<Customer> customers = new ArrayList<>();
		String query = "SELECT * FROM Customer WHERE name LIKE ? OR telephone LIKE ? OR account_number LIKE ? ORDER BY name";

		Connection connection = DBConnectionFactory.getConnection();
		PreparedStatement statement = connection.prepareStatement(query);
		String searchPattern = "%" + searchTerm + "%";
		statement.setString(1, searchPattern);
		statement.setString(2, searchPattern);
		statement.setString(3, searchPattern);
		ResultSet resultSet = statement.executeQuery();

		while (resultSet.next()) {
			Customer customer = new Customer();
			customer.setCustomerId(resultSet.getInt("customer_id"));
			customer.setAccountNumber(resultSet.getString("account_number"));
			customer.setName(resultSet.getString("name"));
			customer.setAddress(resultSet.getString("address"));
			customer.setTelephone(resultSet.getString("telephone"));
			customer.setCreatedAt(resultSet.getTimestamp("created_at"));
			customer.setUpdatedAt(resultSet.getTimestamp("updated_at"));
			customers.add(customer);
		}

		return customers;
	}

	/**
	 * Searches customers by name or telephone with pagination
	 * 
	 * @param searchTerm The search term to look for
	 * @param offset     The offset for pagination
	 * @param limit      The limit for pagination
	 * @return List of matching customers for the current page
	 * @throws SQLException if a database error occurs
	 */
	public List<Customer> searchCustomersPaginated(String searchTerm, int offset, int limit) throws SQLException {
		List<Customer> customers = new ArrayList<>();
		String query = "SELECT * FROM Customer WHERE name LIKE ? OR telephone LIKE ? OR account_number LIKE ? ORDER BY name LIMIT ? OFFSET ?";

		Connection connection = DBConnectionFactory.getConnection();
		PreparedStatement statement = connection.prepareStatement(query);
		String searchPattern = "%" + searchTerm + "%";
		statement.setString(1, searchPattern);
		statement.setString(2, searchPattern);
		statement.setString(3, searchPattern);
		statement.setInt(4, limit);
		statement.setInt(5, offset);
		ResultSet resultSet = statement.executeQuery();

		while (resultSet.next()) {
			Customer customer = new Customer();
			customer.setCustomerId(resultSet.getInt("customer_id"));
			customer.setAccountNumber(resultSet.getString("account_number"));
			customer.setName(resultSet.getString("name"));
			customer.setAddress(resultSet.getString("address"));
			customer.setTelephone(resultSet.getString("telephone"));
			customer.setCreatedAt(resultSet.getTimestamp("created_at"));
			customer.setUpdatedAt(resultSet.getTimestamp("updated_at"));
			customers.add(customer);
		}

		return customers;
	}

	/**
	 * Gets the count of customers matching a search term
	 * 
	 * @param searchTerm The search term to look for
	 * @return Count of matching customers
	 * @throws SQLException if a database error occurs
	 */
	public int getCustomerSearchCount(String searchTerm) throws SQLException {
		String query = "SELECT COUNT(*) FROM Customer WHERE name LIKE ? OR telephone LIKE ? OR account_number LIKE ?";

		Connection connection = DBConnectionFactory.getConnection();
		PreparedStatement statement = connection.prepareStatement(query);
		String searchPattern = "%" + searchTerm + "%";
		statement.setString(1, searchPattern);
		statement.setString(2, searchPattern);
		statement.setString(3, searchPattern);
		ResultSet resultSet = statement.executeQuery();

		if (resultSet.next()) {
			return resultSet.getInt(1);
		}

		return 0;
	}

	/**
	 * Updates a customer in the database
	 * 
	 * @param customer The customer to update
	 * @return true if successful, false otherwise
	 * @throws SQLException if a database error occurs
	 */
	public boolean updateCustomer(Customer customer) throws SQLException {
		String query = "UPDATE Customer SET account_number = ?, name = ?, address = ?, telephone = ? WHERE customer_id = ?";

		Connection connection = DBConnectionFactory.getConnection();
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setString(1, customer.getAccountNumber());
		statement.setString(2, customer.getName());
		statement.setString(3, customer.getAddress());
		statement.setString(4, customer.getTelephone());
		statement.setInt(5, customer.getCustomerId());

		int rowsUpdated = statement.executeUpdate();
		return rowsUpdated > 0;
	}

	/**
	 * Deletes a customer from the database
	 * 
	 * @param customerId The ID of the customer to delete
	 * @return true if successful, false otherwise
	 * @throws SQLException if a database error occurs
	 */
	public boolean deleteCustomer(int customerId) throws SQLException {
		String query = "DELETE FROM Customer WHERE customer_id = ?";

		Connection connection = DBConnectionFactory.getConnection();
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, customerId);

		int rowsDeleted = statement.executeUpdate();
		return rowsDeleted > 0;
	}
}