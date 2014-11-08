package com.qihoo.health;

import java.util.List;

import com.qihoo.health.db.DBManager;
import com.qihoo.health.model.Message;
import com.qihoo.health.model.User;

import android.app.Application;

public class AppHealth extends Application {

	private static User USER;
	private static List<Message> MESSAGES;
	
	@Override
	public void onCreate() {
		super.onCreate();
		loadUser();
		loadMessages();
	}

	private void loadMessages() {
		MESSAGES = DBManager.getInstance(this).loadMessages();
	}

	private void loadUser() {
		USER = DBManager.getInstance(this).getUser();
	}

	public static User getUser() {
		return USER;
	}

	public static void setUser(User user) {
		USER = user;
	}

	public static List<Message> getMessages() {
		return MESSAGES;
	}

	public static void setMessages(List<Message> messages) {
		MESSAGES = messages;
	}
	
}
