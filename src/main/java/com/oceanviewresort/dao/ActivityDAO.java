package com.oceanviewresort.dao;

import com.oceanviewresort.model.Activity;
import com.oceanviewresort.dao.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Activity entity. Handles database operations for
 * system activities and events.
 */
public class ActivityDAO {

	/**
	 * Create a new activity record
	 */
	public boolean createActivity(Activity activity) throws SQLException {
		String sql = "INSERT INTO activities (activity_type, description, entity_type, entity_id, entity_name, username, timestamp, status) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		try (Connection conn = DBConnectionFactory.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, activity.getActivityType());
			pstmt.setString(2, activity.getDescription());
			pstmt.setString(3, activity.getEntityType());
			pstmt.setInt(4, activity.getEntityId());
			pstmt.setString(5, activity.getEntityName());
			pstmt.setString(6, activity.getUsername());
			pstmt.setTimestamp(7, Timestamp.valueOf(activity.getTimestamp()));
			pstmt.setString(8, activity.getStatus());

			return pstmt.executeUpdate() > 0;
		}
	}

	/**
	 * Get all activities ordered by timestamp (most recent first)
	 */
	public List<Activity> getAllActivities() throws SQLException {
		String sql = "SELECT * FROM activities ORDER BY timestamp DESC";
		List<Activity> activities = new ArrayList<>();

		try (Connection conn = DBConnectionFactory.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				activities.add(mapResultSetToActivity(rs));
			}
		}

		return activities;
	}

	/**
	 * Get activities with pagination
	 */
	public List<Activity> getActivitiesPaginated(int offset, int limit) throws SQLException {
		String sql = "SELECT * FROM activities ORDER BY timestamp DESC LIMIT ? OFFSET ?";
		List<Activity> activities = new ArrayList<>();

		try (Connection conn = DBConnectionFactory.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, limit);
			pstmt.setInt(2, offset);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				activities.add(mapResultSetToActivity(rs));
			}
		}

		return activities;
	}

	/**
	 * Get total count of activities
	 */
	public int getActivityCount() throws SQLException {
		String sql = "SELECT COUNT(*) FROM activities";

		try (Connection conn = DBConnectionFactory.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {

			if (rs.next()) {
				return rs.getInt(1);
			}
		}

		return 0;
	}

	/**
	 * Search activities with pagination
	 */
	public List<Activity> searchActivitiesPaginated(String searchTerm, int offset, int limit) throws SQLException {
		String sql = "SELECT * FROM activities WHERE description LIKE ? OR entity_name LIKE ? OR username LIKE ? ORDER BY timestamp DESC LIMIT ? OFFSET ?";
		List<Activity> activities = new ArrayList<>();

		try (Connection conn = DBConnectionFactory.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			String searchPattern = "%" + searchTerm + "%";
			pstmt.setString(1, searchPattern);
			pstmt.setString(2, searchPattern);
			pstmt.setString(3, searchPattern);
			pstmt.setInt(4, limit);
			pstmt.setInt(5, offset);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				activities.add(mapResultSetToActivity(rs));
			}
		}

		return activities;
	}

	/**
	 * Get count of activities matching search term
	 */
	public int getActivitySearchCount(String searchTerm) throws SQLException {
		String sql = "SELECT COUNT(*) FROM activities WHERE description LIKE ? OR entity_name LIKE ? OR username LIKE ?";

		try (Connection conn = DBConnectionFactory.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			String searchPattern = "%" + searchTerm + "%";
			pstmt.setString(1, searchPattern);
			pstmt.setString(2, searchPattern);
			pstmt.setString(3, searchPattern);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return rs.getInt(1);
			}
		}

		return 0;
	}

	/**
	 * Search activities by term (without pagination)
	 */
	public List<Activity> searchActivities(String searchTerm) throws SQLException {
		String sql = "SELECT * FROM activities WHERE description LIKE ? OR entity_name LIKE ? OR username LIKE ? OR activity_type LIKE ? ORDER BY timestamp DESC";
		List<Activity> activities = new ArrayList<>();

		try (Connection conn = DBConnectionFactory.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			String searchPattern = "%" + searchTerm + "%";
			pstmt.setString(1, searchPattern);
			pstmt.setString(2, searchPattern);
			pstmt.setString(3, searchPattern);
			pstmt.setString(4, searchPattern);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				activities.add(mapResultSetToActivity(rs));
			}
		}

		return activities;
	}

	/**
	 * Get recent activities with limit
	 */
	public List<Activity> getRecentActivities(int limit) throws SQLException {
		String sql = "SELECT * FROM activities ORDER BY timestamp DESC LIMIT ?";
		List<Activity> activities = new ArrayList<>();

		try (Connection conn = DBConnectionFactory.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, limit);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				activities.add(mapResultSetToActivity(rs));
			}
		}

		return activities;
	}

	/**
	 * Get activities by type
	 */
	public List<Activity> getActivitiesByType(String activityType) throws SQLException {
		String sql = "SELECT * FROM activities WHERE activity_type = ? ORDER BY timestamp DESC";
		List<Activity> activities = new ArrayList<>();

		try (Connection conn = DBConnectionFactory.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, activityType);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				activities.add(mapResultSetToActivity(rs));
			}
		}

		return activities;
	}

	/**
	 * Get activities by username
	 */
	public List<Activity> getActivitiesByUsername(String username) throws SQLException {
		String sql = "SELECT * FROM activities WHERE username = ? ORDER BY timestamp DESC";
		List<Activity> activities = new ArrayList<>();

		try (Connection conn = DBConnectionFactory.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				activities.add(mapResultSetToActivity(rs));
			}
		}

		return activities;
	}

	/**
	 * Get activities by entity type and ID
	 */
	public List<Activity> getActivitiesByEntity(String entityType, int entityId) throws SQLException {
		String sql = "SELECT * FROM activities WHERE entity_type = ? AND entity_id = ? ORDER BY timestamp DESC";
		List<Activity> activities = new ArrayList<>();

		try (Connection conn = DBConnectionFactory.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, entityType);
			pstmt.setInt(2, entityId);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				activities.add(mapResultSetToActivity(rs));
			}
		}

		return activities;
	}

	/**
	 * Get activities within a date range
	 */
	public List<Activity> getActivitiesByDateRange(LocalDateTime startDate, LocalDateTime endDate) throws SQLException {
		String sql = "SELECT * FROM activities WHERE timestamp BETWEEN ? AND ? ORDER BY timestamp DESC";
		List<Activity> activities = new ArrayList<>();

		try (Connection conn = DBConnectionFactory.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setTimestamp(1, Timestamp.valueOf(startDate));
			pstmt.setTimestamp(2, Timestamp.valueOf(endDate));
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				activities.add(mapResultSetToActivity(rs));
			}
		}

		return activities;
	}

	/**
	 * Delete old activities (cleanup)
	 */
	public boolean deleteOldActivities(int daysToKeep) throws SQLException {
		String sql = "DELETE FROM activities WHERE timestamp < ?";

		try (Connection conn = DBConnectionFactory.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysToKeep);
			pstmt.setTimestamp(1, Timestamp.valueOf(cutoffDate));

			return pstmt.executeUpdate() > 0;
		}
	}

	/**
	 * Get activity count by type
	 */
	public int getActivityCountByType(String activityType) throws SQLException {
		String sql = "SELECT COUNT(*) FROM activities WHERE activity_type = ?";

		try (Connection conn = DBConnectionFactory.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, activityType);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return rs.getInt(1);
			}
		}

		return 0;
	}

	/**
	 * Get today's activity count
	 */
	public int getTodayActivityCount() throws SQLException {
		String sql = "SELECT COUNT(*) FROM activities WHERE DATE(timestamp) = CURDATE()";

		try (Connection conn = DBConnectionFactory.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {

			if (rs.next()) {
				return rs.getInt(1);
			}
		}

		return 0;
	}

	/**
	 * Map ResultSet to Activity object
	 */
	private Activity mapResultSetToActivity(ResultSet rs) throws SQLException {
		Activity activity = new Activity();
		activity.setActivityId(rs.getInt("activity_id"));
		activity.setActivityType(rs.getString("activity_type"));
		activity.setDescription(rs.getString("description"));
		activity.setEntityType(rs.getString("entity_type"));
		activity.setEntityId(rs.getInt("entity_id"));
		activity.setEntityName(rs.getString("entity_name"));
		activity.setUsername(rs.getString("username"));

		Timestamp timestamp = rs.getTimestamp("timestamp");
		if (timestamp != null) {
			activity.setTimestamp(timestamp.toLocalDateTime());
		}

		activity.setStatus(rs.getString("status"));
		return activity;
	}

	/**
	 * Create activities table if it doesn't exist
	 */
	public void createTableIfNotExists() throws SQLException {
		String sql = "CREATE TABLE IF NOT EXISTS activities (" + "activity_id INT AUTO_INCREMENT PRIMARY KEY,"
				+ "activity_type VARCHAR(50) NOT NULL," + "description TEXT NOT NULL," + "entity_type VARCHAR(50),"
				+ "entity_id INT," + "entity_name VARCHAR(255)," + "username VARCHAR(100) NOT NULL,"
				+ "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP," + "status VARCHAR(20) DEFAULT 'completed',"
				+ "INDEX idx_timestamp (timestamp)," + "INDEX idx_activity_type (activity_type),"
				+ "INDEX idx_username (username)," + "INDEX idx_entity (entity_type, entity_id)" + ")";

		try (Connection conn = DBConnectionFactory.getConnection(); Statement stmt = conn.createStatement()) {
			stmt.execute(sql);
		}
	}
}