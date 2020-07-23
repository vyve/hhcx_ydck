package com.estar.hh.survey.view.activity.video;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupWindow;


import com.estar.hh.survey.R;
import com.estar.hh.survey.adapter.video.UserPopwindowAdapter;
import com.estar.hh.survey.entity.vo.video.VideoUserInfoVO;

import java.util.ArrayList;
import java.util.List;


public class UserPopWindow extends PopupWindow {

	private ListView userList;
	private UserPopwindowAdapter adapter;
	List<VideoUserInfoVO> list = new ArrayList<VideoUserInfoVO>();
	private int userType;
	public UserPopWindow(Activity context, List<VideoUserInfoVO> list, int userType, String setLossName) {
		super(context);
		this.list = list;
		this.userType=userType;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View mMenuView = inflater.inflate(R.layout.user_popwindow, null);
		userList = mMenuView.findViewById(R.id.selectList);
		adapter = new UserPopwindowAdapter(context, list,userType,setLossName);
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		setAnimationStyle(R.style.PopupAnimation);
		this.setContentView(mMenuView);
		this.setWidth((int)(dm.widthPixels/1.5));
		this.setHeight(dm.heightPixels/2);
		this.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0x00000000);
		this.setBackgroundDrawable(dw);
		this.update();
		userList.setAdapter(adapter);
	}

	public void notifyDataSetChanged(List<VideoUserInfoVO> list) {
		this.list = list;
		adapter.notifyDataSetChanged();
	}

	public void show(View v) {
//		int[] location = new int[2];
//		v.getLocationOnScreen(location);
//
//		showAtLocation(v, Gravity.NO_GRAVITY, location[0],
//				location[1] - this.getHeight());

		this.showAsDropDown(v);
	}
}
