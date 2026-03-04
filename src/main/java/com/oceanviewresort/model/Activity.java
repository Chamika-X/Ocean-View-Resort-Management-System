package com.oceanviewresort.model;

import java.time.LocalDateTime;

/**
 * Activity model class representing system activities and events at Ocean View Resort.
 * Used for tracking user actions and system events in the activity timeline.
 */
public class Activity {
    private int activityId;
    private String activityType;
    private String description;
    private String entityType;
    private int entityId;
    private String entityName;
    private String username;
    private LocalDateTime timestamp;
    private String status;

    /**
     * Activity types constants for Ocean View Resort
     */
    public static final String TYPE_USER_LOGIN = "user_login";
    public static final String TYPE_USER_LOGOUT = "user_logout";
    public static final String TYPE_GUEST_ADDED = "guest_added";
    public static final String TYPE_GUEST_UPDATED = "guest_updated";
    public static final String TYPE_GUEST_DELETED = "guest_deleted";
    public static final String TYPE_ROOM_ADDED = "room_added";
    public static final String TYPE_ROOM_UPDATED = "room_updated";
    public static final String TYPE_ROOM_STATUS_CHANGED = "room_status_changed";
    public static final String TYPE_RESERVATION_CREATED = "reservation_created";
    public static final String TYPE_RESERVATION_UPDATED = "reservation_updated";
    public static final String TYPE_RESERVATION_CANCELLED = "reservation_cancelled";
    public static final String TYPE_CHECK_IN = "check_in";
    public static final String TYPE_CHECK_OUT = "check_out";
    public static final String TYPE_BILL_GENERATED = "bill_generated";
    public static final String TYPE_BILL_PAID = "bill_paid";
    public static final String TYPE_SYSTEM_BACKUP = "system_backup";

    /**
     * Default constructor
     */
    public Activity() {
        this.timestamp = LocalDateTime.now();
        this.status = "completed";
    }

    /**
     * Constructor with basic fields
     */
    public Activity(String activityType, String description, String entityType, int entityId, String entityName,
            String username) {
        this();
        this.activityType = activityType;
        this.description = description;
        this.entityType = entityType;
        this.entityId = entityId;
        this.entityName = entityName;
        this.username = username;
    }

    /**
     * Constructor for system activities without entity
     */
    public Activity(String activityType, String description, String username) {
        this();
        this.activityType = activityType;
        this.description = description;
        this.username = username;
    }

    // Getters and Setters
    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Get activity color based on type
     */
    public String getActivityColor() {
        switch (activityType) {
        case TYPE_BILL_GENERATED:
        case TYPE_BILL_PAID:
            return "green";
        case TYPE_GUEST_ADDED:
        case TYPE_GUEST_UPDATED:
            return "blue";
        case TYPE_ROOM_ADDED:
        case TYPE_ROOM_UPDATED:
            return "purple";
        case TYPE_SYSTEM_BACKUP:
            return "orange";
        case TYPE_USER_LOGIN:
        case TYPE_USER_LOGOUT:
            return "gray";
        default:
            return "gray";
        }
    }

    /**
     * Get activity icon based on type
     */
    public String getActivityIcon() {
        switch (activityType) {
        case TYPE_BILL_GENERATED:
        case TYPE_BILL_PAID:
            return "fas fa-file-invoice-dollar";
        case TYPE_GUEST_ADDED:
        case TYPE_GUEST_UPDATED:
            return "fas fa-user";
        case TYPE_ROOM_ADDED:
        case TYPE_ROOM_UPDATED:
            return "fas fa-bed";
        case TYPE_SYSTEM_BACKUP:
            return "fas fa-database";
        case TYPE_USER_LOGIN:
            return "fas fa-sign-in-alt";
        case TYPE_USER_LOGOUT:
            return "fas fa-sign-out-alt";
        default:
            return "fas fa-info-circle";
        }
    }

    /**
     * Get formatted time ago string
     */
    public String getTimeAgo() {
        if (timestamp == null) {
            return "Unknown time";
        }

        LocalDateTime now = LocalDateTime.now();
        long seconds = java.time.Duration.between(timestamp, now).getSeconds();

        if (seconds < 60) {
            return seconds + " seconds ago";
        } else if (seconds < 3600) {
            long minutes = seconds / 60;
            return minutes + " minute" + (minutes > 1 ? "s" : "") + " ago";
        } else if (seconds < 86400) {
            long hours = seconds / 3600;
            return hours + " hour" + (hours > 1 ? "s" : "") + " ago";
        } else {
            long days = seconds / 86400;
            return days + " day" + (days > 1 ? "s" : "") + " ago";
        }
    }

    @Override
    public String toString() {
        return "Activity{" + "activityId=" + activityId + ", activityType='" + activityType + '\'' + ", description='"
                + description + '\'' + ", entityType='" + entityType + '\'' + ", entityId=" + entityId
                + ", entityName='" + entityName + '\'' + ", username='" + username + '\'' + ", timestamp=" + timestamp
                + ", status='" + status + '\'' + '}';
    }
}