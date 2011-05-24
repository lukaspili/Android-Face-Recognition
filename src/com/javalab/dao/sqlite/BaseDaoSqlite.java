package com.javalab.dao.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class BaseDaoSqlite {

	protected SqliteDatabaseHelper helper;

	protected SQLiteDatabase db;

	/**
	 * Lors de la construction, instancier notre DbHelper avec le context de
	 * l'activité qui appelle le DAO
	 * 
	 * @param context the activity
	 */
	public BaseDaoSqlite(Context context) {
		helper = new SqliteDatabaseHelper(context);
	}

	/**
	 * Retourner une instance SQLiteDatabase qui represente la connexion à la
	 * base de donnée Ici nous retournons toujours une connexion lecture +
	 * écriture car plus pratique mais pas ce n'est pas forcement la bonne
	 * pratique
	 * 
	 * @return SQLiteDatabase db
	 */
	protected SQLiteDatabase getDb() {

		// si aucune connexion n'a été ouverte ou que celle ci est fermée alors
		// ouvrir
		// une nouvelle connexion
		if (null == db || !db.isOpen()) {
			db = helper.getWritableDatabase();
		}

		return db;
	}
}
