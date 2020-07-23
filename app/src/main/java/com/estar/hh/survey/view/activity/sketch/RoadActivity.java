package com.estar.hh.survey.view.activity.sketch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.estar.hh.survey.R;
import com.estar.hh.survey.view.activity.sketch.Adapter.ImageAdapter;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


/**
 * 现场草图-道路
 */
public class RoadActivity extends Activity {

    @ViewInject(R.id.sketch_road_back)
    private LinearLayout back;

    @ViewInject(R.id.sketch_road_save)
    private TextView save;

    private GridView gridView;//布局文件
    private ImageAdapter imageAdapter;//适配器
    private List<List<Integer>> list = new ArrayList<List<Integer>>();//适配数据
    private int index = -1;//选中下标
    private HorizontalScrollView horizontalScrollView01;//横屏滚动 道路,车型,标志才有
    private LinearLayout linearLayout02,llScrollView;//滚动条布局
    private int buttonIndex=0;//滚动条 选择下标
    private String type="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sketch_road);
        x.view().inject(this);
        initViews();
        initData();
        gridViewonItenClick();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
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
    public View.OnClickListener onClickListener= new View.OnClickListener(){

        public void onClick(View v) {

            initAdapterData(v.getId());
        }

    };

    public void initAdapterData(int indexIt){
        index = -1;//选中下标

        if(!type.equals("D")){
            //清空之前按钮颜色
            for(int i=0;i<linearLayout02.getChildCount();i++){
                linearLayout02.getChildAt(i).setBackgroundResource(R.drawable.textview_road_bg);
            }
            linearLayout02.getChildAt(indexIt).setBackgroundResource(R.drawable.textview_road_bg2);
            buttonIndex=indexIt;//选中的类型下标
        }
        gridView.setAdapter(null);
        initAdapter(indexIt);
    }


    public void initData() {

        list=(List)this.getIntent().getExtras().getParcelableArrayList("list");
        type=this.getIntent().getExtras().getString("type");
        type=type==null?"":type;
        //任务
        if(!type.equals("D")){
            String[] titles=this.getIntent().getExtras().getStringArray("titles");

            //添加滑动图标
            for(int i=0;i<list.size();i++){
                Button btn=new Button(this);
                btn.setTextColor(android.graphics.Color.WHITE);
                if (null!=titles && titles.length>i){

                btn.setText(titles[i]);
                }
                LinearLayout.LayoutParams ll=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                btn.setLayoutParams(ll);
                ll.leftMargin=8;
                btn.setPadding(2, 0, 2, 0);
                btn.setId(i);
                btn.setOnClickListener(onClickListener);
                linearLayout02.addView(btn);

            }

        }else{
            //隐藏滚动条
            llScrollView.setVisibility(View.GONE);
        }
        //组装ArrayList数据
        initAdapterData(0);
    }

    private void initAdapter(int index1){
        imageAdapter = new ImageAdapter(this, list.get(index1));
        imageAdapter.setOnLongCkickListener(new ImageAdapter.OnLongCkickListener(){

            public void onLongClick(int indexx) {
                index=indexx;
                save();
            }

        });
        gridView.setAdapter(imageAdapter);
    }


    public void initViews() {
        gridView = this.findViewById(R.id.survey_survey_photo_gridview);
        horizontalScrollView01 = this.findViewById(R.id.HorizontalScrollView01);
        linearLayout02 = this.findViewById(R.id.LinearLayout02);
        llScrollView= this.findViewById(R.id.llScrollView);
    }




    public void save(){
        if (index == -1) {
            Toast.makeText(this, "请选择一个"+this.getIntent().getExtras().getString("title")+"后点击保存", Toast.LENGTH_SHORT).show();
            return;
        }
        try{
            int result=list.get(buttonIndex).get(index);
            Intent intenr= new Intent();
            intenr.putExtra("result", result);
            this.setResult(20, intenr);
            this.finish();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
