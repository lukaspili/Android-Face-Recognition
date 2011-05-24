package com.javalab.dao;

import android.content.Context;

import com.javalab.dao.sqlite.ContactDaoSqlite;
import com.javalab.dao.sqlite.PictureDaoSqlite;

public class DaoFactory {

	public static ContactDao getContactDao(Context context) {
		return new ContactDaoSqlite(context);
	}

	public static PictureDao getPictureDao(Context context) {
		return new PictureDaoSqlite(context);
	}
}
