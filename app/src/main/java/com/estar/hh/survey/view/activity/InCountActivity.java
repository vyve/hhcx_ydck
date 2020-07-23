package com.estar.hh.survey.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.estar.hh.survey.R;
import com.estar.hh.survey.adapter.InCountAdapter;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/29 0029.
 * 收款账户
 */

public class InCountActivity extends HuangheBaseActivity {

    @ViewInject(R.id.in_count_back)
    private LinearLayout back;

    @ViewInject(R.id.in_count_list)
    private ListView incountList;

    @ViewInject(R.id.in_count_up_step)
    private LinearLayout upStep;

    @ViewInject(R.id.in_count_add)
    private LinearLayout carAdd;

    @ViewInject(R.id.in_count_next_step)
    private LinearLayout nextStep;

    private List<Object> inCounts = new ArrayList<>();
    private InCountAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_count_activity);
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
                Intent intent = new Intent(InCountActivity.this, ManDamegeActivity.class);
                startActivity(intent);
            }
        });

        carAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InCountActivity.this, InCountDetailActivity.class);
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
        inCounts.add(new Object());
        inCounts.add(new Object());
        adapter = new InCountAdapter(this, inCounts);
        incountList.setAdapter(adapter);
    }

}
