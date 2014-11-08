package com.qihoo.health.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static final int DB_VERSION = 1;
	public static final String DB_NAME = "doctor.db";
	public static final String TABLE_NAME = "messages";

	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	public interface MessagesColumns {
		public static final String ID = "_id";
		public static final String FROM_ME = "from_me";
		public static final String HAS_TEXT = "has_text";
		public static final String TEXT = "_text";
		public static final String HAS_IMAGE = "has_image";
		public static final String IMAGE_URL = "image_url";
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table " + TABLE_NAME + "(" + MessagesColumns.ID
				+ " integer PRIMARY KEY AUTOINCREMENT, "
				+ MessagesColumns.FROM_ME + " integer DEFAULT 0, "
				+ MessagesColumns.HAS_TEXT + " integer DEFAULT 0, "
				+ MessagesColumns.TEXT + " TEXT, " + MessagesColumns.HAS_IMAGE
				+ " integer DEFAULT 0," + MessagesColumns.IMAGE_URL + " TEXT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + DB_NAME);
		onCreate(db);
	}

}
