package com.javalab.services;

import java.util.List;

import android.content.Context;

import com.javalab.dao.DaoFactory;
import com.javalab.dao.PictureDao;
import com.javalab.models.Contact;
import com.javalab.models.Picture;

public class ContactService {

	private Context context;

	public ContactService(Context context) {
		this.context = context;
	}

	public void update(Contact contact) {

		DaoFactory.getContactDao(context).updateContact(contact);
	}

	public Contact getContact(Long id) {

		Contact contact = DaoFactory.getContactDao(context).getContactById(id);
		List<Picture> pictures = DaoFactory.getPictureDao(context).getPicturesFromContact(contact);
		contact.setPictures(pictures);

		return contact;
	}

	public List<Contact> getContacts() {
		return DaoFactory.getContactDao(context).getContacts();
	}

	public Long addContact(Contact contact) {

		Long id = DaoFactory.getContactDao(context).addContact(contact);

		if (null == id) {
			return null;
		}

		contact.setId(id);

		PictureDao pictureDao = DaoFactory.getPictureDao(context);
		for (Picture picture : contact.getPictures()) {
			picture.setContact(contact);
			pictureDao.addPicture(picture);
		}
		
		return id;
	}
}
