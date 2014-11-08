package com.qihoo.health;

import java.util.List;

import com.qihoo.health.model.Message;
import com.qihoo.health.model.User;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

	private User mUser;
	private List<Message> mMessages;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mUser = AppHealth.getUser();
		if (mUser == null) {
			onRegister();
		}
		
		setContentView(R.layout.activity_main);
	}

	private void onRegister() {
		MessagesActivity.actionMessages(this);
		finish();
	}
}
