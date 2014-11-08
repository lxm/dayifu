package com.qihoo.health.model;

import android.database.Cursor;

public class User {
	public int id;
	public String name;
	public String phoneNum;
	public String gender;
	public String age;
	
	public User() {
		
	}
	
	public User(Cursor cursor) {
		id = cursor.getInt(0);
		name = cursor.getString(1);
		phoneNum = cursor.getString(2);
		gender = cursor.getString(3);
		age = cursor.getString(4);
	}
}
