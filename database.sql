-- Ocean View Resort - Hotel Reservation System Database Schema
-- Galle Beachside Hotel Management System
-- Created: February 2026

-- Create database
DROP DATABASE IF EXISTS oceanviewresort;
CREATE DATABASE oceanviewresort;
USE oceanviewresort;

-- Users table for authentication
CREATE TABLE User (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    full_name VARCHAR(100),
    role VARCHAR(20) DEFAULT 'staff',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Room Types table
CREATE TABLE RoomType (
    room_type_id INT AUTO_INCREMENT PRIMARY KEY,
    type_name VARCHAR(50) NOT NULL,
    description TEXT,
    rate_per_night DECIMAL(10, 2) NOT NULL,
    max_occupancy INT DEFAULT 2,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Rooms table
CREATE TABLE Room (
    room_id INT AUTO_INCREMENT PRIMARY KEY,
    room_number VARCHAR(10) UNIQUE NOT NULL,
    room_type_id INT NOT NULL,
    floor_number INT DEFAULT 1,
    status VARCHAR(20) DEFAULT 'available',
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (room_type_id) REFERENCES RoomType(room_type_id)
);

-- Guests table
CREATE TABLE Guest (
    guest_id INT AUTO_INCREMENT PRIMARY KEY,
    guest_name VARCHAR(100) NOT NULL,
    address TEXT NOT NULL,
    contact_number VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    nic_passport VARCHAR(50),
    nationality VARCHAR(50) DEFAULT 'Sri Lankan',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Reservations table
CREATE TABLE Reservation (
    reservation_id INT AUTO_INCREMENT PRIMARY KEY,
    reservation_number VARCHAR(20) UNIQUE NOT NULL,
    guest_id INT NOT NULL,
    room_id INT NOT NULL,
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    number_of_nights INT NOT NULL,
    number_of_guests INT DEFAULT 1,
    total_amount DECIMAL(10, 2) NOT NULL,
    advance_payment DECIMAL(10, 2) DEFAULT 0.00,
    balance_amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) DEFAULT 'confirmed',
    special_requests TEXT,
    created_by INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (guest_id) REFERENCES Guest(guest_id),
    FOREIGN KEY (room_id) REFERENCES Room(room_id),
    FOREIGN KEY (created_by) REFERENCES User(user_id)
);

-- Bills table for tracking payments
CREATE TABLE Bill (
    bill_id INT AUTO_INCREMENT PRIMARY KEY,
    bill_number VARCHAR(20) UNIQUE NOT NULL,
    reservation_id INT NOT NULL,
    room_charges DECIMAL(10, 2) NOT NULL,
    additional_charges DECIMAL(10, 2) DEFAULT 0.00,
    tax_amount DECIMAL(10, 2) DEFAULT 0.00,
    discount_amount DECIMAL(10, 2) DEFAULT 0.00,
    total_amount DECIMAL(10, 2) NOT NULL,
    payment_status VARCHAR(20) DEFAULT 'pending',
    payment_method VARCHAR(50),
    bill_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (reservation_id) REFERENCES Reservation(reservation_id)
);

-- Activities table for tracking system activities
CREATE TABLE activities (
    activity_id INT AUTO_INCREMENT PRIMARY KEY,
    activity_type VARCHAR(50) NOT NULL,
    description TEXT NOT NULL,
    entity_type VARCHAR(50),
    entity_id INT,
    entity_name VARCHAR(255),
    username VARCHAR(100) NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'completed',
    INDEX idx_timestamp (timestamp),
    INDEX idx_activity_type (activity_type),
    INDEX idx_username (username),
    INDEX idx_entity (entity_type, entity_id)
);

-- ============================================
-- INSERT SAMPLE DATA
-- ============================================

-- Insert default users (passwords are plain text for demo - hash in production)
INSERT INTO User (username, password, email, full_name, role) VALUES
('admin', 'admin123', 'admin@oceanviewresort.lk', 'System Administrator', 'admin'),
('receptionist', 'staff123', 'reception@oceanviewresort.lk', 'Front Desk Staff', 'staff');

-- Insert room types with rates (prices in LKR)
INSERT INTO RoomType (type_name, description, rate_per_night, max_occupancy) VALUES
('Standard', 'Comfortable room with basic amenities, garden view', 8500.00, 2),
('Deluxe', 'Spacious room with premium amenities, partial ocean view', 15000.00, 2),
('Superior', 'Large room with balcony and full ocean view', 22000.00, 3),
('Suite', 'Luxury suite with separate living area, ocean view balcony', 35000.00, 4),
('Family Room', 'Extra large room suitable for families, garden view', 28000.00, 5);

-- Insert sample rooms
INSERT INTO Room (room_number, room_type_id, floor_number, status, description) VALUES
-- Standard Rooms (Ground Floor)
('101', 1, 1, 'available', 'Ground floor standard room with garden view'),
('102', 1, 1, 'available', 'Ground floor standard room with garden view'),
('103', 1, 1, 'available', 'Ground floor standard room with pool view'),
-- Deluxe Rooms (First Floor)
('201', 2, 2, 'available', 'First floor deluxe room with partial ocean view'),
('202', 2, 2, 'available', 'First floor deluxe room with partial ocean view'),
('203', 2, 2, 'available', 'First floor deluxe room with pool view'),
-- Superior Rooms (Second Floor)
('301', 3, 3, 'available', 'Second floor superior room with full ocean view'),
('302', 3, 3, 'available', 'Second floor superior room with full ocean view'),
-- Suites (Third Floor)
('401', 4, 4, 'available', 'Luxury ocean view suite with private balcony'),
('402', 4, 4, 'available', 'Premium ocean view suite with jacuzzi'),
-- Family Rooms (Ground Floor)
('104', 5, 1, 'available', 'Spacious family room with garden access'),
('105', 5, 1, 'available', 'Large family room near pool area');

-- Insert sample guests
INSERT INTO Guest (guest_name, address, contact_number, email, nic_passport, nationality) VALUES
('Kamal Perera', '123 Galle Road, Colombo 03', '0771234567', 'kamal.perera@email.com', '901234567V', 'Sri Lankan'),
('Sarah Johnson', '456 Beach Street, Sydney, Australia', '+61412345678', 'sarah.j@email.com', 'PA1234567', 'Australian'),
('Nimal Fernando', '789 Temple Road, Kandy', '0772345678', 'nimal.f@email.com', '881234567V', 'Sri Lankan'),
('Emma Wilson', '321 Ocean Drive, London, UK', '+441234567890', 'emma.w@email.com', 'GB9876543', 'British');

-- Insert sample reservations
INSERT INTO Reservation (reservation_number, guest_id, room_id, check_in_date, check_out_date, number_of_nights, number_of_guests, total_amount, advance_payment, balance_amount, status, created_by) VALUES
('RES20260001', 1, 4, '2026-02-10', '2026-02-13', 3, 2, 45000.00, 20000.00, 25000.00, 'confirmed', 1),
('RES20260002', 2, 9, '2026-02-15', '2026-02-20', 5, 2, 175000.00, 50000.00, 125000.00, 'confirmed', 1),
('RES20260003', 3, 1, '2026-02-08', '2026-02-10', 2, 1, 17000.00, 17000.00, 0.00, 'checked_in', 1),
('RES20260004', 4, 7, '2026-02-12', '2026-02-15', 3, 2, 66000.00, 0.00, 66000.00, 'confirmed', 1);

-- Insert sample activities
INSERT INTO activities (activity_type, description, entity_type, entity_id, entity_name, username) VALUES
('user_login', 'User logged into the system', NULL, NULL, NULL, 'admin'),
('guest_added', 'New guest registered', 'guest', 1, 'Kamal Perera', 'admin'),
('guest_added', 'New guest registered', 'guest', 2, 'Sarah Johnson', 'admin'),
('guest_added', 'New guest registered', 'guest', 3, 'Nimal Fernando', 'admin'),
('guest_added', 'New guest registered', 'guest', 4, 'Emma Wilson', 'admin'),
('reservation_created', 'New reservation created', 'reservation', 1, 'RES20260001', 'admin'),
('reservation_created', 'New reservation created', 'reservation', 2, 'RES20260002', 'admin'),
('reservation_created', 'New reservation created', 'reservation', 3, 'RES20260003', 'admin'),
('reservation_created', 'New reservation created', 'reservation', 4, 'RES20260004', 'admin'),
('room_status_updated', 'Room status changed to occupied', 'room', 1, '101', 'admin'),
('user_logout', 'User logged out', NULL, NULL, NULL, 'admin');

-- Create indexes for better performance
CREATE INDEX idx_guest_contact ON Guest(contact_number);
CREATE INDEX idx_reservation_dates ON Reservation(check_in_date, check_out_date);
CREATE INDEX idx_bill_status ON Bill(payment_status);

-- Display final summary
SELECT 'Database Setup Complete!' AS Status;
SELECT COUNT(*) AS TotalUsers FROM User;
SELECT COUNT(*) AS TotalRooms FROM Room;
SELECT COUNT(*) AS TotalGuests FROM Guest;
SELECT COUNT(*) AS TotalReservations FROM Reservation;
