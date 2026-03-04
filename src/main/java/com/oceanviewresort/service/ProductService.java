package com.oceanviewresort.service;

import java.sql.SQLException;
import java.util.List;

import com.oceanviewresort.dao.ProductDAO;
import com.oceanviewresort.model.Product;

public class ProductService {

	private static ProductService instance;
	private ProductDAO productDAO;

	private ProductService() {
		this.productDAO = new ProductDAO();
	}

	public static ProductService getInstance() {
		if (instance == null) {
			synchronized (ProductService.class) {
				if (instance == null) {
					instance = new ProductService();
				}
			}
		}
		return instance;
	}

	public void addProduct(Product product) throws SQLException {
		productDAO.addProduct(product);
	}

	/**
	 * Gets all products
	 * 
	 * @return List of all products
	 * @throws SQLException if a database error occurs
	 */
	public List<Product> getAllProducts() throws SQLException {
		return productDAO.getAllProducts();
	}

	/**
	 * Searches products by name
	 * 
	 * @param searchTerm The search term to look for
	 * @return List of matching products
	 * @throws SQLException if a database error occurs
	 */
	public List<Product> searchProducts(String searchTerm) throws SQLException {
		return productDAO.searchProducts(searchTerm);
	}

	/**
	 * Gets paginated products
	 * 
	 * @param offset The offset for pagination
	 * @param limit  The limit for pagination
	 * @return List of products for the current page
	 * @throws SQLException if a database error occurs
	 */
	public List<Product> getProductsPaginated(int offset, int limit) throws SQLException {
		return productDAO.getProductsPaginated(offset, limit);
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
		return productDAO.searchProductsPaginated(searchTerm, offset, limit);
	}

	/**
	 * Gets the total count of products
	 * 
	 * @return Total number of products
	 * @throws SQLException if a database error occurs
	 */
	public int getProductCount() throws SQLException {
		return productDAO.getProductCount();
	}

	/**
	 * Gets the count of products matching a search term
	 * 
	 * @param searchTerm The search term to look for
	 * @return Count of matching products
	 * @throws SQLException if a database error occurs
	 */
	public int getProductSearchCount(String searchTerm) throws SQLException {
		return productDAO.getProductSearchCount(searchTerm);
	}

	public Product getProductById(int productId) throws SQLException {
		return productDAO.getProductById(productId);
	}

	public boolean updateProduct(Product product) throws SQLException {
		return productDAO.updateProduct(product);
	}

	public boolean deleteProduct(int productId) throws SQLException {
		return productDAO.deleteProduct(productId);
	}

	// Method to update stock quantity when a bill is created
	public boolean updateStockQuantity(int productId, int quantityToReduce) throws SQLException {
		return productDAO.updateStockQuantity(productId, quantityToReduce);
	}

	// Method to check if product has sufficient stock
	public boolean hasSufficientStock(int productId, int requiredQuantity) throws SQLException {
		return productDAO.hasSufficientStock(productId, requiredQuantity);
	}

	// Method to restore stock quantity when a bill is deleted or cancelled
	public boolean restoreStockQuantity(int productId, int quantityToRestore) throws SQLException {
		return productDAO.restoreStockQuantity(productId, quantityToRestore);
	}
}
