package com.oceanviewresort.validation;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Utility class for input validation at Ocean View Resort.
 * Provides validation methods for guests, reservations, rooms, and users.
 */
public class ValidationUtils {

    // Regex patterns for validation
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[+]?[0-9\\s\\-\\(\\)]{7,15}$");
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[A-Za-z0-9_]{3,20}$");
    private static final Pattern NIC_PATTERN = Pattern.compile("^([0-9]{9}[VvXx]|[0-9]{12})$");
    private static final Pattern PASSPORT_PATTERN = Pattern.compile("^[A-Za-z]{1,2}[0-9]{6,9}$");
    private static final Pattern ROOM_NUMBER_PATTERN = Pattern.compile("^[0-9]{3,4}[A-Za-z]?$");


    // ================== Basic Validators ==================

    /**
     * Validates if a string is not null, not empty, and not just whitespace
     */
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Validates if a string has a minimum length
     */
    public static boolean hasMinLength(String value, int minLength) {
        return isNotEmpty(value) && value.trim().length() >= minLength;
    }

    /**
     * Validates if a string has a maximum length
     */
    public static boolean hasMaxLength(String value, int maxLength) {
        return value != null && value.length() <= maxLength;
    }

    /**
     * Validates if a string matches a regex pattern
     */
    public static boolean matchesPattern(String value, Pattern pattern) {
        return isNotEmpty(value) && pattern.matcher(value.trim()).matches();
    }

    /**
     * Validates if a string is a valid email
     */
    public static boolean isValidEmail(String email) {
        return email == null || email.trim().isEmpty() || matchesPattern(email, EMAIL_PATTERN);
    }

    /**
     * Validates if a string is a valid phone number
     */
    public static boolean isValidPhone(String phone) {
        return matchesPattern(phone, PHONE_PATTERN);
    }

    /**
     * Validates if a string is a valid username
     */
    public static boolean isValidUsername(String username) {
        return matchesPattern(username, USERNAME_PATTERN);
    }

    /**
     * Validates if a string is a valid NIC number (Sri Lankan)
     */
    public static boolean isValidNIC(String nic) {
        return nic == null || nic.trim().isEmpty() || matchesPattern(nic, NIC_PATTERN);
    }

    /**
     * Validates if a string is a valid passport number
     */
    public static boolean isValidPassport(String passport) {
        return passport == null || passport.trim().isEmpty() || matchesPattern(passport, PASSPORT_PATTERN);
    }

    /**
     * Validates if a string is a valid NIC or passport
     */
    public static boolean isValidNicOrPassport(String value) {
        return value == null || value.trim().isEmpty() || 
               matchesPattern(value, NIC_PATTERN) || matchesPattern(value, PASSPORT_PATTERN);
    }

    /**
     * Validates if a string is a valid room number
     */
    public static boolean isValidRoomNumber(String roomNumber) {
        return matchesPattern(roomNumber, ROOM_NUMBER_PATTERN);
    }

    /**
     * Validates if a number is positive
     */
    public static boolean isPositive(double value) {
        return value > 0;
    }

    /**
     * Validates if an integer is positive
     */
    public static boolean isPositive(int value) {
        return value > 0;
    }

    /**
     * Validates if dates are valid for a reservation
     */
    public static boolean isValidDateRange(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null) {
            return false;
        }
        return !checkIn.isAfter(checkOut) && !checkIn.isBefore(LocalDate.now());
    }

    // ================== Sanitizers ==================

    /**
     * Sanitizes a string by removing potentially dangerous characters
     */
    public static String sanitizeString(String value) {
        if (value == null) {
            return null;
        }
        // Remove script tags and other potentially dangerous content
        String sanitized = value.replaceAll("<script[^>]*>.*?</script>", "")
                .replaceAll("<[^>]+>", "")
                .replaceAll("[\\x00-\\x08\\x0B\\x0C\\x0E-\\x1F]", "");
        return sanitized.trim();
    }

    // ================== Guest Validation ==================

    /**
     * Validates guest input fields
     * @return Map of field names to error messages (empty if valid)
     */
    public static Map<String, String> validateGuest(String guestName, String address, 
            String contactNumber, String email, String nicPassport) {
        Map<String, String> errors = new HashMap<>();

        // Guest name validation
        if (!isNotEmpty(guestName)) {
            errors.put("guestName", "Guest name is required");
        } else if (!hasMinLength(guestName, 2)) {
            errors.put("guestName", "Guest name must be at least 2 characters");
        } else if (!hasMaxLength(guestName, 100)) {
            errors.put("guestName", "Guest name must be less than 100 characters");
        }

        // Address validation
        if (!isNotEmpty(address)) {
            errors.put("address", "Address is required");
        } else if (!hasMinLength(address, 5)) {
            errors.put("address", "Address must be at least 5 characters");
        }

        // Contact number validation
        if (!isNotEmpty(contactNumber)) {
            errors.put("contactNumber", "Contact number is required");
        } else if (!isValidPhone(contactNumber)) {
            errors.put("contactNumber", "Invalid contact number format");
        }

        // Email validation (optional)
        if (isNotEmpty(email) && !isValidEmail(email)) {
            errors.put("email", "Invalid email format");
        }

        // NIC/Passport validation (optional)
        if (isNotEmpty(nicPassport) && !isValidNicOrPassport(nicPassport)) {
            errors.put("nicPassport", "Invalid NIC/Passport format");
        }

        return errors;
    }

    // ================== Reservation Validation ==================

    /**
     * Validates reservation input fields
     * @return Map of field names to error messages (empty if valid)
     */
    public static Map<String, String> validateReservation(int guestId, int roomId,
            LocalDate checkInDate, LocalDate checkOutDate, int numberOfGuests) {
        Map<String, String> errors = new HashMap<>();

        // Guest ID validation
        if (guestId <= 0) {
            errors.put("guestId", "Please select a guest");
        }

        // Room ID validation
        if (roomId <= 0) {
            errors.put("roomId", "Please select a room");
        }

        // Check-in date validation
        if (checkInDate == null) {
            errors.put("checkInDate", "Check-in date is required");
        } else if (checkInDate.isBefore(LocalDate.now())) {
            errors.put("checkInDate", "Check-in date cannot be in the past");
        }

        // Check-out date validation
        if (checkOutDate == null) {
            errors.put("checkOutDate", "Check-out date is required");
        } else if (checkInDate != null && !checkOutDate.isAfter(checkInDate)) {
            errors.put("checkOutDate", "Check-out date must be after check-in date");
        }

        // Number of guests validation
        if (numberOfGuests <= 0) {
            errors.put("numberOfGuests", "Number of guests must be at least 1");
        }

        return errors;
    }

    // ================== Room Validation ==================

    /**
     * Validates room input fields
     * @return Map of field names to error messages (empty if valid)
     */
    public static Map<String, String> validateRoom(String roomNumber, int roomTypeId, 
            int floorNumber, String description) {
        Map<String, String> errors = new HashMap<>();

        // Room number validation
        if (!isNotEmpty(roomNumber)) {
            errors.put("roomNumber", "Room number is required");
        } else if (!isValidRoomNumber(roomNumber)) {
            errors.put("roomNumber", "Invalid room number format (e.g., 101, 201A)");
        }

        // Room type validation
        if (roomTypeId <= 0) {
            errors.put("roomTypeId", "Please select a room type");
        }

        // Floor number validation
        if (floorNumber <= 0) {
            errors.put("floorNumber", "Floor number must be positive");
        }

        return errors;
    }

    // ================== User Validation ==================

    /**
     * Validates user input fields
     * @return Map of field names to error messages (empty if valid)
     */
    public static Map<String, String> validateUser(String username, String password, 
            String email, String fullName) {
        Map<String, String> errors = new HashMap<>();

        // Username validation
        if (!isNotEmpty(username)) {
            errors.put("username", "Username is required");
        } else if (!isValidUsername(username)) {
            errors.put("username", "Username must be 3-20 characters (letters, numbers, underscores)");
        }

        // Password validation
        if (!isNotEmpty(password)) {
            errors.put("password", "Password is required");
        } else if (!hasMinLength(password, 6)) {
            errors.put("password", "Password must be at least 6 characters");
        }

        // Email validation (optional)
        if (isNotEmpty(email) && !isValidEmail(email)) {
            errors.put("email", "Invalid email format");
        }

        // Full name validation
        if (!isNotEmpty(fullName)) {
            errors.put("fullName", "Full name is required");
        } else if (!hasMinLength(fullName, 2)) {
            errors.put("fullName", "Full name must be at least 2 characters");
        }

        return errors;
    }

    /**
     * Validates login input fields
     * @return Map of field names to error messages (empty if valid)
     */
    public static Map<String, String> validateLogin(String username, String password) {
        Map<String, String> errors = new HashMap<>();

        if (!isNotEmpty(username)) {
            errors.put("username", "Username is required");
        }

        if (!isNotEmpty(password)) {
            errors.put("password", "Password is required");
        }

        return errors;
    }

    // ================== Bill Validation ==================

    /**
     * Validates bill input fields
     * @return Map of field names to error messages (empty if valid)
     */
    public static Map<String, String> validateBillPayment(String paymentMethod, double amount) {
        Map<String, String> errors = new HashMap<>();

        if (!isNotEmpty(paymentMethod)) {
            errors.put("paymentMethod", "Payment method is required");
        }

        if (amount <= 0) {
            errors.put("amount", "Amount must be greater than zero");
        }

        return errors;
    }

    // ================== Legacy Billing System Validation (for backward compatibility) ==================

    /**
     * Validates customer input fields (legacy billing system)
     * @return Map of field names to error messages (empty if valid)
     */
    public static Map<String, String> validateCustomer(String accountNumber, String name, 
            String address, String telephone) {
        Map<String, String> errors = new HashMap<>();

        // Account number validation
        if (!isNotEmpty(accountNumber)) {
            errors.put("accountNumber", "Account number is required");
        } else if (!hasMinLength(accountNumber, 3)) {
            errors.put("accountNumber", "Account number must be at least 3 characters");
        }

        // Name validation
        if (!isNotEmpty(name)) {
            errors.put("name", "Customer name is required");
        } else if (!hasMinLength(name, 2)) {
            errors.put("name", "Customer name must be at least 2 characters");
        }

        // Address validation
        if (!isNotEmpty(address)) {
            errors.put("address", "Address is required");
        } else if (!hasMinLength(address, 5)) {
            errors.put("address", "Address must be at least 5 characters");
        }

        // Telephone validation
        if (!isNotEmpty(telephone)) {
            errors.put("telephone", "Telephone is required");
        } else if (!isValidPhone(telephone)) {
            errors.put("telephone", "Invalid telephone format");
        }

        return errors;
    }

    /**
     * Validates product input fields (legacy billing system)
     * @return Map of field names to error messages (empty if valid)
     */
    public static Map<String, String> validateProduct(String name, String description, 
            String priceStr, String quantityStr) {
        Map<String, String> errors = new HashMap<>();

        // Name validation
        if (!isNotEmpty(name)) {
            errors.put("name", "Product name is required");
        } else if (!hasMinLength(name, 2) || !hasMaxLength(name, 100)) {
            errors.put("name", "Product name must be between 2 and 100 characters");
        }

        // Description validation
        if (description != null && !hasMaxLength(description, 500)) {
            errors.put("description", "Description must not exceed 500 characters");
        }

        // Price validation
        if (!isNotEmpty(priceStr)) {
            errors.put("price", "Price is required");
        } else {
            try {
                double price = Double.parseDouble(priceStr);
                if (price <= 0) {
                    errors.put("price", "Price must be greater than 0");
                }
            } catch (NumberFormatException e) {
                errors.put("price", "Please enter a valid price");
            }
        }

        // Quantity validation
        if (!isNotEmpty(quantityStr)) {
            errors.put("quantity", "Quantity is required");
        } else {
            try {
                int quantity = Integer.parseInt(quantityStr);
                if (quantity < 0) {
                    errors.put("quantity", "Quantity must be 0 or greater");
                } else if (quantity > 999999) {
                    errors.put("quantity", "Quantity must be between 0 and 999,999");
                }
            } catch (NumberFormatException e) {
                errors.put("quantity", "Please enter a valid quantity");
            }
        }

        return errors;
    }

    /**
     * Validates bill item input fields (legacy billing system)
     * @return Map of field names to error messages (empty if valid)
     */
    public static Map<String, String> validateBillItem(String productIdStr, String quantityStr, String priceStr) {
        Map<String, String> errors = new HashMap<>();

        // Product ID validation
        if (!isNotEmpty(productIdStr)) {
            errors.put("productId", "Product is required");
        } else {
            try {
                int productId = Integer.parseInt(productIdStr);
                if (productId <= 0) {
                    errors.put("productId", "Invalid product selection");
                }
            } catch (NumberFormatException e) {
                errors.put("productId", "Invalid product ID");
            }
        }

        // Quantity validation
        if (!isNotEmpty(quantityStr)) {
            errors.put("quantity", "Quantity is required");
        } else {
            try {
                int quantity = Integer.parseInt(quantityStr);
                if (quantity <= 0) {
                    errors.put("quantity", "Quantity must be greater than zero");
                }
            } catch (NumberFormatException e) {
                errors.put("quantity", "Invalid quantity format");
            }
        }

        // Price validation (optional, may come from product)
        if (isNotEmpty(priceStr)) {
            try {
                double price = Double.parseDouble(priceStr);
                if (price < 0) {
                    errors.put("price", "Price cannot be negative");
                }
            } catch (NumberFormatException e) {
                errors.put("price", "Invalid price format");
            }
        }

        return errors;
    }

    /**
     * Validates bill status update (legacy billing system)
     * @return Map of field names to error messages (empty if valid)
     */
    public static Map<String, String> validateBillStatusUpdate(String currentStatus, String newStatus) {
        Map<String, String> errors = new HashMap<>();

        if (!isNotEmpty(newStatus)) {
            errors.put("status", "New status is required");
        }

        // Validate status transitions
        if ("cancelled".equals(currentStatus)) {
            errors.put("status", "Cannot change status of a cancelled bill");
        }

        if ("paid".equals(currentStatus) && !"cancelled".equals(newStatus)) {
            errors.put("status", "Paid bills can only be cancelled");
        }

        return errors;
    }

    /**
     * Validates user input fields for user management (username, password, role)
     * @param username The username
     * @param password The password
     * @param role The user role
     * @return Map of field names to error messages (empty if valid)
     */
    public static Map<String, String> validateUserWithRole(String username, String password, String role) {
        Map<String, String> errors = new HashMap<>();

        // Username validation
        if (!isNotEmpty(username)) {
            errors.put("username", "Username is required");
        } else if (!isValidUsername(username)) {
            errors.put("username", "Username must be 3-20 characters (letters, numbers, underscores)");
        }

        // Password validation
        if (!isNotEmpty(password)) {
            errors.put("password", "Password is required");
        } else if (!hasMinLength(password, 6)) {
            errors.put("password", "Password must be at least 6 characters");
        }

        // Role validation
        if (!isNotEmpty(role)) {
            errors.put("role", "Role is required");
        } else if (!"admin".equals(role) && !"staff".equals(role)) {
            errors.put("role", "Role must be either 'admin' or 'staff'");
        }

        return errors;
    }

    /**
     * Validates user input fields (3 parameter overload for basic registration)
     * @return Map of field names to error messages (empty if valid)
     */
    public static Map<String, String> validateUser(String username, String password, String email) {
        Map<String, String> errors = new HashMap<>();

        // Username validation
        if (!isNotEmpty(username)) {
            errors.put("username", "Username is required");
        } else if (!isValidUsername(username)) {
            errors.put("username", "Username must be 3-20 characters (letters, numbers, underscores)");
        }

        // Password validation
        if (!isNotEmpty(password)) {
            errors.put("password", "Password is required");
        } else if (!hasMinLength(password, 6)) {
            errors.put("password", "Password must be at least 6 characters");
        }

        // Email validation (optional)
        if (isNotEmpty(email) && !isValidEmail(email)) {
            errors.put("email", "Invalid email format");
        }

        return errors;
    }
}