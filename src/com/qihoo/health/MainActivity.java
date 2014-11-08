package com.qihoo.health;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

public class MainActivity extends Activity {

	private ListView mThreadList;
	private EditText mInputText;
	private Button mButtonNegative;
	private Button mButtonPositive;
	private LinearLayout mButtonLine;
	private LinearLayout mMenuContainer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViews();
	}

	private void findViews() {
		mThreadList = (ListView) findViewById(R.id.threadList);
		mInputText = (EditText) findViewById(R.id.inputText);
		mButtonNegative = (Button) findViewById(R.id.buttonNegative);
		mButtonPositive = (Button) findViewById(R.id.buttonPositive);
		mButtonLine = (LinearLayout) findViewById(R.id.buttonLine);
		mMenuContainer = (LinearLayout) findViewById(R.id.menuContainer);
	}
}
