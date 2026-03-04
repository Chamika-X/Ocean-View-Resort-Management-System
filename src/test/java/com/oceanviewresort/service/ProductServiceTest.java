package com.oceanviewresort.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanviewresort.dao.ProductDAO;
import com.oceanviewresort.model.Product;

/**
 * Test class for ProductService Tests all business logic methods with proper
 * mocking
 */
@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {

	@Mock
	private ProductDAO productDAO;

	@InjectMocks
	private ProductService productService;

	private Product testProduct;
	private Product existingProduct;

	@Before
	public void setUp() {
		// Create test product
		testProduct = new Product();
		testProduct.setProductId(1);
		testProduct.setName("Test Product");
		testProduct.setDescription("A test product for testing");
		testProduct.setPrice(29.99);
		testProduct.setQuantity(50);

		// Create existing product for duplicate tests
		existingProduct = new Product();
		existingProduct.setProductId(2);
		existingProduct.setName("Existing Product");
		existingProduct.setDescription("An existing product");
		existingProduct.setPrice(19.99);
		existingProduct.setQuantity(25);
	}

	@Test
	public void testGetInstance_ShouldReturnSameInstance() {
		// Test singleton pattern
		ProductService instance1 = ProductService.getInstance();
		ProductService instance2 = ProductService.getInstance();

		assertSame("Should return the same instance", instance1, instance2);
	}

	@Test
	public void testAddProduct_Success() throws SQLException {
		// Arrange
		Product newProduct = new Product(0, "New Product", "A new product", 39.99, 30);
		doNothing().when(productDAO).addProduct(newProduct);

		// Act
		productService.addProduct(newProduct);

		// Assert
		verify(productDAO).addProduct(newProduct);
	}

	@Test
	public void testAddProduct_DatabaseError() throws SQLException {
		// Arrange
		Product newProduct = new Product(0, "New Product", "A new product", 39.99, 30);
		doThrow(new SQLException("Database error")).when(productDAO).addProduct(newProduct);

		// Act & Assert
		try {
			productService.addProduct(newProduct);
			fail("Should throw SQLException");
		} catch (SQLException e) {
			assertEquals("Database error", e.getMessage());
		}
		verify(productDAO).addProduct(newProduct);
	}

	@Test
	public void testGetAllProducts_Success() throws SQLException {
		// Arrange
		List<Product> expectedProducts = new ArrayList<>();
		expectedProducts.add(testProduct);
		expectedProducts.add(existingProduct);

		when(productDAO.getAllProducts()).thenReturn(expectedProducts);

		// Act
		List<Product> result = productService.getAllProducts();

		// Assert
		assertNotNull("Product list should not be null", result);
		assertEquals("Should return correct number of products", 2, result.size());
		assertTrue("Should contain test product", result.contains(testProduct));
		assertTrue("Should contain existing product", result.contains(existingProduct));
		verify(productDAO).getAllProducts();
	}

	@Test
	public void testGetAllProducts_EmptyList() throws SQLException {
		// Arrange
		List<Product> emptyList = new ArrayList<>();
		when(productDAO.getAllProducts()).thenReturn(emptyList);

		// Act
		List<Product> result = productService.getAllProducts();

		// Assert
		assertNotNull("Product list should not be null", result);
		assertTrue("Product list should be empty", result.isEmpty());
		verify(productDAO).getAllProducts();
	}

	@Test
	public void testGetAllProducts_DatabaseError() throws SQLException {
		// Arrange
		when(productDAO.getAllProducts()).thenThrow(new SQLException("Database error"));

		// Act & Assert
		try {
			productService.getAllProducts();
			fail("Should throw SQLException");
		} catch (SQLException e) {
			assertEquals("Database error", e.getMessage());
		}
		verify(productDAO).getAllProducts();
	}

	@Test
	public void testGetProductById_Success() throws SQLException {
		// Arrange
		int productId = 1;
		when(productDAO.getProductById(productId)).thenReturn(testProduct);

		// Act
		Product result = productService.getProductById(productId);

		// Assert
		assertNotNull("Product should be found", result);
		assertEquals("Product ID should match", productId, result.getProductId());
		assertEquals("Product name should match", "Test Product", result.getName());
		assertEquals("Product price should match", 29.99, result.getPrice(), 0.01);
		assertEquals("Product quantity should match", 50, result.getQuantity());
		verify(productDAO).getProductById(productId);
	}

	@Test
	public void testGetProductById_NotFound() throws SQLException {
		// Arrange
		int productId = 999;
		when(productDAO.getProductById(productId)).thenReturn(null);

		// Act
		Product result = productService.getProductById(productId);

		// Assert
		assertNull("Product should not be found", result);
		verify(productDAO).getProductById(productId);
	}

	@Test
	public void testGetProductById_DatabaseError() throws SQLException {
		// Arrange
		int productId = 1;
		when(productDAO.getProductById(productId)).thenThrow(new SQLException("Database error"));

		// Act & Assert
		try {
			productService.getProductById(productId);
			fail("Should throw SQLException");
		} catch (SQLException e) {
			assertEquals("Database error", e.getMessage());
		}
		verify(productDAO).getProductById(productId);
	}

	@Test
	public void testUpdateProduct_Success() throws SQLException {
		// Arrange
		Product productToUpdate = new Product(1, "Updated Product", "Updated description", 49.99, 40);
		when(productDAO.updateProduct(productToUpdate)).thenReturn(true);

		// Act
		boolean result = productService.updateProduct(productToUpdate);

		// Assert
		assertTrue("Update should succeed", result);
		verify(productDAO).updateProduct(productToUpdate);
	}

	@Test
	public void testUpdateProduct_Failure() throws SQLException {
		// Arrange
		Product productToUpdate = new Product(999, "Nonexistent Product", "Description", 29.99, 15);
		when(productDAO.updateProduct(productToUpdate)).thenReturn(false);

		// Act
		boolean result = productService.updateProduct(productToUpdate);

		// Assert
		assertFalse("Update should fail", result);
		verify(productDAO).updateProduct(productToUpdate);
	}

	@Test
	public void testUpdateProduct_DatabaseError() throws SQLException {
		// Arrange
		Product productToUpdate = new Product(1, "Test Product", "Description", 29.99, 20);
		when(productDAO.updateProduct(productToUpdate)).thenThrow(new SQLException("Database error"));

		// Act & Assert
		try {
			productService.updateProduct(productToUpdate);
			fail("Should throw SQLException");
		} catch (SQLException e) {
			assertEquals("Database error", e.getMessage());
		}
		verify(productDAO).updateProduct(productToUpdate);
	}

	@Test
	public void testDeleteProduct_Success() throws SQLException {
		// Arrange
		int productId = 1;
		when(productDAO.deleteProduct(productId)).thenReturn(true);

		// Act
		boolean result = productService.deleteProduct(productId);

		// Assert
		assertTrue("Delete should succeed", result);
		verify(productDAO).deleteProduct(productId);
	}

	@Test
	public void testDeleteProduct_Failure() throws SQLException {
		// Arrange
		int productId = 999;
		when(productDAO.deleteProduct(productId)).thenReturn(false);

		// Act
		boolean result = productService.deleteProduct(productId);

		// Assert
		assertFalse("Delete should fail", result);
		verify(productDAO).deleteProduct(productId);
	}

	@Test
	public void testDeleteProduct_DatabaseError() throws SQLException {
		// Arrange
		int productId = 1;
		when(productDAO.deleteProduct(productId)).thenThrow(new SQLException("Database error"));

		// Act & Assert
		try {
			productService.deleteProduct(productId);
			fail("Should throw SQLException");
		} catch (SQLException e) {
			assertEquals("Database error", e.getMessage());
		}
		verify(productDAO).deleteProduct(productId);
	}

	// New tests for stock management functionality
	@Test
	public void testUpdateStockQuantity_Success() throws SQLException {
		// Arrange
		int productId = 1;
		int quantityToReduce = 5;
		when(productDAO.updateStockQuantity(productId, quantityToReduce)).thenReturn(true);

		// Act
		boolean result = productService.updateStockQuantity(productId, quantityToReduce);

		// Assert
		assertTrue("Stock update should succeed", result);
		verify(productDAO).updateStockQuantity(productId, quantityToReduce);
	}

	@Test
	public void testUpdateStockQuantity_Failure() throws SQLException {
		// Arrange
		int productId = 1;
		int quantityToReduce = 100; // More than available stock
		when(productDAO.updateStockQuantity(productId, quantityToReduce)).thenReturn(false);

		// Act
		boolean result = productService.updateStockQuantity(productId, quantityToReduce);

		// Assert
		assertFalse("Stock update should fail when insufficient stock", result);
		verify(productDAO).updateStockQuantity(productId, quantityToReduce);
	}

	@Test
	public void testUpdateStockQuantity_DatabaseError() throws SQLException {
		// Arrange
		int productId = 1;
		int quantityToReduce = 5;
		when(productDAO.updateStockQuantity(productId, quantityToReduce)).thenThrow(new SQLException("Database error"));

		// Act & Assert
		try {
			productService.updateStockQuantity(productId, quantityToReduce);
			fail("Should throw SQLException");
		} catch (SQLException e) {
			assertEquals("Database error", e.getMessage());
		}
		verify(productDAO).updateStockQuantity(productId, quantityToReduce);
	}

	@Test
	public void testHasSufficientStock_Success() throws SQLException {
		// Arrange
		int productId = 1;
		int requiredQuantity = 10;
		when(productDAO.hasSufficientStock(productId, requiredQuantity)).thenReturn(true);

		// Act
		boolean result = productService.hasSufficientStock(productId, requiredQuantity);

		// Assert
		assertTrue("Should have sufficient stock", result);
		verify(productDAO).hasSufficientStock(productId, requiredQuantity);
	}

	@Test
	public void testHasSufficientStock_InsufficientStock() throws SQLException {
		// Arrange
		int productId = 1;
		int requiredQuantity = 100;
		when(productDAO.hasSufficientStock(productId, requiredQuantity)).thenReturn(false);

		// Act
		boolean result = productService.hasSufficientStock(productId, requiredQuantity);

		// Assert
		assertFalse("Should not have sufficient stock", result);
		verify(productDAO).hasSufficientStock(productId, requiredQuantity);
	}

	@Test
	public void testHasSufficientStock_DatabaseError() throws SQLException {
		// Arrange
		int productId = 1;
		int requiredQuantity = 10;
		when(productDAO.hasSufficientStock(productId, requiredQuantity)).thenThrow(new SQLException("Database error"));

		// Act & Assert
		try {
			productService.hasSufficientStock(productId, requiredQuantity);
			fail("Should throw SQLException");
		} catch (SQLException e) {
			assertEquals("Database error", e.getMessage());
		}
		verify(productDAO).hasSufficientStock(productId, requiredQuantity);
	}

	@Test
	public void testProductQuantityField() {
		// Test that quantity field works correctly
		Product product = new Product();
		product.setQuantity(25);

		assertEquals("Quantity should be set correctly", 25, product.getQuantity());

		product.setQuantity(0);
		assertEquals("Quantity should handle zero", 0, product.getQuantity());

		product.setQuantity(1000);
		assertEquals("Quantity should handle large numbers", 1000, product.getQuantity());
	}
}
