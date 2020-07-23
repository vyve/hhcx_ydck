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
import com.estar.hh.survey.entity.entity.GoodInfoDto;
import com.estar.hh.survey.entity.entity.KeyValue;
import com.estar.utils.DateUtil;
import com.estar.utils.StringUtils;

import org.litepal.crud.DataSupport;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.estar.hh.survey.utils.TextUtils.setEditTextForChar;


/**
 * Created by Administrator on 2017/9/29 0029.
 * 物损详细信息
 */

public class ThingLossDetailActivity extends HuangheBaseActivity {

    @ViewInject(R.id.thing_loss_detail_back)
    private LinearLayout back;

    @ViewInject(R.id.thing_loss_detail_list)
    private ListView list;

    @ViewInject(R.id.thing_loss_detail_up_step)
    private LinearLayout upStep;

    @ViewInject(R.id.thing_loss_detail_take_pic)
    private LinearLayout takePic;

    @ViewInject(R.id.thing_loss_detail_next_step)
    private LinearLayout nextStep;

    private View content;
    private LinearLayout lossManLayout;
    private TextView lossMan;
    private EditText thingName;

    private LinearLayout lossDegreeLayout;
    private TextView lossDegree;

    private LinearLayout lossSpeciesLayout;
    private TextView lossSpecies;

    private EditText saveAmount;
    private EditText amount;

    private StateSelectDialog lossManDialog;
    private List<KeyValue> lossManStates = new ArrayList<>();
    private StateSelectDialog lossDegreeDialog;
    private List<KeyValue> lossDegreeStates = new ArrayList<>();
    private StateSelectDialog lossSpeciesDialog;
    private List<KeyValue> lossSpeciesStates = new ArrayList<>();

    private GoodInfoDto good = null;
    private String reportNo = null;
    private String taskNo = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thing_loss_detail_activity);
        x.view().inject(this);

        initView();
        initData();

    }

    private void initView(){

        content = LayoutInflater.from(this).inflate(R.layout.thing_loss_detail_content, null);
        lossManLayout = content.findViewById(R.id.thing_loss_detail_lossman_layout);
        lossMan = content.findViewById(R.id.thing_loss_detail_lossman);
        thingName = content.findViewById(R.id.thing_loss_detail_thingname);

        lossDegreeLayout = content.findViewById(R.id.thing_loss_detail_lossdegree_layout);
        lossDegree = content.findViewById(R.id.thing_loss_detail_lossdegree);

        lossSpeciesLayout = content.findViewById(R.id.thing_loss_detail_lossspecies_layout);
        lossSpecies = content.findViewById(R.id.thing_loss_detail_lossspecies);

        saveAmount = content.findViewById(R.id.thing_loss_detail_saveamount);
        amount = content.findViewById(R.id.thing_loss_detail_amount);

        list.addHeaderView(content);
        list.setAdapter(null);

        setEditTextForChar(thingName);

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
                Intent intent = new Intent(ThingLossDetailActivity.this, ImageListActivity.class);
                intent.putExtra("reportNo", reportNo);
                intent.putExtra("taskNo", taskNo);
                startActivity(intent);
            }
        });

        nextStep.setOnClickListener(new View.OnClickListener() {
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

        List<KeyValue> lossDegrees = StateSelect.initArray(this, R.array.SSCD_value, R.array.SSCD_key);
        if (lossDegrees != null && lossDegrees.size() > 0) {
            lossDegreeStates.clear();
            lossDegreeStates.addAll(lossDegrees);
        }
        lossDegreeDialog = new StateSelectDialog(this, lossDegreeStates, lossDegree);
        lossDegreeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lossDegreeDialog.show();
            }
        });

        List<KeyValue> lossSpeciess = StateSelect.initArray(this, R.array.SSZL_value, R.array.SSZL_key);
        if (lossSpeciess != null && lossSpeciess.size() > 0) {
            lossSpeciesStates.clear();
            lossSpeciesStates.addAll(lossSpeciess);
        }
        lossSpeciesDialog = new StateSelectDialog(this, lossSpeciesStates, lossSpecies);
        lossSpeciesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lossSpeciesDialog.show();
            }
        });


        //获取Activity传递过来的参数
        GoodInfoDto goodDto = (GoodInfoDto) getIntent().getSerializableExtra("good");
        reportNo = getIntent().getStringExtra("reportNo");
        taskNo = getIntent().getStringExtra("taskNo");

        if (goodDto != null){

            good = DataSupport.where("reportNo = ? and taskNo = ? and createTime = ?", goodDto.getReportNo(), goodDto.getTaskNo(), goodDto.getCreateTime())
                    .order("createTime desc").findFirst(GoodInfoDto.class);

        }else {
            good = new GoodInfoDto();
            good.setReportNo(reportNo);
            good.setTaskNo(taskNo);
        }

        {
            lossMan.setTag(good.getLossItemType());
            lossMan.setText(StateSelect.getTextValue(good.getLossItemType(), lossManStates));

            lossDegree.setTag(good.getLossDegreeName());
            lossDegree.setText(StateSelect.getTextValue(good.getLossDegreeName(), lossDegreeStates));

            lossSpecies.setTag(good.getLossSpeciesCode());
            lossSpecies.setText(StateSelect.getTextValue(good.getLossSpeciesCode(), lossSpeciesStates));

            thingName.setText(good.getLossItemName());
            saveAmount.setText(good.getSumRescueFee());
            amount.setText(good.getSumLossFee());
        }

    }

    private boolean completeCheck(){

        if (good == null){
            return false;
        }else if (StringUtils.isEmpty(good.getLossItemType())){
            showShortToast("请输入损失方");
            return false;
        }else if (StringUtils.isEmpty(good.getLossItemName())){
            showShortToast("请输入损失名称");
            return false;
        }else if (StringUtils.isEmpty(good.getLossDegreeName())){
            showShortToast("请输入损失程度");
            return false;
        }else if (StringUtils.isEmpty(good.getLossSpeciesCode())){
            showShortToast("请输入损失种类");
            return false;
        }else if (StringUtils.isEmpty(good.getSumRescueFee())){
            showShortToast("请输入施救费用");
            return false;
        }else if (StringUtils.isEmpty(good.getSumLossFee())){
            showShortToast("请输入预估金额");
            return false;
        }else {
            return true;
        }
    }

    private void saveData(){

        if (lossMan.getTag() != null){
            good.setLossItemType(lossMan.getTag().toString());
            good.setLossType(lossMan.getTag().toString());
        }
        if (lossDegree.getTag() != null){
            good.setLossDegreeName(lossDegree.getTag().toString());
        }
        if (lossSpecies.getTag() != null){
            good.setLossSpeciesCode(lossSpecies.getTag().toString());
        }
        good.setLossItemName(thingName.getText().toString());
        good.setSumRescueFee(saveAmount.getText().toString());
        good.setSumLossFee(amount.getText().toString());

    }

}
