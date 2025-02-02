package com.app.contact;

public class Contact {
    private int id;
    private String name;
    private String phone;
    private byte[] image; // Store image as binary data

    // Constructor
    public Contact(String name, String phone, byte[] image) {
        this.name = name;
        this.phone = phone;
        this.image = image;
    }

    public Contact(int id, String name, String phone, byte[] image) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.image = image;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
