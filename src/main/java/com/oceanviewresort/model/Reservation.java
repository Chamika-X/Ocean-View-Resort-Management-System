package com.oceanviewresort.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Reservation model class representing room reservations at Ocean View Resort.
 * Contains all booking details including guest info, room, dates, and payment info.
 */
public class Reservation {
    private int reservationId;
    private String reservationNumber;
    private int guestId;
    private Guest guest; // For joined queries
    private int roomId;
    private Room room; // For joined queries
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int numberOfNights;
    private int numberOfGuests;
    private BigDecimal totalAmount;
    private BigDecimal advancePayment;
    private BigDecimal balanceAmount;
    private String status; // confirmed, checked_in, checked_out, cancelled
    private String specialRequests;
    private int createdBy;
    private String createdByUsername; // For display
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Status constants
    public static final String STATUS_CONFIRMED = "confirmed";
    public static final String STATUS_CHECKED_IN = "checked_in";
    public static final String STATUS_CHECKED_OUT = "checked_out";
    public static final String STATUS_CANCELLED = "cancelled";

    /**
     * Default constructor
     */
    public Reservation() {
        this.status = STATUS_CONFIRMED;
        this.totalAmount = BigDecimal.ZERO;
        this.advancePayment = BigDecimal.ZERO;
        this.balanceAmount = BigDecimal.ZERO;
        this.numberOfGuests = 1;
    }

    /**
     * Constructor with essential fields
     */
    public Reservation(int guestId, int roomId, LocalDate checkInDate, LocalDate checkOutDate) {
        this();
        this.guestId = guestId;
        this.roomId = roomId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        calculateNumberOfNights();
    }

    /**
     * Calculate number of nights between check-in and check-out
     */
    public void calculateNumberOfNights() {
        if (checkInDate != null && checkOutDate != null) {
            this.numberOfNights = (int) ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        }
    }

    /**
     * Calculate total amount based on room rate and nights
     */
    public void calculateTotalAmount(double ratePerNight) {
        calculateNumberOfNights();
        this.totalAmount = BigDecimal.valueOf(ratePerNight * numberOfNights);
        calculateBalanceAmount();
    }

    /**
     * Calculate balance amount
     */
    public void calculateBalanceAmount() {
        if (totalAmount != null && advancePayment != null) {
            this.balanceAmount = totalAmount.subtract(advancePayment);
        }
    }

    // Getters and Setters
    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public String getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public int getGuestId() {
        return guestId;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
        calculateNumberOfNights();
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
        calculateNumberOfNights();
    }

    public int getNumberOfNights() {
        return numberOfNights;
    }

    public void setNumberOfNights(int numberOfNights) {
        this.numberOfNights = numberOfNights;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getAdvancePayment() {
        return advancePayment;
    }

    public void setAdvancePayment(BigDecimal advancePayment) {
        this.advancePayment = advancePayment;
        calculateBalanceAmount();
    }

    public BigDecimal getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(BigDecimal balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSpecialRequests() {
        return specialRequests;
    }

    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByUsername() {
        return createdByUsername;
    }

    public void setCreatedByUsername(String createdByUsername) {
        this.createdByUsername = createdByUsername;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Get status display text
     */
    public String getStatusDisplayText() {
        switch (status) {
            case STATUS_CONFIRMED:
                return "Confirmed";
            case STATUS_CHECKED_IN:
                return "Checked In";
            case STATUS_CHECKED_OUT:
                return "Checked Out";
            case STATUS_CANCELLED:
                return "Cancelled";
            default:
                return status;
        }
    }

    /**
     * Get status badge color class for UI
     */
    public String getStatusBadgeClass() {
        switch (status) {
            case STATUS_CONFIRMED:
                return "bg-blue-100 text-blue-800";
            case STATUS_CHECKED_IN:
                return "bg-green-100 text-green-800";
            case STATUS_CHECKED_OUT:
                return "bg-gray-100 text-gray-800";
            case STATUS_CANCELLED:
                return "bg-red-100 text-red-800";
            default:
                return "bg-gray-100 text-gray-800";
        }
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId=" + reservationId +
                ", reservationNumber='" + reservationNumber + '\'' +
                ", guestId=" + guestId +
                ", roomId=" + roomId +
                ", checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                ", status='" + status + '\'' +
                '}';
    }
}
