package com.app.dao;

import com.app.contact.Contact;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContactDaoImpl implements ContactDao {

    private Connection connection;

    // Constructor to initialize the database connection
    public ContactDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void AddContact(Contact contact) throws SQLException {
        String query = "INSERT INTO contacts (name, phone, image) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, contact.getName());
            pstmt.setString(2, contact.getPhone());
            pstmt.setBytes(3, contact.getImage()); // Set image as binary
            pstmt.executeUpdate();
        }
    }


    @Override
    public Contact getContactById(String id) throws SQLException {
        String query = "SELECT * FROM contacts WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Contact(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getBytes("image") // Retrieve binary data
                );
            }
        }
        return null;
    }


    @Override
    public List<Contact> getAllContacts() throws SQLException {
        String query = "SELECT * FROM contacts";
        List<Contact> contacts = new ArrayList<>();
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                contacts.add(new Contact(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getBytes("image") // Retrieve binary data
                ));
            }
        }
        return contacts;
    }


    @Override
    public void deleteContact(int id) throws SQLException {
        String query = "DELETE FROM contacts WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    @Override
    public List<Contact> searchContact(String criteria) throws SQLException {
        String query = "SELECT * FROM contacts WHERE name LIKE ? OR phone LIKE ?";
        List<Contact> contacts = new ArrayList<>();
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, "%" + criteria + "%");
            pstmt.setString(2, "%" + criteria + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                contacts.add(new Contact(
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getBytes("image")
                ));
            }
        }
        return contacts;
    }

    @Override
    public void updateContact(Contact contact) throws SQLException {
        String query = "UPDATE contacts SET name = ?, phone = ?, image = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, contact.getName());
            pstmt.setString(2, contact.getPhone());
            pstmt.setBytes(3, contact.getImage()); // Update image
            pstmt.setInt(4, contact.getId());
            pstmt.executeUpdate();
        }
    }

}
