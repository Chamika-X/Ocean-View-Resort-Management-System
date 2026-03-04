package com.oceanviewresort.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.oceanviewresort.dao.ReservationDAO;
import com.oceanviewresort.dao.RoomDAO;
import com.oceanviewresort.dao.BillDAO;
import com.oceanviewresort.model.Reservation;
import com.oceanviewresort.model.Room;
import com.oceanviewresort.model.Bill;

/**
 * Service class for Reservation-related operations at Ocean View Resort.
 * Implements Singleton pattern and provides business logic for reservation management.
 */
public class ReservationService {

    private static ReservationService instance;
    private ReservationDAO reservationDAO;
    private RoomDAO roomDAO;
    private BillDAO billDAO;

    /**
     * Private constructor for Singleton pattern
     */
    private ReservationService() {
        this.reservationDAO = new ReservationDAO();
        this.roomDAO = new RoomDAO();
        this.billDAO = new BillDAO();
    }

    /**
     * Gets the singleton instance of ReservationService
     * @return The ReservationService instance
     */
    public static ReservationService getInstance() {
        if (instance == null) {
            synchronized (ReservationService.class) {
                if (instance == null) {
                    instance = new ReservationService();
                }
            }
        }
        return instance;
    }

    /**
     * Creates a new reservation with automatic reservation number generation
     * @param reservation The reservation to create
     * @return The generated reservation ID, or -1 if failed
     * @throws SQLException if a database error occurs
     */
    public int createReservation(Reservation reservation) throws SQLException {
        // Generate reservation number
        String reservationNumber = reservationDAO.generateReservationNumber();
        reservation.setReservationNumber(reservationNumber);

        // Get room rate and calculate total
        Room room = roomDAO.getRoomById(reservation.getRoomId());
        if (room != null && room.getRoomType() != null) {
            reservation.calculateTotalAmount(room.getRoomType().getRatePerNight());
        }

        // Create reservation
        int reservationId = reservationDAO.addReservation(reservation);

        // Update room status to reserved if dates overlap with today
        if (reservationId > 0) {
            LocalDate today = LocalDate.now();
            if (!reservation.getCheckInDate().isAfter(today) && !reservation.getCheckOutDate().isBefore(today)) {
                roomDAO.updateRoomStatus(reservation.getRoomId(), Room.STATUS_RESERVED);
            }
        }

        return reservationId;
    }

    /**
     * Gets a reservation by ID
     * @param reservationId The reservation ID
     * @return Reservation object if found, null otherwise
     * @throws SQLException if a database error occurs
     */
    public Reservation getReservationById(int reservationId) throws SQLException {
        return reservationDAO.getReservationById(reservationId);
    }

    /**
     * Gets a reservation by reservation number
     * @param reservationNumber The reservation number
     * @return Reservation object if found, null otherwise
     * @throws SQLException if a database error occurs
     */
    public Reservation getReservationByNumber(String reservationNumber) throws SQLException {
        return reservationDAO.getReservationByNumber(reservationNumber);
    }

    /**
     * Gets all reservations
     * @return List of all reservations
     * @throws SQLException if a database error occurs
     */
    public List<Reservation> getAllReservations() throws SQLException {
        return reservationDAO.getAllReservations();
    }

    /**
     * Gets reservations by status
     * @param status The reservation status
     * @return List of reservations with the specified status
     * @throws SQLException if a database error occurs
     */
    public List<Reservation> getReservationsByStatus(String status) throws SQLException {
        return reservationDAO.getReservationsByStatus(status);
    }

    /**
     * Gets today's check-ins
     * @return List of reservations checking in today
     * @throws SQLException if a database error occurs
     */
    public List<Reservation> getTodayCheckIns() throws SQLException {
        return reservationDAO.getTodayCheckIns();
    }

    /**
     * Gets today's check-outs
     * @return List of reservations checking out today
     * @throws SQLException if a database error occurs
     */
    public List<Reservation> getTodayCheckOuts() throws SQLException {
        return reservationDAO.getTodayCheckOuts();
    }

    /**
     * Gets paginated reservations
     * @param offset The offset for pagination
     * @param limit The limit for pagination
     * @return List of reservations for the current page
     * @throws SQLException if a database error occurs
     */
    public List<Reservation> getReservationsPaginated(int offset, int limit) throws SQLException {
        return reservationDAO.getReservationsPaginated(offset, limit);
    }

    /**
     * Searches reservations
     * @param searchTerm The search term
     * @return List of matching reservations
     * @throws SQLException if a database error occurs
     */
    public List<Reservation> searchReservations(String searchTerm) throws SQLException {
        return reservationDAO.searchReservations(searchTerm);
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
        return reservationDAO.searchReservationsPaginated(searchTerm, offset, limit);
    }

    /**
     * Gets the count of reservations matching a search term
     * @param searchTerm The search term
     * @return Count of matching reservations
     * @throws SQLException if a database error occurs
     */
    public int getReservationSearchCount(String searchTerm) throws SQLException {
        return reservationDAO.getReservationSearchCount(searchTerm);
    }

    /**
     * Gets the total count of reservations
     * @return Total number of reservations
     * @throws SQLException if a database error occurs
     */
    public int getReservationCount() throws SQLException {
        return reservationDAO.getReservationCount();
    }

    /**
     * Gets the count of reservations by status
     * @param status The reservation status
     * @return Count of reservations with the specified status
     * @throws SQLException if a database error occurs
     */
    public int getReservationCountByStatus(String status) throws SQLException {
        return reservationDAO.getReservationCountByStatus(status);
    }

    /**
     * Updates a reservation
     * @param reservation The reservation to update
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean updateReservation(Reservation reservation) throws SQLException {
        // Recalculate total if room changed
        Room room = roomDAO.getRoomById(reservation.getRoomId());
        if (room != null && room.getRoomType() != null) {
            reservation.calculateTotalAmount(room.getRoomType().getRatePerNight());
        }

        return reservationDAO.updateReservation(reservation);
    }

    /**
     * Performs check-in for a reservation
     * @param reservationId The reservation ID
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean checkIn(int reservationId) throws SQLException {
        Reservation reservation = reservationDAO.getReservationById(reservationId);
        if (reservation == null) {
            return false;
        }

        // Update reservation status
        boolean updated = reservationDAO.updateReservationStatus(reservationId, Reservation.STATUS_CHECKED_IN);

        // Update room status to occupied
        if (updated) {
            roomDAO.updateRoomStatus(reservation.getRoomId(), Room.STATUS_OCCUPIED);
        }

        return updated;
    }

    /**
     * Performs check-out for a reservation and generates bill
     * @param reservationId The reservation ID
     * @param additionalCharges Additional charges (minibar, room service, etc.)
     * @param discountAmount Discount amount if any
     * @return Generated Bill object, or null if failed
     * @throws SQLException if a database error occurs
     */
    public Bill checkOut(int reservationId, BigDecimal additionalCharges, BigDecimal discountAmount) throws SQLException {
        Reservation reservation = reservationDAO.getReservationById(reservationId);
        if (reservation == null) {
            return null;
        }

        // Update reservation status
        boolean updated = reservationDAO.updateReservationStatus(reservationId, Reservation.STATUS_CHECKED_OUT);

        if (!updated) {
            return null;
        }

        // Update room status to available
        roomDAO.updateRoomStatus(reservation.getRoomId(), Room.STATUS_AVAILABLE);

        // Generate bill
        Bill bill = new Bill();
        bill.setBillNumber(billDAO.generateBillNumber());
        bill.setReservationId(reservationId);
        bill.setRoomCharges(reservation.getTotalAmount());
        bill.setAdditionalCharges(additionalCharges != null ? additionalCharges : BigDecimal.ZERO);
        bill.setDiscountAmount(discountAmount != null ? discountAmount : BigDecimal.ZERO);
        bill.calculateTotal();
        bill.setReservation(reservation);

        int billId = billDAO.addBill(bill);
        if (billId > 0) {
            bill.setBillId(billId);
            return bill;
        }

        return null;
    }

    /**
     * Cancels a reservation
     * @param reservationId The reservation ID
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean cancelReservation(int reservationId) throws SQLException {
        Reservation reservation = reservationDAO.getReservationById(reservationId);
        if (reservation == null) {
            return false;
        }

        // Update reservation status
        boolean updated = reservationDAO.updateReservationStatus(reservationId, Reservation.STATUS_CANCELLED);

        // Update room status to available if it was reserved
        if (updated) {
            Room room = roomDAO.getRoomById(reservation.getRoomId());
            if (room != null && Room.STATUS_RESERVED.equals(room.getStatus())) {
                roomDAO.updateRoomStatus(reservation.getRoomId(), Room.STATUS_AVAILABLE);
            }
        }

        return updated;
    }

    /**
     * Gets the active reservation for a specific room
     * @param roomId The room ID
     * @return Reservation if found, null otherwise
     * @throws SQLException if a database error occurs
     */
    public Reservation getActiveReservationByRoomId(int roomId) throws SQLException {
        return reservationDAO.getActiveReservationByRoomId(roomId);
    }

    /**
     * Deletes a reservation
     * @param reservationId The ID of the reservation to delete
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean deleteReservation(int reservationId) throws SQLException {
        return reservationDAO.deleteReservation(reservationId);
    }

    /**
     * Gets total revenue for a date range
     * @param startDate Start date
     * @param endDate End date
     * @return Total revenue
     * @throws SQLException if a database error occurs
     */
    public BigDecimal getTotalRevenue(LocalDate startDate, LocalDate endDate) throws SQLException {
        return reservationDAO.getTotalRevenue(startDate, endDate);
    }
}
