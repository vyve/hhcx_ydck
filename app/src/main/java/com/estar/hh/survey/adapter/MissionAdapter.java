package com.estar.hh.survey.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.estar.hh.survey.entity.entity.Mission;
import com.estar.hh.survey.utils.DialogUtil;
import com.estar.hh.survey.utils.GPSUtils;
import com.estar.hh.survey.view.activity.OtherCarActivity;
import com.estar.hh.survey.view.activity.TaskCarProcessActivity;
import com.estar.hh.survey.view.activity.TaskProcessActivity;
import com.estar.hh.survey.view.activity.TaskPropertyProcessActivity;
import com.estar.utils.DateUtil;
import com.estar.utils.StringUtils;
import com.estar.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/7.
 * 任务适配器
 */

public class MissionAdapter extends BaseAdapter{

    private Context context;
    private List<Mission> missions;
    private int tag;

    public MissionAdapter(Context context, List<Mission> missions, int tag){
        this.context = context;
        this.missions = missions;
        this.tag = tag;
    }

    @Override
    public int getCount() {
        return missions.size();
    }

    @Override
    public Object getItem(int i) {
        return missions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder ;
//        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.mission_list_item, null);
            holder = new ViewHolder();
            holder.layout = view.findViewById(R.id.mission_list_item_layout);
            holder.tag = view.findViewById(R.id.mission_list_item_tag);
            holder.carNo = view.findViewById(R.id.mission_list_item_carno);
            holder.caseNo = view.findViewById(R.id.mission_list_item_caseno);
            holder.taskNo = view.findViewById(R.id.mission_list_item_taskno);
            holder.taskType = view.findViewById(R.id.mission_list_item_tasktype);
            holder.time = view.findViewById(R.id.mission_list_item_time);
            view.setTag(holder);
//        }else {
//            holder = (ViewHolder)view.getTag();
//        }

        /**
         * 此处通过数据刷新页面
         */
        {

            final Mission mission = missions.get(i);

            holder.carNo.setText(mission.getVhl());
            holder.caseNo.setText(mission.getRptNo());
            holder.taskNo.setText(mission.getTaskId());
            holder.taskType.setText(getTaskTypeShow(mission.getTaskType()));
            holder.time.setText(mission.getLongTime());

            if (tag == 1){
                Glide.with(context).load(R.mipmap.new_task_tag).into(holder.tag);
            }else if (tag == 2){
                Glide.with(context).load(R.mipmap.running_task_tag).into(holder.tag);
            }else if (tag == 3){
                Glide.with(context).load(R.mipmap.complete_task_tag).into(holder.tag);
            }

            /**
             * 任务类型：
             * 0、现场查勘；
             * 1、主车车损；
             * 2、三者车损；
             * 3、主车货物；
             * 4、三者车货物；
             * 5、其它三者财产。
             */
            if (!StringUtils.isEmpty(mission.getTaskType())) {

                final String taskType = mission.getTaskType();

                holder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (StringUtils.isEmpty(mission.getRptNo())){
                            ToastUtils.showShort(context, "报案号不存在");
                            return;
                        }

                        if (StringUtils.isEmpty(mission.getTaskId())){
                            ToastUtils.showShort(context, "任务号不存在");
                            return;
                        }

                        /**
                         * 判断GPS是否开启
                         */
                        if (!GPSUtils.isGPSOPen(context)){
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("GPS未开启，是否立即打开?");
                            builder.setTitle("提示");
                            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    GPSUtils.openGPS(context);
                                }
                            });

                            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            builder.create().show();
                            return;
                        }

                        if (taskType.equals("0")) {
                            Intent intent = new Intent(context, TaskProcessActivity.class);
                            intent.putExtra("mission", mission);
                            context.startActivity(intent);
                        } else if (taskType.equals("1") || taskType.equals("2")) {
                            Intent intent = new Intent(context, TaskCarProcessActivity.class);
                            intent.putExtra("mission", mission);
                            context.startActivity(intent);
                        } else if (taskType.equals("3") || taskType.equals("4") || taskType.equals("5")) {
                            Intent intent = new Intent(context, TaskPropertyProcessActivity.class);
                            intent.putExtra("mission", mission);
                            context.startActivity(intent);
                        }
                    }
                });
            }


        }

        return view;
    }

    private class ViewHolder{
        private LinearLayout layout;
        private ImageView tag;
        private TextView carNo;
        private TextView caseNo;
        private TextView taskNo;
        private TextView taskType;
        private TextView time;
    }

    /**
     * 任务类型：
     * 0、现场查勘；
     * 1、主车车损；
     * 2、三者车损；
     * 3、主车货物；
     * 4、三者车货物；
     * 5、其它三者财产。
     * @param taskType
     * @return
     */
    private String getTaskTypeShow(String taskType){
        String taskShow = null;
        switch (taskType){
            case "0":{
                taskShow = "现场查勘";
            }break;
            case "1":{
                taskShow = "主车车损";
            }break;
            case "2":{
                taskShow = "三者车损";
            }break;
            case "3":{
                taskShow = "主车货物";
            }break;
            case "4":{
                taskShow = "三者车货物";
            }break;
            case "5":{
                taskShow = "其它三者财产";
            }break;
            default:break;
        }
        return taskShow;
    }

}
