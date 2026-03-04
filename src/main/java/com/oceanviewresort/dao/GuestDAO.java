package com.oceanviewresort.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.oceanviewresort.model.Guest;

/**
 * Data Access Object for Guest entity at Ocean View Resort.
 * Handles all database operations related to hotel guests.
 */
public class GuestDAO {

    /**
     * Adds a new guest to the database
     * @param guest The guest to add
     * @return The generated guest ID, or -1 if failed
     * @throws SQLException if a database error occurs
     */
    public int addGuest(Guest guest) throws SQLException {
        String query = "INSERT INTO Guest (guest_name, address, contact_number, email, nic_passport, nationality) " +
                       "VALUES (?, ?, ?, ?, ?, ?)";

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, guest.getGuestName());
        statement.setString(2, guest.getAddress());
        statement.setString(3, guest.getContactNumber());
        statement.setString(4, guest.getEmail());
        statement.setString(5, guest.getNicPassport());
        statement.setString(6, guest.getNationality());

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
     * Gets a guest by ID
     * @param guestId The guest ID
     * @return Guest object if found, null otherwise
     * @throws SQLException if a database error occurs
     */
    public Guest getGuestById(int guestId) throws SQLException {
        String query = "SELECT * FROM Guest WHERE guest_id = ?";
        Guest guest = null;

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, guestId);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            guest = mapResultSetToGuest(resultSet);
        }

        return guest;
    }

    /**
     * Gets a guest by contact number
     * @param contactNumber The contact number
     * @return Guest object if found, null otherwise
     * @throws SQLException if a database error occurs
     */
    public Guest getGuestByContactNumber(String contactNumber) throws SQLException {
        String query = "SELECT * FROM Guest WHERE contact_number = ?";
        Guest guest = null;

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, contactNumber);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            guest = mapResultSetToGuest(resultSet);
        }

        return guest;
    }

    /**
     * Gets all guests from the database
     * @return List of all guests
     * @throws SQLException if a database error occurs
     */
    public List<Guest> getAllGuests() throws SQLException {
        List<Guest> guests = new ArrayList<>();
        String query = "SELECT * FROM Guest ORDER BY guest_name";

        Connection connection = DBConnectionFactory.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            guests.add(mapResultSetToGuest(resultSet));
        }

        return guests;
    }

    /**
     * Gets paginated guests from the database
     * @param offset The offset for pagination
     * @param limit The limit for pagination
     * @return List of guests for the current page
     * @throws SQLException if a database error occurs
     */
    public List<Guest> getGuestsPaginated(int offset, int limit) throws SQLException {
        List<Guest> guests = new ArrayList<>();
        String query = "SELECT * FROM Guest ORDER BY guest_name LIMIT ? OFFSET ?";

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, limit);
        statement.setInt(2, offset);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            guests.add(mapResultSetToGuest(resultSet));
        }

        return guests;
    }

    /**
     * Gets the total count of guests
     * @return Total number of guests
     * @throws SQLException if a database error occurs
     */
    public int getGuestCount() throws SQLException {
        String query = "SELECT COUNT(*) FROM Guest";

        Connection connection = DBConnectionFactory.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        if (resultSet.next()) {
            return resultSet.getInt(1);
        }

        return 0;
    }

    /**
     * Searches guests by name, contact number, or NIC/Passport
     * @param searchTerm The search term
     * @return List of matching guests
     * @throws SQLException if a database error occurs
     */
    public List<Guest> searchGuests(String searchTerm) throws SQLException {
        List<Guest> guests = new ArrayList<>();
        String query = "SELECT * FROM Guest WHERE guest_name LIKE ? OR contact_number LIKE ? " +
                       "OR nic_passport LIKE ? OR email LIKE ? ORDER BY guest_name";

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        String searchPattern = "%" + searchTerm + "%";
        statement.setString(1, searchPattern);
        statement.setString(2, searchPattern);
        statement.setString(3, searchPattern);
        statement.setString(4, searchPattern);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            guests.add(mapResultSetToGuest(resultSet));
        }

        return guests;
    }

    /**
     * Searches guests with pagination
     * @param searchTerm The search term
     * @param offset The offset for pagination
     * @param limit The limit for pagination
     * @return List of matching guests for the current page
     * @throws SQLException if a database error occurs
     */
    public List<Guest> searchGuestsPaginated(String searchTerm, int offset, int limit) throws SQLException {
        List<Guest> guests = new ArrayList<>();
        String query = "SELECT * FROM Guest WHERE guest_name LIKE ? OR contact_number LIKE ? " +
                       "OR nic_passport LIKE ? OR email LIKE ? ORDER BY guest_name LIMIT ? OFFSET ?";

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
            guests.add(mapResultSetToGuest(resultSet));
        }

        return guests;
    }

    /**
     * Gets the count of guests matching a search term
     * @param searchTerm The search term
     * @return Count of matching guests
     * @throws SQLException if a database error occurs
     */
    public int getGuestSearchCount(String searchTerm) throws SQLException {
        String query = "SELECT COUNT(*) FROM Guest WHERE guest_name LIKE ? OR contact_number LIKE ? " +
                       "OR nic_passport LIKE ? OR email LIKE ?";

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
     * Updates a guest
     * @param guest The guest to update
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean updateGuest(Guest guest) throws SQLException {
        String query = "UPDATE Guest SET guest_name = ?, address = ?, contact_number = ?, " +
                       "email = ?, nic_passport = ?, nationality = ? WHERE guest_id = ?";

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, guest.getGuestName());
        statement.setString(2, guest.getAddress());
        statement.setString(3, guest.getContactNumber());
        statement.setString(4, guest.getEmail());
        statement.setString(5, guest.getNicPassport());
        statement.setString(6, guest.getNationality());
        statement.setInt(7, guest.getGuestId());

        int rowsUpdated = statement.executeUpdate();
        return rowsUpdated > 0;
    }

    /**
     * Deletes a guest
     * @param guestId The ID of the guest to delete
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean deleteGuest(int guestId) throws SQLException {
        String query = "DELETE FROM Guest WHERE guest_id = ?";

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, guestId);

        int rowsDeleted = statement.executeUpdate();
        return rowsDeleted > 0;
    }

    /**
     * Maps a ResultSet row to a Guest object
     * @param resultSet The ResultSet to map
     * @return Guest object
     * @throws SQLException if a database error occurs
     */
    private Guest mapResultSetToGuest(ResultSet resultSet) throws SQLException {
        Guest guest = new Guest();
        guest.setGuestId(resultSet.getInt("guest_id"));
        guest.setGuestName(resultSet.getString("guest_name"));
        guest.setAddress(resultSet.getString("address"));
        guest.setContactNumber(resultSet.getString("contact_number"));
        guest.setEmail(resultSet.getString("email"));
        guest.setNicPassport(resultSet.getString("nic_passport"));
        guest.setNationality(resultSet.getString("nationality"));
        guest.setCreatedAt(resultSet.getTimestamp("created_at"));
        guest.setUpdatedAt(resultSet.getTimestamp("updated_at"));
        return guest;
    }
}
