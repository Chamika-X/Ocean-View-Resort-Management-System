package com.oceanviewresort.model;

import java.sql.Timestamp;

/**
 * Guest model class representing hotel guests in the Ocean View Resort system.
 * Stores guest personal information for reservations.
 */
public class Guest {
    private int guestId;
    private String guestName;
    private String address;
    private String contactNumber;
    private String email;
    private String nicPassport;
    private String nationality;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    /**
     * Default constructor
     */
    public Guest() {
        this.nationality = "Sri Lankan";
    }

    /**
     * Constructor with essential fields
     */
    public Guest(String guestName, String address, String contactNumber) {
        this();
        this.guestName = guestName;
        this.address = address;
        this.contactNumber = contactNumber;
    }

    /**
     * Constructor with all fields except timestamps
     */
    public Guest(int guestId, String guestName, String address, String contactNumber, 
                 String email, String nicPassport, String nationality) {
        this.guestId = guestId;
        this.guestName = guestName;
        this.address = address;
        this.contactNumber = contactNumber;
        this.email = email;
        this.nicPassport = nicPassport;
        this.nationality = nationality;
    }

    /**
     * Full constructor with all fields
     */
    public Guest(int guestId, String guestName, String address, String contactNumber,
                 String email, String nicPassport, String nationality,
                 Timestamp createdAt, Timestamp updatedAt) {
        this(guestId, guestName, address, contactNumber, email, nicPassport, nationality);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getGuestId() {
        return guestId;
    }

    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNicPassport() {
        return nicPassport;
    }

    public void setNicPassport(String nicPassport) {
        this.nicPassport = nicPassport;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
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

    @Override
    public String toString() {
        return "Guest{" +
                "guestId=" + guestId +
                ", guestName='" + guestName + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", nationality='" + nationality + '\'' +
                '}';
    }
}
