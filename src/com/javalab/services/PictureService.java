package com.javalab.services;

import java.util.List;

import android.content.Context;

import com.javalab.dao.DaoFactory;
import com.javalab.models.Contact;
import com.javalab.models.Picture;

public class PictureService {

	private Context context;

	public PictureService(Context context) {
		this.context = context;
	}

	public void addPicture(Picture picture) {
		DaoFactory.getPictureDao(context).addPicture(picture);
	}

	public List<Picture> getPicturesFromContact(Contact contact) {
		return DaoFactory.getPictureDao(context).getPicturesFromContact(contact);
	}
}
