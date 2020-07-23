package com.estar.hh.survey.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.estar.hh.survey.R;
import com.estar.hh.survey.constants.StateSelect;
import com.estar.hh.survey.entity.entity.CopyRiskDto;
import com.estar.hh.survey.entity.entity.KeyValue;
import com.estar.hh.survey.view.activity.TaskProcessActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/7.
 * 险别信息列表适配器
 */

public class TaskRiskAdapter extends BaseAdapter{

    private Context context;
    private List<CopyRiskDto> risks;

    private List<KeyValue> bjmpStates = new ArrayList<>();

    public TaskRiskAdapter(Context context, List<CopyRiskDto> risks){
        this.context = context;
        this.risks = risks;
        List<KeyValue> bjmps = StateSelect.initArray(context, R.array.BJMP_value, R.array.BJMP_key);
        if (bjmps != null && bjmps.size() > 0) {
            bjmpStates.clear();
            bjmpStates.addAll(bjmps);
        }
    }

    @Override
    public int getCount() {
        return risks.size();
    }

    @Override
    public Object getItem(int i) {
        return risks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder ;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.task_risk_item, null);
            holder = new ViewHolder();
            holder.riskCode = view.findViewById(R.id.task_risk_item_riskcode);
            holder.riskName = view.findViewById(R.id.task_risk_item_riskname);
            holder.noPay = view.findViewById(R.id.task_risk_item_nopay);
            holder.count = view.findViewById(R.id.task_risk_item_riskcount);
            view.setTag(holder);
        }else {
            holder = (ViewHolder)view.getTag();
        }

        /**
         * 此处通过数据刷新页面
         */
        {
            CopyRiskDto risk = risks.get(i);
            if (risk != null){
                holder.riskCode.setText(risk.getInsuranceCode());
                holder.riskName.setText(risk.getInsuranceName());
                holder.noPay.setText(StateSelect.getTextValue(risk.getNoFreeAmount(), bjmpStates));
                holder.count.setText(risk.getInsuranceAmount());
            }
        }

        return view;
    }

    private class ViewHolder{
        private TextView riskCode;
        private TextView riskName;
        private TextView noPay;
        private TextView count;
    }

}
