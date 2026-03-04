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

import com.oceanviewresort.dao.UserDAO;
import com.oceanviewresort.model.User;

/**
 * Test class for UserService Tests all business logic methods with proper
 * mocking
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

	@Mock
	private UserDAO userDAO;

	@InjectMocks
	private UserService userService;

	private User testUser;
	private User existingUser;

	@Before
	public void setUp() {
		// Create test user
		testUser = new User();
		testUser.setUserId(1);
		testUser.setUsername("testuser");
		testUser.setPassword("password123");
		testUser.setRole("user");
		testUser.setCreatedAt(new Timestamp(System.currentTimeMillis()));

		// Create existing user for duplicate tests
		existingUser = new User();
		existingUser.setUserId(2);
		existingUser.setUsername("existinguser");
		existingUser.setPassword("password456");
		existingUser.setRole("admin");
		existingUser.setCreatedAt(new Timestamp(System.currentTimeMillis()));
	}

	@Test
	public void testGetInstance_ShouldReturnSameInstance() {
		// Test singleton pattern
		UserService instance1 = UserService.getInstance();
		UserService instance2 = UserService.getInstance();

		assertSame("Should return the same instance", instance1, instance2);
	}

	@Test
	public void testAuthenticate_Success() throws SQLException {
		// Arrange
		String username = "testuser";
		String password = "password123";
		when(userDAO.authenticate(username, password)).thenReturn(testUser);

		// Act
		User result = userService.authenticate(username, password);

		// Assert
		assertNotNull("Authentication should succeed", result);
		assertEquals("Username should match", username, result.getUsername());
		assertEquals("Password should match", password, result.getPassword());
		verify(userDAO).authenticate(username, password);
	}

	@Test
	public void testAuthenticate_Failure() throws SQLException {
		// Arrange
		String username = "wronguser";
		String password = "wrongpassword";
		when(userDAO.authenticate(username, password)).thenReturn(null);

		// Act
		User result = userService.authenticate(username, password);

		// Assert
		assertNull("Authentication should fail", result);
		verify(userDAO).authenticate(username, password);
	}

	@Test
	public void testAuthenticate_DatabaseError() throws SQLException {
		// Arrange
		String username = "testuser";
		String password = "password123";
		when(userDAO.authenticate(username, password)).thenThrow(new SQLException("Database error"));

		// Act & Assert
		try {
			userService.authenticate(username, password);
			fail("Should throw SQLException");
		} catch (SQLException e) {
			assertEquals("Database error", e.getMessage());
		}
		verify(userDAO).authenticate(username, password);
	}

	@Test
	public void testRegisterUser_Success() throws SQLException {
		// Arrange
		String username = "newuser";
		String password = "newpassword";
		String role = "user";

		when(userDAO.getUserByUsername(username)).thenReturn(null);
		when(userDAO.addUser(any(User.class))).thenReturn(true);

		// Act
		boolean result = userService.registerUser(username, password, role);

		// Assert
		assertTrue("Registration should succeed", result);
		verify(userDAO).getUserByUsername(username);
		verify(userDAO).addUser(any(User.class));
	}

	@Test
	public void testRegisterUser_UsernameAlreadyExists() throws SQLException {
		// Arrange
		String username = "existinguser";
		String password = "password123";
		String role = "user";

		when(userDAO.getUserByUsername(username)).thenReturn(existingUser);

		// Act
		boolean result = userService.registerUser(username, password, role);

		// Assert
		assertFalse("Registration should fail for existing username", result);
		verify(userDAO).getUserByUsername(username);
		verify(userDAO, never()).addUser(any(User.class));
	}

	@Test
	public void testRegisterUser_WithDefaultRole() throws SQLException {
		// Arrange
		String username = "newuser";
		String password = "newpassword";

		when(userDAO.getUserByUsername(username)).thenReturn(null);
		when(userDAO.addUser(any(User.class))).thenReturn(true);

		// Act
		boolean result = userService.registerUser(username, password);

		// Assert
		assertTrue("Registration should succeed with default role", result);
		verify(userDAO).getUserByUsername(username);
		verify(userDAO).addUser(argThat(user -> "user".equals(user.getRole())));
	}

	@Test
	public void testRegisterUser_DatabaseError() throws SQLException {
		// Arrange
		String username = "newuser";
		String password = "newpassword";
		String role = "user";

		when(userDAO.getUserByUsername(username)).thenThrow(new SQLException("Database error"));

		// Act & Assert
		try {
			userService.registerUser(username, password, role);
			fail("Should throw SQLException");
		} catch (SQLException e) {
			assertEquals("Database error", e.getMessage());
		}
		verify(userDAO).getUserByUsername(username);
	}

	@Test
	public void testGetUserById_Success() throws SQLException {
		// Arrange
		int userId = 1;
		when(userDAO.getUserById(userId)).thenReturn(testUser);

		// Act
		User result = userService.getUserById(userId);

		// Assert
		assertNotNull("User should be found", result);
		assertEquals("User ID should match", userId, result.getUserId());
		verify(userDAO).getUserById(userId);
	}

	@Test
	public void testGetUserById_NotFound() throws SQLException {
		// Arrange
		int userId = 999;
		when(userDAO.getUserById(userId)).thenReturn(null);

		// Act
		User result = userService.getUserById(userId);

		// Assert
		assertNull("User should not be found", result);
		verify(userDAO).getUserById(userId);
	}

	@Test
	public void testGetUserByUsername_Success() throws SQLException {
		// Arrange
		String username = "testuser";
		when(userDAO.getUserByUsername(username)).thenReturn(testUser);

		// Act
		User result = userService.getUserByUsername(username);

		// Assert
		assertNotNull("User should be found", result);
		assertEquals("Username should match", username, result.getUsername());
		verify(userDAO).getUserByUsername(username);
	}

	@Test
	public void testGetUserByUsername_NotFound() throws SQLException {
		// Arrange
		String username = "nonexistentuser";
		when(userDAO.getUserByUsername(username)).thenReturn(null);

		// Act
		User result = userService.getUserByUsername(username);

		// Assert
		assertNull("User should not be found", result);
		verify(userDAO).getUserByUsername(username);
	}

	@Test
	public void testGetAllUsers_Success() throws SQLException {
		// Arrange
		List<User> expectedUsers = new ArrayList<>();
		expectedUsers.add(testUser);
		expectedUsers.add(existingUser);

		when(userDAO.getAllUsers()).thenReturn(expectedUsers);

		// Act
		List<User> result = userService.getAllUsers();

		// Assert
		assertNotNull("User list should not be null", result);
		assertEquals("Should return correct number of users", 2, result.size());
		assertTrue("Should contain test user", result.contains(testUser));
		assertTrue("Should contain existing user", result.contains(existingUser));
		verify(userDAO).getAllUsers();
	}

	@Test
	public void testGetAllUsers_EmptyList() throws SQLException {
		// Arrange
		List<User> emptyList = new ArrayList<>();
		when(userDAO.getAllUsers()).thenReturn(emptyList);

		// Act
		List<User> result = userService.getAllUsers();

		// Assert
		assertNotNull("User list should not be null", result);
		assertTrue("User list should be empty", result.isEmpty());
		verify(userDAO).getAllUsers();
	}

//	@Test
//	public void testUpdateUser_Success() throws SQLException {
//		// Arrange
//		User userToUpdate = new User(1, "updateduser", "newpassword", "admin");
//		when(userDAO.updateUser(userToUpdate)).thenReturn(true);
//
//		// Act
//		boolean result = userService.updateUser(userToUpdate);
//
//		// Assert
//		assertTrue("Update should succeed", result);
//		verify(userDAO).updateUser(userToUpdate);
//	}
//
//	@Test
//	public void testUpdateUser_Failure() throws SQLException {
//		// Arrange
//		User userToUpdate = new User(999, "nonexistentuser", "password", "user");
//		when(userDAO.updateUser(userToUpdate)).thenReturn(false);
//
//		// Act
//		boolean result = userService.updateUser(userToUpdate);
//
//		// Assert
//		assertFalse("Update should fail", result);
//		verify(userDAO).updateUser(userToUpdate);
//	}

//	@Test
//	public void testUpdateUser_DatabaseError() throws SQLException {
//		// Arrange
//		User userToUpdate = new User(1, "testuser", "password", "user");
//		when(userDAO.updateUser(userToUpdate)).thenThrow(new SQLException("Database error"));
//
//		// Act & Assert
//		try {
//			userService.updateUser(userToUpdate);
//			fail("Should throw SQLException");
//		} catch (SQLException e) {
//			assertEquals("Database error", e.getMessage());
//		}
//		verify(userDAO).updateUser(userToUpdate);
//	}

	@Test
	public void testDeleteUser_Success() throws SQLException {
		// Arrange
		int userId = 1;
		when(userDAO.deleteUser(userId)).thenReturn(true);

		// Act
		boolean result = userService.deleteUser(userId);

		// Assert
		assertTrue("Delete should succeed", result);
		verify(userDAO).deleteUser(userId);
	}

	@Test
	public void testDeleteUser_Failure() throws SQLException {
		// Arrange
		int userId = 999;
		when(userDAO.deleteUser(userId)).thenReturn(false);

		// Act
		boolean result = userService.deleteUser(userId);

		// Assert
		assertFalse("Delete should fail", result);
		verify(userDAO).deleteUser(userId);
	}

	@Test
	public void testDeleteUser_DatabaseError() throws SQLException {
		// Arrange
		int userId = 1;
		when(userDAO.deleteUser(userId)).thenThrow(new SQLException("Database error"));

		// Act & Assert
		try {
			userService.deleteUser(userId);
			fail("Should throw SQLException");
		} catch (SQLException e) {
			assertEquals("Database error", e.getMessage());
		}
		verify(userDAO).deleteUser(userId);
	}
}
