package com.estar.hh.survey.view.activity.sketch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.estar.hh.survey.R;
import com.estar.hh.survey.utils.LogUtils;
import com.estar.hh.survey.view.activity.HuangheBaseActivity;
import com.estar.hh.survey.view.activity.sketch.vo.BigSize;
import com.estar.hh.survey.view.activity.sketch.vo.Colours;
import com.estarview.MyHorizontalScrollView;
import com.estarview.OnScrollListener;
import com.estarview.TuyaView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 现场草图-图鸭
 */
public class TuyaActivity extends HuangheBaseActivity implements OnScrollListener {
    private ImageView imageview_background;
    private TuyaView tuyaView; //图鸭控件
    private FrameLayout tuyaFrameLayout =null;
    private static final int UNDO_PATH = 1;
    private static final int REDO_PATH = 2;
    private static final int USE_ERASER = 3;
    private static final int USE_PAINT = 4;
    private static final String TAG = "TuyaActivity";
    private LinearLayout linearlayout;
    private Button colourtag,bigtag,buttonPen,buttonEraser;
    private ScrollView scrollviewcolour,scrollviewbig;
    private MyHorizontalScrollView hscrollViewcolour,hscrollViewsize;
    private List<Colours> colours = new ArrayList();
    private Colours colour;
    private List<BigSize> sizes = new ArrayList();
    private BigSize size;
    private int index;
    private int CANCLE_BACKGROUND_IMAGE = 0;
    String filepath =  Environment.getExternalStorageDirectory() + "/dcim/qianming2.jpg";
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UNDO_PATH:
                    int undo = tuyaView.undo(); //撤销上次操作
                    if(undo<0){
                        CANCLE_BACKGROUND_IMAGE ++;
                        switch (CANCLE_BACKGROUND_IMAGE) {
                            case 0:
                                break;
                            case 1:
                                imageview_background.setBackgroundColor(defaultColor);
                                imageview_background.setImageBitmap(null);
                                CANCLE_BACKGROUND_IMAGE =0;
                                break;
                        }
                    }

                    break;
                case REDO_PATH:
                    int redo = tuyaView.redo(); //返回上次操作
                    if(redo<1){
                        //设置按钮不可用
                    }
                    break;
                case USE_ERASER:
                    if(linearlayout.getVisibility()==View.VISIBLE){
                        linearlayout.setVisibility(View.GONE);
                    }
                    TuyaView.color=Color.parseColor("#C9DDFE");
                    TuyaView.srokeWidth = 15;
                    break;
                case USE_PAINT:
                    //设置选择器选中状态
                    if(linearlayout.getVisibility()== View.GONE){
                        linearlayout.setVisibility(View.VISIBLE);
                        TuyaView.srokeWidth = sizes.get(index).getName()+10;
                        for(int i=0;i<colours.size();i++){
                            if(colours.get(i).getButtonbg().getVisibility()==View.VISIBLE){
                                TuyaView.color = Color.parseColor("#"+colours.get(i).getName());
                                break;
                            }
                        }
                    }else{
                        linearlayout.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    };

    @ViewInject(R.id.sketch_road_back)
    private LinearLayout back;

    @ViewInject(R.id.sketch_road_save)
    private TextView save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sketch_tuya);
        x.view().inject(this);
        initViews();
        initColourButton();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTuyaBitmap();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void initViews(){
        new File(filepath).delete();


        buttonPen = this.findViewById(R.id.button_pen);
        buttonEraser = this.findViewById(R.id.button_eraser);

        tuyaFrameLayout = findViewById(R.id.tuya_layout);
        imageview_background = findViewById(R.id.imageview_background);
        tuyaView= findViewById(R.id.tuyaView);
        colourtag = this.findViewById(R.id.colourtag);
        bigtag = this.findViewById(R.id.bigtag);
        scrollviewcolour = this.findViewById(R.id.scrollviewcolour);
        scrollviewbig = this.findViewById(R.id.scrollviewbig);
        hscrollViewcolour = this.findViewById(R.id.HorizontalScrollView01);
        hscrollViewcolour.setOnScrollListener(this);
        hscrollViewsize = this.findViewById(R.id.HorizontalScrollView02);
        hscrollViewsize.setOnScrollListener(this);
        linearlayout = this.findViewById(R.id.ScrollView01);
        buttonPen.setBackgroundResource(R.drawable.bg_ag2);

        findViewById(R.id.button_undo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message undo_message = new Message();
                undo_message.what = UNDO_PATH;
                handler.sendMessage(undo_message);
                onClick2(view);
            }
        });
        findViewById(R.id.button_redo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message redo_message = new Message();
                redo_message.what = REDO_PATH;
                handler.sendMessage(redo_message);
                onClick2(view);
            }
        });
        findViewById(R.id.button_eraser).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackgroundResource(R.drawable.bg_ag2);
                buttonPen.setBackgroundResource(R.drawable.bg_ag);
                Message eraser_message = new Message();
                eraser_message.what = USE_ERASER;
                handler.sendMessage(eraser_message);
                onClick2(v);
            }
        });
        findViewById(R.id.button_pen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackgroundResource(R.drawable.bg_ag2);
                buttonEraser.setBackgroundResource(R.drawable.bg_ag);
                Message pen_message = new Message();
                pen_message.what = USE_PAINT;
                handler.sendMessage(pen_message);
                onClick2(v);
            }
        });
        findViewById(R.id.colourtag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(linearlayout.getVisibility()==View.VISIBLE){
                    scrollviewcolour.setVisibility(View.VISIBLE);
                    scrollviewbig.setVisibility(View.GONE);
                    colourtag.setBackgroundResource(R.drawable.tuya_selectedtrue);
                    bigtag.setBackgroundResource(R.drawable.tuya_selectedfalse);
                }
                onClick2(v);
            }
        });
        findViewById(R.id.bigtag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(linearlayout.getVisibility()==View.VISIBLE){
                    scrollviewcolour.setVisibility(View.GONE);
                    scrollviewbig.setVisibility(View.VISIBLE);
                    bigtag.setBackgroundResource(R.drawable.tuya_selectedtrue);
                    colourtag.setBackgroundResource(R.drawable.tuya_selectedfalse);
                }
                onClick2(v);
            }
        });

    }

    private final int defaultColor=Color.parseColor("#C9DDFE");


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if(linearlayout.getVisibility()==View.VISIBLE){
                linearlayout.setVisibility(View.GONE);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    public void onClick2(View v) {
        //颜色
        int j = -1;
        for(int i=0;i<colours.size();i++){
            if(v.getId()==colours.get(i).getTag()){
                j=i;
            }
        }
        if (j != -1) {
            for (int i = 0; i < colours.size(); i++) {
                colours.get(i).getButtonbg().setVisibility(View.INVISIBLE);
            }
            colours.get(j).getButtonbg().setVisibility(View.VISIBLE);
            //改变颜色
            colours.get(j).getName();
            LogUtils.i(TAG, ""+colours.get(j).getName());
            TuyaView.color = Color.parseColor("#"+colours.get(j).getName());
            return;
        }

        //大小
        for(int i=0;i<sizes.size();i++){
            if(v.getId()==sizes.get(i).getTag()){
                j=i;
            }
        }
        if (j != -1) {
            for (int i = 0; i < sizes.size(); i++) {
                sizes.get(i).getButton().setBackgroundResource(0);
            }
            sizes.get(j).getButton().setBackgroundResource(R.drawable.tuya_brushsizeselectedbg);
            //改变大小
            sizes.get(j).getName();
            LogUtils.i(TAG, ""+sizes.get(j).getName());
            TuyaView.srokeWidth=sizes.get(j).getName()+10;
            index = j;
            return;
        }
    }


    /**
     * 发送图片
     */
    private void sendTuyaBitmap() {
        //发送按钮
        boolean drawingCacheEnabled = tuyaFrameLayout.isDrawingCacheEnabled();
        if(!drawingCacheEnabled){
            tuyaFrameLayout.setDrawingCacheEnabled(true);
            tuyaFrameLayout.buildDrawingCache();
        }
        //获得当前的Bitmap对象
        Bitmap bitmap = tuyaFrameLayout.getDrawingCache();
        if(bitmap!=null){
            //读取SD卡状态
            boolean sdCardIsMounted = Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED);
            if (!sdCardIsMounted){
                Toast.makeText(this, "请插入存储卡！", Toast.LENGTH_SHORT).show();
            }else{
                //保存到SD卡

                if(new File(filepath).exists()){
                    new File(filepath).delete();
                }
                if(!new File( Environment.getExternalStorageDirectory() + "/dcim").exists()){
                    new File( Environment.getExternalStorageDirectory() + "/dcim").mkdir();
                }
                try {
                    FileOutputStream fos = new FileOutputStream(new File(filepath));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                    Toast.makeText(this, "保存成功！", Toast.LENGTH_SHORT).show();
                    //跳转到对应界面
                    Intent intenr= new Intent();
                    intenr.putExtra("imagePath", filepath);
                    this.setResult(20, intenr);
                    this.finish();


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
        }
    }



    public void onLeft() {

    }




    public void onRight() {

    }




    public void onScroll() {

    }
    private void initColourButton() {
        colour = new Colours();
        colour.setButton((Button)this.findViewById(R.id.button01));
        colour.setButtonbg((ImageView)this.findViewById(R.id.imageview01));
        colour.setName("140c09");
        colour.setTag(R.id.button01);
        colours.add(colour);

        colour = new Colours();
        colour.setButton((Button)this.findViewById(R.id.button02));
        colour.setButtonbg((ImageView)this.findViewById(R.id.imageview02));
        colour.setName("fe0000");
        colour.setTag(R.id.button02);
        colours.add(colour);

        colour = new Colours();
        colour.setButton((Button)this.findViewById(R.id.button03));
        colour.setButtonbg((ImageView)this.findViewById(R.id.imageview03));
        colour.setName("ff00ea");
        colour.setTag(R.id.button03);
        colours.add(colour);

        colour = new Colours();
        colour.setButton((Button)this.findViewById(R.id.button04));
        colour.setButtonbg((ImageView)this.findViewById(R.id.imageview04));
        colour.setName("011eff");
        colour.setTag(R.id.button04);
        colours.add(colour);

        colour = new Colours();
        colour.setButton((Button)this.findViewById(R.id.button05));
        colour.setButtonbg((ImageView)this.findViewById(R.id.imageview05));
        colour.setName("00ccff");
        colour.setTag(R.id.button05);
        colours.add(colour);

        colour = new Colours();
        colour.setButton((Button)this.findViewById(R.id.button06));
        colour.setButtonbg((ImageView)this.findViewById(R.id.imageview06));
        colour.setName("00641c");
        colour.setTag(R.id.button06);
        colours.add(colour);

        colour = new Colours();
        colour.setButton((Button)this.findViewById(R.id.button07));
        colour.setButtonbg((ImageView)this.findViewById(R.id.imageview07));
        colour.setName("9bff69");
        colour.setTag(R.id.button07);
        colours.add(colour);

        colour = new Colours();
        colour.setButton((Button)this.findViewById(R.id.button08));
        colour.setButtonbg((ImageView)this.findViewById(R.id.imageview08));
        colour.setName("f0ff00");
        colour.setTag(R.id.button08);
        colours.add(colour);

        colour = new Colours();
        colour.setButton((Button)this.findViewById(R.id.button09));
        colour.setButtonbg((ImageView)this.findViewById(R.id.imageview09));
        colour.setName("ff9c00");
        colour.setTag(R.id.button09);
        colours.add(colour);

        colour = new Colours();
        colour.setButton((Button)this.findViewById(R.id.button10));
        colour.setButtonbg((ImageView)this.findViewById(R.id.imageview10));
        colour.setName("ff5090");
        colour.setTag(R.id.button10);
        colours.add(colour);

        colour = new Colours();
        colour.setButton((Button)this.findViewById(R.id.button11));
        colour.setButtonbg((ImageView)this.findViewById(R.id.imageview11));
        colour.setName("9e9e9e");
        colour.setTag(R.id.button11);
        colours.add(colour);

        colour = new Colours();
        colour.setButton((Button)this.findViewById(R.id.button12));
        colour.setButtonbg((ImageView)this.findViewById(R.id.imageview12));
        colour.setName("f5f5f5");
        colour.setTag(R.id.button12);
        colours.add(colour);


        for(int i=0;i<colours.size();i++){
            colours.get(i).getButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClick2(view);
                }
            });
            colours.get(i).getButtonbg().setVisibility(View.INVISIBLE);
        }
        colours.get(0).getButtonbg().setVisibility(View.VISIBLE);

        size = new BigSize();
        size.setButton((Button)this.findViewById(R.id.sizebutton01));
        size.setName(15);
        size.setTag(R.id.sizebutton01);
        sizes.add(size);
        size = new BigSize();
        size.setButton((Button)this.findViewById(R.id.sizebutton02));
        size.setName(10);
        size.setTag(R.id.sizebutton02);
        sizes.add(size);
        size = new BigSize();
        size.setButton((Button)this.findViewById(R.id.sizebutton03));
        size.setName(5);
        size.setTag(R.id.sizebutton03);
        sizes.add(size);
        size = new BigSize();
        size.setButton((Button)this.findViewById(R.id.sizebutton04));
        size.setName(0);
        size.setTag(R.id.sizebutton04);
        sizes.add(size);
        size = new BigSize();
        size.setButton((Button)this.findViewById(R.id.sizebutton05));
        size.setName(-5);
        size.setTag(R.id.sizebutton05);
        sizes.add(size);
        size = new BigSize();
        size.setButton((Button)this.findViewById(R.id.sizebutton06));
        size.setName(-10);
        size.setTag(R.id.sizebutton06);
        sizes.add(size);
        for(int i=0;i<sizes.size();i++){
            sizes.get(i).getButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClick2(view);
                }
            });
            sizes.get(i).getButton().setBackgroundResource(0);
        }
        sizes.get(2).getButton().setBackgroundResource(R.drawable.tuya_brushsizeselectedbg);
        index = 2;
    }

    protected void onDestroy() {
        super.onDestroy();
        TuyaView.color = Color.parseColor("#fe0000");
        TuyaView.srokeWidth = 15;
    }
}
