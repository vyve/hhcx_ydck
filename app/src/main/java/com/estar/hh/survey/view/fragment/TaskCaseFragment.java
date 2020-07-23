package com.estar.hh.survey.view.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.estar.hh.survey.R;
import com.estar.hh.survey.entity.entity.CopyMainInfoDto;
import com.estar.hh.survey.entity.entity.CopyReportInfoDto;
import com.estar.utils.StringUtils;
import com.estar.utils.ToastUtils;

/**
 * Created by Administrator on 2017/9/30 0030.
 * 报案信息
 */

public class TaskCaseFragment extends Fragment {

    private TextView name;
    private TextView phoneNo;
    private TextView caseCall;
    private TextView driverName;
    private TextView riskLevel;
    private TextView riskTime;
    private TextView riskReason;
    private TextView riskPlace;
    private TextView riskLocation;
    private TextView isOppsite;
    private TextView isXianc;
    private TextView placeType;
    private TextView riskDesc;


    private CopyMainInfoDto copyMainInfo = null;
    private CopyReportInfoDto copyReportInfo = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View contextView = inflater.inflate(R.layout.task_case_fragment, container, false);

        initView(contextView);
        initData();

        return contextView;
    }

    private void initView(View view){
        name = view.findViewById(R.id.task_case_name);
        phoneNo = view.findViewById(R.id.task_case_telno);
        caseCall = view.findViewById(R.id.task_case_call);
        driverName = view.findViewById(R.id.task_case_drivername);
        riskLevel = view.findViewById(R.id.task_case_level);
        riskTime = view.findViewById(R.id.task_case_time);
        riskReason = view.findViewById(R.id.task_case_reason);
        riskPlace = view.findViewById(R.id.task_case_place);
        riskLocation = view.findViewById(R.id.task_case_location);
        isOppsite = view.findViewById(R.id.task_case_isoppsite);
        isXianc = view.findViewById(R.id.task_case_xianchang);
        placeType = view.findViewById(R.id.task_case_placetype);
        riskDesc = view.findViewById(R.id.task_case_desc);

        caseCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String telNo = phoneNo.getText().toString();
                if (!StringUtils.isEmpty(telNo)) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telNo));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else {
                    ToastUtils.showShort(getActivity(), "未知手机号");
                }
            }
        });

        riskLocation.setVisibility(View.GONE);//暂时不需要地图导航
    }

    private void initData(){

        //获取Activity传递过来的参数
        Bundle mBundle = getArguments();
        copyMainInfo = (CopyMainInfoDto) mBundle.getSerializable("copyMainInfo");
        if (copyMainInfo != null) {
            copyReportInfo = copyMainInfo.getCopyReportInfo();
        }

        if (copyReportInfo != null){
            name.setText(copyReportInfo.getReporter());
            phoneNo.setText(copyReportInfo.getReportPhoneNo());
            driverName.setText(copyReportInfo.getDriverName());
            riskLevel.setText(checkCode(copyReportInfo.getUrgencyLevel()));
            riskTime.setText(copyReportInfo.getDamageTime());
            riskReason.setText(copyReportInfo.getDamageReason());
            riskPlace.setText(copyReportInfo.getDamageAddress());
            isOppsite.setText(checkCode(copyReportInfo.getIsSelfCompenClaim()));
            isXianc.setText(checkCode(copyReportInfo.getIsSceneRep()));
            placeType.setText(copyReportInfo.getDamageAdsType());
            riskDesc.setText(copyReportInfo.getDangerAfter());
        }

    }

    private String checkCode(String code){
        if (StringUtils.isEmpty(code)){
            return "";
        }

        if (code.equals("0")){
            return "否";
        }

        if (code.equals("1")){
            return "是";
        }

        return "";
    }


}
