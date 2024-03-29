package com.qihoo.health.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.qihoo.health.model.Message;
import com.qihoo.health.model.User;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBManager {
	private static DBManager dao = null;
	private Context mContext;

	private DBManager(Context context) {
		mContext = context;
	}

	public static DBManager getInstance(Context context) {
		if (dao == null) {
			dao = new DBManager(context);
		}
		return dao;
	}

	public SQLiteDatabase getConnection() {
		SQLiteDatabase sqLiteDatabase = null;
		try {
			sqLiteDatabase = new DBHelper(mContext).getReadableDatabase();
		} catch (Exception e) {
			Log.e("test", "连接数据库错误：" + e);
		}
		return sqLiteDatabase;
	}

	/**
	 * 保存消息
	 * 
	 * @param message
	 */
	public synchronized void saveMessage(Message message) {
		SQLiteDatabase db = getConnection();
		try {
			String sql = "insert into " + DBHelper.TABLE_NAME_MESSAGES + " ("
					+ DBHelper.MessagesColumns.FROM_ME + ", "
					+ DBHelper.MessagesColumns.HAS_TEXT + ", "
					+ DBHelper.MessagesColumns.TEXT + ", "
					+ DBHelper.MessagesColumns.HAS_IMAGE + ", "
					+ DBHelper.MessagesColumns.IMAGE_URL
					+ ") values (?, ?, ?, ?, ?)";
			Object[] bindArgs = { message.fromMe ? 1 : 0,
					message.hasText ? 1 : 0, message.text,
					message.hasImage ? 1 : 0, message.imageUrl };
			db.execSQL(sql, bindArgs);
		} catch (Exception e) {
			Log.e("test", "保存信息失败：" + e);
		} finally {
			if (null != db) {
				db.close();
			}
		}
	}

	/**
	 * 获取消息，取最新的20条
	 * 
	 * @return
	 */
	public synchronized List<Message> loadMessages() {
		List<Message> messages = new ArrayList<Message>();
		SQLiteDatabase db = getConnection();
		Cursor cursor = null;
		try {
			String sql = "select * from " + DBHelper.TABLE_NAME_MESSAGES
					+ " order by " + DBHelper.MessagesColumns.ID
					+ " desc limit 20";
			cursor = db.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				Message message = new Message(cursor);
				messages.add(message);
			}
			Collections.reverse(messages); // 取出的数据是反的，需要反序调整
			return messages;
		} catch (Exception e) {
			Log.e("test", "读取信息失败：" + e);
		} finally {
			if (null != db) {
				db.close();
			}
		}

		return null;
	}

	/**
	 * 保存用户信息
	 * 
	 * @param user
	 */
	public synchronized void saveUser(User user) {
		SQLiteDatabase db = getConnection();
		try {
			String sql = "insert into " + DBHelper.TABLE_NAME_USER + " ("
					+ DBHelper.UserColumns.NAME + ", "
					+ DBHelper.UserColumns.PHONE_NUM + ", "
					+ DBHelper.UserColumns.GENDER + ", "
					+ DBHelper.UserColumns.AGE + ") values (?, ?, ?, ?)";
			Object[] bindArgs = { user.name, user.phoneNum, user.gender,
					user.age };
			db.execSQL(sql, bindArgs);
		} catch (Exception e) {
			Log.e("test", "保存信息失败：" + e);
		} finally {
			if (null != db) {
				db.close();
			}
		}
	}

	/**
	 * 获取用户信息
	 * @return
	 */
	public synchronized User getUser() {
		SQLiteDatabase db = getConnection();
		Cursor cursor = null;
		try {
			String sql = "select * from " + DBHelper.TABLE_NAME_USER;
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				User user = new User(cursor);
				return user;
			}
			return null;
		} catch (Exception e) {
			Log.e("test", "读取信息失败：" + e);
		} finally {
			if (null != db) {
				db.close();
			}
		}

		return null;
	}
}
