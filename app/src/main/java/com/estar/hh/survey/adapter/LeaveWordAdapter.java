package com.estar.hh.survey.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.estar.hh.survey.R;
import com.estar.hh.survey.entity.entity.Word;
import com.estar.hh.survey.view.activity.OtherCarDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/7.
 * 留言板列表适配器
 */

public class LeaveWordAdapter extends BaseAdapter{

    private Context context;
    private List<Word> words;

    public LeaveWordAdapter(Context context, List<Word> words){
        this.context = context;
        this.words = words;
    }

    @Override
    public int getCount() {
        return words.size();
    }

    @Override
    public Object getItem(int i) {
        return words.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder ;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.leave_word_item, null);
            holder = new ViewHolder();
            holder.workNo = view.findViewById(R.id.leave_word_item_workno);
            holder.time = view.findViewById(R.id.leave_word_item_time);
            holder.content = view.findViewById(R.id.leave_word_item_content);
            view.setTag(holder);
        }else {
            holder = (ViewHolder)view.getTag();
        }

        /**
         * 此处通过数据刷新页面
         */
        {
            Word word = words.get(i);
            if (word == null){
                return view;
            }

            holder.workNo.setText(word.getRemarkEmp());
            holder.time.setText(word.getRemarkDate());
            holder.content.setText(word.getRemarkContent());
        }

        return view;
    }

    private class ViewHolder{
        private TextView workNo;
        private TextView time;
        private TextView content;
    }
}
