package com.estar.hh.survey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.estar.hh.survey.R;
import com.estar.hh.survey.entity.vo.KnowlegeVO;

import java.util.List;

/**
 * Created by Administrator on 2017/8/7.
 * 查勘常识适配器
 */

public class KnowlegeInfoAdapter extends BaseAdapter{

    private Context context;
    private List<KnowlegeVO> knowleges;

    public KnowlegeInfoAdapter(Context context, List<KnowlegeVO> knowleges){
        this.context = context;
        this.knowleges = knowleges;
    }

    @Override
    public int getCount() {
        return knowleges.size();
    }

    @Override
    public Object getItem(int i) {
        return knowleges.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder ;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.knowlege_info_item, null);
            holder = new ViewHolder();
            holder.title = view.findViewById(R.id.knowlege_info_item_title);
            holder.content = view.findViewById(R.id.knowlege_info_item_content);
            view.setTag(holder);
        }else {
            holder = (ViewHolder)view.getTag();
        }

        /**
         * 此处通过数据刷新页面
         */
        {
            KnowlegeVO knowlege = knowleges.get(i);
            if (knowlege == null){
                return view;
            }

            holder.title.setText(knowlege.getTitle());
            holder.content.setText(knowlege.getContent());
        }

        return view;
    }

    private class ViewHolder{
        private TextView title;
        private TextView content;
    }
}
