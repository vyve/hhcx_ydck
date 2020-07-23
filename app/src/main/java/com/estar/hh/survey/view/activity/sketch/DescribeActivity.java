package com.estar.hh.survey.view.activity.sketch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.estar.hh.survey.R;
import com.estar.hh.survey.view.activity.HuangheBaseActivity;
import com.estar.hh.survey.view.activity.sketch.Adapter.ImageAdapter;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


/**
 * 现场草图-备注
 */
public class DescribeActivity extends HuangheBaseActivity {

    @ViewInject(R.id.sketch_describe_back)
    private LinearLayout back;

    @ViewInject(R.id.sketch_describe_save)
    private TextView im_add;

    private GridView gridView;
    private ImageAdapter imageAdapter;
    private List<Integer> list = new ArrayList<Integer>();
    private int index = -1;
    private EditText beizhuET;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sketch_describe);
        x.view().inject(this);
        initViews();
        initData();
        gridViewonItenClick();

        im_add.setVisibility(View.VISIBLE);
        im_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(beizhuET.getText().toString().trim().equals("")){
                    Toast.makeText(DescribeActivity.this, "请填写事故描叙", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (index == -1) {
                    Toast.makeText(DescribeActivity.this, "请选择一个备注模板后点击保存", Toast.LENGTH_SHORT).show();
                    return;
                }
                try{
                    int result=list.get(index);
                    Intent intenr= new Intent();
                    intenr.putExtra("result", result);
                    intenr.putExtra("message", beizhuET.getText().toString().trim());
                    setResult(30, intenr);
                    finish();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void gridViewonItenClick() {
        gridView.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                view.setBackgroundColor(android.graphics.Color.GREEN);
                if (index != -1 && index != position) {
                    gridView.getChildAt(index).setBackgroundColor(0);
                }
                index = position;

            }

        });
    }

    public void initData() {
        list.add(R.drawable.chat_other);
        list.add(R.drawable.chat_my);
        list.add(R.drawable.chatto_bg_focused);
        list.add(R.drawable.chatfrom_bg_normal);


        imageAdapter = new ImageAdapter(this, list);
        gridView.setAdapter(imageAdapter);
    }

    public void initViews() {
        gridView = this
                .findViewById(R.id.survey_survey_photo_gridview);
        beizhuET = this
                .findViewById(R.id.beizhu);

    }


}
