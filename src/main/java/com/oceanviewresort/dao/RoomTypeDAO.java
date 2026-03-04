package com.oceanviewresort.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.oceanviewresort.model.RoomType;

/**
 * Data Access Object for RoomType entity at Ocean View Resort.
 * Handles all database operations related to room types and rates.
 */
public class RoomTypeDAO {

    /**
     * Adds a new room type to the database
     * @param roomType The room type to add
     * @return The generated room type ID, or -1 if failed
     * @throws SQLException if a database error occurs
     */
    public int addRoomType(RoomType roomType) throws SQLException {
        String query = "INSERT INTO RoomType (type_name, description, rate_per_night, max_occupancy) " +
                       "VALUES (?, ?, ?, ?)";

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, roomType.getTypeName());
        statement.setString(2, roomType.getDescription());
        statement.setDouble(3, roomType.getRatePerNight());
        statement.setInt(4, roomType.getMaxOccupancy());

        int rowsInserted = statement.executeUpdate();
        
        if (rowsInserted > 0) {
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            }
        }
        return -1;
    }

    /**
     * Gets a room type by ID
     * @param roomTypeId The room type ID
     * @return RoomType object if found, null otherwise
     * @throws SQLException if a database error occurs
     */
    public RoomType getRoomTypeById(int roomTypeId) throws SQLException {
        String query = "SELECT * FROM RoomType WHERE room_type_id = ?";
        RoomType roomType = null;

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, roomTypeId);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            roomType = mapResultSetToRoomType(resultSet);
        }

        return roomType;
    }

    /**
     * Gets all room types from the database
     * @return List of all room types
     * @throws SQLException if a database error occurs
     */
    public List<RoomType> getAllRoomTypes() throws SQLException {
        List<RoomType> roomTypes = new ArrayList<>();
        String query = "SELECT * FROM RoomType ORDER BY rate_per_night";

        Connection connection = DBConnectionFactory.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            roomTypes.add(mapResultSetToRoomType(resultSet));
        }

        return roomTypes;
    }

    /**
     * Updates a room type
     * @param roomType The room type to update
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean updateRoomType(RoomType roomType) throws SQLException {
        String query = "UPDATE RoomType SET type_name = ?, description = ?, rate_per_night = ?, " +
                       "max_occupancy = ? WHERE room_type_id = ?";

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, roomType.getTypeName());
        statement.setString(2, roomType.getDescription());
        statement.setDouble(3, roomType.getRatePerNight());
        statement.setInt(4, roomType.getMaxOccupancy());
        statement.setInt(5, roomType.getRoomTypeId());

        int rowsUpdated = statement.executeUpdate();
        return rowsUpdated > 0;
    }

    /**
     * Deletes a room type
     * @param roomTypeId The ID of the room type to delete
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean deleteRoomType(int roomTypeId) throws SQLException {
        String query = "DELETE FROM RoomType WHERE room_type_id = ?";

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, roomTypeId);

        int rowsDeleted = statement.executeUpdate();
        return rowsDeleted > 0;
    }

    /**
     * Gets the count of room types
     * @return Total number of room types
     * @throws SQLException if a database error occurs
     */
    public int getRoomTypeCount() throws SQLException {
        String query = "SELECT COUNT(*) FROM RoomType";

        Connection connection = DBConnectionFactory.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        if (resultSet.next()) {
            return resultSet.getInt(1);
        }

        return 0;
    }

    /**
     * Maps a ResultSet row to a RoomType object
     * @param resultSet The ResultSet to map
     * @return RoomType object
     * @throws SQLException if a database error occurs
     */
    private RoomType mapResultSetToRoomType(ResultSet resultSet) throws SQLException {
        RoomType roomType = new RoomType();
        roomType.setRoomTypeId(resultSet.getInt("room_type_id"));
        roomType.setTypeName(resultSet.getString("type_name"));
        roomType.setDescription(resultSet.getString("description"));
        roomType.setRatePerNight(resultSet.getDouble("rate_per_night"));
        roomType.setMaxOccupancy(resultSet.getInt("max_occupancy"));
        roomType.setCreatedAt(resultSet.getTimestamp("created_at"));
        return roomType;
    }
}
