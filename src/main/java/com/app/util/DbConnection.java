package com.app.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    // Singleton instance of the DbConnection class
    private static DbConnection instance;
    
    // JDBC Connection instance
    private Connection connection;

    // Private constructor to prevent instantiation
    private DbConnection() {
        try {   
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establish connection to MySQL database on port 3306
            String url = "jdbc:mysql://localhost:3306/contacts";
            String username = "root"; // Change this to your MySQL username
            String password = ""; // Change this to your MySQL password
            this.connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to MySQL");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error initializing connection: " + e.getMessage());
        }
    }

    // Public method to provide access to the singleton instance
    public static synchronized DbConnection getInstance() {
        if (instance == null) {
            instance = new DbConnection();
        }
        return instance;
    }

    // Method to get the Connection object
    public Connection getConnection() {
        return connection;
    }
}
