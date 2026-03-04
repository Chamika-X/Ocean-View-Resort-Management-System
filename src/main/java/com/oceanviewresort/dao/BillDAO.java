package com.oceanviewresort.dao;

import java.sql.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.oceanviewresort.model.Bill;
import com.oceanviewresort.model.Reservation;

/**
 * Data Access Object for Bill entity at Ocean View Resort.
 * Handles all database operations related to guest billing.
 */
public class BillDAO {

    /**
     * Adds a new bill to the database
     * @param bill The bill to add
     * @return The generated bill ID, or -1 if failed
     * @throws SQLException if a database error occurs
     */
    public int addBill(Bill bill) throws SQLException {
        String query = "INSERT INTO Bill (bill_number, reservation_id, room_charges, additional_charges, " +
                       "tax_amount, discount_amount, total_amount, payment_status, payment_method) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, bill.getBillNumber());
        statement.setInt(2, bill.getReservationId());
        statement.setBigDecimal(3, bill.getRoomCharges());
        statement.setBigDecimal(4, bill.getAdditionalCharges());
        statement.setBigDecimal(5, bill.getTaxAmount());
        statement.setBigDecimal(6, bill.getDiscountAmount());
        statement.setBigDecimal(7, bill.getTotalAmount());
        statement.setString(8, bill.getPaymentStatus());
        statement.setString(9, bill.getPaymentMethod());

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
     * Gets a bill by ID
     * @param billId The bill ID
     * @return Bill object if found, null otherwise
     * @throws SQLException if a database error occurs
     */
    public Bill getBillById(int billId) throws SQLException {
        String query = "SELECT * FROM Bill WHERE bill_id = ?";
        Bill bill = null;

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, billId);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            bill = mapResultSetToBill(resultSet);
        }

        return bill;
    }

    /**
     * Gets a bill by bill number
     * @param billNumber The bill number
     * @return Bill object if found, null otherwise
     * @throws SQLException if a database error occurs
     */
    public Bill getBillByNumber(String billNumber) throws SQLException {
        String query = "SELECT * FROM Bill WHERE bill_number = ?";
        Bill bill = null;

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, billNumber);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            bill = mapResultSetToBill(resultSet);
        }

        return bill;
    }

    /**
     * Gets a bill by reservation ID
     * @param reservationId The reservation ID
     * @return Bill object if found, null otherwise
     * @throws SQLException if a database error occurs
     */
    public Bill getBillByReservationId(int reservationId) throws SQLException {
        String query = "SELECT * FROM Bill WHERE reservation_id = ?";
        Bill bill = null;

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, reservationId);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            bill = mapResultSetToBill(resultSet);
        }

        return bill;
    }

    /**
     * Gets all bills from the database
     * @return List of all bills
     * @throws SQLException if a database error occurs
     */
    public List<Bill> getAllBills() throws SQLException {
        List<Bill> bills = new ArrayList<>();
        String query = "SELECT * FROM Bill ORDER BY bill_date DESC";

        Connection connection = DBConnectionFactory.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            bills.add(mapResultSetToBill(resultSet));
        }

        return bills;
    }

    /**
     * Gets paginated bills from the database
     * @param offset The offset for pagination
     * @param limit The limit for pagination
     * @return List of bills for the current page
     * @throws SQLException if a database error occurs
     */
    public List<Bill> getBillsPaginated(int offset, int limit) throws SQLException {
        List<Bill> bills = new ArrayList<>();
        String query = "SELECT * FROM Bill ORDER BY bill_date DESC LIMIT ? OFFSET ?";

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, limit);
        statement.setInt(2, offset);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            bills.add(mapResultSetToBill(resultSet));
        }

        return bills;
    }

    /**
     * Gets the total count of bills
     * @return Total number of bills
     * @throws SQLException if a database error occurs
     */
    public int getBillCount() throws SQLException {
        String query = "SELECT COUNT(*) FROM Bill";

        Connection connection = DBConnectionFactory.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        if (resultSet.next()) {
            return resultSet.getInt(1);
        }

        return 0;
    }

    /**
     * Gets the next bill number
     * @return Next bill number in format BILL[YEAR][SEQUENCE]
     * @throws SQLException if a database error occurs
     */
    public String generateBillNumber() throws SQLException {
        String query = "SELECT MAX(CAST(SUBSTRING(bill_number, 9) AS UNSIGNED)) as max_num " +
                       "FROM Bill WHERE bill_number LIKE ?";
        
        int year = LocalDate.now().getYear();
        String prefix = "BILL" + year;

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
     * Updates a bill
     * @param bill The bill to update
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean updateBill(Bill bill) throws SQLException {
        String query = "UPDATE Bill SET room_charges = ?, additional_charges = ?, tax_amount = ?, " +
                       "discount_amount = ?, total_amount = ?, payment_status = ?, payment_method = ? " +
                       "WHERE bill_id = ?";

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setBigDecimal(1, bill.getRoomCharges());
        statement.setBigDecimal(2, bill.getAdditionalCharges());
        statement.setBigDecimal(3, bill.getTaxAmount());
        statement.setBigDecimal(4, bill.getDiscountAmount());
        statement.setBigDecimal(5, bill.getTotalAmount());
        statement.setString(6, bill.getPaymentStatus());
        statement.setString(7, bill.getPaymentMethod());
        statement.setInt(8, bill.getBillId());

        int rowsUpdated = statement.executeUpdate();
        return rowsUpdated > 0;
    }

    /**
     * Updates bill payment status
     * @param billId The bill ID
     * @param status The new payment status
     * @param paymentMethod The payment method (if paid)
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean updatePaymentStatus(int billId, String status, String paymentMethod) throws SQLException {
        String query = "UPDATE Bill SET payment_status = ?, payment_method = ? WHERE bill_id = ?";

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, status);
        statement.setString(2, paymentMethod);
        statement.setInt(3, billId);

        int rowsUpdated = statement.executeUpdate();
        return rowsUpdated > 0;
    }

    /**
     * Gets total revenue for today
     * @return Total revenue for today
     * @throws SQLException if a database error occurs
     */
    public BigDecimal getTodayRevenue() throws SQLException {
        String query = "SELECT COALESCE(SUM(total_amount), 0) as total FROM Bill " +
                       "WHERE DATE(bill_date) = CURDATE() AND payment_status = 'paid'";

        Connection connection = DBConnectionFactory.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        if (resultSet.next()) {
            return resultSet.getBigDecimal("total");
        }

        return BigDecimal.ZERO;
    }

    /**
     * Gets total revenue for a date range
     * @param startDate Start date
     * @param endDate End date
     * @return Total revenue for the date range
     * @throws SQLException if a database error occurs
     */
    public BigDecimal getRevenueForDateRange(LocalDate startDate, LocalDate endDate) throws SQLException {
        String query = "SELECT COALESCE(SUM(total_amount), 0) as total FROM Bill " +
                       "WHERE DATE(bill_date) BETWEEN ? AND ? AND payment_status = 'paid'";

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
     * Deletes a bill
     * @param billId The ID of the bill to delete
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean deleteBill(int billId) throws SQLException {
        String query = "DELETE FROM Bill WHERE bill_id = ?";

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, billId);

        int rowsDeleted = statement.executeUpdate();
        return rowsDeleted > 0;
    }

    /**
     * Searches bills with pagination (legacy billing system)
     * @param searchTerm The search term
     * @param offset The offset for pagination
     * @param limit The limit for pagination
     * @return List of matching bills for the current page
     * @throws SQLException if a database error occurs
     */
    public List<Bill> searchBillsPaginated(String searchTerm, int offset, int limit) throws SQLException {
        List<Bill> bills = new ArrayList<>();
        String query = "SELECT * FROM Bill WHERE bill_number LIKE ? OR payment_status LIKE ? " +
                       "ORDER BY bill_date DESC LIMIT ? OFFSET ?";

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        String searchPattern = "%" + searchTerm + "%";
        statement.setString(1, searchPattern);
        statement.setString(2, searchPattern);
        statement.setInt(3, limit);
        statement.setInt(4, offset);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            bills.add(mapResultSetToBill(resultSet));
        }

        return bills;
    }

    /**
     * Gets the count of bills matching a search term (legacy billing system)
     * @param searchTerm The search term
     * @return Count of matching bills
     * @throws SQLException if a database error occurs
     */
    public int getBillSearchCount(String searchTerm) throws SQLException {
        String query = "SELECT COUNT(*) FROM Bill WHERE bill_number LIKE ? OR payment_status LIKE ?";

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        String searchPattern = "%" + searchTerm + "%";
        statement.setString(1, searchPattern);
        statement.setString(2, searchPattern);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            return resultSet.getInt(1);
        }

        return 0;
    }

    /**
     * Updates bill status (legacy billing system)
     * @param billId The bill ID
     * @param status The new status
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean updateBillStatus(int billId, String status) throws SQLException {
        String query = "UPDATE Bill SET payment_status = ? WHERE bill_id = ?";

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, status);
        statement.setInt(2, billId);

        int rowsUpdated = statement.executeUpdate();
        return rowsUpdated > 0;
    }

    /**
     * Creates a bill (legacy billing system) - alias for addBill that returns boolean
     * @param bill The bill to create
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean createBill(Bill bill) throws SQLException {
        return addBill(bill) > 0;
    }

    /**
     * Gets bills by customer ID (legacy billing system)
     * @param customerId The customer ID
     * @return List of bills for the customer
     * @throws SQLException if a database error occurs
     */
    public List<Bill> getBillsByCustomerId(int customerId) throws SQLException {
        List<Bill> bills = new ArrayList<>();
        String query = "SELECT * FROM Bill WHERE customer_id = ? ORDER BY bill_date DESC";

        Connection connection = DBConnectionFactory.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, customerId);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            bills.add(mapResultSetToBill(resultSet));
        }

        return bills;
    }

    /**
     * Maps a ResultSet row to a Bill object
     * @param resultSet The ResultSet to map
     * @return Bill object
     * @throws SQLException if a database error occurs
     */
    private Bill mapResultSetToBill(ResultSet resultSet) throws SQLException {
        Bill bill = new Bill();
        bill.setBillId(resultSet.getInt("bill_id"));
        bill.setBillNumber(resultSet.getString("bill_number"));
        bill.setReservationId(resultSet.getInt("reservation_id"));
        bill.setRoomCharges(resultSet.getBigDecimal("room_charges"));
        bill.setAdditionalCharges(resultSet.getBigDecimal("additional_charges"));
        bill.setTaxAmount(resultSet.getBigDecimal("tax_amount"));
        bill.setDiscountAmount(resultSet.getBigDecimal("discount_amount"));
        bill.setTotalAmount(resultSet.getBigDecimal("total_amount"));
        bill.setPaymentStatus(resultSet.getString("payment_status"));
        bill.setPaymentMethod(resultSet.getString("payment_method"));
        bill.setBillDate(resultSet.getTimestamp("bill_date"));
        return bill;
    }
}
