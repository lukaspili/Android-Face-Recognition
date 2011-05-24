package com.javalab.dao.sqlite;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.javalab.dao.PictureDao;
import com.javalab.models.Contact;
import com.javalab.models.Picture;

public class PictureDaoSqlite extends BaseDaoSqlite implements PictureDao {

	public PictureDaoSqlite(Context context) {
		super(context);
	}

	@Override
	public List<Picture> getPicturesFromContact(Contact contact) {

		List<Picture> list = new ArrayList<Picture>();

		String[] cols = { Picture.ID, Picture.URI };
		String[] params = { contact.getId().toString() };

		Cursor cursor = getDb().query(Picture.TABLE, cols, Picture.CONTACT_ID + "= ?", params, null, null, null);

		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {

			Picture picture = new Picture(Long.valueOf(cursor.getLong(0)), cursor.getString(1), new Date(), contact);

			list.add(picture);

			cursor.moveToNext();
		}

		cursor.close();
		getDb().close();

		return list;
	}

	@Override
	public Long addPicture(Picture picture) {

		// mapper pour insérer dans la table
		ContentValues values = mapFromContact(picture);

		// inserer ces valeurs dans la table
		Long id = getDb().insert(Picture.TABLE, null, values);

		// puis fermer la connexion
		getDb().close();

		return id;
	}

	protected ContentValues mapFromContact(Picture picture) {

		// sélectionner les valeurs à ajouter dans la table
		ContentValues values = new ContentValues();
		values.put(Picture.URI, picture.getUri());
		values.put(Picture.DATE, new Date().toString());
		values.put(Picture.CONTACT_ID, picture.getContact().getId());

		return values;
	}

}
