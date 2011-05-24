package com.javalab.models;

import java.io.Serializable;
import java.util.List;

import com.javalab.models.mapping.ContactMapping;

public class Contact implements ContactMapping, Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String lastName;
	private String firstName;

	private List<Picture> pictures;

	public Contact() {

	}

	public Contact(Long id, String lastName, String firstName) {
		this.id = id;
		this.lastName = lastName;
		this.firstName = firstName;
	}

	@Override
	public String toString() {
		return firstName + " " + lastName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public List<Picture> getPictures() {
		return pictures;
	}

	public void setPictures(List<Picture> pictures) {
		this.pictures = pictures;
	}
}
