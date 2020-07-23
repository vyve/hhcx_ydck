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
import com.estar.hh.survey.view.activity.ManDamegeDetailActivity;
import com.estar.hh.survey.view.activity.ThingLossDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/7.
 * 人伤列表适配器
 */

public class ManDamegeAdapter extends BaseAdapter{

    private Context context;
    private List<Object> clientInfos;

    private List<HomeMissionStatus> clientStatues = new ArrayList<>();

    public ManDamegeAdapter(Context context, List<Object> clientInfos){
        this.context = context;
        this.clientInfos = clientInfos;
        initClientStatus();
    }

    @Override
    public int getCount() {
        return clientInfos.size();
    }

    @Override
    public Object getItem(int i) {
        return clientInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder ;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.man_damege_item, null);
            holder = new ViewHolder();
            holder.damegeType = view.findViewById(R.id.man_damege_damegetype);
            holder.name = view.findViewById(R.id.man_damege_name);
            holder.amount = view.findViewById(R.id.man_damege_amount);
            holder.lossMan = view.findViewById(R.id.man_damege_lossman);
            holder.delete = view.findViewById(R.id.man_damege_delete);
            holder.detail = view.findViewById(R.id.man_damege_detail);
            view.setTag(holder);
        }else {
            holder = (ViewHolder)view.getTag();
        }

        /**
         * 此处通过数据刷新页面
         */
        {
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            holder.detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ManDamegeDetailActivity.class);
                    context.startActivity(intent);
                }
            });
        }

        return view;
    }

    private class ViewHolder{
        private TextView damegeType;
        private TextView name;
        private TextView amount;
        private TextView lossMan;
        private LinearLayout delete;
        private LinearLayout detail;
    }

    private void initClientStatus(){
        clientStatues.clear();
        HomeMissionStatus status0 = new HomeMissionStatus();
        status0.setCode("0");
        status0.setDesc("失效");
        clientStatues.add(status0);
        HomeMissionStatus status1 = new HomeMissionStatus();
        status1.setCode("1");
        status1.setDesc("续保");
        clientStatues.add(status1);
        HomeMissionStatus status2 = new HomeMissionStatus();
        status2.setCode("2");
        status2.setDesc("生日");
        clientStatues.add(status2);
        HomeMissionStatus status3 = new HomeMissionStatus();
        status3.setCode("3");
        status3.setDesc("待跟单");
        clientStatues.add(status3);
        HomeMissionStatus status4 = new HomeMissionStatus();
        status4.setCode("4");
        status4.setDesc("待回访");
        clientStatues.add(status4);
    }

    /**
     * 客户状态
     */
    public class HomeMissionStatus{
        private String code;
        private String desc;

        public String getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
