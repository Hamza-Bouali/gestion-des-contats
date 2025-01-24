package com.app.util;

import java.sql.Connection;
import java.sql.SQLException;

public class TestMain {
    public static void main(String[] args) {
        // Get the DbConnection instance
        DbConnection dbConnection = DbConnection.getInstance();
        
        // Retrieve the connection object
        Connection connection = dbConnection.getConnection();
        
        // Check if the connection is established
        if (connection != null) {
            System.out.println("Database connection established successfully.");
        } else {
            System.out.println("Failed to establish database connection.");
        }

        try {
            // Optional: Close the connection if needed
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }
}
