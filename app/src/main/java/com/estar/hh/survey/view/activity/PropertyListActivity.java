package com.estar.hh.survey.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.estar.hh.survey.R;
import com.estar.hh.survey.adapter.PropertyListAdapter;
import com.estar.hh.survey.entity.entity.CopyMainInfoDto;
import com.estar.hh.survey.entity.entity.GoodLossInfoDto;
import com.estar.hh.survey.entity.entity.Mission;
import com.estar.hh.survey.utils.ActivityManagerUtils;

import org.litepal.crud.DataSupport;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/29 0029.
 * 物品明细
 */

public class PropertyListActivity extends HuangheBaseActivity {

    @ViewInject(R.id.property_list_back)
    private LinearLayout back;

    @ViewInject(R.id.property_list_list)
    private ListView list;

    @ViewInject(R.id.property_list_up_step)
    private LinearLayout upStep;

    @ViewInject(R.id.property_list_add)
    private LinearLayout proAdd;

    @ViewInject(R.id.property_list_next_step)
    private LinearLayout nextStep;

    public List<GoodLossInfoDto> things = new ArrayList<>();
    private Mission mission = null;
    public String reportNo;
    public String taskNo;

    public CopyMainInfoDto copyMainInfoDto = null;

    private PropertyListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.property_list_activity);
        x.view().inject(this);

        initView();
        initData();

    }

    private void initView(){

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
                Intent intent = new Intent(PropertyListActivity.this, PropertyBaseInfoActivity.class);
                intent.putExtra("mission", mission);
                startActivity(intent);
            }
        });

        proAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PropertyListActivity.this, PropertyDetailActivity.class);
                intent.putExtra("reportNo", reportNo);
                intent.putExtra("taskNo", taskNo);
                intent.putExtra("copyMainInfo", copyMainInfoDto);
                intent.putExtra("mission", mission);
                startActivity(intent);
            }
        });

        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initData(){

        copyMainInfoDto = (CopyMainInfoDto)getIntent().getSerializableExtra("copyMainInfo");
        mission = (Mission) getIntent().getSerializableExtra("mission");
        reportNo = mission.getRptNo();
        taskNo = mission.getTaskId();

        things = DataSupport.where("reportNo = ? and taskNo = ?", reportNo, taskNo)
                .order("createTime desc").find(GoodLossInfoDto.class);

        if (things == null){
            things = new ArrayList<>();
        }
        adapter = new PropertyListAdapter(this, things);
        list.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        /**
         * 改变节点颜色
         */
        if (things != null && things.size() > 0){
            Activity activity = ActivityManagerUtils.getInstance().getActivityclass(TaskPropertyProcessActivity.class);
            if (activity != null) {
                ((TaskPropertyProcessActivity) activity).changeProcessStyle(((TaskPropertyProcessActivity) activity).detail);
                ((TaskPropertyProcessActivity) activity).goodLossMainInfoDto.setGoodlistFlag(1);
                ((TaskPropertyProcessActivity) activity).goodLossMainInfoDto.save();
            }
        }else {
            Activity activity = ActivityManagerUtils.getInstance().getActivityclass(TaskPropertyProcessActivity.class);
            if (activity != null) {
                ((TaskPropertyProcessActivity) activity).returnProcessStyle(((TaskPropertyProcessActivity) activity).detail);
                ((TaskPropertyProcessActivity) activity).goodLossMainInfoDto.setGoodlistFlag(0);
                ((TaskPropertyProcessActivity) activity).goodLossMainInfoDto.save();
            }
        }
        super.onStop();
    }

//    @Override
//    protected void onDestroy() {
//        /**
//         * 改变节点颜色
//         */
//        if (things != null && things.size() > 0){
//            Activity activity = ActivityManagerUtils.getInstance().getActivityclass(TaskPropertyProcessActivity.class);
//            if (activity != null) {
//                ((TaskPropertyProcessActivity) activity).changeProcessStyle(((TaskPropertyProcessActivity) activity).detail);
//                ((TaskPropertyProcessActivity) activity).goodLossMainInfoDto.setGoodlistFlag(1);
//                ((TaskPropertyProcessActivity) activity).goodLossMainInfoDto.save();
//            }
//        }else {
//            Activity activity = ActivityManagerUtils.getInstance().getActivityclass(TaskPropertyProcessActivity.class);
//            if (activity != null) {
//                ((TaskPropertyProcessActivity) activity).returnProcessStyle(((TaskPropertyProcessActivity) activity).detail);
//                ((TaskPropertyProcessActivity) activity).goodLossMainInfoDto.setGoodlistFlag(0);
//                ((TaskPropertyProcessActivity) activity).goodLossMainInfoDto.save();
//            }
//        }
//        super.onDestroy();
//    }

    @Override
    protected void onResume() {
        super.onResume();

        things = DataSupport.where("reportNo = ? and taskNo = ?", reportNo, taskNo)
                .order("createTime desc").find(GoodLossInfoDto.class);

        if (things == null){
            things = new ArrayList<>();
        }
        adapter = new PropertyListAdapter(this, things);
        list.setAdapter(adapter);
    }

}
