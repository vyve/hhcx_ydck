package com.estar.hh.survey.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.estar.hh.survey.R;
import com.estar.hh.survey.adapter.OtherCarAdapter;
import com.estar.hh.survey.entity.entity.CarLossInfoDto;
import com.estar.hh.survey.entity.entity.CopyMainInfoDto;
import com.estar.hh.survey.entity.entity.Mission;
import com.estar.hh.survey.utils.ActivityManagerUtils;

import org.litepal.crud.DataSupport;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import static com.estar.hh.survey.entity.entity.CarLossInfoDto.LOSSTYPEOTHERCAR;

/**
 * Created by Administrator on 2017/9/29 0029.
 * 三者车损
 */

public class OtherCarActivity extends HuangheBaseActivity {

    @ViewInject(R.id.other_car_back)
    private LinearLayout back;

    @ViewInject(R.id.other_car_list)
    private ListView carList;

    @ViewInject(R.id.other_car_up_step)
    private LinearLayout upStep;

    @ViewInject(R.id.other_car_add)
    private LinearLayout carAdd;

    @ViewInject(R.id.other_car_next_step)
    private LinearLayout nextStep;

    public List<CarLossInfoDto> otherCars = null;
    private Mission mission = null;
    public String reportNo;
    public String taskNo;

    private OtherCarAdapter adapter;
    public CopyMainInfoDto copyMainInfoDto = null;//抄单总信息

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_car_activity);
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
                Intent intent = new Intent(OtherCarActivity.this, CaseSurveyActivity.class);
                intent.putExtra("mission", mission);
                intent.putExtra("copyMainInfo", copyMainInfoDto);
                startActivity(intent);

                finish();
            }
        });

        carAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OtherCarActivity.this, OtherCarDetailActivity.class);
                intent.putExtra("reportNo", reportNo);
                intent.putExtra("taskNo", taskNo);
                startActivity(intent);
            }
        });

        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OtherCarActivity.this, ThingLossActivity.class);
                intent.putExtra("mission", mission);
                intent.putExtra("copyMainInfo", copyMainInfoDto);
                startActivity(intent);

                finish();
            }
        });

    }

    private void initData(){

        mission = (Mission) getIntent().getSerializableExtra("mission");
        copyMainInfoDto = (CopyMainInfoDto) getIntent().getSerializableExtra("copyMainInfo");
        reportNo = mission.getRptNo();
        taskNo = mission.getTaskId();

        otherCars =DataSupport.where("reportNo = ? and taskNo = ? and lossItemType = ?", reportNo, taskNo, LOSSTYPEOTHERCAR)
                .order("createTime desc").find(CarLossInfoDto.class);

        if (otherCars == null){
            otherCars = new ArrayList<>();
        }

        adapter = new OtherCarAdapter(this, otherCars);
        carList.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        otherCars = DataSupport.where("reportNo = ? and taskNo = ? and lossItemType = ?", reportNo, taskNo, LOSSTYPEOTHERCAR)
                .order("createTime desc").find(CarLossInfoDto.class);

        if (otherCars == null){
            otherCars = new ArrayList<>();
        }

        adapter = new OtherCarAdapter(this, otherCars);
        carList.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        /**
         * 改变节点颜色
         */
        if (otherCars != null && otherCars.size() > 0){
            Activity activity = ActivityManagerUtils.getInstance().getActivityclass(TaskProcessActivity.class);
            if (activity != null) {
                ((TaskProcessActivity) activity).changeProcessStyle(((TaskProcessActivity) activity).otherCar);
                ((TaskProcessActivity) activity).survMainInfoDto.setCarInfoFlag(1);
                ((TaskProcessActivity) activity).survMainInfoDto.save();
            }
        }else {
            Activity activity = ActivityManagerUtils.getInstance().getActivityclass(TaskProcessActivity.class);
            if (activity != null) {
                ((TaskProcessActivity) activity).returnProcessStyle(((TaskProcessActivity) activity).otherCar);
                ((TaskProcessActivity) activity).survMainInfoDto.setCarInfoFlag(0);
                ((TaskProcessActivity) activity).survMainInfoDto.save();
            }
        }
        super.onStop();
    }

//    @Override
//    protected void onDestroy() {
//        /**
//         * 改变节点颜色
//         */
//        if (otherCars != null && otherCars.size() > 0){
//            Activity activity = ActivityManagerUtils.getInstance().getActivityclass(TaskProcessActivity.class);
//            if (activity != null) {
//                ((TaskProcessActivity) activity).changeProcessStyle(((TaskProcessActivity) activity).otherCar);
//                ((TaskProcessActivity) activity).survMainInfoDto.setCarInfoFlag(1);
//                ((TaskProcessActivity) activity).survMainInfoDto.save();
//            }
//        }else {
//            Activity activity = ActivityManagerUtils.getInstance().getActivityclass(TaskProcessActivity.class);
//            if (activity != null) {
//                ((TaskProcessActivity) activity).returnProcessStyle(((TaskProcessActivity) activity).otherCar);
//                ((TaskProcessActivity) activity).survMainInfoDto.setCarInfoFlag(0);
//                ((TaskProcessActivity) activity).survMainInfoDto.save();
//            }
//        }
//        super.onDestroy();
//    }
}
