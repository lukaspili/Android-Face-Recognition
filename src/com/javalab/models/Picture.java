package com.javalab.models;

import java.io.Serializable;
import java.util.Date;

import android.net.Uri;

import com.javalab.models.mapping.PictureMapping;

public class Picture implements PictureMapping, Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String uri;
	private Date date;
	private Contact contact;

	public Picture() {

	}

	public Picture(Long id, String uri, Date date, Contact contact) {
		this.id = id;
		this.uri = uri;
		this.date = date;
		this.contact = contact;
	}

	public Uri getPictureUri() {
		return Uri.parse(uri);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}
}
