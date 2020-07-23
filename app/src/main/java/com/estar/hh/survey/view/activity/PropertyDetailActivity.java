package com.estar.hh.survey.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.estar.hh.survey.R;
import com.estar.hh.survey.component.StateSelectDialog;
import com.estar.hh.survey.constants.StateSelect;
import com.estar.hh.survey.entity.entity.CopyMainInfoDto;
import com.estar.hh.survey.entity.entity.CopyRiskDto;
import com.estar.hh.survey.entity.entity.GoodInfoDto;
import com.estar.hh.survey.entity.entity.GoodLossInfoDto;
import com.estar.hh.survey.entity.entity.KeyValue;
import com.estar.hh.survey.entity.entity.Mission;
import com.estar.hh.survey.view.component.MyProgressDialog;
import com.estar.utils.DateUtil;
import com.estar.utils.StringUtils;

import org.litepal.crud.DataSupport;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.estar.hh.survey.utils.TextUtils.setEditTextForChar;
import static com.estar.hh.survey.utils.TextUtils.setEditTextNoEngChar;

/**
 * Created by Administrator on 2017/10/12 0012.
 * 物品明细
 */

public class PropertyDetailActivity extends HuangheBaseActivity {

    @ViewInject(R.id.property_detail_back)
    private LinearLayout back;

    @ViewInject(R.id.property_detail_list)
    private ListView list;

    @ViewInject(R.id.property_detail_return)
    private LinearLayout upStep;

    @ViewInject(R.id.property_detail_takepic)
    private LinearLayout takePic;

    @ViewInject(R.id.property_detail_next_step)
    private LinearLayout save;

    private View content;
    private LinearLayout lossManLayout;
    private TextView lossMan;
    private LinearLayout riskCodeLayout;
    private TextView riskCode;
    private EditText propertyName;
    private EditText count;
    private EditText restValue;
    private EditText opinion;

    private StateSelectDialog lossManDialog;
    private List<KeyValue> lossManStates = new ArrayList<>();
    private StateSelectDialog riskCodeDialog;
    private List<KeyValue> riskCodeStates = new ArrayList<>();

    private GoodLossInfoDto good = null;
    private String reportNo = null;
    private String taskNo = null;

    private CopyMainInfoDto copyMainInfoDto = null;

    private Mission mission = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.property_detail_activity);
        x.view().inject(this);

        initView();
        initData();

    }

    private void initView(){
        content = LayoutInflater.from(this).inflate(R.layout.property_detail_content, null);
        lossManLayout = content.findViewById(R.id.property_detail_lossMan_layout);
        lossMan = content.findViewById(R.id.property_detail_lossMan);
        riskCodeLayout = content.findViewById(R.id.property_detail_riskCode_layout);
        riskCode = content.findViewById(R.id.property_detail_riskCode);
        propertyName = content.findViewById(R.id.property_detail_propertyname);
        count = content.findViewById(R.id.property_detail_count);
        restValue = content.findViewById(R.id.property_detail_restvalue);
        opinion = content.findViewById(R.id.property_detail_opinion);

        list.addHeaderView(content);
        list.setAdapter(null);

        setEditTextForChar(propertyName);
        setEditTextNoEngChar(opinion);
    }

    private void initData(){

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

        takePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PropertyDetailActivity.this, ImageListActivity.class);
                intent.putExtra("reportNo", reportNo);
                intent.putExtra("taskNo", taskNo);
                startActivity(intent);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (good != null){
                    saveData();
                }else {
                    showShortToast("物损信息为空");
                    return;
                }

                if (completeCheck()){
                    good.setCreateTime(DateUtil.getTime_YMDMS(new Date()));
                    good.save();
                    finish();
                }
            }
        });

        /**
         * 险别代码列表
         */
        copyMainInfoDto = (CopyMainInfoDto)getIntent().getSerializableExtra("copyMainInfo");
        if (copyMainInfoDto != null) {
            List<CopyRiskDto> listRisk = copyMainInfoDto.getListRisk();
            if (listRisk != null && listRisk.size() > 0){

                MyProgressDialog dialog = new MyProgressDialog(this, "险别代码初始化中...");

                riskCodeStates.clear();
                for (CopyRiskDto riskDto : listRisk){
                    KeyValue keyValue = new KeyValue();
                    keyValue.setKey(riskDto.getInsuranceName());
                    keyValue.setValue(riskDto.getInsuranceCode());
                    riskCodeStates.add(keyValue);
                }
                riskCodeDialog = new StateSelectDialog(this, riskCodeStates, riskCode);
                riskCodeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        riskCodeDialog.show();
                    }
                });

                dialog.stopDialog();
            }else {
                riskCodeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showShortToast("未找到险别信息，请重新获取抄单信息");
                    }
                });
            }

        }else {
            riskCodeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showShortToast("未找到险别信息，请重新获取抄单信息");
                }
            });
        }

        List<KeyValue> lossMans = StateSelect.initArray(this, R.array.SSF_value, R.array.SSF_key);
        if (lossMans != null && lossMans.size() > 0) {
            lossManStates.clear();
            lossManStates.addAll(lossMans);
        }
        lossManDialog = new StateSelectDialog(this, lossManStates, lossMan);
        lossManLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lossManDialog.show();
            }
        });



        //获取Activity传递过来的参数
        GoodLossInfoDto goodDto = (GoodLossInfoDto) getIntent().getSerializableExtra("good");
        reportNo = getIntent().getStringExtra("reportNo");
        taskNo = getIntent().getStringExtra("taskNo");

        /**
         * 刷新页面数
         */
        if (goodDto != null){

            good = DataSupport.where("reportNo = ? and taskNo = ? and createTime = ?", goodDto.getReportNo(), goodDto.getTaskNo(), goodDto.getCreateTime())
                    .order("createTime desc").findFirst(GoodLossInfoDto.class);

        }else {

            mission = (Mission) getIntent().getSerializableExtra("mission");

            good = new GoodLossInfoDto();
            good.setReportNo(reportNo);
            good.setTaskNo(taskNo);

            if (mission != null && copyMainInfoDto != null){
                String vhlName = mission.getVhl();
                if (copyMainInfoDto.getSurveyAlreadySubmit().getListGoodInfoDto() != null
                        && copyMainInfoDto.getSurveyAlreadySubmit().getListGoodInfoDto().size() > 0){
                    for (GoodInfoDto goodInfoDto : copyMainInfoDto.getSurveyAlreadySubmit().getListGoodInfoDto()){
                        if (vhlName.contains(goodInfoDto.getLossItemName())){
                            good.setLossItemName(goodInfoDto.getLossItemName());
                            good.setLossParty(goodInfoDto.getLossItemType());
                        }
                    }
                }
            }
        }

        /**
         * 界面赋值
         */
        {
            lossMan.setTag(good.getLossParty());
            lossMan.setText(StateSelect.getTextValue(good.getLossParty(), lossManStates));
            if (riskCodeStates != null) {
                riskCode.setTag(good.getKinCode());
                riskCode.setText(StateSelect.getTextValue(good.getKinCode(), riskCodeStates));
            }
            propertyName.setText(good.getLossItemName());
            count.setText(good.getLossMoney());
            restValue.setText(good.getRecyclePrice());
            opinion.setText(good.getLossOpinion());
        }
    }

    private void saveData(){

        if (lossMan.getTag() != null) {
            good.setLossParty(lossMan.getTag().toString());
        }

        if (riskCode.getTag() != null){
            good.setKinCode(riskCode.getTag().toString());
        }

        good.setLossItemName(propertyName.getText().toString());
        good.setLossMoney(count.getText().toString());
        good.setRecyclePrice(restValue.getText().toString());
        good.setLossOpinion(opinion.getText().toString());

        good.setSumLoss(count.getText().toString());//报损金额 = 定损金额/损失比例 + 扣减报损金额

    }


    private boolean completeCheck(){

        if (good == null){
            return false;
        }else if (StringUtils.isEmpty(good.getLossParty())){
            showShortToast("请输入损失方");
            return false;
        }else if (StringUtils.isEmpty(good.getKinCode())){
            showShortToast("请输入险别代码");
            return false;
        }else if (StringUtils.isEmpty(good.getLossItemName())){
            showShortToast("请输入财产名称");
            return false;
        }else if (StringUtils.isEmpty(good.getLossMoney())){
            showShortToast("请输入定损金额");
            return false;
        }else if (StringUtils.isEmpty(good.getLossOpinion())){
            showShortToast("请输入定损意见");
            return false;
        }else {
            return true;
        }
    }

}
