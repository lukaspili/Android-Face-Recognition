package com.javalab.dao.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.javalab.dao.ContactDao;
import com.javalab.models.Contact;

public class ContactDaoSqlite extends BaseDaoSqlite implements ContactDao {

	public ContactDaoSqlite(Context context) {
		super(context);
	}

	@Override
	public List<Contact> getContacts() {

		List<Contact> list = new ArrayList<Contact>();

		String[] cols = { Contact.ID, Contact.LASTNAME, Contact.FIRSTNAME };

		Cursor cursor = getDb().query(Contact.TABLE, cols, null, null, null, null, null);

		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {

			Contact contact = new Contact(cursor.getLong(0), cursor.getString(1), cursor.getString(2));

			list.add(contact);

			cursor.moveToNext();
		}

		cursor.close();
		getDb().close();

		return list;
	}

	@Override
	public Long addContact(Contact contact) {

		// mapper pour insérer dans la table
		ContentValues values = mapFromContact(contact);

		// inserer ces valeurs dans la table
		Long id = getDb().insert(Contact.TABLE, null, values);

		// puis fermer la connexion
		getDb().close();

		return id;
	}

	@Override
	public Contact getContactById(Long id) {

		String[] cols = { Contact.ID, Contact.LASTNAME, Contact.FIRSTNAME };
		String[] params = { id.toString() };

		Cursor cursor = getDb().query(Contact.TABLE, cols, Contact.ID + "= ?", params, null, null, null);

		cursor.moveToFirst();

		Contact contact = new Contact(cursor.getLong(0), cursor.getString(1), cursor.getString(2));

		cursor.close();
		getDb().close();

		return contact;
	}

	@Override
	public void updateContact(Contact contact) {

		// mapper pour modifier dans la table
		ContentValues values = mapFromContact(contact);

		// ajouter un paramètre dans la requête
		String[] params = { contact.getId().toString() };

		// executer la requête d'update
		getDb().update(Contact.TABLE, values, Contact.ID + " = ?", params);

		// puis fermer la connexion
		getDb().close();
	}

	/**
	 * Retourne les valeurs mappée d'une contact
	 * 
	 * @param Contact the contact to map
	 * @return ContentValues the mapped values
	 */
	protected ContentValues mapFromContact(Contact contact) {

		// sélectionner les valeurs à ajouter dans la table
		ContentValues values = new ContentValues();
		values.put(Contact.LASTNAME, contact.getLastName());
		values.put(Contact.FIRSTNAME, contact.getFirstName());

		return values;
	}
}
