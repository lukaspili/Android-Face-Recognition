package com.javalab.dao;

import java.util.List;

import com.javalab.models.Contact;
import com.javalab.models.Picture;

public interface PictureDao {

	public List<Picture> getPicturesFromContact(Contact contact);

	public Long addPicture(Picture picture);
}
