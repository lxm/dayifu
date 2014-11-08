package com.qihoo.health;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.qihoo.health.controller.HealthController;
import com.qihoo.health.controller.HealthListener;
import com.qihoo.health.db.DBManager;
import com.qihoo.health.model.Message;
import com.qihoo.health.model.User;

public class MessagesActivity extends Activity {

	public static void actionMessages(Context context) {
		Intent intent = new Intent(context, MessagesActivity.class);
		context.startActivity(intent);
	}

	private ListView mThreadList;
	private EditText mInputText;
	private Button mButtonNegative;
	private Button mButtonPositive;
	private LinearLayout mButtonsContainer;
	private LinearLayout mButtonLine;
	private LinearLayout mMenuContainer;
	private LinearLayout mInputContainer;

	private User mUser;
	private List<Message> mMessages;

	private MessagesAdapter mAdapter;

	private boolean mRegistering = false;
	private int mRegisterStep = 0;

	private HealthController mController;
	private MessageListener mListener = new MessageListener();
	private MessageHandler mHandler = new MessageHandler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messages);
		mController = HealthController.getInstance(getApplication());
		findViews();
		mUser = AppHealth.getUser();
		mMessages = AppHealth.getMessages();
		if (mMessages == null) {
			mMessages = new ArrayList<Message>();
		}
		bindMessages();
		if (mUser == null) {
			mRegistering = true;
			register();
		}
	}

	private void findViews() {
		mThreadList = (ListView) findViewById(R.id.threadList);
		mInputText = (EditText) findViewById(R.id.inputText);
		mButtonNegative = (Button) findViewById(R.id.buttonNegative);
		mButtonPositive = (Button) findViewById(R.id.buttonPositive);
		// mButtonLine = (LinearLayout) findViewById(R.id.buttonLine);
		mMenuContainer = (LinearLayout) findViewById(R.id.menuContainer);
		mButtonsContainer = (LinearLayout) findViewById(R.id.buttonsContainer);
		mInputContainer = (LinearLayout) findViewById(R.id.inputContainer);
	}

	@Override
	protected void onDestroy() {
		if (mRegistering) {
			AppHealth.setUser(null);
			AppHealth.setMessages(null);
		}
		super.onDestroy();
	}

	// 开始注册流程
	private void register() {
		Message message;
		switch (mRegisterStep) {
		case 0:
			// 发送欢迎消息
			mUser = new User();
			message = new Message(false, true, R.string.message_welcome, false,
					null, this);
			appendMessage(message, false, true);
			break;
		case 1:
			// 填写名字
			message = new Message(false, true, R.string.message_require_name,
					false, null, this);
			appendMessage(message, false, false);
			showBottomMenu();
			break;
		case 2:
			// 填写填写手机号
			message = new Message(false, true, getResources().getString(
					R.string.message_name_inputed)
					+ mUser.name, false, null, this);
			appendMessage(message, false, false);
			message = new Message(false, true, R.string.message_require_phone,
					false, null, this);
			appendMessage(message, false, false);
			showBottomMenu();
			break;
		case 3:
			// 填写验证码
			showBottomMenu();
			break;
		case 4:
			// 注册完成
			// message = new Message(false, true, getResources().getString(
			// R.string.message_register_completed), false, null, this);
			// mMessages.clear();
			// mRegistering = false;
			// appendMessage(message, true, true);
			break;
		}
	}

	private void bindMessages() {
		mAdapter = new MessagesAdapter();
		mThreadList.setAdapter(mAdapter);
		mThreadList.setSelection(mMessages.size());
	}

	private class ViewHolder {
		LinearLayout itemContainer;
		TextView textView;
		ImageView imageView;
	}

	private class MessagesAdapter extends BaseAdapter {
		LayoutInflater inflater = null;

		@Override
		public int getCount() {
			return mMessages.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if (convertView == null) {
				if (inflater == null) {
					inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				}
				convertView = inflater.inflate(R.layout.item_thread, parent,
						false);
				viewHolder = new ViewHolder();
				viewHolder.itemContainer = (LinearLayout) convertView
						.findViewById(R.id.itemContainer);
				viewHolder.textView = (TextView) convertView
						.findViewById(R.id.textView);
				viewHolder.imageView = (ImageView) convertView
						.findViewById(R.id.imageView);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			if (mMessages.get(position).fromMe) {
				viewHolder.itemContainer.setGravity(Gravity.RIGHT);
			} else {
				viewHolder.itemContainer.setGravity(Gravity.LEFT);
			}
			if (mMessages.get(position).hasText) {
				viewHolder.textView.setVisibility(View.VISIBLE);
				viewHolder.textView.setText(mMessages.get(position).text);
			} else {
				viewHolder.textView.setVisibility(View.GONE);
			}
			if (mMessages.get(position).hasImage) {
				viewHolder.imageView.setVisibility(View.VISIBLE);
			} else {
				viewHolder.imageView.setVisibility(View.GONE);
			}
			return convertView;
		}

	}

	/**
	 * 向列表中添加消息
	 * 
	 * @param message
	 *            要显示的消息
	 * @param needSave
	 *            是否需要记录进入数据库
	 * @param stepMoved
	 *            是否完成了注册的一个步骤
	 */
	private void appendMessage(Message message, boolean needSave,
			boolean stepMoved) {
		if (message == null)
			return;
		mMessages.add(message);
		mAdapter.notifyDataSetChanged();
		if (needSave) {
//			DBManager.getInstance(this).saveMessage(message);
			mController.saveMessage(message);
		}
		mThreadList.setSelection(mMessages.size());
		if (mRegistering && stepMoved) {
			mRegisterStep++;
			register();
		}
	}

	// 展示下面的菜单
	private void showBottomMenu() {
		if (mRegistering) {
			switch (mRegisterStep) {
			case 1:
				// 填写名字
				mMenuContainer.setVisibility(View.VISIBLE);
				mInputContainer.setVisibility(View.VISIBLE);
				mButtonsContainer.setVisibility(View.VISIBLE);
				mInputText.setHint(R.string.hint_input_name);
				mButtonNegative.setText(R.string.button_any_name);
				mButtonPositive.setText(R.string.button_name_ok);
				mButtonPositive.setVisibility(View.VISIBLE);
				mButtonNegative.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mUser.name = "9527";
						Message message = new Message(true, true,
								R.string.message_default_name, false, null,
								MessagesActivity.this);
						mInputText.setText("");
						// mMenuContainer.setVisibility(View.GONE);
						// mInputContainer.setVisibility(View.GONE);
						appendMessage(message, false, true);
					}
				});
				mButtonPositive.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (mInputText.getText() == null
								|| mInputText.getText().toString().equals(""))
							return;
						mUser.name = mInputText.getText().toString();
						Message message = new Message(true, true,
								getResources().getString(
										R.string.message_custom_name)
										+ mUser.name, false, null,
								MessagesActivity.this);
						mInputText.setText("");
						// mMenuContainer.setVisibility(View.GONE);
						// mInputContainer.setVisibility(View.GONE);
						appendMessage(message, false, true);
					}
				});
				break;
			case 2:
				// 填写手机号
				mMenuContainer.setVisibility(View.VISIBLE);
				mInputContainer.setVisibility(View.VISIBLE);
				mButtonsContainer.setVisibility(View.VISIBLE);
				mInputText.setHint(R.string.hint_input_phone);
				mButtonPositive.setText(R.string.button_phone_ok);
				mButtonNegative.setVisibility(View.GONE);
				mButtonPositive.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (mInputText.getText() == null
								|| mInputText.getText().toString().equals(""))
							return;
						mUser.phoneNum = mInputText.getText().toString();
						mController.registerPhone(mUser.phoneNum, mListener);
						Message message = new Message(true, true,
								getResources().getString(
										R.string.message_phone_inputed)
										+ mUser.phoneNum, false, null,
								MessagesActivity.this);
						appendMessage(message, false, false);
						mInputText.setText("");
						// mMenuContainer.setVisibility(View.GONE);
						// mInputContainer.setVisibility(View.GONE);
					}
				});

				break;
			case 3:
				// 填写验证码
				mMenuContainer.setVisibility(View.VISIBLE);
				mInputContainer.setVisibility(View.VISIBLE);
				mButtonsContainer.setVisibility(View.VISIBLE);
				mButtonNegative.setVisibility(View.VISIBLE);
				mInputText.setHint(R.string.hint_input_verify_code);
				mButtonPositive.setText(R.string.button_verify_code_ok);
				mButtonNegative.setText(R.string.button_verify_code_retry);
				mButtonNegative.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// mUser.name = "9527";
						// Message message = new Message(true, true,
						// R.string.message_default_name, false, null,
						// MessagesActivity.this);
						// mInputText.setText("");
						// // mMenuContainer.setVisibility(View.GONE);
						// // mInputContainer.setVisibility(View.GONE);
						// appendMessage(message, false, true);
						mInputText.setText("");
					}
				});
				mButtonPositive.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (mInputText.getText() == null
								|| mInputText.getText().toString().equals(""))
							return;
						mController.checkCode(mUser.phoneNum, mInputText.getText().toString(), mListener);
						Message message = new Message(true, true,
								getResources().getString(
										R.string.message_verify_code_inputed)
										+ mInputText.getText().toString(),
								false, null, MessagesActivity.this);
						appendMessage(message, false, false);
//						mMenuContainer.setVisibility(View.GONE);
//						mInputContainer.setVisibility(View.GONE);
						mInputText.setText("");
					}
				});

				break;
			}
		}
	}

	class MessageListener extends HealthListener {

		@Override
		public void registerFinished() {
			mHandler.registerFinished();
		}

		@Override
		public void registerFailed() {
			mHandler.registerFailed();
		}

		@Override
		public void checkCodeFinished() {
			mHandler.checkCodeFinished();
		}

		@Override
		public void checkCodeFailed() {
			mHandler.checkCodeFailed();
		}
	}

	class MessageHandler extends Handler {

		public void registerFinished() {
			post(new Runnable() {

				@Override
				public void run() {
					Message message = new Message(false, true, mUser.name
							+ getResources().getString(
									R.string.message_require_verify_code),
							false, null, MessagesActivity.this);
					appendMessage(message, false, true);
				}
			});
		}

		public void registerFailed() {
			post(new Runnable() {

				@Override
				public void run() {
					Message message = new Message(false, true, "手机号注册失败了，请重试",
							false, null, MessagesActivity.this);
					appendMessage(message, false, false);
				}
			});
		}

		public void checkCodeFailed() {
			post(new Runnable() {

				@Override
				public void run() {
					// 验证错误
					Message message = new Message(false, true, "验证失败，请重试",
							false, null, MessagesActivity.this);
					appendMessage(message, false, false);
				}

			});
		}

		public void checkCodeFinished() {
			post(new Runnable() {

				@Override
				public void run() {
					// 注册完成
					Message message = new Message(false, true, getResources()
							.getString(R.string.message_register_completed),
							false, null, MessagesActivity.this);
					mMessages.clear();
					mRegistering = false;
					appendMessage(message, true, true);
					mInputContainer.setVisibility(View.GONE);
					mButtonsContainer.setVisibility(View.GONE);
					mController.saveUser(mUser);
				}

			});
		}
	}
}
