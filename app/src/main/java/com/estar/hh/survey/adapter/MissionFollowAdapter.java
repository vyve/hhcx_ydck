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
import com.estar.hh.survey.entity.entity.CaseSearchDto;
import com.estar.hh.survey.view.activity.TaskCarProcessActivity;
import com.estar.hh.survey.view.activity.TaskProcessActivity;
import com.estar.hh.survey.view.activity.TaskPropertyProcessActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/7.
 * 任务跟踪适配器
 */

public class MissionFollowAdapter extends BaseAdapter{

    private Context context;
    private List<CaseSearchDto> caseDtos;
    private int tag;

    private List<HomeMissionStatus> clientStatues = new ArrayList<>();

    public MissionFollowAdapter(Context context, List<CaseSearchDto> caseDtos, int tag){
        this.context = context;
        this.caseDtos = caseDtos;
        this.tag = tag;
        initClientStatus();
    }

    @Override
    public int getCount() {
        return caseDtos.size();
    }

    @Override
    public Object getItem(int i) {
        return caseDtos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder ;
//        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.mission_follow_item, null);
            holder = new ViewHolder();
            holder.rptNo = view.findViewById(R.id.mission_follow_item_rptno);
            holder.carNo = view.findViewById(R.id.mission_follow_item_carno);
            holder.rptMan = view.findViewById(R.id.mission_follow_item_rptman);
            holder.caseStatus = view.findViewById(R.id.mission_follow_item_casestutas);
            view.setTag(holder);
//        }else {
//            holder = (ViewHolder)view.getTag();
//        }

        /**
         * 此处通过数据刷新页面
         */
        {
            CaseSearchDto caseDto = caseDtos.get(i);
            if (caseDto != null){
                holder.rptNo.setText(caseDto.getRptNo());
                holder.carNo.setText(caseDto.getLcnNo());
                holder.rptMan.setText(caseDto.getReporter());
                holder.caseStatus.setText(caseDto.getStatus());
            }
        }

        return view;
    }

    private class ViewHolder{
        private TextView rptNo;
        private TextView carNo;
        private TextView rptMan;
        private TextView caseStatus;
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
