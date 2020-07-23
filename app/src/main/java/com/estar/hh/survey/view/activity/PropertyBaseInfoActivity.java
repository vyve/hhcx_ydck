package com.estar.hh.survey.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.estar.hh.survey.R;
import com.estar.hh.survey.entity.entity.CopyMainInfoDto;
import com.estar.hh.survey.entity.entity.GoodInfoDto;
import com.estar.hh.survey.entity.entity.Mission;
import com.estar.hh.survey.entity.entity.PropLossBaseInfoDto;
import com.estar.hh.survey.utils.ActivityManagerUtils;
import com.estar.utils.CommonUtils;
import com.estar.utils.DateUtil;
import com.estar.utils.KeyboardDialog;
import com.estar.utils.StringUtils;
import com.estar.utils.ToastUtils;
import com.estarview.ToggleButtonView;

import org.litepal.crud.DataSupport;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Date;

import static com.estar.hh.survey.utils.TextUtils.setEditTextNoEngChar;

/**
 * Created by Administrator on 2017/10/12 0012.
 * 财产定损基本信息
 */

public class PropertyBaseInfoActivity extends HuangheBaseActivity{

    @ViewInject(R.id.property_base_back)
    private LinearLayout back;

    @ViewInject(R.id.property_base_list)
    private ListView list;

    @ViewInject(R.id.property_base_return)
    private LinearLayout upStep;

    @ViewInject(R.id.property_base_takepic)
    private LinearLayout takePic;

    @ViewInject(R.id.property_base_next_step)
    private LinearLayout nextStep;

    private View content;
    private TextView rptNo;
    private TextView type;//赔案类别
    private TextView insMan;
    private TextView carNo;
    private TextView engNo;
    private TextView vinNo;
    private TextView accidentType;//事故类型
    private TextView accidentClass;//事故分类

    public LinearLayout riskTimeLayout;
    public TextView riskTime;//查勘日期
    private ToggleButtonView caseFlag;//互碰自赔
    private ToggleButtonView isRoad;//是否路面
    private TextView defRescueFee;//事故分类
    private EditText defRescueRemark;//过程描述
    private EditText lossOpinion;//定损意见

    private Mission mission = null;
    private String reportNo;
    private String taskNo;

    private PropLossBaseInfoDto propLossBaseInfoDto = null;
    public CopyMainInfoDto copyMainInfo = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.property_base_info_activity);
        x.view().inject(this);

        initView();
        initData();

    }

    private void initView(){
        content = LayoutInflater.from(this).inflate(R.layout.property_base_info_content, null);
        rptNo = content.findViewById(R.id.property_base_rptno);
        type = content.findViewById(R.id.property_base_type);
        insMan = content.findViewById(R.id.property_base_insman);
        carNo = content.findViewById(R.id.property_base_carno);
        engNo = content.findViewById(R.id.property_base_engno);
        vinNo = content.findViewById(R.id.property_base_vin);
        accidentType = content.findViewById(R.id.property_base_accidenttype);
        accidentClass = content.findViewById(R.id.property_base_class);

        riskTimeLayout = content.findViewById(R.id.property_base_info_time_layout);
        riskTime = content.findViewById(R.id.property_base_info_time);
        caseFlag = content.findViewById(R.id.property_base_info_caseFlag);
        isRoad = content.findViewById(R.id.property_base_info_isRoad);
        defRescueFee = content.findViewById(R.id.property_base_info_amount);
        defRescueRemark = content.findViewById(R.id.property_base_info_remark);
        lossOpinion = content.findViewById(R.id.property_base_info_optition);

        /**
         * 录入规则
         */
        setEditTextNoEngChar(defRescueRemark);
        setEditTextNoEngChar(lossOpinion);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        upStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        caseFlag.setOnToggleChanged(new ToggleButtonView.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    propLossBaseInfoDto.setCaseFlag("1");
                } else {
                    propLossBaseInfoDto.setCaseFlag("0");
                }
            }
        });

        isRoad.setOnToggleChanged(new ToggleButtonView.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    propLossBaseInfoDto.setIsRoad("1");
                } else {
                    propLossBaseInfoDto.setIsRoad("0");
                }
            }
        });

        takePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PropertyBaseInfoActivity.this, ImageListActivity.class);
                intent.putExtra("reportNo", reportNo);
                intent.putExtra("taskNo", taskNo);
                startActivity(intent);
            }
        });

        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveData();

                if (!checkBaseComplete(propLossBaseInfoDto)){
                    return;
                }

                Intent intent = new Intent(PropertyBaseInfoActivity.this, PropertyListActivity.class);
                intent.putExtra("copyMainInfo", copyMainInfo);
                intent.putExtra("mission", mission);
                startActivity(intent);
                finish();
            }
        });

        list.addHeaderView(content);
        list.setAdapter(null);
    }

    private TimePickerView pvTime;//时间选择器
    private Date nowTime;

    private void initData(){

        copyMainInfo = (CopyMainInfoDto)getIntent().getSerializableExtra("copyMainInfo");
        mission = (Mission)getIntent().getSerializableExtra("mission");
        reportNo = mission.getRptNo();
        taskNo = mission.getTaskId();

        propLossBaseInfoDto = DataSupport.where("reportNo = ? and taskNo = ?", mission.getRptNo(), mission.getTaskId())
                .order("createTime desc").findFirst(PropLossBaseInfoDto.class);

        if (propLossBaseInfoDto == null){
            propLossBaseInfoDto = new PropLossBaseInfoDto();
            propLossBaseInfoDto.setReportNo(reportNo);
            propLossBaseInfoDto.setTaskNo(taskNo);
            propLossBaseInfoDto.setCreateTime(DateUtil.getTime_YMDMS(new Date()));
        }

        nowTime = new Date();

        //时间选择器
        pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                if (date.after(nowTime)) {
                    showShortToast("时间不能晚于当前日期");
                    return;
                }

                if (!StringUtils.isEmpty(copyMainInfo.getCopyReportInfo().getDamageDate())
                        && date.before(DateUtil.getDate(copyMainInfo.getCopyReportInfo().getDamageDate()))){
                    ToastUtils.showShort(PropertyBaseInfoActivity.this, "时间不能早于出险日期");
                    return;
                }

                riskTime.setText(DateUtil.getTime(date));
            }
        });
        riskTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvTime.setTime(TimePickerView.Type.YEAR_MONTH_DAY, DateUtil.getDate(riskTime.getText().toString()));
                pvTime.show();
            }
        });

        /**
         * 给界面赋值
         */
        {
            if (!StringUtils.isEmpty(propLossBaseInfoDto.getCaseFlag()) && propLossBaseInfoDto.getCaseFlag().equals("1")) {
                caseFlag.setToggleOn();
            } else {
                caseFlag.setToggleOff();
            }

            if (!StringUtils.isEmpty(propLossBaseInfoDto.getIsRoad()) && propLossBaseInfoDto.getIsRoad().equals("1")) {
                isRoad.setToggleOn();
            } else {
                isRoad.setToggleOff();
            }

            defRescueFee.setText(propLossBaseInfoDto.getDefRescueFee());
            defRescueRemark.setText(propLossBaseInfoDto.getDefRescueRemark());
            lossOpinion.setText(propLossBaseInfoDto.getLossOpinion());
            riskTime.setText(propLossBaseInfoDto.getDefLossDate());

        }

    }

    /**
     * 检测必传项是否完成
     * @param propLossBaseInfoDto
     * @return
     */
    private boolean checkBaseComplete(PropLossBaseInfoDto propLossBaseInfoDto){
        if (propLossBaseInfoDto == null){
            showShortToast("请完成财产定损-基本信息录入");
            return false;
        }else if (StringUtils.isEmpty(propLossBaseInfoDto.getDefLossDate())){
            showShortToast("请完成基本信息-定损时间录入");
            return false;
        }else if (StringUtils.isEmpty(propLossBaseInfoDto.getIsRoad())){
            showShortToast("请完成基本信息-是否路面录入");
            return false;
        }else if (StringUtils.isEmpty(propLossBaseInfoDto.getDefRescueFee())){
            showShortToast("请完成基本信息-施救金额录入");
            return false;
        }else if (StringUtils.isEmpty(propLossBaseInfoDto.getDefRescueRemark())){
            showShortToast("请完成基本信息-过程描述录入");
            return false;
        }else if (StringUtils.isEmpty(propLossBaseInfoDto.getLossOpinion())){
            showShortToast("请完成基本信息-定损意见录入");
            return false;
        }else {
            return true;
        }
    }

    /**
     * 保存当前财产定损基本信息
     */
    private void saveData(){
        propLossBaseInfoDto.setDefRescueFee(defRescueFee.getText().toString());
        propLossBaseInfoDto.setDefRescueRemark(defRescueRemark.getText().toString());
        propLossBaseInfoDto.setLossOpinion(lossOpinion.getText().toString());
        propLossBaseInfoDto.setDefLossDate(riskTime.getText().toString());
        propLossBaseInfoDto.setCreateTime(DateUtil.getTime_YMDMS(new Date()));
        propLossBaseInfoDto.save();
    }

    @Override
    protected void onStop() {

        saveData();

        /**
         * 若必录项全部已完成，则显示该流程已完成
         */
        if (checkBaseComplete(propLossBaseInfoDto)) {
            Activity activity = ActivityManagerUtils.getInstance().getActivityclass(TaskPropertyProcessActivity.class);
            if (activity != null) {
                ((TaskPropertyProcessActivity) activity).changeProcessStyle(((TaskPropertyProcessActivity) activity).baseInfo);
                ((TaskPropertyProcessActivity) activity).goodLossMainInfoDto.setBaseInfoFlag(1);
                ((TaskPropertyProcessActivity) activity).goodLossMainInfoDto.save();
            }
        }else {
            Activity activity = ActivityManagerUtils.getInstance().getActivityclass(TaskPropertyProcessActivity.class);
            if (activity != null) {
                ((TaskPropertyProcessActivity) activity).returnProcessStyle(((TaskPropertyProcessActivity) activity).baseInfo);
                ((TaskPropertyProcessActivity) activity).goodLossMainInfoDto.setBaseInfoFlag(0);
                ((TaskPropertyProcessActivity) activity).goodLossMainInfoDto.save();
            }
        }

        super.onStop();
    }

}
