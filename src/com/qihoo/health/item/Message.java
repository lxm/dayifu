package com.qihoo.health.item;

import android.database.Cursor;

public class Message {
	public boolean fromMe = false;
	public boolean hasText = false;
	public String text = null;
	public boolean hasImage = false;
	public String imageUrl = null;

	public Message() {

	}

	public Message(Cursor cursor) {
		if (cursor != null) {
			fromMe = cursor.getInt(0) == 1;
			hasText = cursor.getInt(1) == 1;
			text = cursor.getString(2);
			hasImage = cursor.getInt(3) == 1;
			imageUrl = cursor.getString(4);
		}
	}
}
