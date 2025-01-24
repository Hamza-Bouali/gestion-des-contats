package com.app.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import com.app.contact.Contact;
import com.app.dao.ContactDao;
import com.app.dao.ContactDaoImpl;
import com.app.util.DbConnection;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class ContactManagerUI {
    private JFrame frame;
    private JTextField nameField, phoneField;
    private JLabel imageLabel;
    private JTable contactTable;
    private DefaultTableModel tableModel;
    private byte[] imageBytes;

    private final ContactDao contactDao;

    public ContactManagerUI(ContactDao contactDao) {
        this.contactDao = contactDao;
        initialize();
    }

    private void initialize() {
        try {
            // Set the system look and feel for a modern appearance
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        frame = new JFrame("Contact Manager");
        frame.setSize(1000, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        // Center the window on the screen
        frame.setLocationRelativeTo(null);

        // Main panel with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form Panel (Two Columns)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Add Contact"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding

        // Left Column: Name and Phone Fields
        JPanel leftColumn = new JPanel(new GridBagLayout());
        GridBagConstraints leftGbc = new GridBagConstraints();
        leftGbc.insets = new Insets(5, 5, 5, 5);

        leftGbc.gridx = 0;
        leftGbc.gridy = 0;
        leftGbc.anchor = GridBagConstraints.EAST;
        leftColumn.add(new JLabel("Name:"), leftGbc);

        leftGbc.gridx = 1;
        leftGbc.gridy = 0;
        leftGbc.fill = GridBagConstraints.HORIZONTAL;
        nameField = new JTextField(20);
        leftColumn.add(nameField, leftGbc);

        leftGbc.gridx = 0;
        leftGbc.gridy = 1;
        leftGbc.anchor = GridBagConstraints.EAST;
        leftColumn.add(new JLabel("Phone:"), leftGbc);

        leftGbc.gridx = 1;
        leftGbc.gridy = 1;
        leftGbc.fill = GridBagConstraints.HORIZONTAL;
        phoneField = new JTextField(20);
        leftColumn.add(phoneField, leftGbc);

        // Right Column: Image Upload Section
        JPanel rightColumn = new JPanel(new BorderLayout());
        rightColumn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        imageLabel = new JLabel("No Image Selected", SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(250, 250)); // Fixed size for image preview
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Add border
        JButton uploadButton = new JButton("Upload Image");
        uploadButton.setIcon(new ImageIcon("icons/upload.png")); // Add an icon
        uploadButton.addActionListener(e -> uploadImage());

        rightColumn.add(imageLabel, BorderLayout.CENTER);
        rightColumn.add(uploadButton, BorderLayout.SOUTH);

        // Add Left and Right Columns to Form Panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(leftColumn, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(rightColumn, gbc);

        // Save Button (Centered Below the Columns)
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton saveButton = new JButton("Save Contact");
        saveButton.setIcon(new ImageIcon("icons/save.png")); // Add an icon
        saveButton.addActionListener(e -> saveContact());
        formPanel.add(saveButton, gbc);

        mainPanel.add(formPanel, BorderLayout.NORTH);

        // Contact Table
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Phone"}, 0);
        contactTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(contactTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Contacts"));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        frame.add(mainPanel, BorderLayout.CENTER);

        loadContacts();

        frame.setVisible(true);
    }

    private void uploadImage() {
        var fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (var fis = new FileInputStream(file)) {
                imageBytes = fis.readAllBytes();
                // Convert byte array to BufferedImage
                BufferedImage img = ImageIO.read(file);
                if (img != null) {
                    // Scale the image to fit the label
                    Image scaledImage = img.getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImage));
                    imageLabel.setText(""); // Clear the "No Image Selected" text
                } else {
                    JOptionPane.showMessageDialog(frame, "Unsupported image format!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Failed to load image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void saveContact() {
        String name = nameField.getText();
        String phone = phoneField.getText();

        if (name.isEmpty() || phone.isEmpty() || imageBytes == null) {
            JOptionPane.showMessageDialog(frame, "All fields are required!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            var contact = new Contact(name, phone, imageBytes);
            contactDao.AddContact(contact);
            JOptionPane.showMessageDialog(frame, "Contact saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadContacts();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Failed to save contact: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        nameField.setText("");
        phoneField.setText("");
        imageLabel.setIcon(null);
        imageLabel.setText("No Image Selected");
        imageBytes = null;
    }

    private void loadContacts() {
        try {
            List<Contact> contacts = contactDao.getAllContacts();
            tableModel.setRowCount(0);
            for (Contact contact : contacts) {
                tableModel.addRow(new Object[]{contact.getId(), contact.getName(), contact.getPhone()});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Failed to load contacts: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            var contactDao = new ContactDaoImpl(DbConnection.getInstance().getConnection());
            SwingUtilities.invokeLater(() -> new ContactManagerUI(contactDao));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}