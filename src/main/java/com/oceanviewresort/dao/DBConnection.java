package com.oceanviewresort.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database connection class for Ocean View Resort Reservation System.
 * Implements Singleton pattern for efficient connection management.
 */
public class DBConnection {

    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/oceanviewresort";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";

    private static DBConnection instance;

    private DBConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            try {
                // Fallback: try loading via the thread's context classloader (webapp classloader)
                Thread.currentThread().getContextClassLoader().loadClass("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e2) {
                try {
                    // Legacy driver name
                    Class.forName("com.mysql.jdbc.Driver");
                } catch (ClassNotFoundException e3) {
                    System.err.println("MySQL JDBC Driver not found. Ensure mysql-connector-j is in WEB-INF/lib.");
                    e3.printStackTrace();
                }
            }
        }
    }

    public static DBConnection getInstance() {
        if (instance == null) {
            synchronized (DBConnection.class) {
                if (instance == null) {
                    instance = new DBConnection();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}