package com.estar.hh.survey.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.estar.hh.survey.R;
import com.estar.hh.survey.view.activity.sketch.DescribeActivity;
import com.estar.hh.survey.view.activity.sketch.RoadActivity;
import com.estar.hh.survey.view.activity.sketch.TuyaActivity;
import com.estar.hh.survey.view.activity.sketch.vo.SketchVO;
import com.estar.hh.survey.view.activity.sketch.vo.SubSurveyVO;
import com.estar.utils.DateUtil;
import com.estar.utils.DialogUtil;
import com.estar.utils.ScreenShot;
import com.estarview.RotatTextView;
import com.estarview.RotatView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/11 0011.
 * 现场草图
 */

public class DrawPlaceActivity extends HuangheBaseActivity {

    @ViewInject(R.id.draw_place_back)
    private LinearLayout back;

    @ViewInject(R.id.draw_place_save)
    private TextView save;

    @ViewInject(R.id.draw_place_paint)
    private AbsoluteLayout paint;


    private SubSurveyVO subSurveyVO  =	new SubSurveyVO();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.draw_place_activity);
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

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert(paint);
            }
        });
    }

    private void initData(){

    }

    public void insert(View view) {
        new GetSubmitData().execute();
    }

    class GetSubmitData extends AsyncTask<Object, String, String> {

        private Dialog dialog;

        protected void onPreExecute() {

            dialog = DialogUtil.show(DrawPlaceActivity.this, "正在保存草图,请稍候...");

        }

        protected String doInBackground(Object... params) {

            try {
//                String jpg = taskno + "_" + DateUtil.getLongData() + ".jpg";// 图片名称
//                String filepath = FileUtils.getInstance().getImageMd(mTaskInfoVO.getTaskNo()) + "/" + "sketch_" +
////                        DateUtil.getDateShortSerial() +
//                        ".jpg";
//                ImageVO imageVO = new ImageVO();
//                imageVO.setImgdate(DateUtil.getYMD());//
//                imageVO.setTaskid(taskno);// 任务id
//                imageVO.setImagename(jpg);// 图片名称
//                imageVO.setPath(filepath);//路径
//                imageVO.setUsercode(myApplication.getUserName());// 用户代码
////                imageVO.setPiccls(bs.getSpinnerKey(sp_bigClass));// 大类
////                imageVO.setPicDtl(bs.getSpinnerKey(sp_smallClass));// 小类
//                imageVO.setPiccls("60000");// 大类
//                imageVO.setPicDtl("61002");// 小类
//                imageVO.setReportno(reportNo);// 报案号
//
////                imageVO.setDptCde("110");//所属保险公司机构代码
////                imageVO.setImageTaskType(type);//任务类型 121现在查勘 ，122：人伤查勘，131：车辆定损，132：财产定损
////                imageVO.setPicClsName(bs.getSpinnerValue(sp_bigClass));//图片大类名称
////                imageVO.setPicDtlName(bs.getSpinnerValue(sp_smallClass));//图片小类名称
////                imageVO.setPicCategory(picCategory);// 1其他 2单证收集
//                imageVO.saveFast();

                String filepath = Environment.getExternalStorageDirectory().toString() + "/sketch.jpg";
                File file = new File(filepath);
                if (!file.exists()){
                    file.createNewFile();
                }
                new ScreenShot().shoot(DrawPlaceActivity.this, filepath, paint);

                return filepath;
            } catch (Exception e) {
                e.printStackTrace();
                return "0";
            }

        }

        protected void onPostExecute(String result) {

            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            if (result.equals("0")) {
                Toast.makeText(DrawPlaceActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                return;
            }


            if (new File(result).exists()) {
                if (paint.getChildCount() > 0) {
                    List<SketchVO> list = new ArrayList<SketchVO>();
                    for (int i = 0; i < paint.getChildCount(); i++) {
                        View view = paint.getChildAt(i);
                        SketchVO sketchVO = new SketchVO();
                        if (view instanceof RotatView) {
                            RotatView rotatView = (RotatView) view;
                            //保存
                            sketchVO.setWidth(rotatView.getWidth());
                            sketchVO.setHeight(rotatView.getHeight());
                            sketchVO.setX(rotatView.getLeft());
                            sketchVO.setY(rotatView.getTop());

                            sketchVO.setAngle(rotatView.getRoadDegree());

                            if (null != rotatView.getTag() && !"".equals(rotatView.getTag())) {
                                sketchVO.setPath(Integer.parseInt(rotatView.getTag().toString()));

                            }

                        } else if (view instanceof RotatTextView) {

                            RotatTextView rotatTextView = (RotatTextView) view;
                            sketchVO.setWidth(rotatTextView.getWidth());
                            sketchVO.setHeight(rotatTextView.getHeight());
                            sketchVO.setX(rotatTextView.getLeft());
                            sketchVO.setY(rotatTextView.getTop());
                            sketchVO.setRemark(rotatTextView.getText().toString().trim());

                            if (null != rotatTextView.getTag() && !"".equals(rotatTextView.getTag())) {
                                sketchVO.setPath(Integer.parseInt(rotatTextView.getTag().toString()));
                            }
                        }
                        list.add(sketchVO);
                    }

                    subSurveyVO.setSketchVOList(list);

                    if (null != paint.getTag()) {
                        subSurveyVO.setSketchbg(Integer.parseInt(paint.getTag().toString()));
                    }

                    //保存
//                    String filePath = FileUtils.getInstance().carateSubmitSubSurveyVOPath(mTaskInfoVO.getTaskNo());
//                    FileUtils.getInstance().saveFile(JSON.toJSONString(subSurveyVO), filePath);
                }


//                //回收图片
//                new RecycleBitmapInLayout(true).recycle(abl);
//                //退出时，请求杀死进程
//                android.os.Process.killProcess(android.os.Process.myPid());
                finish();
            }
        }
    }

    public void road(View view) {
        // 初始化数据 初始化图标
        ArrayList<List<Integer>> list = new ArrayList<List<Integer>>();
        List<Integer> list1 = new ArrayList<Integer>();

        list1.add(R.drawable.road3);


        List<Integer> list2 = new ArrayList<Integer>();
        list2.add(R.drawable.road5);
        List<Integer> list3 = new ArrayList<Integer>();
        list3.add(R.drawable.road8);

        List<Integer> list4 = new ArrayList<Integer>();

        list4.add(R.drawable.road7);
        list.add(list1);
        list.add(list2);
        list.add(list3);
        list.add(list4);
        // 初始化标题按钮
        String[] titles = new String[list.size()];
        titles[0] = "   直路   ";
        titles[1] = " 十字路 ";
        titles[2] = "   弯路   ";
        titles[3] = "   岔路   ";

        Intent intent = new Intent();
        intent.putExtra("list", list);
        intent.putExtra("titles", titles);
        intent.putExtra("title", "道路");
        intent.setClass(this, RoadActivity.class);
        startActivityForResult(intent, 10);
    }

    public void car(View view) {
        // 初始化数据 初始化图标
        ArrayList<List<Integer>> list = new ArrayList<List<Integer>>();
        List<Integer> list1 = new ArrayList<Integer>();
        list1.add(R.drawable.c1);

        list1.add(R.drawable.c3);
        list1.add(R.drawable.c7);

        list1.add(R.drawable.c30);

        List<Integer> list2 = new ArrayList<Integer>();
        list2.add(R.drawable.c13);
        list2.add(R.drawable.c14);
        List<Integer> list3 = new ArrayList<Integer>();
        list3.add(R.drawable.c19);
        list3.add(R.drawable.c21);

        List<Integer> list4 = new ArrayList<Integer>();

        list4.add(R.drawable.c36);
        list4.add(R.drawable.c37);
        list4.add(R.drawable.c38);

        List<Integer> list5 = new ArrayList<Integer>();
        list5.add(R.drawable.c35);

        list.add(list1);
        list.add(list2);
        list.add(list3);
        list.add(list4);
        list.add(list5);

        // 初始化标题按钮
        String[] titles = new String[list.size()];
        titles[0] = "   小车   ";
        titles[1] = "  客车  ";
        titles[2] = "   货车   ";
        titles[3] = "   专用车  ";
        titles[4] = "   其它   ";
        Intent intent = new Intent();
        intent.putExtra("list", list);
        intent.putExtra("titles", titles);
        intent.putExtra("title", "车型");
        intent.setClass(this, RoadActivity.class);
        startActivityForResult(intent, 20);
    }

    public void mark(View view) {
        // 初始化数据 初始化图标
        ArrayList<List<Integer>> list = new ArrayList<List<Integer>>();
        List<Integer> list1 = new ArrayList<Integer>();
        list1.add(R.drawable.mark0);
        list1.add(R.drawable.mark1);
        list1.add(R.drawable.mark2);
        list1.add(R.drawable.mark3);
        list1.add(R.drawable.mark4);
        list1.add(R.drawable.mark5);

        List<Integer> list2 = new ArrayList<Integer>();
        list2.add(R.drawable.mark21);
        list2.add(R.drawable.mark22);
        list2.add(R.drawable.mark23);
        list2.add(R.drawable.mark24);
        list2.add(R.drawable.mark25);
        list2.add(R.drawable.mark26);
        list2.add(R.drawable.mark27);
        list2.add(R.drawable.mark28);
        list2.add(R.drawable.mark29);

        List<Integer> list3 = new ArrayList<Integer>();
        list3.add(R.drawable.mark6);
        list3.add(R.drawable.mark7);
        list3.add(R.drawable.mark8);
        list3.add(R.drawable.mark9);
        list3.add(R.drawable.mark10);
        list3.add(R.drawable.mark11);
        list3.add(R.drawable.mark12);
        list3.add(R.drawable.mark13);
        list3.add(R.drawable.mark14);
        list3.add(R.drawable.mark15);
        list3.add(R.drawable.mark16);
        list3.add(R.drawable.mark17);
        list3.add(R.drawable.mark18);
        list3.add(R.drawable.mark19);
        list3.add(R.drawable.mark20);

        List<Integer> list4 = new ArrayList<Integer>();
        list4.add(R.drawable.mark30);
        list4.add(R.drawable.mark31);
        list4.add(R.drawable.mark32);
        list4.add(R.drawable.mark33);
        list4.add(R.drawable.mark34);
        list4.add(R.drawable.mark35);
        list4.add(R.drawable.mark36);
        list4.add(R.drawable.mark37);
        list4.add(R.drawable.mark39);
        list.add(list1);
        list.add(list2);
        list.add(list3);
        list.add(list4);

        // 初始化标题按钮
        String[] titles = new String[list.size()];
        titles[0] = "   警告   ";
        titles[1] = "   指示   ";
        titles[2] = "   禁令   ";
        titles[3] = "   其它   ";

        Intent intent = new Intent();
        intent.putExtra("list", list);
        intent.putExtra("titles", titles);
        intent.putExtra("title", "标志");
        intent.setClass(this, RoadActivity.class);
        startActivityForResult(intent, 20);
    }

    public void people(View view) {
        // 初始化数据 初始化图标
        ArrayList<List<Integer>> list = new ArrayList<List<Integer>>();
        List<Integer> list1 = new ArrayList<Integer>();
        list1.add(R.drawable.people01);
        list1.add(R.drawable.people02);
        list1.add(R.drawable.people03);
        list1.add(R.drawable.people04);
        list1.add(R.drawable.people05);
        list1.add(R.drawable.people06);
        list1.add(R.drawable.people07);
        list1.add(R.drawable.people08);

        list.add(list1);
        Intent intent = new Intent();
        intent.putExtra("list", list);
        intent.putExtra("titles", "");
        intent.putExtra("title", "人物");
        intent.putExtra("type", "D");
        intent.setClass(this, RoadActivity.class);
        startActivityForResult(intent, 20);

    }

    /**
     * 线条
     *
     * @param view
     */
    public void map(View view) {
        // 初始化数据 初始化图标
        ArrayList<List<Integer>> list = new ArrayList<List<Integer>>();
        List<Integer> list1 = new ArrayList<Integer>();
        list1.add(R.drawable.line00);
        list1.add(R.drawable.line01);
        list1.add(R.drawable.line02);
        list1.add(R.drawable.line03);
        list1.add(R.drawable.line04);
        list1.add(R.drawable.line05);
        list1.add(R.drawable.line06);
        list1.add(R.drawable.line07);
        list1.add(R.drawable.line08);
        list1.add(R.drawable.line09);
        list1.add(R.drawable.line10);
        list1.add(R.drawable.line11);
        list1.add(R.drawable.line12);
        list1.add(R.drawable.line13);
        list1.add(R.drawable.line14);
        list.add(list1);
        Intent intent = new Intent();
        intent.putExtra("list", list);
        intent.putExtra("titles", "");
        intent.putExtra("title", "线条");
        intent.putExtra("type", "D");
        intent.setClass(this, RoadActivity.class);
        startActivityForResult(intent, 20);

    }

    /**
     * 重选
     * @param view
     */
    public void again(View view) {
        paint.removeAllViews();
    }

    public void tuya(View view) {
        Intent intent = new Intent();
        intent.setClass(this, TuyaActivity.class);
        startActivityForResult(intent, 30);

    }

    public void describe(View view) {
        Intent intent = new Intent();
        intent.setClass(this, DescribeActivity.class);
        startActivityForResult(intent, 20);

    }

    public void wupin(View view) {
        // 初始化数据 初始化图标
        ArrayList<List<Integer>> list = new ArrayList<List<Integer>>();
        List<Integer> list1 = new ArrayList<Integer>();
        list1.add(R.drawable.substance01);
        list1.add(R.drawable.substance02);
        list1.add(R.drawable.substance03);
        list1.add(R.drawable.substance04);
        list1.add(R.drawable.substance05);
        list1.add(R.drawable.substance06);
        list1.add(R.drawable.substance07);
        list1.add(R.drawable.substance08);
        list.add(list1);
        Intent intent = new Intent();
        intent.putExtra("list", list);
        intent.putExtra("titles", "");
        intent.putExtra("title", "物品");
        intent.putExtra("type", "D");
        intent.setClass(this, RoadActivity.class);
        startActivityForResult(intent, 20);

    }

    private void insertView(int id) {
        final RotatView image = new RotatView(DrawPlaceActivity.this,0);
        image.setTag(id);
        image.setRotatDrawableResource(id);
        image.setId(2);
        paint.addView(image);
    }

    private void insertTextView(int id, String message) {
        RotatTextView image = new RotatTextView(DrawPlaceActivity.this);
        image.setWidth(200);
        image.setBackgroundResource(id);
        image.setText(message);
        image.setId(4);
        image.setTag(id);
        image.setTextColor(android.graphics.Color.BLACK);
        paint.addView(image);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null || data.getExtras() == null)
            return;
        int index = data.getExtras().getInt("result");

        switch (resultCode) {
            // 20更新主界面
            case 20:

                if (requestCode == 10) {
                    paint.setBackgroundResource(index);
                    paint.setTag(index);
                } else if (requestCode == 30) {

                    String imagePath = data.getExtras().getString("imagePath");

//                    Bitmap bitmap = new ImageViewUtil().bitmap2ImageView(this,imagePath);
                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                    if (bitmap != null) {
                        paint.setBackgroundDrawable(null);
                        System.gc();
                        paint.setBackgroundDrawable(new BitmapDrawable(bitmap));

                    } else {
                        android.widget.Toast.makeText(this, "加载手绘图片出错", Toast.LENGTH_SHORT)
                                .show();
                    }

                } else {
                    insertView(index);
                }
                break;
            case 30:

                insertTextView(index, data.getExtras().getString("message"));
                break;
        }
    }

}
