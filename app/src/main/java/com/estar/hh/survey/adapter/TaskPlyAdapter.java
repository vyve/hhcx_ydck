package com.estar.hh.survey.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.estar.hh.survey.R;
import com.estar.hh.survey.entity.entity.CopyPolicyDto;
import com.estar.utils.StringUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/8/7.
 * 保单信息适配器
 */

public class TaskPlyAdapter extends BaseAdapter {

    private Context context;
    private List<CopyPolicyDto> plys;

    public TaskPlyAdapter(Context context, List<CopyPolicyDto> plys) {
        this.context = context;
        this.plys = plys;
    }

    @Override
    public int getCount() {
        return plys.size();
    }

    @Override
    public Object getItem(int i) {
        return plys.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        view = LayoutInflater.from(context).inflate(R.layout.task_ply_item, null);
        holder = new ViewHolder();
        holder.vipLevel = view.findViewById(R.id.vipLevel);
        holder.rptNo = view.findViewById(R.id.task_ply_item_caseno);
        holder.plyNo = view.findViewById(R.id.task_ply_item_plyno);
        holder.orgName = view.findViewById(R.id.task_ply_item_orgname);
        holder.itemName = view.findViewById(R.id.task_ply_item_classname);
        holder.riskCode = view.findViewById(R.id.task_ply_item_casekind);
        holder.riskName = view.findViewById(R.id.task_ply_item_casename);
        holder.starttime = view.findViewById(R.id.task_ply_item_starttime);
        holder.endtime = view.findViewById(R.id.task_ply_item_endtime);
        holder.carno = view.findViewById(R.id.task_ply_item_carno);
        holder.model = view.findViewById(R.id.task_ply_item_model);
        holder.insman = view.findViewById(R.id.task_ply_item_insman);
        holder.salesCommissionerCode= view.findViewById(R.id.task_ply_item_salescom);

        holder.shareholderIdentity = view.findViewById(R.id.task_ply_item_shareholderIdentity);
        holder.shareholderIdentityCode = view.findViewById(R.id.task_ply_item_shareholderIdentityCode);
        holder.task_ply_item_custType = view.findViewById(R.id.task_ply_item_custType);
//        holder.isAgriculture = view.findViewById(R.id.isAgriculture);

        view.setTag(holder);

        /**
         * 此处通过数据刷新页面
         */
        {

            final CopyPolicyDto ply = plys.get(i);

            if (ply != null) {

                holder.rptNo.setText(ply.getReportNo());
                holder.plyNo.setText(ply.getPolicyNo());
                holder.orgName.setText(ply.getInsureInstitution());
                holder.itemName.setText(ply.getClauseType());
                holder.riskCode.setText(ply.getInsTypeCode());
                holder.riskName.setText(ply.getInsTypeName());
                holder.starttime.setText(ply.getInsureStartTime());
                holder.endtime.setText(ply.getInsureEndTime());
                holder.carno.setText(ply.getCarNo());
                holder.model.setText(ply.getBrandName());
                holder.insman.setText(ply.getInsuranceName());
                if(!StringUtils.isEmpty(ply.getVipLevel())){
                    holder.vipLevel.setText("【"+ply.getVipLevel()+"】");
                }
                holder.salesCommissionerCode.setText(ply.getSalesCommissionerCode());

                holder.shareholderIdentity.setText(ply.getShareholderIdentity());
                holder.shareholderIdentityCode.setText(ply.getShareholderIdentityCode());
                String specialClientName = ply.getSpecialClientName();
                if("黄河车管家".equals(specialClientName)){
                    holder.task_ply_item_custType.setTypeface(Typeface.DEFAULT_BOLD);
                }
                if(!android.text.TextUtils.isEmpty(specialClientName)){
                    holder.task_ply_item_custType.setText(specialClientName);
                }
//                if (!StringUtils.isEmpty(ply.getIsAgriculture()) && ply.getIsAgriculture().equals("1")) {
//                    holder.isAgriculture.setToggleOn();
//                } else {
//                    holder.isAgriculture.setToggleOff();
//                }


            }


        }

        return view;
    }

    private class ViewHolder {
        private TextView vipLevel;
        private TextView rptNo;
        private TextView plyNo;
        private TextView orgName;
        private TextView itemName;
        private TextView riskCode;
        private TextView riskName;
        private TextView starttime;
        private TextView endtime;
        private TextView carno;
        private TextView model;
        private TextView insman;

        private TextView salesCommissionerCode;

        private TextView shareholderIdentity;
        private TextView shareholderIdentityCode;
        private TextView task_ply_item_custType;

//        private ToggleButtonView isAgriculture;
    }

    /**
     * 任务类型：
     * 0、现场查勘；
     * 1、主车车损；
     * 2、三者车损；
     * 3、主车货物；
     * 4、三者车货物；
     * 5、其它三者财产。
     *
     * @param taskType
     * @return
     */
    private String getTaskTypeShow(String taskType) {
        String taskShow = null;
        switch (taskType) {
            case "0": {
                taskShow = "现场查勘";
            }
            break;
            case "1": {
                taskShow = "主车车损";
            }
            break;
            case "2": {
                taskShow = "三者车损";
            }
            break;
            case "3": {
                taskShow = "主车货物";
            }
            break;
            case "4": {
                taskShow = "三者车货物";
            }
            break;
            case "5": {
                taskShow = "其它三者财产";
            }
            break;
            default:
                break;
        }
        return taskShow;
    }

}
