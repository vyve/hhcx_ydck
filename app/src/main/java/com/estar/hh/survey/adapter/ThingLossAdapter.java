package com.estar.hh.survey.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.estar.hh.survey.R;
import com.estar.hh.survey.entity.entity.GoodInfoDto;
import com.estar.hh.survey.view.activity.PropertyListActivity;
import com.estar.hh.survey.view.activity.ThingLossActivity;
import com.estar.hh.survey.view.activity.ThingLossDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/7.
 * 物损列表适配器
 */

public class ThingLossAdapter extends BaseAdapter{

    private Context context;
    private List<GoodInfoDto> goods;

    private List<HomeMissionStatus> clientStatues = new ArrayList<>();

    public ThingLossAdapter(Context context, List<GoodInfoDto> goods){
        this.context = context;
        this.goods = goods;
        initClientStatus();
    }

    @Override
    public int getCount() {
        return goods.size();
    }

    @Override
    public Object getItem(int i) {
        return goods.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder ;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.thing_loss_item, null);
            holder = new ViewHolder();
            holder.riskType = view.findViewById(R.id.thing_loss_risktype);
            holder.carNo = view.findViewById(R.id.thing_loss_carno);
            holder.thingName = view.findViewById(R.id.thing_loss_tingname);
            holder.lossMan = view.findViewById(R.id.thing_loss_lossman);
            holder.delete = view.findViewById(R.id.thing_loss_delete);
            holder.detail = view.findViewById(R.id.thing_loss_detail);
            view.setTag(holder);
        }else {
            holder = (ViewHolder)view.getTag();
        }

        /**
         * 此处通过数据刷新页面
         */
        {
            final int position = i;
            final GoodInfoDto good = goods.get(position);

            holder.thingName.setText(good.getLossItemName());

            if (good.getLossItemType().equals("04")){
                holder.lossMan.setText("地面财产");
            }else if (good.getLossItemType().equals("07")){
                holder.lossMan.setText("标的");
            }else if (good.getLossItemType().equals("03")){
                holder.lossMan.setText("三者");
            }
//            holder.lossMan.setText(good.getLossItemType());

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("是否删除?");
                    builder.setTitle("提示");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            good.delete();
                            ((ThingLossActivity)context).things.remove(position);
                            notifyDataSetChanged();
                        }
                    });

                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builder.create().show();
                }
            });

            holder.detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ThingLossDetailActivity.class);
                    intent.putExtra("good", good);
                    intent.putExtra("reportNo", good.getReportNo());
                    intent.putExtra("taskNo", good.getTaskNo());
                    context.startActivity(intent);
                }
            });
        }

        return view;
    }

    private class ViewHolder{
        private TextView riskType;
        private TextView carNo;
        private TextView thingName;
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
