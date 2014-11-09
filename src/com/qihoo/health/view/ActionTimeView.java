package com.qihoo.health.view;

import com.qihoo.health.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActionTimeView extends LinearLayout {

	private Context mContext;
	View mViewPoint;
	LinearLayout mViewRectangel;
	TextView mActionTypeText;
	TextView mActionTimeText;

	public ActionTimeView(Context context, int colorId, String actionType, int minutes) {
		super(context);
		init(context);
		setStyle(colorId, actionType, minutes);
	}
	
	public ActionTimeView(Context context, String distanceDiscribe) {
		super(context);
		init(context);
		setStyle(distanceDiscribe);
	}
	
	public ActionTimeView(Context context) {
		super(context);
		init(context);
	}

	public ActionTimeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public ActionTimeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		mContext = context;
		LayoutInflater.from(mContext).inflate(R.layout.view_action_time, this,
				true);
		mViewPoint = findViewById(R.id.viewPoint);
		mViewRectangel = (LinearLayout) findViewById(R.id.viewRectangel);
		mActionTypeText = (TextView) findViewById(R.id.actionTypeText);
		mActionTimeText = (TextView) findViewById(R.id.actionTimeText);
	}

	private void setStyle(int colorId, String actionType, int minutes) {
//		mViewPoint.setBackgroundColor(colorId);
//		mViewRectangel.setBackgroundColor(colorId);
		mActionTypeText.setText(actionType + ":");
		if (minutes > 60)
			mActionTimeText.setText(minutes/60 + "小时");
		else 
			mActionTimeText.setText(minutes + "分钟");
		LinearLayout.LayoutParams params = (LayoutParams) mViewRectangel
				.getLayoutParams();
		params.width = minutes * 30 / 60;
		mViewRectangel.setLayoutParams(params);
	}

	private void setStyle(String describe) {
//		mViewPoint.setBackgroundColor(colorId);
//		mViewRectangel.setBackgroundColor(colorId);
		mActionTypeText.setText(describe);
		mViewRectangel.setVisibility(View.GONE);
		mActionTimeText.setVisibility(View.GONE);
	}

}
