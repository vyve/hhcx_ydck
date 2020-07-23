package com.estar.hh.survey.adapter.video;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.estar.hh.survey.R;
import com.estar.hh.survey.entity.vo.MsgVO;

import java.util.List;


public class MsgListAdapter extends BaseAdapter {

	private Context mContext;
	private List<MsgVO> list = null;
	private LayoutInflater layoutInflater = null;

	public MsgListAdapter(Context context, List<MsgVO> list) {
		mContext = context;
		this.list = list;
		layoutInflater = LayoutInflater.from(mContext);
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

	public void notifyDataSetChanged(List<MsgVO> list) {
		this.list = list;
		this.notifyDataSetChanged();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;

		MsgVO modelBgVO = list.get(position);
		View view  = layoutInflater.inflate(R.layout.msg_list_data, null);
		vh = new ViewHolder(view);
		vh.msg = view.findViewById(R.id.msg);
		vh.leftLL = view.findViewById(R.id.leftLL);
		vh.rightLL = view.findViewById(R.id.rightLL);

		vh.msg.setText(modelBgVO.getMsg() + "");
		vh.leftLL.setText(modelBgVO.getName() + ":");
		vh.rightLL.setText(":" + modelBgVO.getName());
		if (modelBgVO.getOptionType().equals("1")) {
			// 左边
			vh.rightLL.setVisibility(View.GONE);
		} else {
			// 右边
			vh.leftLL.setVisibility(View.GONE);
		}

		return view;
	}

	class ViewHolder {
		TextView msg, leftLL, rightLL;

		public ViewHolder(View view) {

		}
	}

}
