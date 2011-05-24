package com.javalab.models.mapping;

public interface ContactMapping extends BaseMapping {

	/**
	 * Colonnes
	 */
	public static final String LASTNAME = "last_name";
	public static final String FIRSTNAME = "first_name";

	/**
	 * DML
	 */
	public static final String TABLE = "contacts";
	public static final String DML_CREATE = "CREATE TABLE " + TABLE + " (" + ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + LASTNAME + " TEXT NOT NULL, " + FIRSTNAME
			+ " TEXT NOT NULL);";
}
