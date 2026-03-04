package com.oceanviewresort.service;

import com.oceanviewresort.dao.ActivityDAO;
import com.oceanviewresort.model.Activity;

import java.sql.SQLException;
import java.util.List;

/**
 * Service class for managing system activities and events at Ocean View Resort.
 * Provides business logic for activity tracking and retrieval.
 */
public class ActivityService {

    private ActivityDAO activityDAO;

    public ActivityService() {
        this.activityDAO = new ActivityDAO();
    }

    /**
     * Initialize the activity system (create table if needed)
     */
    public void initialize() throws SQLException {
        activityDAO.createTableIfNotExists();
    }

    /**
     * Log a new activity
     */
    public boolean logActivity(Activity activity) throws SQLException {
        return activityDAO.createActivity(activity);
    }

    // ================== Guest Activities ==================

    /**
     * Log guest registration activity
     */
    public boolean logGuestAdded(int guestId, String guestName, String username) throws SQLException {
        Activity activity = new Activity(Activity.TYPE_GUEST_ADDED, "New guest registered", "guest", guestId,
                guestName, username);
        return logActivity(activity);
    }

    /**
     * Log guest update activity
     */
    public boolean logGuestUpdated(int guestId, String guestName, String username) throws SQLException {
        Activity activity = new Activity(Activity.TYPE_GUEST_UPDATED, "Guest information updated", "guest",
                guestId, guestName, username);
        return logActivity(activity);
    }

    /**
     * Log guest deletion activity
     */
    public boolean logGuestDeleted(int guestId, String guestName, String username) throws SQLException {
        Activity activity = new Activity(Activity.TYPE_GUEST_DELETED, "Guest record deleted", "guest",
                guestId, guestName, username);
        return logActivity(activity);
    }

    // ================== Room Activities ==================

    /**
     * Log room status change activity
     */
    public boolean logRoomStatusChanged(int roomId, String roomNumber, String newStatus, String username) throws SQLException {
        Activity activity = new Activity(Activity.TYPE_ROOM_STATUS_CHANGED, 
                "Room status changed to " + newStatus, "room", roomId, roomNumber, username);
        return logActivity(activity);
    }

    // ================== Reservation Activities ==================

    /**
     * Log reservation creation activity
     */
    public boolean logReservationCreated(int reservationId, String reservationNumber, String guestName, String username) throws SQLException {
        Activity activity = new Activity(Activity.TYPE_RESERVATION_CREATED, 
                "New reservation created for " + guestName, "reservation", reservationId, reservationNumber, username);
        return logActivity(activity);
    }

    /**
     * Log reservation update activity
     */
    public boolean logReservationUpdated(int reservationId, String reservationNumber, String username) throws SQLException {
        Activity activity = new Activity(Activity.TYPE_RESERVATION_UPDATED, 
                "Reservation updated", "reservation", reservationId, reservationNumber, username);
        return logActivity(activity);
    }

    /**
     * Log reservation cancellation activity
     */
    public boolean logReservationCancelled(int reservationId, String reservationNumber, String username) throws SQLException {
        Activity activity = new Activity(Activity.TYPE_RESERVATION_CANCELLED, 
                "Reservation cancelled", "reservation", reservationId, reservationNumber, username);
        return logActivity(activity);
    }

    /**
     * Log check-in activity
     */
    public boolean logCheckIn(int reservationId, String reservationNumber, String guestName, String roomNumber, String username) throws SQLException {
        Activity activity = new Activity(Activity.TYPE_CHECK_IN, 
                guestName + " checked in to Room " + roomNumber, "reservation", reservationId, reservationNumber, username);
        return logActivity(activity);
    }

    /**
     * Log check-out activity
     */
    public boolean logCheckOut(int reservationId, String reservationNumber, String guestName, String roomNumber, String username) throws SQLException {
        Activity activity = new Activity(Activity.TYPE_CHECK_OUT, 
                guestName + " checked out from Room " + roomNumber, "reservation", reservationId, reservationNumber, username);
        return logActivity(activity);
    }

    // ================== Bill Activities ==================

    /**
     * Log bill generation activity
     */
    public boolean logBillGenerated(int billId, String billNumber, String guestName, String username) throws SQLException {
        Activity activity = new Activity(Activity.TYPE_BILL_GENERATED, 
                "Bill generated for " + guestName, "bill", billId, billNumber, username);
        return logActivity(activity);
    }

    /**
     * Log bill payment activity
     */
    public boolean logBillPaid(int billId, String billNumber, String paymentMethod, String username) throws SQLException {
        Activity activity = new Activity(Activity.TYPE_BILL_PAID, 
                "Bill paid via " + paymentMethod, "bill", billId, billNumber, username);
        return logActivity(activity);
    }

    // ================== User Activities ==================

    /**
     * Log user login activity
     */
    public boolean logUserLogin(String username) throws SQLException {
        Activity activity = new Activity(Activity.TYPE_USER_LOGIN, "User logged in", username);
        return logActivity(activity);
    }

    /**
     * Log user logout activity
     */
    public boolean logUserLogout(String username) throws SQLException {
        Activity activity = new Activity(Activity.TYPE_USER_LOGOUT, "User logged out", username);
        return logActivity(activity);
    }

    // ================== Legacy Billing System Activities (for backward compatibility) ==================

    /**
     * Log customer addition activity (legacy)
     */
    public boolean logCustomerAdded(int customerId, String customerName, String username) throws SQLException {
        Activity activity = new Activity("customer_added", "New customer added", "customer", customerId,
                customerName, username);
        return logActivity(activity);
    }

    /**
     * Log customer update activity (legacy)
     */
    public boolean logCustomerUpdated(int customerId, String customerName, String username) throws SQLException {
        Activity activity = new Activity("customer_updated", "Customer information updated", "customer",
                customerId, customerName, username);
        return logActivity(activity);
    }

    /**
     * Log customer deletion activity (legacy)
     */
    public boolean logCustomerDeleted(int customerId, String customerName, String username) throws SQLException {
        Activity activity = new Activity("customer_deleted", "Customer record deleted", "customer",
                customerId, customerName, username);
        return logActivity(activity);
    }

    /**
     * Log product addition activity (legacy)
     */
    public boolean logProductAdded(int productId, String productName, String username) throws SQLException {
        Activity activity = new Activity("product_added", "New product added", "product", productId,
                productName, username);
        return logActivity(activity);
    }

    /**
     * Log product update activity (legacy)
     */
    public boolean logProductUpdated(int productId, String productName, String username) throws SQLException {
        Activity activity = new Activity("product_updated", "Product information updated", "product",
                productId, productName, username);
        return logActivity(activity);
    }

    /**
     * Log product deletion activity (legacy)
     */
    public boolean logProductDeleted(int productId, String productName, String username) throws SQLException {
        Activity activity = new Activity("product_deleted", "Product record deleted", "product",
                productId, productName, username);
        return logActivity(activity);
    }

    /**
     * Log bill creation activity (legacy)
     */
    public boolean logBillCreated(int billId, String customerName, String username) throws SQLException {
        Activity activity = new Activity("bill_created", "New bill created for " + customerName, "bill", billId,
                customerName, username);
        return logActivity(activity);
    }

    /**
     * Log bill update activity (legacy)
     */
    public boolean logBillUpdated(int billId, String customerName, String username) throws SQLException {
        Activity activity = new Activity("bill_updated", "Bill updated for " + customerName, "bill", billId,
                customerName, username);
        return logActivity(activity);
    }

    /**
     * Log bill email sent activity (legacy)
     */
    public boolean logBillEmailSent(int billId, String customerName, String username, String recipientEmail) throws SQLException {
        Activity activity = new Activity("bill_email_sent", "Bill emailed to " + recipientEmail + " for " + customerName, 
                "bill", billId, customerName, username);
        return logActivity(activity);
    }

    // ================== Activity Retrieval ==================

    /**
     * Get all activities
     */
    public List<Activity> getAllActivities() throws SQLException {
        return activityDAO.getAllActivities();
    }

    /**
     * Get activities with pagination
     */
    public List<Activity> getActivitiesPaginated(int offset, int limit) throws SQLException {
        return activityDAO.getActivitiesPaginated(offset, limit);
    }

    /**
     * Get recent activities
     */
    public List<Activity> getRecentActivities(int limit) throws SQLException {
        return activityDAO.getRecentActivities(limit);
    }

    /**
     * Get activities by type
     */
    public List<Activity> getActivitiesByType(String activityType) throws SQLException {
        return activityDAO.getActivitiesByType(activityType);
    }

    /**
     * Get activities by username
     */
    public List<Activity> getActivitiesByUsername(String username) throws SQLException {
        return activityDAO.getActivitiesByUsername(username);
    }

    /**
     * Get activities by date range
     */
    public List<Activity> getActivitiesByDateRange(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate) throws SQLException {
        return activityDAO.getActivitiesByDateRange(startDate, endDate);
    }

    /**
     * Get activity count
     */
    public int getActivityCount() throws SQLException {
        return activityDAO.getActivityCount();
    }

    /**
     * Search activities
     */
    public List<Activity> searchActivities(String searchTerm) throws SQLException {
        return activityDAO.searchActivities(searchTerm);
    }

    /**
     * Get today's activity count
     */
    public int getTodayActivityCount() throws SQLException {
        return activityDAO.getTodayActivityCount();
    }
}