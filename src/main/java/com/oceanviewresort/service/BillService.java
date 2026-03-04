package com.oceanviewresort.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.oceanviewresort.dao.BillDAO;
import com.oceanviewresort.dao.BillItemDAO;
import com.oceanviewresort.dao.CustomerDAO;
import com.oceanviewresort.dao.ProductDAO;
import com.oceanviewresort.dao.ReservationDAO;
import com.oceanviewresort.model.Bill;
import com.oceanviewresort.model.BillItem;
import com.oceanviewresort.model.Customer;
import com.oceanviewresort.model.Product;
import com.oceanviewresort.model.Reservation;

/**
 * Service class for Bill-related operations at Ocean View Resort.
 * Implements Singleton pattern and provides business logic for billing management.
 */
public class BillService {

    private static BillService instance;
    private BillDAO billDAO;
    private ReservationDAO reservationDAO;
    private CustomerDAO customerDAO;
    private ProductDAO productDAO;
    private BillItemDAO billItemDAO;

    /**
     * Public constructor (for backward compatibility with legacy billing system)
     */
    public BillService() {
        this.billDAO = new BillDAO();
        this.reservationDAO = new ReservationDAO();
        this.customerDAO = new CustomerDAO();
        this.productDAO = new ProductDAO();
        this.billItemDAO = new BillItemDAO();
    }

    /**
     * Gets the singleton instance of BillService
     * @return The BillService instance
     */
    public static BillService getInstance() {
        if (instance == null) {
            synchronized (BillService.class) {
                if (instance == null) {
                    instance = new BillService();
                }
            }
        }
        return instance;
    }

    /**
     * Generates a bill for a reservation
     * @param reservationId The reservation ID
     * @param additionalCharges Additional charges
     * @param discountAmount Discount amount
     * @return Generated Bill object, or null if failed
     * @throws SQLException if a database error occurs
     */
    public Bill generateBill(int reservationId, BigDecimal additionalCharges, BigDecimal discountAmount) throws SQLException {
        // Check if bill already exists for this reservation
        Bill existingBill = billDAO.getBillByReservationId(reservationId);
        if (existingBill != null) {
            return existingBill;
        }

        // Get reservation details
        Reservation reservation = reservationDAO.getReservationById(reservationId);
        if (reservation == null) {
            return null;
        }

        // Create new bill
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
     * Gets a bill by ID with reservation details
     * @param billId The bill ID
     * @return Bill object if found, null otherwise
     * @throws SQLException if a database error occurs
     */
    public Bill getBillById(int billId) throws SQLException {
        Bill bill = billDAO.getBillById(billId);
        if (bill != null && bill.getReservationId() > 0) {
            // Load reservation details
            Reservation reservation = reservationDAO.getReservationById(bill.getReservationId());
            bill.setReservation(reservation);
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
        Bill bill = billDAO.getBillByNumber(billNumber);
        if (bill != null) {
            Reservation reservation = reservationDAO.getReservationById(bill.getReservationId());
            bill.setReservation(reservation);
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
        Bill bill = billDAO.getBillByReservationId(reservationId);
        if (bill != null) {
            Reservation reservation = reservationDAO.getReservationById(reservationId);
            bill.setReservation(reservation);
        }
        return bill;
    }

    /**
     * Gets all bills
     * @return List of all bills
     * @throws SQLException if a database error occurs
     */
    public List<Bill> getAllBills() throws SQLException {
        return billDAO.getAllBills();
    }

    /**
     * Gets paginated bills
     * @param offset The offset for pagination
     * @param limit The limit for pagination
     * @return List of bills for the current page
     * @throws SQLException if a database error occurs
     */
    public List<Bill> getBillsPaginated(int offset, int limit) throws SQLException {
        return billDAO.getBillsPaginated(offset, limit);
    }

    /**
     * Gets the total count of bills
     * @return Total number of bills
     * @throws SQLException if a database error occurs
     */
    public int getBillCount() throws SQLException {
        return billDAO.getBillCount();
    }

    /**
     * Updates a bill
     * @param bill The bill to update
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean updateBill(Bill bill) throws SQLException {
        bill.calculateTotal();
        return billDAO.updateBill(bill);
    }

    /**
     * Processes payment for a bill
     * @param billId The bill ID
     * @param paymentMethod The payment method (cash, card, bank_transfer)
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean processPayment(int billId, String paymentMethod) throws SQLException {
        return billDAO.updatePaymentStatus(billId, Bill.PAYMENT_PAID, paymentMethod);
    }

    /**
     * Gets today's revenue
     * @return Total revenue for today
     * @throws SQLException if a database error occurs
     */
    public BigDecimal getTodayRevenue() throws SQLException {
        return billDAO.getTodayRevenue();
    }

    /**
     * Gets revenue for a date range
     * @param startDate Start date
     * @param endDate End date
     * @return Total revenue for the date range
     * @throws SQLException if a database error occurs
     */
    public BigDecimal getRevenueForDateRange(LocalDate startDate, LocalDate endDate) throws SQLException {
        return billDAO.getRevenueForDateRange(startDate, endDate);
    }

    /**
     * Calculates bill breakdown for display
     * @param reservation The reservation
     * @param additionalCharges Additional charges
     * @param discountAmount Discount amount
     * @return Bill object with calculated amounts (not persisted)
     */
    public Bill calculateBillPreview(Reservation reservation, BigDecimal additionalCharges, BigDecimal discountAmount) {
        Bill bill = new Bill();
        bill.setReservationId(reservation.getReservationId());
        bill.setRoomCharges(reservation.getTotalAmount());
        bill.setAdditionalCharges(additionalCharges != null ? additionalCharges : BigDecimal.ZERO);
        bill.setDiscountAmount(discountAmount != null ? discountAmount : BigDecimal.ZERO);
        bill.calculateTotal();
        bill.setReservation(reservation);
        return bill;
    }

    // ================== Legacy Billing System Methods ==================

    /**
     * Searches bills with pagination (legacy billing system)
     * @param searchTerm The search term
     * @param offset The offset for pagination
     * @param limit The limit for pagination
     * @return List of matching bills for the current page
     * @throws SQLException if a database error occurs
     */
    public List<Bill> searchBillsPaginated(String searchTerm, int offset, int limit) throws SQLException {
        return billDAO.searchBillsPaginated(searchTerm, offset, limit);
    }

    /**
     * Gets the count of bills matching a search term (legacy billing system)
     * @param searchTerm The search term
     * @return Count of matching bills
     * @throws SQLException if a database error occurs
     */
    public int getBillSearchCount(String searchTerm) throws SQLException {
        return billDAO.getBillSearchCount(searchTerm);
    }

    /**
     * Creates a bill from items (legacy billing system)
     * Validates customer and products before creating the bill.
     * @param customerId The customer ID
     * @param items The list of bill items
     * @param discount The discount percentage
     * @return Created Bill object, or null if failed
     * @throws SQLException if a database error occurs
     */
    public Bill createBillFromItems(int customerId, List<BillItem> items, BigDecimal discount) throws SQLException {
        // Validate customer exists
        Customer customer = customerDAO.getCustomerById(customerId);
        if (customer == null) {
            return null;
        }

        // Validate products and filter invalid items
        List<BillItem> validItems = new ArrayList<>();
        for (BillItem item : items) {
            Product product = productDAO.getProductById(item.getProductId());
            if (product != null) {
                validItems.add(item);
            }
        }

        Bill bill = new Bill();
        bill.setCustomerId(customerId);
        bill.setCustomerName(customer.getName());
        bill.setAccountNumber(customer.getAccountNumber());
        bill.setItems(validItems);
        bill.setDiscount(discount != null ? discount : BigDecimal.ZERO);
        
        // Calculate total from valid items
        BigDecimal total = BigDecimal.ZERO;
        for (BillItem item : validItems) {
            if (item.getSubtotal() != null) {
                total = total.add(item.getSubtotal());
            }
        }
        
        // Apply discount
        if (discount != null && discount.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal discountAmount = total.multiply(discount).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            total = total.subtract(discountAmount);
        }
        
        bill.setTotalAmount(total);
        bill.setStatus("pending");
        
        return bill;
    }

    /**
     * Creates a bill (legacy billing system) with customer validation, stock validation,
     * bill items creation and stock reduction.
     * @param bill The bill to create
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean createBill(Bill bill) throws SQLException {
        // Validate customer exists
        Customer customer = customerDAO.getCustomerById(bill.getCustomerId());
        if (customer == null) {
            return false;
        }

        List<BillItem> items = bill.getItems();

        // Validate products and stock for each item
        if (items != null && !items.isEmpty()) {
            for (BillItem item : items) {
                Product product = productDAO.getProductById(item.getProductId());
                if (product == null) {
                    return false;
                }
                // Check stock availability
                if (product.getQuantity() < item.getQuantity()) {
                    return false;
                }
            }
        }

        // Create the bill
        boolean billCreated = billDAO.createBill(bill);
        if (!billCreated) {
            return false;
        }

        // Create bill items if present
        if (items != null && !items.isEmpty()) {
            boolean itemsCreated = billItemDAO.createBillItems(items);
            if (!itemsCreated) {
                return false;
            }

            // Reduce stock for each item
            for (BillItem item : items) {
                productDAO.updateStockQuantity(item.getProductId(), item.getQuantity());
            }
        }

        return true;
    }

    /**
     * Creates a bill and returns the created Bill object
     * @param bill The bill to create
     * @return Created Bill object with ID, or null if failed
     * @throws SQLException if a database error occurs
     */
    public Bill createBillAndReturn(Bill bill) throws SQLException {
        int billId = billDAO.addBill(bill);
        if (billId > 0) {
            bill.setBillId(billId);
            return bill;
        }
        return null;
    }

    /**
     * Updates bill status (legacy billing system).
     * Restores stock when cancelling a bill.
     * @param billId The bill ID
     * @param status The new status
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean updateBillStatus(int billId, String status) throws SQLException {
        // If cancelling, restore stock
        if ("Cancelled".equalsIgnoreCase(status)) {
            Bill bill = billDAO.getBillById(billId);
            if (bill != null && bill.getItems() != null) {
                for (BillItem item : bill.getItems()) {
                    productDAO.restoreStockQuantity(item.getProductId(), item.getQuantity());
                }
            }
        }
        return billDAO.updateBillStatus(billId, status);
    }

    /**
     * Deletes a bill (legacy billing system).
     * Restores stock for all bill items before deletion.
     * @param billId The ID of the bill to delete
     * @return true if successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean deleteBill(int billId) throws SQLException {
        // Restore stock before deleting
        Bill bill = billDAO.getBillById(billId);
        if (bill != null && bill.getItems() != null) {
            for (BillItem item : bill.getItems()) {
                productDAO.restoreStockQuantity(item.getProductId(), item.getQuantity());
            }
        }
        return billDAO.deleteBill(billId);
    }

    /**
     * Gets bills by customer ID (legacy billing system)
     * @param customerId The customer ID
     * @return List of bills for the customer
     * @throws SQLException if a database error occurs
     */
    public List<Bill> getBillsByCustomerId(int customerId) throws SQLException {
        return billDAO.getBillsByCustomerId(customerId);
    }

    /**
     * Generates a new bill number (legacy billing system)
     * @return Generated bill number
     * @throws SQLException if a database error occurs
     */
    public String generateBillNumber() throws SQLException {
        List<Bill> allBills = billDAO.getAllBills();
        int nextNumber = allBills.size() + 1;
        return String.format("BILL%06d", nextNumber);
    }

    /**
     * Calculates total amount from bill items (legacy billing system)
     * @param items List of bill items
     * @return Total amount
     */
    public BigDecimal calculateTotalAmount(List<BillItem> items) {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal total = BigDecimal.ZERO;
        for (BillItem item : items) {
            if (item.getSubtotal() != null) {
                total = total.add(item.getSubtotal());
            }
        }
        return total;
    }

    /**
     * Validates bill items (legacy billing system).
     * Checks that products exist and have sufficient stock.
     * @param items List of bill items to validate
     * @return true if all items are valid, false otherwise
     */
    public boolean validateBillItems(List<BillItem> items) throws SQLException {
        if (items == null || items.isEmpty()) {
            return false;
        }
        for (BillItem item : items) {
            if (item.getProductId() <= 0) {
                return false;
            }
            // Check product exists
            Product product = productDAO.getProductById(item.getProductId());
            if (product == null) {
                return false;
            }
            // Check quantity is valid
            if (item.getQuantity() <= 0) {
                return false;
            }
            if (item.getUnitPrice() != null && item.getUnitPrice().compareTo(BigDecimal.ZERO) < 0) {
                return false;
            }
            // Check stock availability
            if (product.getQuantity() < item.getQuantity()) {
                return false;
            }
        }
        return true;
    }
}
