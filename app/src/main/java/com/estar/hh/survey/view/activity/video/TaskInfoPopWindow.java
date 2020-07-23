package com.estar.hh.survey.view.activity.video;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.estar.hh.survey.R;
import com.estar.hh.survey.entity.vo.VideoResult;


public class TaskInfoPopWindow extends PopupWindow {

	
	public TaskInfoPopWindow(Activity context, VideoResult videoResult) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View mMenuView = inflater.inflate(R.layout.video_taskinfo_pop, null);
		
		TextView taskId= mMenuView.findViewById(R.id.taskId);
		TextView reportTime= mMenuView.findViewById(R.id.reportTime);
		TextView carNo= mMenuView.findViewById(R.id.carNo);
		TextView insurenameName= mMenuView.findViewById(R.id.insurenameName);
		TextView insurenameMobile= mMenuView.findViewById(R.id.insurenameMobile);
		TextView setLossName= mMenuView.findViewById(R.id.setLossName);
		TextView lossMobile= mMenuView.findViewById(R.id.lossMobile);
		TextView remark= mMenuView.findViewById(R.id.remark);
		TextView dangerAfter= mMenuView.findViewById(R.id.dangerAfter);

		taskId.setText("报  案  号："+videoResult.getReportno());
		reportTime.setText("任  务  号：" + videoResult.getTaskNo());
		carNo.setText("报案时间："+videoResult.getReporttime());
		insurenameName.setText("车  牌  号："+videoResult.getLicenseno());
		insurenameMobile.setText("定  损  员："+videoResult.getLossers());
		setLossName.setText("任务来源："+videoResult.getTasktype());
		lossMobile.setText("出险时间："+videoResult.getDamageTime());
		remark.setText("被保险人："+videoResult.getInsNme());
		remark.setText("出险经过："+videoResult.getDangerAfter());

		
		
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		this.setContentView(mMenuView);
		this.setWidth((int)(dm.widthPixels/1.2));
		this.setHeight(dm.heightPixels/2);
		this.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0x00000000);
		this.setBackgroundDrawable(dw);
		this.update();
	}

	public void show(View v) {
		this.showAsDropDown(v);
	}
}
