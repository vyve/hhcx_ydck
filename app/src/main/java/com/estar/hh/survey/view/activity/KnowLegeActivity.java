package com.estar.hh.survey.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.estar.hh.survey.R;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Administrator on 2017/10/12 0012.
 * 查勘常识
 */

public class KnowLegeActivity extends HuangheBaseActivity implements View.OnClickListener{

    @ViewInject(R.id.knowlege_back)
    private LinearLayout back;

    @ViewInject(R.id.knowlege_one)
    private LinearLayout one;

    @ViewInject(R.id.knowlege_two)
    private LinearLayout two;

    @ViewInject(R.id.knowlege_three)
    private LinearLayout three;

    @ViewInject(R.id.knowlege_four)
    private LinearLayout four;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.knowlege_activity);
        x.view().inject(this);

        initView();

    }

    private void initView(){
        back.setOnClickListener(this);
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(KnowLegeActivity.this, KnowlegeInfoActivity.class);

        switch (v.getId()){
            case R.id.knowlege_back:{
                finish();
            }break;
            case R.id.knowlege_one:{
                intent.putExtra("code", 0);
                startActivity(intent);
            }break;
            case R.id.knowlege_two:{
                intent.putExtra("code", 1);
                startActivity(intent);
            }break;
            case R.id.knowlege_three:{
                intent.putExtra("code", 2);
                startActivity(intent);
            }break;
            case R.id.knowlege_four:{
                intent.putExtra("code", 3);
                startActivity(intent);
            }break;
        }
    }
}
