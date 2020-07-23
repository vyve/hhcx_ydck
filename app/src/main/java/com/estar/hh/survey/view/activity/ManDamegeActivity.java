package com.estar.hh.survey.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.estar.hh.survey.R;
import com.estar.hh.survey.adapter.ManDamegeAdapter;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/29 0029.
 * 人伤信息
 */

public class ManDamegeActivity extends HuangheBaseActivity {

    @ViewInject(R.id.man_damege_back)
    private LinearLayout back;

    @ViewInject(R.id.man_damege_list)
    private ListView damegeList;

    @ViewInject(R.id.man_damege_up_step)
    private LinearLayout upStep;

    @ViewInject(R.id.man_damege_add)
    private LinearLayout add;

    @ViewInject(R.id.man_damege_next_step)
    private LinearLayout nextStep;

    private List<Object> dameges = new ArrayList<>();
    private ManDamegeAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.man_damege_activity);
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
                Intent intent = new Intent(ManDamegeActivity.this, ThingLossActivity.class);
                startActivity(intent);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManDamegeActivity.this, ManDamegeDetailActivity.class);
                startActivity(intent);
            }
        });

        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManDamegeActivity.this, InCountActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void initData(){
        dameges.add(new Object());
        dameges.add(new Object());
        adapter = new ManDamegeAdapter(this, dameges);
        damegeList.setAdapter(adapter);
    }

}
