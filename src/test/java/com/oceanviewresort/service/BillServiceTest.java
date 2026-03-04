package com.oceanviewresort.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanviewresort.dao.BillDAO;
import com.oceanviewresort.dao.BillItemDAO;
import com.oceanviewresort.dao.CustomerDAO;
import com.oceanviewresort.dao.ProductDAO;
import com.oceanviewresort.model.Bill;
import com.oceanviewresort.model.BillItem;
import com.oceanviewresort.model.Customer;
import com.oceanviewresort.model.Product;

/**
 * Test class for BillService Tests all business logic methods with proper
 * mocking
 */
@RunWith(MockitoJUnitRunner.class)
public class BillServiceTest {

	@Mock
	private BillDAO billDAO;

	@Mock
	private BillItemDAO billItemDAO;

	@Mock
	private CustomerDAO customerDAO;

	@Mock
	private ProductDAO productDAO;

	@InjectMocks
	private BillService billService;

	private Bill testBill;
	private Customer testCustomer;
	private Product testProduct;
	private BillItem testBillItem;

	@Before
	public void setUp() {
		// Create test customer
		testCustomer = new Customer();
		testCustomer.setCustomerId(1);
		testCustomer.setAccountNumber("ACC001");
		testCustomer.setName("John Doe");
		testCustomer.setAddress("123 Main St");
		testCustomer.setTelephone("+1234567890");

		// Create test product
		testProduct = new Product();
		testProduct.setProductId(1);
		testProduct.setName("Test Book");
		testProduct.setDescription("A test book");
		testProduct.setPrice(29.99);
		testProduct.setQuantity(50); // Add quantity field

		// Create test bill item (no discount field anymore)
		testBillItem = new BillItem();
		testBillItem.setBillItemId(1);
		testBillItem.setBillId(1);
		testBillItem.setProductId(1);
		testBillItem.setProductName("Test Book");
		testBillItem.setQuantity(2);
		testBillItem.setUnitPrice(new BigDecimal("29.99"));
		testBillItem.setSubtotal(new BigDecimal("59.98"));

		// Create test bill
		testBill = new Bill();
		testBill.setBillId(1);
		testBill.setCustomerId(1);
		testBill.setCustomerName("John Doe");
		testBill.setAccountNumber("ACC001");
		testBill.setBillDate(Timestamp.valueOf(LocalDateTime.now()));
		testBill.setTotalAmount(new BigDecimal("59.98"));
		testBill.setDiscount(BigDecimal.ZERO); // Initialize with zero discount
		testBill.setStatus("PENDING");

		List<BillItem> items = new ArrayList<>();
		items.add(testBillItem);
		testBill.setItems(items);
	}

	@Test
	public void testCreateBill_Success() throws SQLException {
		// Arrange
		when(customerDAO.getCustomerById(1)).thenReturn(testCustomer);
		when(productDAO.getProductById(1)).thenReturn(testProduct);
		when(billDAO.createBill(testBill)).thenReturn(true);
		when(billItemDAO.createBillItems(testBill.getItems())).thenReturn(true);

		// Act
		boolean result = billService.createBill(testBill);

		// Assert
		assertTrue("Bill creation should succeed", result);
		verify(customerDAO).getCustomerById(1);
		verify(productDAO).getProductById(1);
		verify(billDAO).createBill(testBill);
		verify(billItemDAO).createBillItems(testBill.getItems());
	}

	@Test
	public void testCreateBill_CustomerNotFound() throws SQLException {
		// Arrange
		when(customerDAO.getCustomerById(999)).thenReturn(null);
		testBill.setCustomerId(999);

		// Act
		boolean result = billService.createBill(testBill);

		// Assert
		assertFalse("Bill creation should fail for non-existent customer", result);
		verify(customerDAO).getCustomerById(999);
		verify(billDAO, never()).createBill(any(Bill.class));
		verify(billItemDAO, never()).createBillItems(anyList());
	}

	@Test
	public void testCreateBill_BillCreationFails() throws SQLException {
		// Arrange
		when(customerDAO.getCustomerById(1)).thenReturn(testCustomer);
		when(productDAO.getProductById(1)).thenReturn(testProduct);
		when(billDAO.createBill(testBill)).thenReturn(false);

		// Act
		boolean result = billService.createBill(testBill);

		// Assert
		assertFalse("Bill creation should fail", result);
		verify(customerDAO).getCustomerById(1);
		verify(productDAO).getProductById(1);
		verify(billDAO).createBill(testBill);
		verify(billItemDAO, never()).createBillItems(anyList());
	}

	@Test
	public void testCreateBill_BillItemsCreationFails() throws SQLException {
		// Arrange
		when(customerDAO.getCustomerById(1)).thenReturn(testCustomer);
		when(productDAO.getProductById(1)).thenReturn(testProduct);
		when(billDAO.createBill(testBill)).thenReturn(true);
		when(billItemDAO.createBillItems(testBill.getItems())).thenReturn(false);

		// Act
		boolean result = billService.createBill(testBill);

		// Assert
		assertFalse("Bill creation should fail when bill items creation fails", result);
		verify(customerDAO).getCustomerById(1);
		verify(productDAO).getProductById(1);
		verify(billDAO).createBill(testBill);
		verify(billItemDAO).createBillItems(testBill.getItems());
	}

	@Test
	public void testCreateBill_EmptyItemsList() throws SQLException {
		// Arrange
		Bill billWithNoItems = new Bill();
		billWithNoItems.setCustomerId(1);
		billWithNoItems.setCustomerName("John Doe");
		billWithNoItems.setAccountNumber("ACC001");
		billWithNoItems.setBillDate(Timestamp.valueOf(LocalDateTime.now()));
		billWithNoItems.setTotalAmount(new BigDecimal("0.00"));
		billWithNoItems.setDiscount(BigDecimal.ZERO);
		billWithNoItems.setStatus("PENDING");
		billWithNoItems.setItems(new ArrayList<>());

		when(customerDAO.getCustomerById(1)).thenReturn(testCustomer);
		when(billDAO.createBill(billWithNoItems)).thenReturn(true);

		// Act
		boolean result = billService.createBill(billWithNoItems);

		// Assert
		assertTrue("Bill creation should succeed with empty items list", result);
		verify(customerDAO).getCustomerById(1);
		verify(billDAO).createBill(billWithNoItems);
		verify(billItemDAO, never()).createBillItems(anyList());
	}

	@Test
	public void testCreateBill_DatabaseError() throws SQLException {
		// Arrange
		when(customerDAO.getCustomerById(1)).thenThrow(new SQLException("Database error"));

		// Act & Assert
		try {
			billService.createBill(testBill);
			fail("Should throw SQLException");
		} catch (SQLException e) {
			assertEquals("Database error", e.getMessage());
		}
		verify(customerDAO).getCustomerById(1);
	}

	@Test
	public void testGetBillById_Success() throws SQLException {
		// Arrange
		int billId = 1;
		when(billDAO.getBillById(billId)).thenReturn(testBill);

		// Act
		Bill result = billService.getBillById(billId);

		// Assert
		assertNotNull("Bill should be found", result);
		assertEquals("Bill ID should match", billId, result.getBillId());
		assertEquals("Customer ID should match", 1, result.getCustomerId());
		verify(billDAO).getBillById(billId);
	}

	@Test
	public void testGetBillById_NotFound() throws SQLException {
		// Arrange
		int billId = 999;
		when(billDAO.getBillById(billId)).thenReturn(null);

		// Act
		Bill result = billService.getBillById(billId);

		// Assert
		assertNull("Bill should not be found", result);
		verify(billDAO).getBillById(billId);
	}

	@Test
	public void testGetBillById_DatabaseError() throws SQLException {
		// Arrange
		int billId = 1;
		when(billDAO.getBillById(billId)).thenThrow(new SQLException("Database error"));

		// Act & Assert
		try {
			billService.getBillById(billId);
			fail("Should throw SQLException");
		} catch (SQLException e) {
			assertEquals("Database error", e.getMessage());
		}
		verify(billDAO).getBillById(billId);
	}

	@Test
	public void testGetBillsByCustomerId_Success() throws SQLException {
		// Arrange
		int customerId = 1;
		List<Bill> expectedBills = new ArrayList<>();
		expectedBills.add(testBill);

		when(billDAO.getBillsByCustomerId(customerId)).thenReturn(expectedBills);

		// Act
		List<Bill> result = billService.getBillsByCustomerId(customerId);

		// Assert
		assertNotNull("Bill list should not be null", result);
		assertEquals("Should return correct number of bills", 1, result.size());
		assertTrue("Should contain test bill", result.contains(testBill));
		verify(billDAO).getBillsByCustomerId(customerId);
	}

	@Test
	public void testGetBillsByCustomerId_EmptyList() throws SQLException {
		// Arrange
		int customerId = 999;
		List<Bill> emptyList = new ArrayList<>();
		when(billDAO.getBillsByCustomerId(customerId)).thenReturn(emptyList);

		// Act
		List<Bill> result = billService.getBillsByCustomerId(customerId);

		// Assert
		assertNotNull("Bill list should not be null", result);
		assertTrue("Bill list should be empty", result.isEmpty());
		verify(billDAO).getBillsByCustomerId(customerId);
	}

	@Test
	public void testGetBillsByCustomerId_DatabaseError() throws SQLException {
		// Arrange
		int customerId = 1;
		when(billDAO.getBillsByCustomerId(customerId)).thenThrow(new SQLException("Database error"));

		// Act & Assert
		try {
			billService.getBillsByCustomerId(customerId);
			fail("Should throw SQLException");
		} catch (SQLException e) {
			assertEquals("Database error", e.getMessage());
		}
		verify(billDAO).getBillsByCustomerId(customerId);
	}

	@Test
	public void testGetAllBills_Success() throws SQLException {
		// Arrange
		List<Bill> expectedBills = new ArrayList<>();
		expectedBills.add(testBill);

		when(billDAO.getAllBills()).thenReturn(expectedBills);

		// Act
		List<Bill> result = billService.getAllBills();

		// Assert
		assertNotNull("Bill list should not be null", result);
		assertEquals("Should return correct number of bills", 1, result.size());
		assertTrue("Should contain test bill", result.contains(testBill));
		verify(billDAO).getAllBills();
	}

	@Test
	public void testGetAllBills_EmptyList() throws SQLException {
		// Arrange
		List<Bill> emptyList = new ArrayList<>();
		when(billDAO.getAllBills()).thenReturn(emptyList);

		// Act
		List<Bill> result = billService.getAllBills();

		// Assert
		assertNotNull("Bill list should not be null", result);
		assertTrue("Bill list should be empty", result.isEmpty());
		verify(billDAO).getAllBills();
	}

	@Test
	public void testGetAllBills_DatabaseError() throws SQLException {
		// Arrange
		when(billDAO.getAllBills()).thenThrow(new SQLException("Database error"));

		// Act & Assert
		try {
			billService.getAllBills();
			fail("Should throw SQLException");
		} catch (SQLException e) {
			assertEquals("Database error", e.getMessage());
		}
		verify(billDAO).getAllBills();
	}

	@Test
	public void testUpdateBillStatus_Success() throws SQLException {
		// Arrange
		int billId = 1;
		String newStatus = "PAID";

		when(billDAO.updateBillStatus(billId, newStatus)).thenReturn(true);

		// Act
		boolean result = billService.updateBillStatus(billId, newStatus);

		// Assert
		assertTrue("Bill status update should succeed", result);
		verify(billDAO).updateBillStatus(billId, newStatus);
	}

	@Test
	public void testUpdateBillStatus_Failure() throws SQLException {
		// Arrange
		int billId = 999;
		String newStatus = "PAID";

		when(billDAO.updateBillStatus(billId, newStatus)).thenReturn(false);

		// Act
		boolean result = billService.updateBillStatus(billId, newStatus);

		// Assert
		assertFalse("Bill status update should fail", result);
		verify(billDAO).updateBillStatus(billId, newStatus);
	}

	@Test
	public void testUpdateBillStatus_DatabaseError() throws SQLException {
		// Arrange
		int billId = 1;
		String newStatus = "PAID";

		when(billDAO.updateBillStatus(billId, newStatus)).thenThrow(new SQLException("Database error"));

		// Act & Assert
		try {
			billService.updateBillStatus(billId, newStatus);
			fail("Should throw SQLException");
		} catch (SQLException e) {
			assertEquals("Database error", e.getMessage());
		}
		verify(billDAO).updateBillStatus(billId, newStatus);
	}

	@Test
	public void testDeleteBill_Success() throws SQLException {
		// Arrange
		int billId = 1;
		when(billDAO.deleteBill(billId)).thenReturn(true);

		// Act
		boolean result = billService.deleteBill(billId);

		// Assert
		assertTrue("Bill deletion should succeed", result);
		verify(billDAO).deleteBill(billId);
	}

	@Test
	public void testDeleteBill_Failure() throws SQLException {
		// Arrange
		int billId = 999;
		when(billDAO.deleteBill(billId)).thenReturn(false);

		// Act
		boolean result = billService.deleteBill(billId);

		// Assert
		assertFalse("Bill deletion should fail", result);
		verify(billDAO).deleteBill(billId);
	}

	@Test
	public void testDeleteBill_DatabaseError() throws SQLException {
		// Arrange
		int billId = 1;
		when(billDAO.deleteBill(billId)).thenThrow(new SQLException("Database error"));

		// Act & Assert
		try {
			billService.deleteBill(billId);
			fail("Should throw SQLException");
		} catch (SQLException e) {
			assertEquals("Database error", e.getMessage());
		}
		verify(billDAO).deleteBill(billId);
	}

	@Test
	public void testCreateBillFromItems_Success() throws SQLException {
		// Arrange
		int customerId = 1;
		List<BillItem> items = new ArrayList<>();
		items.add(testBillItem);
		BigDecimal billDiscount = BigDecimal.ZERO;

		when(customerDAO.getCustomerById(customerId)).thenReturn(testCustomer);
		when(productDAO.getProductById(1)).thenReturn(testProduct);

		// Act
		Bill result = billService.createBillFromItems(customerId, items, billDiscount);

		// Assert
		assertNotNull("Bill should be created", result);
		assertEquals("Customer ID should match", customerId, result.getCustomerId());
		assertEquals("Customer name should match", testCustomer.getName(), result.getCustomerName());
		assertEquals("Account number should match", testCustomer.getAccountNumber(), result.getAccountNumber());
		assertEquals("Discount should be set", billDiscount, result.getDiscount());
		verify(customerDAO).getCustomerById(customerId);
		verify(productDAO).getProductById(1);
	}

	@Test
	public void testCreateBillFromItems_WithDiscount() throws SQLException {
		// Arrange
		int customerId = 1;
		List<BillItem> items = new ArrayList<>();
		items.add(testBillItem);
		BigDecimal billDiscount = new BigDecimal("10.00"); // 10% discount

		when(customerDAO.getCustomerById(customerId)).thenReturn(testCustomer);
		when(productDAO.getProductById(1)).thenReturn(testProduct);

		// Act
		Bill result = billService.createBillFromItems(customerId, items, billDiscount);

		// Assert
		assertNotNull("Bill should be created", result);
		assertEquals("Customer ID should match", customerId, result.getCustomerId());
		assertEquals("Discount should be set", billDiscount, result.getDiscount());

		// Verify discount calculation: original total = 59.98, 10% discount = 6.00,
		// final total = 53.98
		BigDecimal expectedOriginalTotal = new BigDecimal("59.98");
		BigDecimal expectedDiscountAmount = expectedOriginalTotal.multiply(billDiscount)
				.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal expectedFinalTotal = expectedOriginalTotal.subtract(expectedDiscountAmount).setScale(2,
				BigDecimal.ROUND_HALF_UP);

		assertEquals("Final total should be calculated with discount", expectedFinalTotal, result.getTotalAmount());
		verify(customerDAO).getCustomerById(customerId);
		verify(productDAO).getProductById(1);
	}

	@Test
	public void testCreateBillFromItems_CustomerNotFound() throws SQLException {
		// Arrange
		int customerId = 999;
		List<BillItem> items = new ArrayList<>();
		items.add(testBillItem);
		BigDecimal billDiscount = BigDecimal.ZERO;

		when(customerDAO.getCustomerById(customerId)).thenReturn(null);

		// Act
		Bill result = billService.createBillFromItems(customerId, items, billDiscount);

		// Assert
		assertNull("Bill should not be created for non-existent customer", result);
		verify(customerDAO).getCustomerById(customerId);
		verify(productDAO, never()).getProductById(anyInt());
	}

	@Test
	public void testCreateBillFromItems_DatabaseError() throws SQLException {
		// Arrange
		int customerId = 1;
		List<BillItem> items = new ArrayList<>();
		items.add(testBillItem);
		BigDecimal billDiscount = BigDecimal.ZERO;

		when(customerDAO.getCustomerById(customerId)).thenThrow(new SQLException("Database error"));

		// Act & Assert
		try {
			billService.createBillFromItems(customerId, items, billDiscount);
			fail("Should throw SQLException");
		} catch (SQLException e) {
			assertEquals("Database error", e.getMessage());
		}
		verify(customerDAO).getCustomerById(customerId);
	}

	@Test
	public void testCreateBillFromItems_WithInvalidProduct() throws SQLException {
		// Arrange
		int customerId = 1;
		List<BillItem> items = new ArrayList<>();
		items.add(testBillItem);
		BigDecimal billDiscount = BigDecimal.ZERO;

		when(customerDAO.getCustomerById(customerId)).thenReturn(testCustomer);
		when(productDAO.getProductById(1)).thenReturn(null); // Product not found

		// Act
		Bill result = billService.createBillFromItems(customerId, items, billDiscount);

		// Assert
		assertNotNull("Bill should be created", result);
		assertEquals("Customer ID should match", customerId, result.getCustomerId());
		assertTrue("Items list should be empty when product is invalid", result.getItems().isEmpty());
		verify(customerDAO).getCustomerById(customerId);
		verify(productDAO).getProductById(1);
	}

	@Test
	public void testGenerateBillNumber_Success() throws SQLException {
		// Arrange
		List<Bill> existingBills = new ArrayList<>();
		existingBills.add(testBill);

		when(billDAO.getAllBills()).thenReturn(existingBills);

		// Act
		String result = billService.generateBillNumber();

		// Assert
		assertNotNull("Bill number should not be null", result);
		assertTrue("Bill number should start with BILL", result.startsWith("BILL"));
		verify(billDAO).getAllBills();
	}

	@Test
	public void testGenerateBillNumber_FirstBill() throws SQLException {
		// Arrange
		List<Bill> emptyList = new ArrayList<>();
		when(billDAO.getAllBills()).thenReturn(emptyList);

		// Act
		String result = billService.generateBillNumber();

		// Assert
		assertEquals("Should return BILL000001 for first bill", "BILL000001", result);
		verify(billDAO).getAllBills();
	}

	@Test
	public void testCalculateTotalAmount_Success() {
		// Arrange
		List<BillItem> items = new ArrayList<>();
		items.add(testBillItem);

		BillItem item2 = new BillItem();
		item2.setSubtotal(new BigDecimal("25.50"));
		items.add(item2);

		// Act
		BigDecimal result = billService.calculateTotalAmount(items);

		// Assert
		assertEquals("Total should be calculated correctly", new BigDecimal("85.48"), result);
	}

	@Test
	public void testCalculateTotalAmount_EmptyList() {
		// Arrange
		List<BillItem> emptyList = new ArrayList<>();

		// Act
		BigDecimal result = billService.calculateTotalAmount(emptyList);

		// Assert
		assertEquals("Total should be zero for empty list", BigDecimal.ZERO, result);
	}

	@Test
	public void testCalculateTotalAmount_NullList() {
		// Act
		BigDecimal result = billService.calculateTotalAmount(null);

		// Assert
		assertEquals("Total should be zero for null list", BigDecimal.ZERO, result);
	}

	@Test
	public void testValidateBillItems_Success() throws SQLException {
		// Arrange
		List<BillItem> items = new ArrayList<>();
		items.add(testBillItem);

		when(productDAO.getProductById(1)).thenReturn(testProduct);

		// Act
		boolean result = billService.validateBillItems(items);

		// Assert
		assertTrue("Bill items should be valid", result);
		verify(productDAO).getProductById(1);
	}

	@Test
	public void testValidateBillItems_EmptyList() throws SQLException {
		// Arrange
		List<BillItem> emptyList = new ArrayList<>();

		// Act
		boolean result = billService.validateBillItems(emptyList);

		// Assert
		assertFalse("Empty list should be invalid", result);
		verify(productDAO, never()).getProductById(anyInt());
	}

	@Test
	public void testValidateBillItems_ProductNotFound() throws SQLException {
		// Arrange
		List<BillItem> items = new ArrayList<>();
		items.add(testBillItem);

		when(productDAO.getProductById(1)).thenReturn(null);

		// Act
		boolean result = billService.validateBillItems(items);

		// Assert
		assertFalse("Invalid product should make items invalid", result);
		verify(productDAO).getProductById(1);
	}

	@Test
	public void testValidateBillItems_InvalidQuantity() throws SQLException {
		// Arrange
		List<BillItem> items = new ArrayList<>();
		BillItem invalidItem = new BillItem();
		invalidItem.setProductId(1);
		invalidItem.setQuantity(0); // Invalid quantity
		items.add(invalidItem);

		when(productDAO.getProductById(1)).thenReturn(testProduct);

		// Act
		boolean result = billService.validateBillItems(items);

		// Assert
		assertFalse("Invalid quantity should make items invalid", result);
		verify(productDAO).getProductById(1);
	}

	@Test
	public void testBillDiscountCalculation() {
		// Test the discount calculation logic
		BigDecimal originalAmount = new BigDecimal("100.00");
		BigDecimal discountPercent = new BigDecimal("15.00"); // 15%

		BigDecimal discountAmount = originalAmount.multiply(discountPercent)
				.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal finalAmount = originalAmount.subtract(discountAmount).setScale(2, BigDecimal.ROUND_HALF_UP);

		assertEquals("Discount amount should be 15.00", new BigDecimal("15.00"), discountAmount);
		assertEquals("Final amount should be 85.00", new BigDecimal("85.00"), finalAmount);
	}

	@Test
	public void testBillDiscountCalculationWithPrecision() {
		// Test discount calculation with more complex amounts
		BigDecimal originalAmount = new BigDecimal("123.45");
		BigDecimal discountPercent = new BigDecimal("10.50"); // 10.5%

		BigDecimal discountAmount = originalAmount.multiply(discountPercent)
				.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal finalAmount = originalAmount.subtract(discountAmount).setScale(2, BigDecimal.ROUND_HALF_UP);

		// 123.45 * 10.5% = 12.96 (rounded to 2 decimal places)
		assertEquals("Discount amount should be 12.96", new BigDecimal("12.96"), discountAmount);
		assertEquals("Final amount should be 110.49", new BigDecimal("110.49"), finalAmount);
	}

	@Test
	public void testBillDiscountCalculationZeroDiscount() {
		// Test discount calculation with zero discount
		BigDecimal originalAmount = new BigDecimal("100.00");
		BigDecimal discountPercent = BigDecimal.ZERO; // 0%

		BigDecimal discountAmount = originalAmount.multiply(discountPercent)
				.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal finalAmount = originalAmount.subtract(discountAmount).setScale(2, BigDecimal.ROUND_HALF_UP);

		assertEquals("Discount amount should be 0.00", BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP),
				discountAmount);
		assertEquals("Final amount should equal original amount", originalAmount, finalAmount);
	}

	@Test
	public void testBillDiscountCalculationHundredPercentDiscount() {
		// Test discount calculation with 100% discount
		BigDecimal originalAmount = new BigDecimal("100.00");
		BigDecimal discountPercent = new BigDecimal("100.00"); // 100%

		BigDecimal discountAmount = originalAmount.multiply(discountPercent)
				.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal finalAmount = originalAmount.subtract(discountAmount).setScale(2, BigDecimal.ROUND_HALF_UP);

		assertEquals("Discount amount should be 100.00", new BigDecimal("100.00"), discountAmount);
		assertEquals("Final amount should be 0.00", BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_UP), finalAmount);
	}

	// New tests for stock validation functionality
	@Test
	public void testValidateBillItems_SufficientStock() throws SQLException {
		// Arrange
		List<BillItem> items = new ArrayList<>();
		BillItem item = new BillItem();
		item.setProductId(1);
		item.setQuantity(5); // Request 5 units
		items.add(item);

		Product product = new Product();
		product.setProductId(1);
		product.setQuantity(10); // Available 10 units
		when(productDAO.getProductById(1)).thenReturn(product);

		// Act
		boolean result = billService.validateBillItems(items);

		// Assert
		assertTrue("Should be valid with sufficient stock", result);
		verify(productDAO).getProductById(1);
	}

	@Test
	public void testValidateBillItems_InsufficientStock() throws SQLException {
		// Arrange
		List<BillItem> items = new ArrayList<>();
		BillItem item = new BillItem();
		item.setProductId(1);
		item.setQuantity(15); // Request 15 units
		items.add(item);

		Product product = new Product();
		product.setProductId(1);
		product.setQuantity(10); // Available only 10 units
		when(productDAO.getProductById(1)).thenReturn(product);

		// Act
		boolean result = billService.validateBillItems(items);

		// Assert
		assertFalse("Should be invalid with insufficient stock", result);
		verify(productDAO).getProductById(1);
	}

	@Test
	public void testValidateBillItems_ExactStockMatch() throws SQLException {
		// Arrange
		List<BillItem> items = new ArrayList<>();
		BillItem item = new BillItem();
		item.setProductId(1);
		item.setQuantity(10); // Request exactly 10 units
		items.add(item);

		Product product = new Product();
		product.setProductId(1);
		product.setQuantity(10); // Available exactly 10 units
		when(productDAO.getProductById(1)).thenReturn(product);

		// Act
		boolean result = billService.validateBillItems(items);

		// Assert
		assertTrue("Should be valid with exact stock match", result);
		verify(productDAO).getProductById(1);
	}

	@Test
	public void testValidateBillItems_ZeroStock() throws SQLException {
		// Arrange
		List<BillItem> items = new ArrayList<>();
		BillItem item = new BillItem();
		item.setProductId(1);
		item.setQuantity(1); // Request 1 unit
		items.add(item);

		Product product = new Product();
		product.setProductId(1);
		product.setQuantity(0); // No stock available
		when(productDAO.getProductById(1)).thenReturn(product);

		// Act
		boolean result = billService.validateBillItems(items);

		// Assert
		assertFalse("Should be invalid with zero stock", result);
		verify(productDAO).getProductById(1);
	}

	@Test
	public void testValidateBillItems_MultipleItemsWithStock() throws SQLException {
		// Arrange
		List<BillItem> items = new ArrayList<>();

		BillItem item1 = new BillItem();
		item1.setProductId(1);
		item1.setQuantity(5);
		items.add(item1);

		BillItem item2 = new BillItem();
		item2.setProductId(2);
		item2.setQuantity(3);
		items.add(item2);

		Product product1 = new Product();
		product1.setProductId(1);
		product1.setQuantity(10);

		Product product2 = new Product();
		product2.setProductId(2);
		product2.setQuantity(5);

		when(productDAO.getProductById(1)).thenReturn(product1);
		when(productDAO.getProductById(2)).thenReturn(product2);

		// Act
		boolean result = billService.validateBillItems(items);

		// Assert
		assertTrue("Should be valid with sufficient stock for all items", result);
		verify(productDAO).getProductById(1);
		verify(productDAO).getProductById(2);
	}

	@Test
	public void testValidateBillItems_MultipleItemsInsufficientStock() throws SQLException {
		// Arrange
		List<BillItem> items = new ArrayList<>();

		BillItem item1 = new BillItem();
		item1.setProductId(1);
		item1.setQuantity(5);
		items.add(item1);

		BillItem item2 = new BillItem();
		item2.setProductId(2);
		item2.setQuantity(10); // More than available
		items.add(item2);

		Product product1 = new Product();
		product1.setProductId(1);
		product1.setQuantity(10);

		Product product2 = new Product();
		product2.setProductId(2);
		product2.setQuantity(5); // Only 5 available

		when(productDAO.getProductById(1)).thenReturn(product1);
		when(productDAO.getProductById(2)).thenReturn(product2);

		// Act
		boolean result = billService.validateBillItems(items);

		// Assert
		assertFalse("Should be invalid when any item has insufficient stock", result);
		verify(productDAO).getProductById(1);
		verify(productDAO).getProductById(2);
	}

	@Test
	public void testCreateBillWithStockValidation_Success() throws SQLException {
		// Arrange
		when(customerDAO.getCustomerById(1)).thenReturn(testCustomer);
		when(productDAO.getProductById(1)).thenReturn(testProduct);
		when(billDAO.createBill(testBill)).thenReturn(true);
		when(billItemDAO.createBillItems(testBill.getItems())).thenReturn(true);
		when(productDAO.updateStockQuantity(1, 2)).thenReturn(true);

		// Act
		boolean result = billService.createBill(testBill);

		// Assert
		assertTrue("Bill creation should succeed with stock validation", result);
		verify(customerDAO).getCustomerById(1);
		verify(productDAO).getProductById(1);
		verify(billDAO).createBill(testBill);
		verify(billItemDAO).createBillItems(testBill.getItems());
		verify(productDAO).updateStockQuantity(1, 2);
	}

	@Test
	public void testCreateBillWithStockValidation_InsufficientStock() throws SQLException {
		// Arrange
		when(customerDAO.getCustomerById(1)).thenReturn(testCustomer);

		Product lowStockProduct = new Product();
		lowStockProduct.setProductId(1);
		lowStockProduct.setQuantity(1); // Only 1 available
		when(productDAO.getProductById(1)).thenReturn(lowStockProduct);

		// Create bill item requesting more than available
		BillItem highQuantityItem = new BillItem();
		highQuantityItem.setProductId(1);
		highQuantityItem.setQuantity(5); // Request 5, but only 1 available

		List<BillItem> items = new ArrayList<>();
		items.add(highQuantityItem);
		testBill.setItems(items);

		// Act
		boolean result = billService.createBill(testBill);

		// Assert
		assertFalse("Bill creation should fail with insufficient stock", result);
		verify(customerDAO).getCustomerById(1);
		verify(productDAO).getProductById(1);
		verify(billDAO, never()).createBill(any(Bill.class));
		verify(billItemDAO, never()).createBillItems(anyList());
		verify(productDAO, never()).updateStockQuantity(anyInt(), anyInt());
	}

	@Test
	public void testStockReductionOnBillCreation() throws SQLException {
		// Arrange
		when(customerDAO.getCustomerById(1)).thenReturn(testCustomer);
		when(productDAO.getProductById(1)).thenReturn(testProduct);
		when(billDAO.createBill(testBill)).thenReturn(true);
		when(billItemDAO.createBillItems(testBill.getItems())).thenReturn(true);
		when(productDAO.updateStockQuantity(1, 2)).thenReturn(true);

		// Act
		boolean result = billService.createBill(testBill);

		// Assert
		assertTrue("Bill creation should succeed", result);
		verify(productDAO).updateStockQuantity(1, 2);
	}

	@Test
	public void testStockRestorationOnBillDeletion() throws SQLException {
		// Arrange
		when(billDAO.getBillById(1)).thenReturn(testBill);
		when(billDAO.deleteBill(1)).thenReturn(true);
		when(productDAO.restoreStockQuantity(1, 2)).thenReturn(true);

		// Act
		boolean result = billService.deleteBill(1);

		// Assert
		assertTrue("Bill deletion should succeed", result);
		verify(productDAO).restoreStockQuantity(1, 2);
	}

	@Test
	public void testStockRestorationOnBillCancellation() throws SQLException {
		// Arrange
		when(billDAO.getBillById(1)).thenReturn(testBill);
		when(billDAO.updateBillStatus(1, "Cancelled")).thenReturn(true);
		when(productDAO.restoreStockQuantity(1, 2)).thenReturn(true);

		// Act
		boolean result = billService.updateBillStatus(1, "Cancelled");

		// Assert
		assertTrue("Bill status update should succeed", result);
		verify(productDAO).restoreStockQuantity(1, 2);
	}
}
