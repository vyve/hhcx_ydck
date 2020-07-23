package com.estar.hh.survey.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.estar.hh.survey.R;
import com.estar.hh.survey.adapter.KnowlegeInfoAdapter;
import com.estar.hh.survey.entity.vo.KnowlegeVO;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


/**
 * 查勘常识
 * Created by ding on 2017/1/17.
 */
public class KnowlegeInfoActivity extends HuangheBaseActivity {

    @ViewInject(R.id.knowlege_info_back)
    private LinearLayout back;

    @ViewInject(R.id.knowlege_info_list)
    private ListView knowList;

    @ViewInject(R.id.knowlege_info_title)
    private TextView title;

    private List<KnowlegeVO> list=new ArrayList<KnowlegeVO>();
    private KnowlegeInfoAdapter adapter=null;
    private int code=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.knowlege_info_activity);
        x.view().inject(this);

        initData();
        initView();

    }

    public void initData() {
        code=getIntent().getExtras().getInt("code");
    }

    public void initView() {

        if(code==0){
            title.setText(getResources().getString(R.string.common_sense_1));
            list.add(new KnowlegeVO("(一)",getResources().getString(R.string.common_sense_1_1)));
            list.add(new KnowlegeVO("(二)",getResources().getString(R.string.common_sense_1_2)));
            list.add(new KnowlegeVO("(三)",getResources().getString(R.string.common_sense_1_3)));
            list.add(new KnowlegeVO("(四)",getResources().getString(R.string.common_sense_1_4)));
            list.add(new KnowlegeVO("(五)",getResources().getString(R.string.common_sense_1_5)));
            list.add(new KnowlegeVO("(六)",getResources().getString(R.string.common_sense_1_6)));
            list.add(new KnowlegeVO("(七)",getResources().getString(R.string.common_sense_1_7)));
            list.add(new KnowlegeVO("(七)",getResources().getString(R.string.common_sense_1_8)));
        }else if(code==1){
            title.setText(getResources().getString(R.string.common_sense_2));
            list.add(new KnowlegeVO("（一）查看承保信息",getResources().getString(R.string.common_sense_2_1)));
            list.add(new KnowlegeVO("（二）查看报案信息",getResources().getString(R.string.common_sense_2_2)));
            list.add(new KnowlegeVO("（三）查看历史出险记录",getResources().getString(R.string.common_sense_2_3)));
            list.add(new KnowlegeVO("（四）准备查勘资料",getResources().getString(R.string.common_sense_2_4)));
            list.add(new KnowlegeVO("（五）检查查勘工具",getResources().getString(R.string.common_sense_2_5)));
        }else if(code==2){
            title.setText(getResources().getString(R.string.common_sense_3));
            list.add(new KnowlegeVO("（一）查验报案人、驾驶人",getResources().getString(R.string.common_sense_3_1)));
            list.add(new KnowlegeVO("（二）确认标的",getResources().getString(R.string.common_sense_3_2)));
            list.add(new KnowlegeVO("（三）核实出险时间、地点",getResources().getString(R.string.common_sense_3_3)));
            list.add(new KnowlegeVO("（四）核实车辆使用性质",getResources().getString(R.string.common_sense_3_4)));
            list.add(new KnowlegeVO("（五）对比痕迹",getResources().getString(R.string.common_sense_3_5)));
            list.add(new KnowlegeVO("（六）查勘照片拍摄要求",getResources().getString(R.string.common_sense_3_6)));
            list.add(new KnowlegeVO("（七）现场询问笔录",getResources().getString(R.string.common_sense_3_7)));
            list.add(new KnowlegeVO("（八）协助施救",getResources().getString(R.string.common_sense_3_8)));
            list.add(new KnowlegeVO("（九）初判事故责任",getResources().getString(R.string.common_sense_3_9)));
            list.add(new KnowlegeVO("（十）预估损失",getResources().getString(R.string.common_sense_3_10)));
            list.add(new KnowlegeVO("（十一）缮制《查勘报告》",getResources().getString(R.string.common_sense_3_11)));
            list.add(new KnowlegeVO("（十二）索赔告知",getResources().getString(R.string.common_sense_3_12)));
//            list.add(new KnowlegeVO("（十三）",getResources().getString(R.string.common_sense_3_13)));


        }else if(code==3){
            title.setText(getResources().getString(R.string.common_sense_4));
            list.add(new KnowlegeVO("（一）多次出险",getResources().getString(R.string.common_sense_4_1)));
            list.add(new KnowlegeVO("（二）夜间出险",getResources().getString(R.string.common_sense_4_2)));
            list.add(new KnowlegeVO("（三）老旧车型",getResources().getString(R.string.common_sense_4_3)));
            list.add(new KnowlegeVO("（四）临近保单起止期",getResources().getString(R.string.common_sense_4_4)));
            list.add(new KnowlegeVO("（五）急于修车",getResources().getString(R.string.common_sense_4_5)));
            list.add(new KnowlegeVO("（六）车损严重无人伤",getResources().getString(R.string.common_sense_4_6)));
            list.add(new KnowlegeVO("（七）报案时间距出险时间较长或修理厂代报案",getResources().getString(R.string.common_sense_4_7)));
            list.add(new KnowlegeVO("（八）损失部位不符",getResources().getString(R.string.common_sense_4_8)));
            list.add(new KnowlegeVO("（九）已撤离现场",getResources().getString(R.string.common_sense_4_9)));
            list.add(new KnowlegeVO("（十）涉及人伤",getResources().getString(R.string.common_sense_4_10)));
            list.add(new KnowlegeVO("（十一）自燃",getResources().getString(R.string.common_sense_4_11)));
            list.add(new KnowlegeVO("（十二）水淹",getResources().getString(R.string.common_sense_4_12)));
            list.add(new KnowlegeVO("（十三）全车盗抢",getResources().getString(R.string.common_sense_4_13)));
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        adapter=new KnowlegeInfoAdapter(KnowlegeInfoActivity.this, list);
        knowList.setAdapter(adapter);

    }


}
