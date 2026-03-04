package com.oceanviewresort.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.oceanviewresort.model.Room;
import com.oceanviewresort.model.RoomType;

/**
 * Data Access Object for Room entity at Ocean View Resort.
 * Handles all database operations related to hotel rooms.
 */
public class RoomDAO {

    /**
     * Adds a new room to the database
     * @param room The room to add
     * @return The generated room ID, or -1 if failed
     * @throws SQLException if a database error occurs
     */
    public int addRoom(Room room) throws SQLException {
        String query = "INSERT INTO Room (room_number, room_type_id, floor_number, status, description) " +
                       "VALUES (?, ?, ?, ?, ?)";

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, room.getRoomNumber());
        statement.setInt(2, room.getRoomTypeId());
        statement.setInt(3, room.getFloorNumber());
        statement.setString(4, room.getStatus());
        statement.setString(5, room.getDescription());

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
     * Gets a room by ID
     * @param roomId The room ID
     * @return Room object if found, null otherwise
     * @throws SQLException if a database error occurs
     */
    public Room getRoomById(int roomId) throws SQLException {
        String query = "SELECT r.*, rt.type_name, rt.description as type_description, " +
                       "rt.rate_per_night, rt.max_occupancy " +
                       "FROM Room r JOIN RoomType rt ON r.room_type_id = rt.room_type_id " +
                       "WHERE r.room_id = ?";
        Room room = null;

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, roomId);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            room = mapResultSetToRoom(resultSet);
        }

        return room;
    }

    /**
     * Gets a room by room number
     * @param roomNumber The room number
     * @return Room object if found, null otherwise
     * @throws SQLException if a database error occurs
     */
    public Room getRoomByNumber(String roomNumber) throws SQLException {
        String query = "SELECT r.*, rt.type_name, rt.description as type_description, " +
                       "rt.rate_per_night, rt.max_occupancy " +
                       "FROM Room r JOIN RoomType rt ON r.room_type_id = rt.room_type_id " +
                       "WHERE r.room_number = ?";
        Room room = null;

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, roomNumber);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            room = mapResultSetToRoom(resultSet);
        }

        return room;
    }

    /**
     * Gets all rooms from the database
     * @return List of all rooms
     * @throws SQLException if a database error occurs
     */
    public List<Room> getAllRooms() throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT r.*, rt.type_name, rt.description as type_description, " +
                       "rt.rate_per_night, rt.max_occupancy " +
                       "FROM Room r JOIN RoomType rt ON r.room_type_id = rt.room_type_id " +
                       "ORDER BY r.room_number";

        Connection connection = DBConnectionFactory.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            rooms.add(mapResultSetToRoom(resultSet));
        }

        return rooms;
    }

    /**
     * Gets available rooms
     * @return List of available rooms
     * @throws SQLException if a database error occurs
     */
    public List<Room> getAvailableRooms() throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT r.*, rt.type_name, rt.description as type_description, " +
                       "rt.rate_per_night, rt.max_occupancy " +
                       "FROM Room r JOIN RoomType rt ON r.room_type_id = rt.room_type_id " +
                       "WHERE r.status = 'available' ORDER BY r.room_number";

        Connection connection = DBConnectionFactory.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            rooms.add(mapResultSetToRoom(resultSet));
        }

        return rooms;
    }

    /**
     * Gets available rooms for specific dates (checking for conflicts)
     * @param checkInDate Check-in date
     * @param checkOutDate Check-out date
     * @return List of available rooms
     * @throws SQLException if a database error occurs
     */
    public List<Room> getAvailableRoomsForDates(java.time.LocalDate checkInDate, java.time.LocalDate checkOutDate) throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT r.*, rt.type_name, rt.description as type_description, " +
                       "rt.rate_per_night, rt.max_occupancy " +
                       "FROM Room r JOIN RoomType rt ON r.room_type_id = rt.room_type_id " +
                       "WHERE r.status != 'maintenance' AND r.room_id NOT IN (" +
                       "  SELECT room_id FROM Reservation " +
                       "  WHERE status NOT IN ('cancelled', 'checked_out') " +
                       "  AND ((check_in_date <= ? AND check_out_date > ?) " +
                       "       OR (check_in_date < ? AND check_out_date >= ?) " +
                       "       OR (check_in_date >= ? AND check_out_date <= ?))" +
                       ") ORDER BY r.room_number";

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setDate(1, java.sql.Date.valueOf(checkOutDate));
        statement.setDate(2, java.sql.Date.valueOf(checkInDate));
        statement.setDate(3, java.sql.Date.valueOf(checkOutDate));
        statement.setDate(4, java.sql.Date.valueOf(checkInDate));
        statement.setDate(5, java.sql.Date.valueOf(checkInDate));
        statement.setDate(6, java.sql.Date.valueOf(checkOutDate));
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            rooms.add(mapResultSetToRoom(resultSet));
        }

        return rooms;
    }

    /**
     * Gets rooms by type
     * @param roomTypeId The room type ID
     * @return List of rooms of the specified type
     * @throws SQLException if a database error occurs
     */
    public List<Room> getRoomsByType(int roomTypeId) throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT r.*, rt.type_name, rt.description as type_description, " +
                       "rt.rate_per_night, rt.max_occupancy " +
                       "FROM Room r JOIN RoomType rt ON r.room_type_id = rt.room_type_id " +
                       "WHERE r.room_type_id = ? ORDER BY r.room_number";

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, roomTypeId);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            rooms.add(mapResultSetToRoom(resultSet));
        }

        return rooms;
    }

    /**
     * Gets rooms by status
     * @param status The room status
     * @return List of rooms with the specified status
     * @throws SQLException if a database error occurs
     */
    public List<Room> getRoomsByStatus(String status) throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT r.*, rt.type_name, rt.description as type_description, " +
                       "rt.rate_per_night, rt.max_occupancy " +
                       "FROM Room r JOIN RoomType rt ON r.room_type_id = rt.room_type_id " +
                       "WHERE r.status = ? ORDER BY r.room_number";

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, status);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            rooms.add(mapResultSetToRoom(resultSet));
        }

        return rooms;
    }

    /**
     * Gets paginated rooms from the database
     * @param offset The offset for pagination
     * @param limit The limit for pagination
     * @return List of rooms for the current page
     * @throws SQLException if a database error occurs
     */
    public List<Room> getRoomsPaginated(int offset, int limit) throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT r.*, rt.type_name, rt.description as type_description, " +
                       "rt.rate_per_night, rt.max_occupancy " +
                       "FROM Room r JOIN RoomType rt ON r.room_type_id = rt.room_type_id " +
                       "ORDER BY r.room_number LIMIT ? OFFSET ?";

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, limit);
        statement.setInt(2, offset);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            rooms.add(mapResultSetToRoom(resultSet));
        }

        return rooms;
    }

    /**
     * Gets the total count of rooms
     * @return Total number of rooms
     * @throws SQLException if a database error occurs
     */
    public int getRoomCount() throws SQLException {
        String query = "SELECT COUNT(*) FROM Room";

        Connection connection = DBConnectionFactory.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        if (resultSet.next()) {
            return resultSet.getInt(1);
        }

        return 0;
    }

    /**
     * Gets the count of rooms by status
     * @param status The room status
     * @return Count of rooms with the specified status
     * @throws SQLException if a database error occurs
     */
    public int getRoomCountByStatus(String status) throws SQLException {
        String query = "SELECT COUNT(*) FROM Room WHERE status = ?";

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, status);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getInt(1);
        }

        return 0;
    }

    /**
     * Updates a room
     * @param room The room to update
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean updateRoom(Room room) throws SQLException {
        String query = "UPDATE Room SET room_number = ?, room_type_id = ?, floor_number = ?, " +
                       "status = ?, description = ? WHERE room_id = ?";

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, room.getRoomNumber());
        statement.setInt(2, room.getRoomTypeId());
        statement.setInt(3, room.getFloorNumber());
        statement.setString(4, room.getStatus());
        statement.setString(5, room.getDescription());
        statement.setInt(6, room.getRoomId());

        int rowsUpdated = statement.executeUpdate();
        return rowsUpdated > 0;
    }

    /**
     * Updates room status
     * @param roomId The room ID
     * @param status The new status
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean updateRoomStatus(int roomId, String status) throws SQLException {
        String query = "UPDATE Room SET status = ? WHERE room_id = ?";

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, status);
        statement.setInt(2, roomId);

        int rowsUpdated = statement.executeUpdate();
        return rowsUpdated > 0;
    }

    /**
     * Deletes a room
     * @param roomId The ID of the room to delete
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean deleteRoom(int roomId) throws SQLException {
        String query = "DELETE FROM Room WHERE room_id = ?";

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, roomId);

        int rowsDeleted = statement.executeUpdate();
        return rowsDeleted > 0;
    }

    /**
     * Maps a ResultSet row to a Room object with RoomType
     * @param resultSet The ResultSet to map
     * @return Room object with RoomType
     * @throws SQLException if a database error occurs
     */
    private Room mapResultSetToRoom(ResultSet resultSet) throws SQLException {
        Room room = new Room();
        room.setRoomId(resultSet.getInt("room_id"));
        room.setRoomNumber(resultSet.getString("room_number"));
        room.setRoomTypeId(resultSet.getInt("room_type_id"));
        room.setFloorNumber(resultSet.getInt("floor_number"));
        room.setStatus(resultSet.getString("status"));
        room.setDescription(resultSet.getString("description"));
        room.setCreatedAt(resultSet.getTimestamp("created_at"));
        room.setUpdatedAt(resultSet.getTimestamp("updated_at"));

        // Set RoomType if available in result set
        try {
            RoomType roomType = new RoomType();
            roomType.setRoomTypeId(resultSet.getInt("room_type_id"));
            roomType.setTypeName(resultSet.getString("type_name"));
            roomType.setDescription(resultSet.getString("type_description"));
            roomType.setRatePerNight(resultSet.getDouble("rate_per_night"));
            roomType.setMaxOccupancy(resultSet.getInt("max_occupancy"));
            room.setRoomType(roomType);
        } catch (SQLException e) {
            // RoomType columns not in result set, ignore
        }

        return room;
    }
}
