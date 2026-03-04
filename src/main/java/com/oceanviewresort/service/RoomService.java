package com.oceanviewresort.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.oceanviewresort.dao.RoomDAO;
import com.oceanviewresort.dao.RoomTypeDAO;
import com.oceanviewresort.model.Room;
import com.oceanviewresort.model.RoomType;

/**
 * Service class for Room-related operations at Ocean View Resort.
 * Implements Singleton pattern and provides business logic for room management.
 */
public class RoomService {

    private static RoomService instance;
    private RoomDAO roomDAO;
    private RoomTypeDAO roomTypeDAO;

    /**
     * Private constructor for Singleton pattern
     */
    private RoomService() {
        this.roomDAO = new RoomDAO();
        this.roomTypeDAO = new RoomTypeDAO();
    }

    /**
     * Gets the singleton instance of RoomService
     * @return The RoomService instance
     */
    public static RoomService getInstance() {
        if (instance == null) {
            synchronized (RoomService.class) {
                if (instance == null) {
                    instance = new RoomService();
                }
            }
        }
        return instance;
    }

    // ================== Room Type Operations ==================

    /**
     * Gets all room types
     * @return List of all room types
     * @throws SQLException if a database error occurs
     */
    public List<RoomType> getAllRoomTypes() throws SQLException {
        return roomTypeDAO.getAllRoomTypes();
    }

    /**
     * Gets a room type by ID
     * @param roomTypeId The room type ID
     * @return RoomType object if found, null otherwise
     * @throws SQLException if a database error occurs
     */
    public RoomType getRoomTypeById(int roomTypeId) throws SQLException {
        return roomTypeDAO.getRoomTypeById(roomTypeId);
    }

    /**
     * Adds a new room type
     * @param roomType The room type to add
     * @return The generated room type ID, or -1 if failed
     * @throws SQLException if a database error occurs
     */
    public int addRoomType(RoomType roomType) throws SQLException {
        return roomTypeDAO.addRoomType(roomType);
    }

    /**
     * Updates a room type
     * @param roomType The room type to update
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean updateRoomType(RoomType roomType) throws SQLException {
        return roomTypeDAO.updateRoomType(roomType);
    }

    // ================== Room Operations ==================

    /**
     * Adds a new room
     * @param room The room to add
     * @return The generated room ID, or -1 if failed
     * @throws SQLException if a database error occurs
     */
    public int addRoom(Room room) throws SQLException {
        // Check if room number already exists
        if (roomDAO.getRoomByNumber(room.getRoomNumber()) != null) {
            return -1;
        }
        return roomDAO.addRoom(room);
    }

    /**
     * Gets a room by ID
     * @param roomId The room ID
     * @return Room object if found, null otherwise
     * @throws SQLException if a database error occurs
     */
    public Room getRoomById(int roomId) throws SQLException {
        return roomDAO.getRoomById(roomId);
    }

    /**
     * Gets a room by room number
     * @param roomNumber The room number
     * @return Room object if found, null otherwise
     * @throws SQLException if a database error occurs
     */
    public Room getRoomByNumber(String roomNumber) throws SQLException {
        return roomDAO.getRoomByNumber(roomNumber);
    }

    /**
     * Gets all rooms
     * @return List of all rooms
     * @throws SQLException if a database error occurs
     */
    public List<Room> getAllRooms() throws SQLException {
        return roomDAO.getAllRooms();
    }

    /**
     * Gets available rooms
     * @return List of available rooms
     * @throws SQLException if a database error occurs
     */
    public List<Room> getAvailableRooms() throws SQLException {
        return roomDAO.getAvailableRooms();
    }

    /**
     * Gets available rooms for specific dates
     * @param checkInDate Check-in date
     * @param checkOutDate Check-out date
     * @return List of available rooms
     * @throws SQLException if a database error occurs
     */
    public List<Room> getAvailableRoomsForDates(LocalDate checkInDate, LocalDate checkOutDate) throws SQLException {
        return roomDAO.getAvailableRoomsForDates(checkInDate, checkOutDate);
    }

    /**
     * Gets rooms by type
     * @param roomTypeId The room type ID
     * @return List of rooms of the specified type
     * @throws SQLException if a database error occurs
     */
    public List<Room> getRoomsByType(int roomTypeId) throws SQLException {
        return roomDAO.getRoomsByType(roomTypeId);
    }

    /**
     * Gets rooms by status
     * @param status The room status
     * @return List of rooms with the specified status
     * @throws SQLException if a database error occurs
     */
    public List<Room> getRoomsByStatus(String status) throws SQLException {
        return roomDAO.getRoomsByStatus(status);
    }

    /**
     * Gets paginated rooms
     * @param offset The offset for pagination
     * @param limit The limit for pagination
     * @return List of rooms for the current page
     * @throws SQLException if a database error occurs
     */
    public List<Room> getRoomsPaginated(int offset, int limit) throws SQLException {
        return roomDAO.getRoomsPaginated(offset, limit);
    }

    /**
     * Gets the total count of rooms
     * @return Total number of rooms
     * @throws SQLException if a database error occurs
     */
    public int getRoomCount() throws SQLException {
        return roomDAO.getRoomCount();
    }

    /**
     * Gets the count of rooms by status
     * @param status The room status
     * @return Count of rooms with the specified status
     * @throws SQLException if a database error occurs
     */
    public int getRoomCountByStatus(String status) throws SQLException {
        return roomDAO.getRoomCountByStatus(status);
    }

    /**
     * Updates a room
     * @param room The room to update
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean updateRoom(Room room) throws SQLException {
        // Check if room number already exists for a different room
        Room existingRoom = roomDAO.getRoomByNumber(room.getRoomNumber());
        if (existingRoom != null && existingRoom.getRoomId() != room.getRoomId()) {
            return false;
        }
        return roomDAO.updateRoom(room);
    }

    /**
     * Updates room status
     * @param roomId The room ID
     * @param status The new status
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean updateRoomStatus(int roomId, String status) throws SQLException {
        return roomDAO.updateRoomStatus(roomId, status);
    }

    /**
     * Deletes a room
     * @param roomId The ID of the room to delete
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean deleteRoom(int roomId) throws SQLException {
        return roomDAO.deleteRoom(roomId);
    }
}
