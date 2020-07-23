package com.estar.hh.survey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.estar.hh.survey.R;
import com.estar.hh.survey.entity.entity.KeyValue;
import com.estar.hh.survey.view.activity.MessageNotifyActivity;
import com.estar.hh.survey.view.component.MessageStateSelectDialog;

import java.util.List;

/**
 * Created by Administrator on 2017/8/7.
 */

public class MessageStateSelectAdapter extends BaseAdapter{

    private Context context;
    private List<KeyValue> states;
    private MessageStateSelectDialog dialog;

    public MessageStateSelectAdapter(Context context, List<KeyValue> states){
        this.context = context;
        this.states = states;
    }

    public MessageStateSelectAdapter(Context context, List<KeyValue> states, MessageStateSelectDialog dialog){
        this.context = context;
        this.states = states;
        this.dialog = dialog;
    }

    @Override
    public int getCount() {
        return states.size();
    }

    @Override
    public Object getItem(int i) {
        return states.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder ;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.state_select_item, null);
            holder = new ViewHolder();
            holder.key = view.findViewById(R.id.state_key);
            holder.layout = view.findViewById(R.id.state_layout);
            view.setTag(holder);
        }else {
            holder = (ViewHolder)view.getTag();
        }

        /**
         * 此处通过数据刷新页面
         */
        {
            final KeyValue state = states.get(i);
            holder.key.setText(state.getKey());
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MessageNotifyActivity)context).caseList(Integer.valueOf(state.getValue()));
                    dialog.dismiss();
                }
            });
        }

        return view;
    }

    private class ViewHolder{
        private TextView key;
        private LinearLayout layout;
    }

}
