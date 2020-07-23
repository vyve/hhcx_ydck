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
import com.estar.hh.survey.entity.entity.CarLossInfoDto;
import com.estar.hh.survey.view.activity.OtherCarActivity;
import com.estar.hh.survey.view.activity.OtherCarDetailActivity;
import com.estar.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/7.
 * 三者车列表适配器
 */

public class OtherCarAdapter extends BaseAdapter{

    private Context context;
    private List<CarLossInfoDto> cars;

    private List<HomeMissionStatus> clientStatues = new ArrayList<>();

    public OtherCarAdapter(Context context, List<CarLossInfoDto> cars){
        this.context = context;
        this.cars = cars;
        initClientStatus();
    }

    @Override
    public int getCount() {
        return cars.size();
    }

    @Override
    public Object getItem(int i) {
        return cars.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder ;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.other_car_item, null);
            holder = new ViewHolder();
            holder.carName = view.findViewById(R.id.other_car_carname);
            holder.dutyType = view.findViewById(R.id.other_car_duty_type);
            holder.isDamege = view.findViewById(R.id.other_car_isdamege);
            holder.driver = view.findViewById(R.id.other_car_driver);
            holder.delete = view.findViewById(R.id.other_car_delete);
            holder.detail = view.findViewById(R.id.other_car_detail);
            view.setTag(holder);
        }else {
            holder = (ViewHolder)view.getTag();
        }

        /**
         * 此处通过数据刷新页面
         */
        {
            final int position = i;
            final CarLossInfoDto car = cars.get(position);

            holder.carName.setText(car.getBrandName());
            holder.dutyType.setText(car.getLicenseNo());
            if (!StringUtils.isEmpty(car.getDamageFlag()) && car.getDamageFlag().equals("1")) {
                holder.isDamege.setText("有");
            }else {
                holder.isDamege.setText("无");
            }
            holder.driver.setText(car.getDriverName());

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("是否删除" + car.getLicenseNo() + "?");
                    builder.setTitle("提示");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            car.delete();
                            ((OtherCarActivity)context).otherCars.remove(position);
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
                    Intent intent = new Intent(context, OtherCarDetailActivity.class);
                    intent.putExtra("carLossInfoDto", car);
                    intent.putExtra("reportNo", car.getReportNo());
                    intent.putExtra("taskNo", car.getTaskNo());
                    context.startActivity(intent);
                }
            });
        }

        return view;
    }

    private class ViewHolder{
        private TextView carName;
        private TextView dutyType;
        private TextView isDamege;
        private TextView driver;
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
