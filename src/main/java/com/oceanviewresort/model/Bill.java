package com.oceanviewresort.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;

/**
 * Bill model class for generating invoices at Ocean View Resort.
 * Tracks room charges, additional charges, taxes, and payment status.
 */
public class Bill {
    private int billId;
    private String billNumber;
    private int reservationId;
    private Reservation reservation; // For joined queries
    private BigDecimal roomCharges;
    private BigDecimal additionalCharges;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private String paymentStatus; // pending, paid, partial
    private String paymentMethod; // cash, card, bank_transfer
    private Timestamp billDate;

    // Legacy billing system fields (for backward compatibility)
    private int customerId;
    private String customerName;
    private String accountNumber;
    private BigDecimal discount;
    private String status;
    private List<BillItem> items; // For legacy billing system

    // Payment status constants
    public static final String PAYMENT_PENDING = "pending";
    public static final String PAYMENT_PAID = "paid";
    public static final String PAYMENT_PARTIAL = "partial";

    // Tax rate (10% service charge for Sri Lanka hotels)
    public static final BigDecimal TAX_RATE = new BigDecimal("0.10");

    /**
     * Default constructor
     */
    public Bill() {
        this.roomCharges = BigDecimal.ZERO;
        this.additionalCharges = BigDecimal.ZERO;
        this.taxAmount = BigDecimal.ZERO;
        this.discountAmount = BigDecimal.ZERO;
        this.totalAmount = BigDecimal.ZERO;
        this.paymentStatus = PAYMENT_PENDING;
        this.discount = BigDecimal.ZERO; // Legacy field
        this.items = new ArrayList<>(); // Legacy field
    }

    /**
     * Constructor with reservation
     */
    public Bill(int reservationId, BigDecimal roomCharges) {
        this();
        this.reservationId = reservationId;
        this.roomCharges = roomCharges;
        calculateTotal();
    }

    /**
     * Calculate tax amount based on room charges
     */
    public void calculateTax() {
        BigDecimal taxableAmount = roomCharges.add(additionalCharges).subtract(discountAmount);
        this.taxAmount = taxableAmount.multiply(TAX_RATE);
    }

    /**
     * Calculate total amount
     */
    public void calculateTotal() {
        calculateTax();
        this.totalAmount = roomCharges
                .add(additionalCharges)
                .add(taxAmount)
                .subtract(discountAmount);
    }

    // Getters and Setters
    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public BigDecimal getRoomCharges() {
        return roomCharges;
    }

    public void setRoomCharges(BigDecimal roomCharges) {
        this.roomCharges = roomCharges;
    }

    public BigDecimal getAdditionalCharges() {
        return additionalCharges;
    }

    public void setAdditionalCharges(BigDecimal additionalCharges) {
        this.additionalCharges = additionalCharges;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Timestamp getBillDate() {
        return billDate;
    }

    public void setBillDate(Timestamp billDate) {
        this.billDate = billDate;
    }

    // Legacy billing system getters and setters
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<BillItem> getItems() {
        return items;
    }

    public void setItems(List<BillItem> items) {
        this.items = items;
    }

    public void addItem(BillItem item) {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        this.items.add(item);
    }

    /**
     * Get payment status display text
     */
    public String getPaymentStatusDisplayText() {
        switch (paymentStatus) {
            case PAYMENT_PENDING:
                return "Pending";
            case PAYMENT_PAID:
                return "Paid";
            case PAYMENT_PARTIAL:
                return "Partial Payment";
            default:
                return paymentStatus;
        }
    }

    /**
     * Get payment status badge class
     */
    public String getPaymentStatusBadgeClass() {
        switch (paymentStatus) {
            case PAYMENT_PENDING:
                return "bg-yellow-100 text-yellow-800";
            case PAYMENT_PAID:
                return "bg-green-100 text-green-800";
            case PAYMENT_PARTIAL:
                return "bg-orange-100 text-orange-800";
            default:
                return "bg-gray-100 text-gray-800";
        }
    }

    @Override
    public String toString() {
        return "Bill{" +
                "billId=" + billId +
                ", billNumber='" + billNumber + '\'' +
                ", reservationId=" + reservationId +
                ", totalAmount=" + totalAmount +
                ", paymentStatus='" + paymentStatus + '\'' +
                '}';
    }
}
