package com.qihoo.health.view;

import com.qihoo.health.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MenuItemView extends FrameLayout {

	private Context mContext;

	private TextView mItemTitle;
	private TextView mItemHint;
	private LinearLayout mLine;

	public MenuItemView(Context context) {
		super(context);
		init(context);
	}

	public MenuItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public MenuItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		mContext = context;
		LayoutInflater.from(mContext).inflate(R.layout.view_menu_item, this,
				true);
		mItemTitle = (TextView) findViewById(R.id.itemTitle);
		mItemHint = (TextView) findViewById(R.id.itemHint);
		mLine = (LinearLayout) findViewById(R.id.line);
	}

	public void bindView(String title, String hint, OnClickListener listener,
			boolean hideHint, boolean hideLine) {
		mItemTitle.setText(title);
		if (hideHint)
			mItemHint.setVisibility(View.GONE);
		mItemHint.setText(hint);
		if (hideLine)
			mLine.setVisibility(GONE);
		this.setOnClickListener(listener);
	}

}
