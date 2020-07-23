package com.estar.hh.survey.view.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.adapter.listener.OnItemClickListener;
import com.adapter.listener.OnItemLongClickListener;
import com.estar.hh.survey.R;
import com.estar.hh.survey.adapter.GridAdapter;
import com.estar.hh.survey.common.UserSharePrefrence;
import com.estar.hh.survey.entity.vo.ImageVO;
import com.estar.hh.survey.service.MyIntentService;
import com.estar.hh.survey.utils.FileUtils;
import com.estar.utils.MessageDialog;
import com.estar.utils.ShowImagePopWindow;
import com.estar.utils.ToastUtils;

import org.litepal.crud.DataSupport;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.VISIBLE;

/**
 * 图片列表
 */
public class ImageListActivity extends HuangheBaseActivity {

    @ViewInject(R.id.image_list_back)
    private LinearLayout back;

    @ViewInject(R.id.image_list_recyclerview)
    private RecyclerView recyclerView;

    @ViewInject(R.id.image_list_take_pic)
    private LinearLayout camera;

    @ViewInject(R.id.image_list_delete)
    private LinearLayout delete;

    @ViewInject(R.id.image_list_selectall)
    private LinearLayout selectAll;

    @ViewInject(R.id.image_list_tv_select)
    private TextView tv_select;

    @ViewInject(R.id.image_list_upload)
    private LinearLayout upload;

    /**数据库图片数据*/
    private List<ImageVO> imageList;
    /**图片数据*/
    protected List<String> imagePathList = new ArrayList<String>();
    /**存在的图片数据*/
    private List<ImageVO> imageList2 = new ArrayList<ImageVO>();
    private GridAdapter adapter;
    private String reportNo;
    private String taskno;//任务Id
    private String type = "";//任务类型 121现在查勘 ，122：人伤查勘，131：车辆定损，132：财产定损
    private UpdateUIBroadcastReceiver broadcastReceiver;

    public FileUtils fileUtils;

    private UserSharePrefrence userSharePrefrence = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_list_activity);
        x.view().inject(this);

        fileUtils = FileUtils.getInstance(this);

        initView();
        initData();

    }

    /**
     * 初始化数据
     */

    public void initData() {

        userSharePrefrence = new UserSharePrefrence(this);

        try {
            Bundle b = getIntent().getExtras();
            reportNo = b.getString("reportNo");
            taskno = b.getString("taskNo");
            type = b.getString("type");//任务类型 121现在查勘 ，122：人伤查勘，131：车辆定损，132：财产定损
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(reportNo)||TextUtils.isEmpty(taskno)){
            ToastUtils.showShort(ImageListActivity.this, "报案号或任务号为空！");
            finish();
        }
    }

    public void initView() {
        setAdapter();//初始化列表
        initImage();//查询数据库
        /**返回*/
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        /**拍照*/
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camera();
            }
        });
        /**删除*/
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        });
        /**全选*/
        selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAll();
            }
        });
        /**上传*/
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploading();
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3,
                GridLayoutManager.VERTICAL, false);
        // 设置布局管理器
        recyclerView.setLayoutManager(gridLayoutManager);
        // 使用RecyclerView提供的默认的动画效果
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }



    @Override
    protected void onResume() {
        super.onResume();
        // 动态注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("action.updateUI");
        broadcastReceiver = new UpdateUIBroadcastReceiver();
//        registerReceiver(broadcastReceiver, filter,"com.estar.hh.survey.view.activity.ImageListActivity",new Handler());
        registerReceiver(broadcastReceiver, filter);

        if (null!=adapter){
            initImage();//查询数据库
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 注销广播
        if (null != broadcastReceiver) {
            unregisterReceiver(broadcastReceiver);
            broadcastReceiver = null;
        }
    }

    /**
     * 定义广播接收器（内部类）
     *
     * @author lenovo
     */
    private class UpdateUIBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            //要做的事情
            initImage();
        }
    }


    private void initImage() {
        try {
            /**
             * LitePal数据库  查询数据库
             */
            imageList = DataSupport.where("taskid=? ", taskno).find(ImageVO.class);
            imageList2.clear();
            for (ImageVO imageVO : imageList) {
                // 文件存在
                if (new File(imageVO.getImagePath()).exists()) {
                    imageList2.add(imageVO);

                } else {
                    imageVO.delete();
                }
            }
            adapter.clean(imageList2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAdapter() {

        // 创建Adapter，并指定数据集
        adapter = new GridAdapter(ImageListActivity.this,imageList2);
        // 设置Adapter
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View view, Object item, int position) {
                imagePathList.removeAll(imagePathList);
                for (ImageVO imageVO : imageList2) {
                    if (!TextUtils.isEmpty(imageVO.getImagePath())) {
                        imagePathList.add(imageVO.getImagePath());
                    }
                }
                ShowImagePopWindow show = new ShowImagePopWindow(ImageListActivity.this, imagePathList, position);//因为第一项为空
                //设置PopupWindow全屏
                show.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                show.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                show.showAtLocation(findViewById(R.id.image_list_recyclerview), Gravity.CENTER, 0, 0);
            }
        });
        adapter.setOnItemLongClickListener(new OnItemLongClickListener<ImageVO>() {
            @Override
            public void onLongClick(View view, final ImageVO item, final int position) {

                // 初始化提示框
                final AlertDialog alertDialog = new AlertDialog.Builder(ImageListActivity.this).create();
                alertDialog.show();
                alertDialog.getWindow().setContentView(R.layout.dialog);
                alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                TextView tv_dialog_title = alertDialog.getWindow().findViewById(R.id.tv_dialog_title);
                TextView tv_cancel = alertDialog.getWindow().findViewById(R.id.tv_cancel);
                TextView tv_sure = alertDialog.getWindow().findViewById(R.id.tv_sure);
                final EditText et = alertDialog.getWindow().findViewById(R.id.et);
                et.setText(item.getMark());
                ((TableRow)et.getParent()).setVisibility(VISIBLE);
                ((LinearLayout)tv_cancel.getParent()).setVisibility(VISIBLE);

                tv_dialog_title.setText("重命名");//标题
                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                tv_sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("".equals(et.getText().toString())) {
                            ToastUtils.showShort(ImageListActivity.this, "不能命名为空！");
                            return;
                        }
                        imageList2.get(position).setMark(et.getText().toString());
                        adapter.changePosition(imageList2,position);//更新适配器数据
                        /**
                         * 更新数据库
                         */
                        ImageVO updataImageVO = new ImageVO();
                        updataImageVO.setMark(et.getText().toString());
                        updataImageVO.updateAll("imagename = ? ", item.getImagename());
                        alertDialog.dismiss();
                    }
                });

            }
        });
    }

    /**
     * 拍照
     */
    private void camera() {
        Intent intent = new Intent(ImageListActivity.this, CameraActivity.class);
        intent.putExtra("reportNo", reportNo);
        intent.putExtra("taskno", taskno);

//        intent.putExtra("type", type);// 任务类型 121现在查勘 ，122：人伤查勘，131：车辆定损，132：财产定损

        startActivity(intent);
    }

    /**
     * 删除
     */
    private void delete() {

        final List<ImageVO> mSelectedImage = adapter.getCheckList();
        if (mSelectedImage.size() > 0) {//已选中图片
            new MessageDialog(this, new MessageDialog.SubmitOnClick() {
                @Override
                public void onSubmitOnClickSure() {
                    /**LitePal数据库  更新数据库*/
                    try {
                        for (ImageVO imageVO : mSelectedImage) {
                            fileUtils.deletePath(imageVO.getImagePath());
                            imageVO.delete();
                        }

                        initImage();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    tv_select.setText("全选");
                }

                @Override
                public void onSubmitOnClickCancel() {
                }
            }, "友情提示", "删除所选图片？", "确定",
                    "取消", false);

        } else {
            ToastUtils.showShort(this, "请先选择照片");
        }
    }

    /**
     * 全选
     */
    private void checkAll() {
//        LogUtil.w("照片张数="+adapter.getItemCount());
        int size=imageList2.size();
        if(adapter.getItemCount()>0){
            if ("全选".equals(tv_select.getText().toString())) {
                tv_select.setText("反选");
                for (int i =0; i<size;i++){
                    if (0 == imageList2.get(i).getUpcliamflag()
                            || 3 == imageList2.get(i).getUpcliamflag() ) {// 是否上传理赔 0 为初始状态， 1 为上传状态 ，2 为已上传成功 ，3 为上传失败  每张图片最多上传3次
                        imageList2.get(i).setChecked(1);//改成选中状态
                    }
                }

            } else {
                tv_select.setText("全选");
                for (int i =0; i<size;i++){
                    if (0 == imageList2.get(i).getUpcliamflag()
                            || 3 == imageList2.get(i).getUpcliamflag() ) {// 是否上传理赔 0 为初始状态， 1 为上传状态 ，2 为已上传成功 ，3 为上传失败  每张图片最多上传3次
                        imageList2.get(i).setChecked(0);//改成选中状态
                    }
                }

            }
            setAdapter();
        }else {
            ToastUtils.showShort(ImageListActivity.this, "无图片,请先拍照!");
        }

    }

    /**
     * 上传
     */
    private void uploading() {
        final List<ImageVO> mSelectedImage2 = adapter.getCheckList();
        if (mSelectedImage2.size() > 0) {
            new MessageDialog(ImageListActivity.this, new MessageDialog.SubmitOnClick() {
                @Override
                public void onSubmitOnClickSure() {
                    try {
                        for (ImageVO imageVO : mSelectedImage2) {
                            if (1 == imageVO.getChecked()) {
                                ImageVO imageVO1=new ImageVO();
                                imageVO1.setUpcliamflag(1);// 是否上传理赔 0 为初始状态， 1 为上传状态 ，2 为已上传成功 ，3 为上传失败
                                imageVO1.setUploadtimes(1);// 数据库不能更新0，上传次数
                                imageVO1.setChecked(1);// 选中
                                imageVO1.updateAll( "imagePath = ? ", imageVO.getImagePath());
                            }

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    adapter.cleanStatus2();//更新正在上传状态
                    try {
                        List<ImageVO> imageList = DataSupport.where("usercode=? and uploadtimes<? and upcliamflag=? ", userSharePrefrence.getUserEntity().getEmpCde(), "3", "1").find(ImageVO.class);
                        Map<String, List<ImageVO>> map = new HashMap<String, List<ImageVO>>();
                        map.put("imageList",imageList);
                        Intent intent = new Intent(ImageListActivity.this, MyIntentService.class);
                        intent.putExtra("map", (Serializable) map);
                        startService(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onSubmitOnClickCancel() {
                }
            }, "重要提示", "是否上传照片", "确定",
                    "取消", false);
            tv_select.setText("全选");
        } else {
            ToastUtils.showShort(this, "请先选择照片");
        }
    }
}
