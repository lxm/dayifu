package com.qihoo.health;

import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.qihoo.health.fragment.CountTimeFragment;
import com.qihoo.health.model.Message;
import com.qihoo.health.model.User;

public class MainActivity extends ParentActivityActionBar {

	public static void actionMain(Context context) {
		Intent intent = new Intent(context, MainActivity.class);
		context.startActivity(intent);
	}

	private User mUser;
	private List<Message> mMessages;

	private LinearLayout mTab1;
	private LinearLayout mTab2;
	private LinearLayout mTab3;
	private LinearLayout mTab4;

	private OnClickListener mOnTabClickListener;

	private CountTimeFragment mCountTimeFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mUser = AppHealth.getUser();
		if (mUser == null) {
			onRegister();
		}

		setContentView(R.layout.activity_main);
		mCountTimeFragment = new CountTimeFragment();
		getFragmentManager().beginTransaction().add(R.id.fragmentContainer,
				mCountTimeFragment, CountTimeFragment.TAG).commit();
		findViews();
	}

	private void findViews() {
		mTab1 = (LinearLayout) findViewById(R.id.tab1);
		mTab2 = (LinearLayout) findViewById(R.id.tab2);
		mTab3 = (LinearLayout) findViewById(R.id.tab3);
		mTab4 = (LinearLayout) findViewById(R.id.tab4);
		mOnTabClickListener = new OnTabClickListener();
		mTab1.setOnClickListener(mOnTabClickListener);
		mTab2.setOnClickListener(mOnTabClickListener);
		mTab3.setOnClickListener(mOnTabClickListener);
		mTab4.setOnClickListener(mOnTabClickListener);
	}

	private void onRegister() {
		MessagesActivity.actionMessages(this);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.messages) {
			MessagesActivity.actionMessages(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/*
	 * 点击下面的tab执行切换操作
	 */
	class OnTabClickListener implements OnClickListener {

		private LinearLayout mCurrentTab;

		public OnTabClickListener() {
			mCurrentTab = mTab1;
		}

		@Override
		public void onClick(View v) {
			if (mCurrentTab == v) {
				return;
			}
			changeState(mCurrentTab, false);
			changeState(v, true);
			mCurrentTab = (LinearLayout) v;
		}

		void changeState(View v, boolean focused) {
			int id = v.getId();
			ImageView image;
			switch (id) {
			case R.id.tab1:
				image = (ImageView) v.findViewById(R.id.tab1Image);
				if (focused) {
					image.setImageResource(R.drawable.tab1_count_focused);
				} else {
					image.setImageResource(R.drawable.tab1_count_unfocused);
				}
				break;
			case R.id.tab2:
				image = (ImageView) v.findViewById(R.id.tab2Image);
				if (focused) {
					image.setImageResource(R.drawable.tab2_report_focused);
				} else {
					image.setImageResource(R.drawable.tab2_report_unfocused);
				}
				break;
			case R.id.tab3:
				image = (ImageView) v.findViewById(R.id.tab3Image);
				if (focused) {
					image.setImageResource(R.drawable.tab3_discuss_focused);
				} else {
					image.setImageResource(R.drawable.tab3_discuss_unfocused);
				}
				break;
			case R.id.tab4:
				image = (ImageView) v.findViewById(R.id.tab4Image);
				if (focused) {
					image.setImageResource(R.drawable.tab4_me_focused);
				} else {
					image.setImageResource(R.drawable.tab4_me_unfocused);
				}
				break;
			}
		}

	}

}
