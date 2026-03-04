package com.oceanviewresort.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.oceanviewresort.dao.CustomerDAO;
import com.oceanviewresort.model.Customer;

/**
 * Test class for CustomerService Tests all business logic methods with proper
 * mocking
 */
@RunWith(MockitoJUnitRunner.class)
public class CustomerServiceTest {

	@Mock
	private CustomerDAO customerDAO;

	@InjectMocks
	private CustomerService customerService;

	private Customer testCustomer;
	private Customer existingCustomer;

	@Before
	public void setUp() {
		// Create test customer
		testCustomer = new Customer();
		testCustomer.setCustomerId(1);
		testCustomer.setAccountNumber("ACC001");
		testCustomer.setName("John Doe");
		testCustomer.setAddress("123 Main St, City");
		testCustomer.setTelephone("+1234567890");
		testCustomer.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		testCustomer.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

		// Create existing customer for duplicate tests
		existingCustomer = new Customer();
		existingCustomer.setCustomerId(2);
		existingCustomer.setAccountNumber("ACC002");
		existingCustomer.setName("Jane Smith");
		existingCustomer.setAddress("456 Oak Ave, Town");
		existingCustomer.setTelephone("+0987654321");
		existingCustomer.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		existingCustomer.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
	}

	@Test
	public void testGetInstance_ShouldReturnSameInstance() {
		// Test singleton pattern
		CustomerService instance1 = CustomerService.getInstance();
		CustomerService instance2 = CustomerService.getInstance();

		assertSame("Should return the same instance", instance1, instance2);
	}

	@Test
	public void testAddCustomer_Success() throws SQLException {
		// Arrange
		Customer newCustomer = new Customer("ACC003", "New Customer", "789 Pine Rd", "+1122334455");
		when(customerDAO.getCustomerByAccountNumber("ACC003")).thenReturn(null);
		when(customerDAO.addCustomer(newCustomer)).thenReturn(true);

		// Act
		boolean result = customerService.addCustomer(newCustomer);

		// Assert
		assertTrue("Customer addition should succeed", result);
		verify(customerDAO).getCustomerByAccountNumber("ACC003");
		verify(customerDAO).addCustomer(newCustomer);
	}

	@Test
	public void testAddCustomer_AccountNumberAlreadyExists() throws SQLException {
		// Arrange
		Customer duplicateCustomer = new Customer("ACC001", "Duplicate Customer", "Address", "+1234567890");
		when(customerDAO.getCustomerByAccountNumber("ACC001")).thenReturn(existingCustomer);

		// Act
		boolean result = customerService.addCustomer(duplicateCustomer);

		// Assert
		assertFalse("Customer addition should fail for duplicate account number", result);
		verify(customerDAO).getCustomerByAccountNumber("ACC001");
		verify(customerDAO, never()).addCustomer(any(Customer.class));
	}

	@Test
	public void testAddCustomer_DatabaseError() throws SQLException {
		// Arrange
		Customer newCustomer = new Customer("ACC003", "New Customer", "Address", "+1234567890");
		when(customerDAO.getCustomerByAccountNumber("ACC003")).thenThrow(new SQLException("Database error"));

		// Act & Assert
		try {
			customerService.addCustomer(newCustomer);
			fail("Should throw SQLException");
		} catch (SQLException e) {
			assertEquals("Database error", e.getMessage());
		}
		verify(customerDAO).getCustomerByAccountNumber("ACC003");
	}

	@Test
	public void testGetCustomerById_Success() throws SQLException {
		// Arrange
		int customerId = 1;
		when(customerDAO.getCustomerById(customerId)).thenReturn(testCustomer);

		// Act
		Customer result = customerService.getCustomerById(customerId);

		// Assert
		assertNotNull("Customer should be found", result);
		assertEquals("Customer ID should match", customerId, result.getCustomerId());
		assertEquals("Account number should match", "ACC001", result.getAccountNumber());
		verify(customerDAO).getCustomerById(customerId);
	}

	@Test
	public void testGetCustomerById_NotFound() throws SQLException {
		// Arrange
		int customerId = 999;
		when(customerDAO.getCustomerById(customerId)).thenReturn(null);

		// Act
		Customer result = customerService.getCustomerById(customerId);

		// Assert
		assertNull("Customer should not be found", result);
		verify(customerDAO).getCustomerById(customerId);
	}

	@Test
	public void testGetCustomerById_DatabaseError() throws SQLException {
		// Arrange
		int customerId = 1;
		when(customerDAO.getCustomerById(customerId)).thenThrow(new SQLException("Database error"));

		// Act & Assert
		try {
			customerService.getCustomerById(customerId);
			fail("Should throw SQLException");
		} catch (SQLException e) {
			assertEquals("Database error", e.getMessage());
		}
		verify(customerDAO).getCustomerById(customerId);
	}

	@Test
	public void testGetCustomerByAccountNumber_Success() throws SQLException {
		// Arrange
		String accountNumber = "ACC001";
		when(customerDAO.getCustomerByAccountNumber(accountNumber)).thenReturn(testCustomer);

		// Act
		Customer result = customerService.getCustomerByAccountNumber(accountNumber);

		// Assert
		assertNotNull("Customer should be found", result);
		assertEquals("Account number should match", accountNumber, result.getAccountNumber());
		verify(customerDAO).getCustomerByAccountNumber(accountNumber);
	}

	@Test
	public void testGetCustomerByAccountNumber_NotFound() throws SQLException {
		// Arrange
		String accountNumber = "NONEXISTENT";
		when(customerDAO.getCustomerByAccountNumber(accountNumber)).thenReturn(null);

		// Act
		Customer result = customerService.getCustomerByAccountNumber(accountNumber);

		// Assert
		assertNull("Customer should not be found", result);
		verify(customerDAO).getCustomerByAccountNumber(accountNumber);
	}

	@Test
	public void testGetCustomerByAccountNumber_DatabaseError() throws SQLException {
		// Arrange
		String accountNumber = "ACC001";
		when(customerDAO.getCustomerByAccountNumber(accountNumber)).thenThrow(new SQLException("Database error"));

		// Act & Assert
		try {
			customerService.getCustomerByAccountNumber(accountNumber);
			fail("Should throw SQLException");
		} catch (SQLException e) {
			assertEquals("Database error", e.getMessage());
		}
		verify(customerDAO).getCustomerByAccountNumber(accountNumber);
	}

	@Test
	public void testGetAllCustomers_Success() throws SQLException {
		// Arrange
		List<Customer> expectedCustomers = new ArrayList<>();
		expectedCustomers.add(testCustomer);
		expectedCustomers.add(existingCustomer);

		when(customerDAO.getAllCustomers()).thenReturn(expectedCustomers);

		// Act
		List<Customer> result = customerService.getAllCustomers();

		// Assert
		assertNotNull("Customer list should not be null", result);
		assertEquals("Should return correct number of customers", 2, result.size());
		assertTrue("Should contain test customer", result.contains(testCustomer));
		assertTrue("Should contain existing customer", result.contains(existingCustomer));
		verify(customerDAO).getAllCustomers();
	}

	@Test
	public void testGetAllCustomers_EmptyList() throws SQLException {
		// Arrange
		List<Customer> emptyList = new ArrayList<>();
		when(customerDAO.getAllCustomers()).thenReturn(emptyList);

		// Act
		List<Customer> result = customerService.getAllCustomers();

		// Assert
		assertNotNull("Customer list should not be null", result);
		assertTrue("Customer list should be empty", result.isEmpty());
		verify(customerDAO).getAllCustomers();
	}

	@Test
	public void testGetAllCustomers_DatabaseError() throws SQLException {
		// Arrange
		when(customerDAO.getAllCustomers()).thenThrow(new SQLException("Database error"));

		// Act & Assert
		try {
			customerService.getAllCustomers();
			fail("Should throw SQLException");
		} catch (SQLException e) {
			assertEquals("Database error", e.getMessage());
		}
		verify(customerDAO).getAllCustomers();
	}

	@Test
	public void testUpdateCustomer_Success() throws SQLException {
		// Arrange
		Customer customerToUpdate = new Customer(1, "ACC001", "Updated Name", "Updated Address", "+9876543210");
		when(customerDAO.updateCustomer(customerToUpdate)).thenReturn(true);

		// Act
		boolean result = customerService.updateCustomer(customerToUpdate);

		// Assert
		assertTrue("Update should succeed", result);
		verify(customerDAO).updateCustomer(customerToUpdate);
	}

	@Test
	public void testUpdateCustomer_Failure() throws SQLException {
		// Arrange
		Customer customerToUpdate = new Customer(999, "ACC999", "Nonexistent", "Address", "+1234567890");
		when(customerDAO.updateCustomer(customerToUpdate)).thenReturn(false);

		// Act
		boolean result = customerService.updateCustomer(customerToUpdate);

		// Assert
		assertFalse("Update should fail", result);
		verify(customerDAO).updateCustomer(customerToUpdate);
	}

	@Test
	public void testUpdateCustomer_DatabaseError() throws SQLException {
		// Arrange
		Customer customerToUpdate = new Customer(1, "ACC001", "Test", "Address", "+1234567890");
		when(customerDAO.updateCustomer(customerToUpdate)).thenThrow(new SQLException("Database error"));

		// Act & Assert
		try {
			customerService.updateCustomer(customerToUpdate);
			fail("Should throw SQLException");
		} catch (SQLException e) {
			assertEquals("Database error", e.getMessage());
		}
		verify(customerDAO).updateCustomer(customerToUpdate);
	}

	@Test
	public void testDeleteCustomer_Success() throws SQLException {
		// Arrange
		int customerId = 1;
		when(customerDAO.deleteCustomer(customerId)).thenReturn(true);

		// Act
		boolean result = customerService.deleteCustomer(customerId);

		// Assert
		assertTrue("Delete should succeed", result);
		verify(customerDAO).deleteCustomer(customerId);
	}

	@Test
	public void testDeleteCustomer_Failure() throws SQLException {
		// Arrange
		int customerId = 999;
		when(customerDAO.deleteCustomer(customerId)).thenReturn(false);

		// Act
		boolean result = customerService.deleteCustomer(customerId);

		// Assert
		assertFalse("Delete should fail", result);
		verify(customerDAO).deleteCustomer(customerId);
	}

	@Test
	public void testDeleteCustomer_DatabaseError() throws SQLException {
		// Arrange
		int customerId = 1;
		when(customerDAO.deleteCustomer(customerId)).thenThrow(new SQLException("Database error"));

		// Act & Assert
		try {
			customerService.deleteCustomer(customerId);
			fail("Should throw SQLException");
		} catch (SQLException e) {
			assertEquals("Database error", e.getMessage());
		}
		verify(customerDAO).deleteCustomer(customerId);
	}

	@Test
	public void testGenerateAccountNumber_Success() throws SQLException {
		// Arrange
		List<Customer> existingCustomers = new ArrayList<>();
		existingCustomers.add(testCustomer);
		existingCustomers.add(existingCustomer);

		when(customerDAO.getAllCustomers()).thenReturn(existingCustomers);

		// Act
		String result = customerService.generateAccountNumber();

		// Assert
		assertNotNull("Account number should not be null", result);
		assertTrue("Account number should start with CUST", result.startsWith("CUST"));
		verify(customerDAO).getAllCustomers();
	}

	@Test
	public void testGenerateAccountNumber_FirstCustomer() throws SQLException {
		// Arrange
		List<Customer> emptyList = new ArrayList<>();
		when(customerDAO.getAllCustomers()).thenReturn(emptyList);

		// Act
		String result = customerService.generateAccountNumber();

		// Assert
		assertEquals("Should return CUST001 for first customer", "CUST001", result);
		verify(customerDAO).getAllCustomers();
	}

	@Test
	public void testGenerateAccountNumber_DatabaseError() throws SQLException {
		// Arrange
		when(customerDAO.getAllCustomers()).thenThrow(new SQLException("Database error"));

		// Act & Assert
		try {
			customerService.generateAccountNumber();
			fail("Should throw SQLException");
		} catch (SQLException e) {
			assertEquals("Database error", e.getMessage());
		}
		verify(customerDAO).getAllCustomers();
	}
}
