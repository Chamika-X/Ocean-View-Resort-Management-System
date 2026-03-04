package com.oceanviewresort.validation;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

/**
 * Test class for ValidationUtils Tests all validation methods including the new
 * quantity validation
 */
public class ValidationUtilsTest {

	@Test
	public void testValidateProduct_ValidProduct() {
		// Arrange
		String name = "Test Product";
		String description = "A test product description";
		String price = "29.99";
		String quantity = "50";

		// Act
		Map<String, String> errors = ValidationUtils.validateProduct(name, description, price, quantity);

		// Assert
		assertTrue("Should have no validation errors", errors.isEmpty());
	}

	@Test
	public void testValidateProduct_MissingName() {
		// Arrange
		String name = "";
		String description = "A test product description";
		String price = "29.99";
		String quantity = "50";

		// Act
		Map<String, String> errors = ValidationUtils.validateProduct(name, description, price, quantity);

		// Assert
		assertTrue("Should have name error", errors.containsKey("name"));
		assertEquals("Product name is required", errors.get("name"));
	}

	@Test
	public void testValidateProduct_MissingPrice() {
		// Arrange
		String name = "Test Product";
		String description = "A test product description";
		String price = "";
		String quantity = "50";

		// Act
		Map<String, String> errors = ValidationUtils.validateProduct(name, description, price, quantity);

		// Assert
		assertTrue("Should have price error", errors.containsKey("price"));
		assertEquals("Price is required", errors.get("price"));
	}

	@Test
	public void testValidateProduct_MissingQuantity() {
		// Arrange
		String name = "Test Product";
		String description = "A test product description";
		String price = "29.99";
		String quantity = "";

		// Act
		Map<String, String> errors = ValidationUtils.validateProduct(name, description, price, quantity);

		// Assert
		assertTrue("Should have quantity error", errors.containsKey("quantity"));
		assertEquals("Quantity is required", errors.get("quantity"));
	}

	@Test
	public void testValidateProduct_InvalidPrice() {
		// Arrange
		String name = "Test Product";
		String description = "A test product description";
		String price = "invalid";
		String quantity = "50";

		// Act
		Map<String, String> errors = ValidationUtils.validateProduct(name, description, price, quantity);

		// Assert
		assertTrue("Should have price error", errors.containsKey("price"));
		assertEquals("Please enter a valid price", errors.get("price"));
	}

	@Test
	public void testValidateProduct_NegativePrice() {
		// Arrange
		String name = "Test Product";
		String description = "A test product description";
		String price = "-10.00";
		String quantity = "50";

		// Act
		Map<String, String> errors = ValidationUtils.validateProduct(name, description, price, quantity);

		// Assert
		assertTrue("Should have price error", errors.containsKey("price"));
		assertEquals("Price must be greater than 0", errors.get("price"));
	}

	@Test
	public void testValidateProduct_InvalidQuantity() {
		// Arrange
		String name = "Test Product";
		String description = "A test product description";
		String price = "29.99";
		String quantity = "invalid";

		// Act
		Map<String, String> errors = ValidationUtils.validateProduct(name, description, price, quantity);

		// Assert
		assertTrue("Should have quantity error", errors.containsKey("quantity"));
		assertEquals("Please enter a valid quantity", errors.get("quantity"));
	}

	@Test
	public void testValidateProduct_NegativeQuantity() {
		// Arrange
		String name = "Test Product";
		String description = "A test product description";
		String price = "29.99";
		String quantity = "-5";

		// Act
		Map<String, String> errors = ValidationUtils.validateProduct(name, description, price, quantity);

		// Assert
		assertTrue("Should have quantity error", errors.containsKey("quantity"));
		assertEquals("Quantity must be 0 or greater", errors.get("quantity"));
	}

	@Test
	public void testValidateProduct_ZeroQuantity() {
		// Arrange
		String name = "Test Product";
		String description = "A test product description";
		String price = "29.99";
		String quantity = "0";

		// Act
		Map<String, String> errors = ValidationUtils.validateProduct(name, description, price, quantity);

		// Assert
		assertTrue("Should have no validation errors for zero quantity", errors.isEmpty());
	}

	@Test
	public void testValidateProduct_LargeQuantity() {
		// Arrange
		String name = "Test Product";
		String description = "A test product description";
		String price = "29.99";
		String quantity = "1000000"; // Over the limit

		// Act
		Map<String, String> errors = ValidationUtils.validateProduct(name, description, price, quantity);

		// Assert
		assertTrue("Should have quantity error", errors.containsKey("quantity"));
		assertEquals("Quantity must be between 0 and 999,999", errors.get("quantity"));
	}

	@Test
	public void testValidateProduct_MultipleErrors() {
		// Arrange
		String name = ""; // Missing name
		String description = "A test product description";
		String price = "invalid"; // Invalid price
		String quantity = "-5"; // Negative quantity

		// Act
		Map<String, String> errors = ValidationUtils.validateProduct(name, description, price, quantity);

		// Assert
		assertEquals("Should have 3 validation errors", 3, errors.size());
		assertTrue("Should have name error", errors.containsKey("name"));
		assertTrue("Should have price error", errors.containsKey("price"));
		assertTrue("Should have quantity error", errors.containsKey("quantity"));
	}

	@Test
	public void testValidateProduct_DescriptionTooLong() {
		// Arrange
		String name = "Test Product";
		String description = "A".repeat(501); // 501 characters - over the limit
		String price = "29.99";
		String quantity = "50";

		// Act
		Map<String, String> errors = ValidationUtils.validateProduct(name, description, price, quantity);

		// Assert
		assertTrue("Should have description error", errors.containsKey("description"));
		assertEquals("Description must not exceed 500 characters", errors.get("description"));
	}

	@Test
	public void testValidateProduct_NameTooShort() {
		// Arrange
		String name = "A"; // 1 character - under the minimum
		String description = "A test product description";
		String price = "29.99";
		String quantity = "50";

		// Act
		Map<String, String> errors = ValidationUtils.validateProduct(name, description, price, quantity);

		// Assert
		assertTrue("Should have name error", errors.containsKey("name"));
		assertEquals("Product name must be between 2 and 100 characters", errors.get("name"));
	}

	@Test
	public void testValidateProduct_NameTooLong() {
		// Arrange
		String name = "A".repeat(101); // 101 characters - over the limit
		String description = "A test product description";
		String price = "29.99";
		String quantity = "50";

		// Act
		Map<String, String> errors = ValidationUtils.validateProduct(name, description, price, quantity);

		// Assert
		assertTrue("Should have name error", errors.containsKey("name"));
		assertEquals("Product name must be between 2 and 100 characters", errors.get("name"));
	}
}
