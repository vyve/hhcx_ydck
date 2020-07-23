package com.estar.hh.survey.view.activity.video;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.estar.hh.survey.R;


public class MenuPopWindow extends PopupWindow implements OnClickListener {

	private LinearLayout videoLL, sparkLL, redioLL, sendProLL, cancelLL,
			completeLL, exitLL;
	private OnMenuOnClickListener menuOnClickListener;
	public interface OnMenuOnClickListener{
		void videoOption(TextView tv);
		void spark(TextView tv);
		void front(TextView tv);
		void exit(TextView tv);
		void sendUser(TextView tv);
		void cancel(TextView tv);
		void complete(TextView tv);
		
	}
	

	public void setOnMenuOnClickListener(OnMenuOnClickListener menuOnClickListener) {
		this.menuOnClickListener = menuOnClickListener;
	}

	public MenuPopWindow(Activity context, int userType) {
		super(context);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View mMenuView = inflater.inflate(R.layout.video_menu, null);
		videoLL = mMenuView.findViewById(R.id.videoLL);
		sparkLL = mMenuView.findViewById(R.id.sparkLL);
		redioLL = mMenuView.findViewById(R.id.redioLL);
		sendProLL = mMenuView.findViewById(R.id.sendProLL);
		cancelLL = mMenuView.findViewById(R.id.cancelLL);
		completeLL = mMenuView.findViewById(R.id.completeLL);
		exitLL = mMenuView.findViewById(R.id.exitLL);
		videoLL.setOnClickListener(this);
		sparkLL.setOnClickListener(this);
		redioLL.setOnClickListener(this);
		sendProLL.setOnClickListener(this);
		cancelLL.setOnClickListener(this);
		completeLL.setOnClickListener(this);
		exitLL.setOnClickListener(this);
		//1现场人员 2为后台人员  
		if(userType!=1){
			videoLL.setVisibility(View.INVISIBLE);
		}else{
			cancelLL.setVisibility(View.INVISIBLE);
		}
		setAnimationStyle(R.style.MenuPopupAnimation);
		this.setContentView(mMenuView);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0x00000000);
		this.setBackgroundDrawable(dw);
		this.update();
	}

	@Override
	public void onClick(View v) {
		if(null==menuOnClickListener)return;
		switch (v.getId()) {
		case R.id.videoLL:
			//视频
			menuOnClickListener.videoOption((TextView)videoLL.getChildAt(1));
			break;
		case R.id.sparkLL:
			//语音
			menuOnClickListener.spark((TextView)sparkLL.getChildAt(1));
			
			break;
		case R.id.redioLL:
			//是否外放
			menuOnClickListener.front((TextView)redioLL.getChildAt(1));
			
			break;
		case R.id.sendProLL:
			//邀请
			menuOnClickListener.sendUser((TextView)sendProLL.getChildAt(1));
			break;
		case R.id.cancelLL:
			//取消任务
			menuOnClickListener.cancel((TextView)cancelLL.getChildAt(1));
			break;
		case R.id.completeLL:
			//完成定损
			menuOnClickListener.complete((TextView)completeLL.getChildAt(1));
			
			break;
		case R.id.exitLL:
			//退出
			menuOnClickListener.exit((TextView)videoLL.getChildAt(1));
			break;
			
		}
		this.dismiss();
	}
}
