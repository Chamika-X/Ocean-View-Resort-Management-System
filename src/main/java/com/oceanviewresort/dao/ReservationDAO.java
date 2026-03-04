package com.oceanviewresort.dao;

import java.sql.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.oceanviewresort.model.Reservation;
import com.oceanviewresort.model.Guest;
import com.oceanviewresort.model.Room;
import com.oceanviewresort.model.RoomType;

/**
 * Data Access Object for Reservation entity at Ocean View Resort.
 * Handles all database operations related to room reservations.
 */
public class ReservationDAO {

    /**
     * Adds a new reservation to the database
     * @param reservation The reservation to add
     * @return The generated reservation ID, or -1 if failed
     * @throws SQLException if a database error occurs
     */
    public int addReservation(Reservation reservation) throws SQLException {
        String query = "INSERT INTO Reservation (reservation_number, guest_id, room_id, check_in_date, " +
                       "check_out_date, number_of_nights, number_of_guests, total_amount, advance_payment, " +
                       "balance_amount, status, special_requests, created_by) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, reservation.getReservationNumber());
        statement.setInt(2, reservation.getGuestId());
        statement.setInt(3, reservation.getRoomId());
        statement.setDate(4, Date.valueOf(reservation.getCheckInDate()));
        statement.setDate(5, Date.valueOf(reservation.getCheckOutDate()));
        statement.setInt(6, reservation.getNumberOfNights());
        statement.setInt(7, reservation.getNumberOfGuests());
        statement.setBigDecimal(8, reservation.getTotalAmount());
        statement.setBigDecimal(9, reservation.getAdvancePayment());
        statement.setBigDecimal(10, reservation.getBalanceAmount());
        statement.setString(11, reservation.getStatus());
        statement.setString(12, reservation.getSpecialRequests());
        statement.setInt(13, reservation.getCreatedBy());

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
     * Gets a reservation by ID with full details
     * @param reservationId The reservation ID
     * @return Reservation object if found, null otherwise
     * @throws SQLException if a database error occurs
     */
    public Reservation getReservationById(int reservationId) throws SQLException {
        String query = "SELECT res.*, g.guest_name, g.address, g.contact_number, g.email, g.nic_passport, g.nationality, " +
                       "r.room_number, r.floor_number, r.status as room_status, r.description as room_description, " +
                       "rt.type_name, rt.rate_per_night, rt.max_occupancy, rt.description as type_description, " +
                       "u.username as created_by_username " +
                       "FROM Reservation res " +
                       "JOIN Guest g ON res.guest_id = g.guest_id " +
                       "JOIN Room r ON res.room_id = r.room_id " +
                       "JOIN RoomType rt ON r.room_type_id = rt.room_type_id " +
                       "LEFT JOIN User u ON res.created_by = u.user_id " +
                       "WHERE res.reservation_id = ?";
        
        Reservation reservation = null;

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, reservationId);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            reservation = mapResultSetToReservation(resultSet);
        }

        return reservation;
    }

    /**
     * Gets a reservation by reservation number
     * @param reservationNumber The reservation number
     * @return Reservation object if found, null otherwise
     * @throws SQLException if a database error occurs
     */
    public Reservation getReservationByNumber(String reservationNumber) throws SQLException {
        String query = "SELECT res.*, g.guest_name, g.address, g.contact_number, g.email, g.nic_passport, g.nationality, " +
                       "r.room_number, r.floor_number, r.status as room_status, r.description as room_description, " +
                       "rt.type_name, rt.rate_per_night, rt.max_occupancy, rt.description as type_description, " +
                       "u.username as created_by_username " +
                       "FROM Reservation res " +
                       "JOIN Guest g ON res.guest_id = g.guest_id " +
                       "JOIN Room r ON res.room_id = r.room_id " +
                       "JOIN RoomType rt ON r.room_type_id = rt.room_type_id " +
                       "LEFT JOIN User u ON res.created_by = u.user_id " +
                       "WHERE res.reservation_number = ?";
        
        Reservation reservation = null;

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, reservationNumber);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            reservation = mapResultSetToReservation(resultSet);
        }

        return reservation;
    }

    /**
     * Gets all reservations from the database
     * @return List of all reservations
     * @throws SQLException if a database error occurs
     */
    public List<Reservation> getAllReservations() throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT res.*, g.guest_name, g.address, g.contact_number, g.email, g.nic_passport, g.nationality, " +
                       "r.room_number, r.floor_number, r.status as room_status, r.description as room_description, " +
                       "rt.type_name, rt.rate_per_night, rt.max_occupancy, rt.description as type_description, " +
                       "u.username as created_by_username " +
                       "FROM Reservation res " +
                       "JOIN Guest g ON res.guest_id = g.guest_id " +
                       "JOIN Room r ON res.room_id = r.room_id " +
                       "JOIN RoomType rt ON r.room_type_id = rt.room_type_id " +
                       "LEFT JOIN User u ON res.created_by = u.user_id " +
                       "ORDER BY res.check_in_date DESC";

        Connection connection = DBConnectionFactory.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            reservations.add(mapResultSetToReservation(resultSet));
        }

        return reservations;
    }

    /**
     * Gets reservations by status
     * @param status The reservation status
     * @return List of reservations with the specified status
     * @throws SQLException if a database error occurs
     */
    public List<Reservation> getReservationsByStatus(String status) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT res.*, g.guest_name, g.address, g.contact_number, g.email, g.nic_passport, g.nationality, " +
                       "r.room_number, r.floor_number, r.status as room_status, r.description as room_description, " +
                       "rt.type_name, rt.rate_per_night, rt.max_occupancy, rt.description as type_description, " +
                       "u.username as created_by_username " +
                       "FROM Reservation res " +
                       "JOIN Guest g ON res.guest_id = g.guest_id " +
                       "JOIN Room r ON res.room_id = r.room_id " +
                       "JOIN RoomType rt ON r.room_type_id = rt.room_type_id " +
                       "LEFT JOIN User u ON res.created_by = u.user_id " +
                       "WHERE res.status = ? ORDER BY res.check_in_date DESC";

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, status);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            reservations.add(mapResultSetToReservation(resultSet));
        }

        return reservations;
    }

    /**
     * Gets today's check-ins
     * @return List of reservations checking in today
     * @throws SQLException if a database error occurs
     */
    public List<Reservation> getTodayCheckIns() throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT res.*, g.guest_name, g.address, g.contact_number, g.email, g.nic_passport, g.nationality, " +
                       "r.room_number, r.floor_number, r.status as room_status, r.description as room_description, " +
                       "rt.type_name, rt.rate_per_night, rt.max_occupancy, rt.description as type_description, " +
                       "u.username as created_by_username " +
                       "FROM Reservation res " +
                       "JOIN Guest g ON res.guest_id = g.guest_id " +
                       "JOIN Room r ON res.room_id = r.room_id " +
                       "JOIN RoomType rt ON r.room_type_id = rt.room_type_id " +
                       "LEFT JOIN User u ON res.created_by = u.user_id " +
                       "WHERE res.check_in_date = CURDATE() AND res.status = 'confirmed' " +
                       "ORDER BY res.created_at";

        Connection connection = DBConnectionFactory.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            reservations.add(mapResultSetToReservation(resultSet));
        }

        return reservations;
    }

    /**
     * Gets today's check-outs
     * @return List of reservations checking out today
     * @throws SQLException if a database error occurs
     */
    public List<Reservation> getTodayCheckOuts() throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT res.*, g.guest_name, g.address, g.contact_number, g.email, g.nic_passport, g.nationality, " +
                       "r.room_number, r.floor_number, r.status as room_status, r.description as room_description, " +
                       "rt.type_name, rt.rate_per_night, rt.max_occupancy, rt.description as type_description, " +
                       "u.username as created_by_username " +
                       "FROM Reservation res " +
                       "JOIN Guest g ON res.guest_id = g.guest_id " +
                       "JOIN Room r ON res.room_id = r.room_id " +
                       "JOIN RoomType rt ON r.room_type_id = rt.room_type_id " +
                       "LEFT JOIN User u ON res.created_by = u.user_id " +
                       "WHERE res.check_out_date = CURDATE() AND res.status = 'checked_in' " +
                       "ORDER BY res.created_at";

        Connection connection = DBConnectionFactory.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            reservations.add(mapResultSetToReservation(resultSet));
        }

        return reservations;
    }

    /**
     * Gets paginated reservations from the database
     * @param offset The offset for pagination
     * @param limit The limit for pagination
     * @return List of reservations for the current page
     * @throws SQLException if a database error occurs
     */
    public List<Reservation> getReservationsPaginated(int offset, int limit) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT res.*, g.guest_name, g.address, g.contact_number, g.email, g.nic_passport, g.nationality, " +
                       "r.room_number, r.floor_number, r.status as room_status, r.description as room_description, " +
                       "rt.type_name, rt.rate_per_night, rt.max_occupancy, rt.description as type_description, " +
                       "u.username as created_by_username " +
                       "FROM Reservation res " +
                       "JOIN Guest g ON res.guest_id = g.guest_id " +
                       "JOIN Room r ON res.room_id = r.room_id " +
                       "JOIN RoomType rt ON r.room_type_id = rt.room_type_id " +
                       "LEFT JOIN User u ON res.created_by = u.user_id " +
                       "ORDER BY res.check_in_date DESC LIMIT ? OFFSET ?";

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, limit);
        statement.setInt(2, offset);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            reservations.add(mapResultSetToReservation(resultSet));
        }

        return reservations;
    }

    /**
     * Searches reservations by reservation number, guest name, or contact
     * @param searchTerm The search term
     * @return List of matching reservations
     * @throws SQLException if a database error occurs
     */
    public List<Reservation> searchReservations(String searchTerm) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT res.*, g.guest_name, g.address, g.contact_number, g.email, g.nic_passport, g.nationality, " +
                       "r.room_number, r.floor_number, r.status as room_status, r.description as room_description, " +
                       "rt.type_name, rt.rate_per_night, rt.max_occupancy, rt.description as type_description, " +
                       "u.username as created_by_username " +
                       "FROM Reservation res " +
                       "JOIN Guest g ON res.guest_id = g.guest_id " +
                       "JOIN Room r ON res.room_id = r.room_id " +
                       "JOIN RoomType rt ON r.room_type_id = rt.room_type_id " +
                       "LEFT JOIN User u ON res.created_by = u.user_id " +
                       "WHERE res.reservation_number LIKE ? OR g.guest_name LIKE ? OR g.contact_number LIKE ? OR r.room_number LIKE ? " +
                       "ORDER BY res.check_in_date DESC";

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        String searchPattern = "%" + searchTerm + "%";
        statement.setString(1, searchPattern);
        statement.setString(2, searchPattern);
        statement.setString(3, searchPattern);
        statement.setString(4, searchPattern);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            reservations.add(mapResultSetToReservation(resultSet));
        }

        return reservations;
    }

    /**
     * Searches reservations with pagination
     * @param searchTerm The search term
     * @param offset The offset for pagination
     * @param limit The limit for pagination
     * @return List of matching reservations for the current page
     * @throws SQLException if a database error occurs
     */
    public List<Reservation> searchReservationsPaginated(String searchTerm, int offset, int limit) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT res.*, g.guest_name, g.address, g.contact_number, g.email, g.nic_passport, g.nationality, " +
                       "r.room_number, r.floor_number, r.status as room_status, r.description as room_description, " +
                       "rt.type_name, rt.rate_per_night, rt.max_occupancy, rt.description as type_description, " +
                       "u.username as created_by_username " +
                       "FROM Reservation res " +
                       "JOIN Guest g ON res.guest_id = g.guest_id " +
                       "JOIN Room r ON res.room_id = r.room_id " +
                       "JOIN RoomType rt ON r.room_type_id = rt.room_type_id " +
                       "LEFT JOIN User u ON res.created_by = u.user_id " +
                       "WHERE res.reservation_number LIKE ? OR g.guest_name LIKE ? OR g.contact_number LIKE ? OR r.room_number LIKE ? " +
                       "ORDER BY res.check_in_date DESC LIMIT ? OFFSET ?";

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        String searchPattern = "%" + searchTerm + "%";
        statement.setString(1, searchPattern);
        statement.setString(2, searchPattern);
        statement.setString(3, searchPattern);
        statement.setString(4, searchPattern);
        statement.setInt(5, limit);
        statement.setInt(6, offset);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            reservations.add(mapResultSetToReservation(resultSet));
        }

        return reservations;
    }

    /**
     * Gets the count of reservations matching a search term
     * @param searchTerm The search term
     * @return Count of matching reservations
     * @throws SQLException if a database error occurs
     */
    public int getReservationSearchCount(String searchTerm) throws SQLException {
        String query = "SELECT COUNT(*) FROM Reservation res " +
                       "JOIN Guest g ON res.guest_id = g.guest_id " +
                       "JOIN Room r ON res.room_id = r.room_id " +
                       "WHERE res.reservation_number LIKE ? OR g.guest_name LIKE ? OR g.contact_number LIKE ? OR r.room_number LIKE ?";

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        String searchPattern = "%" + searchTerm + "%";
        statement.setString(1, searchPattern);
        statement.setString(2, searchPattern);
        statement.setString(3, searchPattern);
        statement.setString(4, searchPattern);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getInt(1);
        }

        return 0;
    }

    /**
     * Gets the total count of reservations
     * @return Total number of reservations
     * @throws SQLException if a database error occurs
     */
    public int getReservationCount() throws SQLException {
        String query = "SELECT COUNT(*) FROM Reservation";

        Connection connection = DBConnectionFactory.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        if (resultSet.next()) {
            return resultSet.getInt(1);
        }

        return 0;
    }

    /**
     * Gets the count of reservations by status
     * @param status The reservation status
     * @return Count of reservations with the specified status
     * @throws SQLException if a database error occurs
     */
    public int getReservationCountByStatus(String status) throws SQLException {
        String query = "SELECT COUNT(*) FROM Reservation WHERE status = ?";

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
     * Gets the next reservation number
     * @return Next reservation number in format RES[YEAR][SEQUENCE]
     * @throws SQLException if a database error occurs
     */
    public String generateReservationNumber() throws SQLException {
        String query = "SELECT MAX(CAST(SUBSTRING(reservation_number, 8) AS UNSIGNED)) as max_num " +
                       "FROM Reservation WHERE reservation_number LIKE ?";
        
        int year = LocalDate.now().getYear();
        String prefix = "RES" + year;

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, prefix + "%");
        ResultSet resultSet = statement.executeQuery();

        int nextNum = 1;
        if (resultSet.next()) {
            int maxNum = resultSet.getInt("max_num");
            if (maxNum > 0) {
                nextNum = maxNum + 1;
            }
        }

        return String.format("%s%04d", prefix, nextNum);
    }

    /**
     * Updates a reservation
     * @param reservation The reservation to update
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean updateReservation(Reservation reservation) throws SQLException {
        String query = "UPDATE Reservation SET guest_id = ?, room_id = ?, check_in_date = ?, " +
                       "check_out_date = ?, number_of_nights = ?, number_of_guests = ?, total_amount = ?, " +
                       "advance_payment = ?, balance_amount = ?, status = ?, special_requests = ? " +
                       "WHERE reservation_id = ?";

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, reservation.getGuestId());
        statement.setInt(2, reservation.getRoomId());
        statement.setDate(3, Date.valueOf(reservation.getCheckInDate()));
        statement.setDate(4, Date.valueOf(reservation.getCheckOutDate()));
        statement.setInt(5, reservation.getNumberOfNights());
        statement.setInt(6, reservation.getNumberOfGuests());
        statement.setBigDecimal(7, reservation.getTotalAmount());
        statement.setBigDecimal(8, reservation.getAdvancePayment());
        statement.setBigDecimal(9, reservation.getBalanceAmount());
        statement.setString(10, reservation.getStatus());
        statement.setString(11, reservation.getSpecialRequests());
        statement.setInt(12, reservation.getReservationId());

        int rowsUpdated = statement.executeUpdate();
        return rowsUpdated > 0;
    }

    /**
     * Updates reservation status
     * @param reservationId The reservation ID
     * @param status The new status
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean updateReservationStatus(int reservationId, String status) throws SQLException {
        String query = "UPDATE Reservation SET status = ? WHERE reservation_id = ?";

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, status);
        statement.setInt(2, reservationId);

        int rowsUpdated = statement.executeUpdate();
        return rowsUpdated > 0;
    }

    /**
     * Gets the active reservation for a specific room (confirmed or checked_in)
     * @param roomId The room ID
     * @return Reservation object if found, null otherwise
     * @throws SQLException if a database error occurs
     */
    public Reservation getActiveReservationByRoomId(int roomId) throws SQLException {
        String query = "SELECT res.*, g.guest_name, g.address, g.contact_number, g.email, g.nic_passport, g.nationality, " +
                       "r.room_number, r.floor_number, r.status AS room_status, r.description AS room_description, " +
                       "rt.type_name, rt.rate_per_night, rt.max_occupancy, rt.description AS type_description, " +
                       "u.username AS created_by_username " +
                       "FROM Reservation res " +
                       "JOIN Guest g ON res.guest_id = g.guest_id " +
                       "JOIN Room r ON res.room_id = r.room_id " +
                       "JOIN RoomType rt ON r.room_type_id = rt.room_type_id " +
                       "LEFT JOIN User u ON res.created_by = u.user_id " +
                       "WHERE res.room_id = ? AND res.status IN ('confirmed', 'checked_in') " +
                       "ORDER BY res.created_at DESC LIMIT 1";

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, roomId);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return mapResultSetToReservation(resultSet);
        }

        return null;
    }

    /**
     * Deletes a reservation
     * @param reservationId The ID of the reservation to delete
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean deleteReservation(int reservationId) throws SQLException {
        String query = "DELETE FROM Reservation WHERE reservation_id = ?";

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, reservationId);

        int rowsDeleted = statement.executeUpdate();
        return rowsDeleted > 0;
    }

    /**
     * Gets total revenue for a date range
     * @param startDate Start date
     * @param endDate End date
     * @return Total revenue
     * @throws SQLException if a database error occurs
     */
    public BigDecimal getTotalRevenue(LocalDate startDate, LocalDate endDate) throws SQLException {
        String query = "SELECT COALESCE(SUM(total_amount), 0) as total FROM Reservation " +
                       "WHERE status != 'cancelled' AND check_in_date BETWEEN ? AND ?";

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setDate(1, Date.valueOf(startDate));
        statement.setDate(2, Date.valueOf(endDate));
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getBigDecimal("total");
        }

        return BigDecimal.ZERO;
    }

    /**
     * Maps a ResultSet row to a Reservation object with Guest and Room details
     * @param resultSet The ResultSet to map
     * @return Reservation object
     * @throws SQLException if a database error occurs
     */
    private Reservation mapResultSetToReservation(ResultSet resultSet) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setReservationId(resultSet.getInt("reservation_id"));
        reservation.setReservationNumber(resultSet.getString("reservation_number"));
        reservation.setGuestId(resultSet.getInt("guest_id"));
        reservation.setRoomId(resultSet.getInt("room_id"));
        reservation.setCheckInDate(resultSet.getDate("check_in_date").toLocalDate());
        reservation.setCheckOutDate(resultSet.getDate("check_out_date").toLocalDate());
        reservation.setNumberOfNights(resultSet.getInt("number_of_nights"));
        reservation.setNumberOfGuests(resultSet.getInt("number_of_guests"));
        reservation.setTotalAmount(resultSet.getBigDecimal("total_amount"));
        reservation.setAdvancePayment(resultSet.getBigDecimal("advance_payment"));
        reservation.setBalanceAmount(resultSet.getBigDecimal("balance_amount"));
        reservation.setStatus(resultSet.getString("status"));
        reservation.setSpecialRequests(resultSet.getString("special_requests"));
        reservation.setCreatedBy(resultSet.getInt("created_by"));
        reservation.setCreatedAt(resultSet.getTimestamp("created_at"));
        reservation.setUpdatedAt(resultSet.getTimestamp("updated_at"));

        // Set created by username
        try {
            reservation.setCreatedByUsername(resultSet.getString("created_by_username"));
        } catch (SQLException e) {
            // Column not in result set
        }

        // Set Guest details
        try {
            Guest guest = new Guest();
            guest.setGuestId(resultSet.getInt("guest_id"));
            guest.setGuestName(resultSet.getString("guest_name"));
            guest.setAddress(resultSet.getString("address"));
            guest.setContactNumber(resultSet.getString("contact_number"));
            guest.setEmail(resultSet.getString("email"));
            guest.setNicPassport(resultSet.getString("nic_passport"));
            guest.setNationality(resultSet.getString("nationality"));
            reservation.setGuest(guest);
        } catch (SQLException e) {
            // Guest columns not in result set
        }

        // Set Room details
        try {
            Room room = new Room();
            room.setRoomId(resultSet.getInt("room_id"));
            room.setRoomNumber(resultSet.getString("room_number"));
            room.setFloorNumber(resultSet.getInt("floor_number"));
            room.setStatus(resultSet.getString("room_status"));
            room.setDescription(resultSet.getString("room_description"));

            // Set RoomType
            RoomType roomType = new RoomType();
            roomType.setTypeName(resultSet.getString("type_name"));
            roomType.setRatePerNight(resultSet.getDouble("rate_per_night"));
            roomType.setMaxOccupancy(resultSet.getInt("max_occupancy"));
            roomType.setDescription(resultSet.getString("type_description"));
            room.setRoomType(roomType);

            reservation.setRoom(room);
        } catch (SQLException e) {
            // Room columns not in result set
        }

        return reservation;
    }
}
