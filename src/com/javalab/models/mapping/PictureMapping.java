package com.javalab.models.mapping;


public interface PictureMapping extends BaseMapping {

	public static final String URI = "uri";
	public static final String DATE = "date";
	public static final String CONTACT_ID = "contact_id";

	public static final String TABLE = "pictures";
	public static final String DML_CREATE = "CREATE TABLE " + TABLE + " (" + ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + URI + " TEXT NOT NULL, " + DATE + " TEXT NOT NULL, "
			+ CONTACT_ID + " INTEGER NOT NULL);";
}
