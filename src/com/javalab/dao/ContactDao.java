package com.javalab.dao;

import java.util.List;

import com.javalab.models.Contact;

public interface ContactDao {

	public List<Contact> getContacts();

	public Long addContact(Contact contact);

	public void updateContact(Contact contact);

	public Contact getContactById(Long id);

}
