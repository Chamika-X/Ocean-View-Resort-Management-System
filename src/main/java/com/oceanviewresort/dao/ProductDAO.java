package com.oceanviewresort.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.oceanviewresort.model.Product;

public class ProductDAO {
	public void addProduct(Product product) throws SQLException {
		String query = "INSERT INTO Product (name, price, description, stock_quantity) VALUES (?, ?, ?, ?)";

		Connection connection = DBConnectionFactory.getConnection();
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setString(1, product.getName());
		statement.setDouble(2, product.getPrice());
		statement.setString(3, product.getDescription());
		statement.setInt(4, product.getQuantity());
		statement.executeUpdate();
	}

	public List<Product> getAllProducts() throws SQLException {
		List<Product> products = new ArrayList<>();
		String query = "SELECT * FROM Product ORDER BY name";

		Connection connection = DBConnectionFactory.getConnection();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(query);
		while (resultSet.next()) {
			int id = resultSet.getInt("product_id");
			String name = resultSet.getString("name");
			double price = resultSet.getDouble("price");
			String desc = resultSet.getString("description");
			int quantity = resultSet.getInt("stock_quantity");

			Product product = new Product(id, name, desc, price, quantity);
			products.add(product);
		}

		return products;
	}

	/**
	 * Gets paginated products from the database
	 * 
	 * @param offset The offset for pagination
	 * @param limit  The limit for pagination
	 * @return List of products for the current page
	 * @throws SQLException if a database error occurs
	 */
	public List<Product> getProductsPaginated(int offset, int limit) throws SQLException {
		List<Product> products = new ArrayList<>();
		String query = "SELECT * FROM Product ORDER BY name LIMIT ? OFFSET ?";

		Connection connection = DBConnectionFactory.getConnection();
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, limit);
		statement.setInt(2, offset);
		ResultSet resultSet = statement.executeQuery();

		while (resultSet.next()) {
			int id = resultSet.getInt("product_id");
			String name = resultSet.getString("name");
			double price = resultSet.getDouble("price");
			String desc = resultSet.getString("description");
			int quantity = resultSet.getInt("stock_quantity");

			Product product = new Product(id, name, desc, price, quantity);
			products.add(product);
		}

		return products;
	}

	/**
	 * Gets the total count of products
	 * 
	 * @return Total number of products
	 * @throws SQLException if a database error occurs
	 */
	public int getProductCount() throws SQLException {
		String query = "SELECT COUNT(*) FROM Product";

		Connection connection = DBConnectionFactory.getConnection();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(query);

		if (resultSet.next()) {
			return resultSet.getInt(1);
		}

		return 0;
	}

	/**
	 * Searches products by name
	 * 
	 * @param searchTerm The search term to look for
	 * @return List of matching products
	 * @throws SQLException if a database error occurs
	 */
	public List<Product> searchProducts(String searchTerm) throws SQLException {
		List<Product> products = new ArrayList<>();
		String query = "SELECT * FROM Product WHERE name LIKE ? ORDER BY name";

		Connection connection = DBConnectionFactory.getConnection();
		PreparedStatement statement = connection.prepareStatement(query);
		String searchPattern = "%" + searchTerm + "%";
		statement.setString(1, searchPattern);
		ResultSet resultSet = statement.executeQuery();

		while (resultSet.next()) {
			int id = resultSet.getInt("product_id");
			String name = resultSet.getString("name");
			String desc = resultSet.getString("description");
			double price = resultSet.getDouble("price");
			int quantity = resultSet.getInt("stock_quantity");

			Product product = new Product(id, name, desc, price, quantity);
			products.add(product);
		}

		return products;
	}

	/**
	 * Searches products by name with pagination
	 * 
	 * @param searchTerm The search term to look for
	 * @param offset     The offset for pagination
	 * @param limit      The limit for pagination
	 * @return List of matching products for the current page
	 * @throws SQLException if a database error occurs
	 */
	public List<Product> searchProductsPaginated(String searchTerm, int offset, int limit) throws SQLException {
		List<Product> products = new ArrayList<>();
		String query = "SELECT * FROM Product WHERE name LIKE ? ORDER BY name LIMIT ? OFFSET ?";

		Connection connection = DBConnectionFactory.getConnection();
		PreparedStatement statement = connection.prepareStatement(query);
		String searchPattern = "%" + searchTerm + "%";
		statement.setString(1, searchPattern);
		statement.setInt(2, limit);
		statement.setInt(3, offset);
		ResultSet resultSet = statement.executeQuery();

		while (resultSet.next()) {
			int id = resultSet.getInt("product_id");
			String name = resultSet.getString("name");
			String desc = resultSet.getString("description");
			double price = resultSet.getDouble("price");
			int quantity = resultSet.getInt("stock_quantity");

			Product product = new Product(id, name, desc, price, quantity);
			products.add(product);
		}

		return products;
	}

	/**
	 * Gets the count of products matching a search term
	 * 
	 * @param searchTerm The search term to look for
	 * @return Count of matching products
	 * @throws SQLException if a database error occurs
	 */
	public int getProductSearchCount(String searchTerm) throws SQLException {
		String query = "SELECT COUNT(*) FROM Product WHERE name LIKE ?";

		Connection connection = DBConnectionFactory.getConnection();
		PreparedStatement statement = connection.prepareStatement(query);
		String searchPattern = "%" + searchTerm + "%";
		statement.setString(1, searchPattern);
		ResultSet resultSet = statement.executeQuery();

		if (resultSet.next()) {
			return resultSet.getInt(1);
		}

		return 0;
	}

	public Product getProductById(int productId) throws SQLException {
		String query = "SELECT * FROM Product WHERE product_id = ?";
		Product product = null;

		Connection connection = DBConnectionFactory.getConnection();
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, productId);
		ResultSet resultSet = statement.executeQuery();

		if (resultSet.next()) {
			String name = resultSet.getString("name");
			double price = resultSet.getDouble("price");
			String description = resultSet.getString("description");
			int quantity = resultSet.getInt("stock_quantity");

			product = new Product(productId, name, description, price, quantity);
		}

		return product;
	}

	public boolean updateProduct(Product product) throws SQLException {
		String query = "UPDATE Product SET name = ?, price = ?, description = ?, stock_quantity = ? WHERE product_id = ?";

		Connection connection = DBConnectionFactory.getConnection();
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setString(1, product.getName());
		statement.setDouble(2, product.getPrice());
		statement.setString(3, product.getDescription());
		statement.setInt(4, product.getQuantity());
		statement.setInt(5, product.getProductId());

		int rowsUpdated = statement.executeUpdate();
		return rowsUpdated > 0;
	}

	public boolean deleteProduct(int productId) throws SQLException {
		String query = "DELETE FROM Product WHERE product_id = ?";

		Connection connection = DBConnectionFactory.getConnection();
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, productId);

		int rowsDeleted = statement.executeUpdate();
		return rowsDeleted > 0;
	}

	// Method to update stock quantity when a bill is created
	public boolean updateStockQuantity(int productId, int quantityToReduce) throws SQLException {
		String query = "UPDATE Product SET stock_quantity = stock_quantity - ? WHERE product_id = ? AND stock_quantity >= ?";

		Connection connection = DBConnectionFactory.getConnection();
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, quantityToReduce);
		statement.setInt(2, productId);
		statement.setInt(3, quantityToReduce);

		int rowsUpdated = statement.executeUpdate();
		return rowsUpdated > 0;
	}

	// Method to check if product has sufficient stock
	public boolean hasSufficientStock(int productId, int requiredQuantity) throws SQLException {
		String query = "SELECT stock_quantity FROM Product WHERE product_id = ?";

		Connection connection = DBConnectionFactory.getConnection();
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, productId);
		ResultSet resultSet = statement.executeQuery();

		if (resultSet.next()) {
			int availableStock = resultSet.getInt("stock_quantity");
			return availableStock >= requiredQuantity;
		}
		return false;
	}

	// Method to restore stock quantity when a bill is deleted or cancelled
	public boolean restoreStockQuantity(int productId, int quantityToRestore) throws SQLException {
		String query = "UPDATE Product SET stock_quantity = stock_quantity + ? WHERE product_id = ?";

		Connection connection = DBConnectionFactory.getConnection();
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, quantityToRestore);
		statement.setInt(2, productId);

		int rowsUpdated = statement.executeUpdate();
		return rowsUpdated > 0;
	}
}