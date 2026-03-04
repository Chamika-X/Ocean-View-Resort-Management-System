package com.oceanviewresort.model;

import java.sql.Timestamp;

/**
 * Room model class representing individual rooms at Ocean View Resort.
 * Each room has a room number, type, floor, and availability status.
 */
public class Room {
    private int roomId;
    private String roomNumber;
    private int roomTypeId;
    private RoomType roomType; // For joined queries
    private int floorNumber;
    private String status; // available, occupied, maintenance, reserved
    private String description;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Status constants
    public static final String STATUS_AVAILABLE = "available";
    public static final String STATUS_OCCUPIED = "occupied";
    public static final String STATUS_MAINTENANCE = "maintenance";
    public static final String STATUS_RESERVED = "reserved";

    /**
     * Default constructor
     */
    public Room() {
        this.status = STATUS_AVAILABLE;
        this.floorNumber = 1;
    }

    /**
     * Constructor with essential fields
     */
    public Room(String roomNumber, int roomTypeId) {
        this();
        this.roomNumber = roomNumber;
        this.roomTypeId = roomTypeId;
    }

    /**
     * Constructor with all fields except timestamps
     */
    public Room(int roomId, String roomNumber, int roomTypeId, int floorNumber, 
                String status, String description) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.roomTypeId = roomTypeId;
        this.floorNumber = floorNumber;
        this.status = status;
        this.description = description;
    }

    /**
     * Full constructor
     */
    public Room(int roomId, String roomNumber, int roomTypeId, int floorNumber,
                String status, String description, Timestamp createdAt, Timestamp updatedAt) {
        this(roomId, roomNumber, roomTypeId, floorNumber, status, description);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(int roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
     * Check if room is available for booking
     */
    public boolean isAvailable() {
        return STATUS_AVAILABLE.equals(this.status);
    }

    /**
     * Get display name with room type
     */
    public String getDisplayName() {
        if (roomType != null) {
            return "Room " + roomNumber + " (" + roomType.getTypeName() + ")";
        }
        return "Room " + roomNumber;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomId=" + roomId +
                ", roomNumber='" + roomNumber + '\'' +
                ", status='" + status + '\'' +
                ", floorNumber=" + floorNumber +
                '}';
    }
}
