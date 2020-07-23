package com.estar.hh.survey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.estar.hh.survey.R;
import com.estar.hh.survey.entity.entity.CopyHisDangerDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/7.
 * 历史赔案列表适配器
 */

public class TaskHistoryAdapter extends BaseAdapter {

    private Context context;
    private List<CopyHisDangerDto> hisDangers;

    public TaskHistoryAdapter(Context context, List<CopyHisDangerDto> hisDangers) {
        this.context = context;
        this.hisDangers = hisDangers;
    }

    @Override
    public int getCount() {
        return hisDangers.size();
    }

    @Override
    public Object getItem(int i) {
        return hisDangers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        view = LayoutInflater.from(context).inflate(R.layout.task_history_item, null);
        holder = new ViewHolder();
        holder.rptTime = view.findViewById(R.id.task_history_item_time);
        holder.rptNo = view.findViewById(R.id.task_history_item_rptno);
        holder.people = view.findViewById(R.id.task_history_item_people);
        holder.kind = view.findViewById(R.id.task_history_item_plykind);

        holder.riskTime = view.findViewById(R.id.task_history_item_risktime);
        holder.riskPlace = view.findViewById(R.id.task_history_item_riskplace);
        holder.amount = view.findViewById(R.id.task_history_item_amount);

        holder.in = view.findViewById(R.id.task_history_item_in);
        view.setTag(holder);


        /**
         * 此处通过数据刷新页面
         */
        {

            CopyHisDangerDto hisDanger = hisDangers.get(i);

            if (hisDanger != null) {
                holder.rptTime.setText(hisDanger.getReportTime());
                holder.rptNo.setText(hisDanger.getReportNo());
                holder.people.setText(hisDanger.getInsName());
                holder.kind.setText(hisDanger.getPolicyType());

                holder.riskTime.setText(hisDanger.getDangerTime());
                holder.riskPlace.setText(hisDanger.getDangerDetailAddress());
                holder.amount.setText(hisDanger.getAmount());
            }
        }

        return view;
    }

    private class ViewHolder {
        private TextView rptTime;
        private TextView rptNo;
        private TextView people;

        private TextView riskTime;
        private TextView riskPlace;
        private TextView amount;

        private TextView kind;
        private LinearLayout in;
    }

}
