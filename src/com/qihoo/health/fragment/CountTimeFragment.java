package com.qihoo.health.fragment;

import com.qihoo.health.R;
import com.qihoo.health.view.ActionTimeView;

import android.app.Application;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class CountTimeFragment extends Fragment {

	public final static String TAG = "CountTimeFragment";

	Application mApplication;

	private LinearLayout mCountChart;
	private Button mCountTime;
	private Button mCountDistance;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_count_time,
				container, false);
		mApplication = getActivity().getApplication();
		mCountChart = (LinearLayout) rootView.findViewById(R.id.countChart);
		addActionTime();
		mCountTime = (Button) rootView.findViewById(R.id.countTime);
		mCountDistance = (Button) rootView.findViewById(R.id.countDistance);
		mCountTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.bg_btn_circle_selected);
				mCountDistance
						.setBackgroundResource(R.drawable.bg_btn_circle_unselected);
				mCountTime.setTextColor(getResources().getColor(R.color.theme_blue));
				mCountDistance.setTextColor(getResources().getColor(android.R.color.white));
				addActionTime();
			}
		});
		mCountDistance.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setBackgroundResource(R.drawable.bg_btn_circle_selected);
				mCountTime
						.setBackgroundResource(R.drawable.bg_btn_circle_unselected);
				mCountTime.setTextColor(getResources().getColor(android.R.color.white));
				mCountDistance.setTextColor(getResources().getColor(R.color.theme_blue));
				addActionDis();
			}
		});
		return rootView;
	}

	private void addActionTime() {
		mCountChart.removeAllViews();
		ActionTimeView v1 = new ActionTimeView(getActivity(),
				R.color.theme_blue, "用鼠标", 9 * 60);
		ActionTimeView v2 = new ActionTimeView(getActivity(),
				R.color.theme_green, "打篮球", 2 * 60);
		ActionTimeView v3 = new ActionTimeView(getActivity(),
				R.color.theme_fuchsin_red, "骑车", 2 * 60);
		ActionTimeView v4 = new ActionTimeView(getActivity(),
				R.color.theme_orange, "LU", 25);
		mCountChart.addView(v1);
		mCountChart.addView(v2);
		mCountChart.addView(v3);
		mCountChart.addView(v4);
	}

	private void addActionDis() {
		mCountChart.removeAllViews();
		ActionTimeView v1 = new ActionTimeView(getActivity(), "鼠标在鼠标垫上运动了 5公里");
		ActionTimeView v2 = new ActionTimeView(getActivity(), "在篮球场上奔跑了 3公里");
		ActionTimeView v3 = new ActionTimeView(getActivity(), "骑车行驶了 3公里");
		ActionTimeView v4 = new ActionTimeView(getActivity(), "撸，上下活动了:) 0.1公里");
		mCountChart.addView(v1);
		mCountChart.addView(v2);
		mCountChart.addView(v3);
		mCountChart.addView(v4);
	}
}
