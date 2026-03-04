package com.oceanviewresort.model;

import java.sql.Timestamp;

/**
 * RoomType model class representing different categories of rooms
 * at Ocean View Resort with their respective rates.
 */
public class RoomType {
    private int roomTypeId;
    private String typeName;
    private String description;
    private double ratePerNight;
    private int maxOccupancy;
    private Timestamp createdAt;

    /**
     * Default constructor
     */
    public RoomType() {
        this.maxOccupancy = 2;
    }

    /**
     * Constructor with essential fields
     */
    public RoomType(String typeName, double ratePerNight) {
        this();
        this.typeName = typeName;
        this.ratePerNight = ratePerNight;
    }

    /**
     * Constructor with all fields except timestamp
     */
    public RoomType(int roomTypeId, String typeName, String description, 
                    double ratePerNight, int maxOccupancy) {
        this.roomTypeId = roomTypeId;
        this.typeName = typeName;
        this.description = description;
        this.ratePerNight = ratePerNight;
        this.maxOccupancy = maxOccupancy;
    }

    /**
     * Full constructor
     */
    public RoomType(int roomTypeId, String typeName, String description,
                    double ratePerNight, int maxOccupancy, Timestamp createdAt) {
        this(roomTypeId, typeName, description, ratePerNight, maxOccupancy);
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(int roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getRatePerNight() {
        return ratePerNight;
    }

    public void setRatePerNight(double ratePerNight) {
        this.ratePerNight = ratePerNight;
    }

    public int getMaxOccupancy() {
        return maxOccupancy;
    }

    public void setMaxOccupancy(int maxOccupancy) {
        this.maxOccupancy = maxOccupancy;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "RoomType{" +
                "roomTypeId=" + roomTypeId +
                ", typeName='" + typeName + '\'' +
                ", ratePerNight=" + ratePerNight +
                ", maxOccupancy=" + maxOccupancy +
                '}';
    }
}
