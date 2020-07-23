package com.estar.hh.survey.adapter.video;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.estar.hh.survey.R;
import com.estar.hh.survey.entity.vo.video.VideoUserInfoVO;
import com.estar.hh.survey.utils.LogUtils;

import java.util.List;

import info.emm.meeting.MeetingSession;


public class UserPopwindowAdapter extends BaseAdapter {

	private Context mContext;
	private List<VideoUserInfoVO> list = null;
	private LayoutInflater layoutInflater = null;
	private int userType;
	private String setLossName;
	public UserPopwindowAdapter(Context context, List<VideoUserInfoVO> list, int userType, String setLossName) {
		mContext = context;
		this.list = list;
		layoutInflater = LayoutInflater.from(mContext);
		this.userType=userType;
		this.setLossName=setLossName;
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}





	public void notifyDataSetChanged(List<VideoUserInfoVO> list) {
		this.list=list;
		this.notifyDataSetChanged();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;

		VideoUserInfoVO modelBgVO = list.get(position);
		View view = convertView;
		if (view == null) {

			view = layoutInflater.inflate(R.layout.user_popwindow_child, null);

			vh = new ViewHolder(convertView);
			assert view != null;
			vh.tv = view.findViewById(R.id.childto);
			vh.redio = view.findViewById(R.id.redio);
			vh.video = view.findViewById(R.id.video);

			view.setTag(vh);
		} else {
			vh = (ViewHolder) view.getTag();
		}
		vh.tv.setText(modelBgVO.getName());


		if(modelBgVO.getRedioType()== MeetingSession.RequestSpeak_Disable){
			vh.redio.setVisibility(View.GONE);
		}else{
			vh.redio.setVisibility(View.VISIBLE);
			if(modelBgVO.getRedioType()== MeetingSession.RequestSpeak_Allow){
				//正在说话
				vh.redio.setImageResource(R.drawable.call);
			}else if(modelBgVO.getRedioType()== MeetingSession.RequestSpeak_Pending){
				//在排队
				vh.redio.setImageResource(R.drawable.jpt);
			}else{
				vh.redio.setVisibility(View.GONE);
			}
		}
		LogUtils.e(setLossName, modelBgVO.getName()+"==");
		if(setLossName.equals(modelBgVO.getName())){

			vh.video.setVisibility(View.VISIBLE);
		}else{
			vh.video.setVisibility(View.INVISIBLE);
		}


		return view;
	}



	class ViewHolder {
		TextView tv;
		ImageView redio,video;

		public ViewHolder(View view) {

		}
	}


}
