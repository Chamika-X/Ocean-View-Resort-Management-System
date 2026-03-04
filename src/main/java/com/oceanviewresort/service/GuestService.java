package com.oceanviewresort.service;

import java.sql.SQLException;
import java.util.List;

import com.oceanviewresort.dao.GuestDAO;
import com.oceanviewresort.model.Guest;

/**
 * Service class for Guest-related operations at Ocean View Resort.
 * Implements Singleton pattern and provides business logic for guest management.
 */
public class GuestService {

    private static GuestService instance;
    private GuestDAO guestDAO;

    /**
     * Private constructor for Singleton pattern
     */
    private GuestService() {
        this.guestDAO = new GuestDAO();
    }

    /**
     * Gets the singleton instance of GuestService
     * @return The GuestService instance
     */
    public static GuestService getInstance() {
        if (instance == null) {
            synchronized (GuestService.class) {
                if (instance == null) {
                    instance = new GuestService();
                }
            }
        }
        return instance;
    }

    /**
     * Adds a new guest
     * @param guest The guest to add
     * @return The generated guest ID, or -1 if failed
     * @throws SQLException if a database error occurs
     */
    public int addGuest(Guest guest) throws SQLException {
        return guestDAO.addGuest(guest);
    }

    /**
     * Gets a guest by ID
     * @param guestId The guest ID
     * @return Guest object if found, null otherwise
     * @throws SQLException if a database error occurs
     */
    public Guest getGuestById(int guestId) throws SQLException {
        return guestDAO.getGuestById(guestId);
    }

    /**
     * Gets a guest by contact number
     * @param contactNumber The contact number
     * @return Guest object if found, null otherwise
     * @throws SQLException if a database error occurs
     */
    public Guest getGuestByContactNumber(String contactNumber) throws SQLException {
        return guestDAO.getGuestByContactNumber(contactNumber);
    }

    /**
     * Gets all guests
     * @return List of all guests
     * @throws SQLException if a database error occurs
     */
    public List<Guest> getAllGuests() throws SQLException {
        return guestDAO.getAllGuests();
    }

    /**
     * Gets paginated guests
     * @param offset The offset for pagination
     * @param limit The limit for pagination
     * @return List of guests for the current page
     * @throws SQLException if a database error occurs
     */
    public List<Guest> getGuestsPaginated(int offset, int limit) throws SQLException {
        return guestDAO.getGuestsPaginated(offset, limit);
    }

    /**
     * Gets the total count of guests
     * @return Total number of guests
     * @throws SQLException if a database error occurs
     */
    public int getGuestCount() throws SQLException {
        return guestDAO.getGuestCount();
    }

    /**
     * Searches guests by name, contact, NIC/Passport, or email
     * @param searchTerm The search term
     * @return List of matching guests
     * @throws SQLException if a database error occurs
     */
    public List<Guest> searchGuests(String searchTerm) throws SQLException {
        return guestDAO.searchGuests(searchTerm);
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
        return guestDAO.searchGuestsPaginated(searchTerm, offset, limit);
    }

    /**
     * Gets the count of guests matching a search term
     * @param searchTerm The search term
     * @return Count of matching guests
     * @throws SQLException if a database error occurs
     */
    public int getGuestSearchCount(String searchTerm) throws SQLException {
        return guestDAO.getGuestSearchCount(searchTerm);
    }

    /**
     * Updates a guest
     * @param guest The guest to update
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean updateGuest(Guest guest) throws SQLException {
        return guestDAO.updateGuest(guest);
    }

    /**
     * Deletes a guest
     * @param guestId The ID of the guest to delete
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean deleteGuest(int guestId) throws SQLException {
        return guestDAO.deleteGuest(guestId);
    }
}
