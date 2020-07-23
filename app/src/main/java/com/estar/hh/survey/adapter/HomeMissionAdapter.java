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

import com.bumptech.glide.Glide;
import com.estar.hh.survey.R;
import com.estar.hh.survey.view.activity.MissionListActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/7.
 * 客户列表适配器
 */

public class HomeMissionAdapter extends BaseAdapter{

    private Context context;
    private List<Object> clientInfos;

    private List<HomeMissionStatus> clientStatues = new ArrayList<>();

    public HomeMissionAdapter(Context context, List<Object> clientInfos){
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
            view = LayoutInflater.from(context).inflate(R.layout.home_mission_item, null);
            holder = new ViewHolder();
            holder.layout = view.findViewById(R.id.home_mission_item_layout);
            holder.tag = view.findViewById(R.id.home_mission_item_tag);
            holder.name = view.findViewById(R.id.home_mission_item_name);
            holder.telNo = view.findViewById(R.id.home_mission_item_telno);
            holder.carNo = view.findViewById(R.id.home_mission_item_carno);
            holder.address = view.findViewById(R.id.home_mission_item_address);
            holder.distance = view.findViewById(R.id.home_mission_item_distance);
            holder.time = view.findViewById(R.id.home_mission_item_time);
            view.setTag(holder);
        }else {
            holder = (ViewHolder)view.getTag();
        }

        /**
         * 此处通过数据刷新页面
         */
        {
            Glide.with(context).load(R.mipmap.new_task_tag).into(holder.tag);

            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MissionListActivity.class);
                    context.startActivity(intent);
                }
            });
        }

        return view;
    }

    private class ViewHolder{
        private LinearLayout layout;
        private ImageView tag;
        private TextView name;
        private TextView telNo;
        private TextView carNo;
        private TextView address;
        private TextView distance;
        private TextView time;
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
