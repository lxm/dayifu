package com.qihoo.health.model;

import android.content.Context;
import android.database.Cursor;

public class Message {
	public int id;
	public boolean fromMe = false;
	public boolean hasText = false;
	public String text = null;
	public boolean hasImage = false;
	public String imageUrl = null;

	public Message() {

	}

	public Message(Cursor cursor) {
		if (cursor != null) {
			id = cursor.getInt(0);
			fromMe = cursor.getInt(1) == 1;
			hasText = cursor.getInt(2) == 1;
			text = cursor.getString(3);
			hasImage = cursor.getInt(4) == 1;
			imageUrl = cursor.getString(5);
		}
	}

	public Message(boolean fromMe, boolean hasText, int textResId,
			boolean hasImage, String imageUrl, Context context) {
		super();
		this.fromMe = fromMe;
		this.hasText = hasText;
		this.text = context.getResources().getString(textResId);
		this.hasImage = hasImage;
		this.imageUrl = imageUrl;
	}

	public Message(boolean fromMe, boolean hasText, String text,
			boolean hasImage, String imageUrl, Context context) {
		super();
		this.fromMe = fromMe;
		this.hasText = hasText;
		this.text = text;
		this.hasImage = hasImage;
		this.imageUrl = imageUrl;
	}
}
