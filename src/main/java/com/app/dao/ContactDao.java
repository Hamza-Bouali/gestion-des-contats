package com.app.dao;

import java.sql.SQLException;
import com.app.contact.Contact;
import java.util.List;

public interface ContactDao {
	
	




    void AddContact(Contact contact) throws SQLException; 
    Contact getContactById(String string) throws SQLException;
    List<Contact> getAllContacts() throws SQLException; 
    void deleteContact(int id) throws SQLException;
    List<Contact> searchContact(String critere) throws SQLException; 
	void updateContact(Contact emprunt) throws SQLException;

}
