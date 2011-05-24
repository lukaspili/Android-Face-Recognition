package com.javalab.dao.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.javalab.models.Contact;
import com.javalab.models.Picture;

public class SqliteDatabaseHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "facerecognition.db";
	private static final int DB_VERSION = 3;

	public SqliteDatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(Contact.DML_CREATE);
		db.execSQL(Picture.DML_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + Contact.TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + Picture.TABLE);
		onCreate(db);
	}
}
