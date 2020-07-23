package com.estar.hh.survey.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.estar.hh.survey.R;
import com.estar.hh.survey.adapter.OtherCarAdapter;
import com.estar.hh.survey.adapter.ThingLossAdapter;
import com.estar.hh.survey.entity.entity.CarLossInfoDto;
import com.estar.hh.survey.entity.entity.CopyMainInfoDto;
import com.estar.hh.survey.entity.entity.GoodInfoDto;
import com.estar.hh.survey.entity.entity.Mission;
import com.estar.hh.survey.utils.ActivityManagerUtils;

import org.litepal.crud.DataSupport;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/29 0029.
 * 物损信息
 */

public class ThingLossActivity extends HuangheBaseActivity {

    @ViewInject(R.id.thing_loss_back)
    private LinearLayout back;

    @ViewInject(R.id.thing_loss_list)
    private ListView thingList;

    @ViewInject(R.id.thing_loss_up_step)
    private LinearLayout upStep;

    @ViewInject(R.id.thing_loss_add)
    private LinearLayout carAdd;

    @ViewInject(R.id.thing_loss_next_step)
    private LinearLayout nextStep;

    public List<GoodInfoDto> things = new ArrayList<>();
    private Mission mission = null;
    public String reportNo;
    public String taskNo;

    private ThingLossAdapter adapter;
    public CopyMainInfoDto copyMainInfoDto = null;//抄单总信息


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thing_loss_activity);
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
                Intent intent = new Intent(ThingLossActivity.this, OtherCarActivity.class);
                intent.putExtra("mission", mission);
                intent.putExtra("copyMainInfo", copyMainInfoDto);
                startActivity(intent);
            }
        });

        carAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThingLossActivity.this, ThingLossDetailActivity.class);
                intent.putExtra("reportNo", reportNo);
                intent.putExtra("taskNo", taskNo);
                startActivity(intent);
            }
        });

        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(ThingLossActivity.this, ManDamegeActivity.class);
//                intent.putExtra("mission", mission);
//                startActivity(intent);

                finish();
            }
        });

    }

    private void initData(){
        copyMainInfoDto = (CopyMainInfoDto) getIntent().getSerializableExtra("copyMainInfo");
        mission = (Mission) getIntent().getSerializableExtra("mission");
        reportNo = mission.getRptNo();
        taskNo = mission.getTaskId();

        things = DataSupport.where("reportNo = ? and taskNo = ?", reportNo, taskNo)
                .order("createTime desc").find(GoodInfoDto.class);

        if (things == null){
            things = new ArrayList<>();
        }


        adapter = new ThingLossAdapter(this, things);
        thingList.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        things = DataSupport.where("reportNo = ? and taskNo = ?", reportNo, taskNo)
                .order("createTime desc").find(GoodInfoDto.class);

        if (things == null){
            things = new ArrayList<>();
        }

        adapter = new ThingLossAdapter(this, things);
        thingList.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        /**
         * 改变节点颜色
         */
        if (things != null && things.size() > 0){
            Activity activity = ActivityManagerUtils.getInstance().getActivityclass(TaskProcessActivity.class);
            ((TaskProcessActivity) activity).changeProcessStyle(((TaskProcessActivity)activity).thing);
            ((TaskProcessActivity) activity).survMainInfoDto.setGoodInfoFlag(1);
            ((TaskProcessActivity) activity).survMainInfoDto.save();
        }else {
            Activity activity = ActivityManagerUtils.getInstance().getActivityclass(TaskProcessActivity.class);
            ((TaskProcessActivity) activity).returnProcessStyle(((TaskProcessActivity)activity).thing);
            ((TaskProcessActivity) activity).survMainInfoDto.setGoodInfoFlag(0);
            ((TaskProcessActivity) activity).survMainInfoDto.save();
        }
        super.onStop();
    }

//    @Override
//    protected void onDestroy() {
//        /**
//         * 改变节点颜色
//         */
//        if (things != null && things.size() > 0){
//            Activity activity = ActivityManagerUtils.getInstance().getActivityclass(TaskProcessActivity.class);
//            ((TaskProcessActivity) activity).changeProcessStyle(((TaskProcessActivity)activity).thing);
//            ((TaskProcessActivity) activity).survMainInfoDto.setGoodInfoFlag(1);
//            ((TaskProcessActivity) activity).survMainInfoDto.save();
//        }else {
//            Activity activity = ActivityManagerUtils.getInstance().getActivityclass(TaskProcessActivity.class);
//            ((TaskProcessActivity) activity).returnProcessStyle(((TaskProcessActivity)activity).thing);
//            ((TaskProcessActivity) activity).survMainInfoDto.setGoodInfoFlag(0);
//            ((TaskProcessActivity) activity).survMainInfoDto.save();
//        }
//        super.onDestroy();
//    }


}
