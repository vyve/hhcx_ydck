package com.estar.hh.survey.view.component;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Administrator on 2017/8/21.
 */

public class MyProgressDialog {

    private ProgressDialog dialog;
    private Context context;
    private String content;

    public MyProgressDialog(Context context, String content){
        this.context = context;
        this.content = content;
        dialog = new ProgressDialog(context);
        dialog.setCancelable(false);//是否可以被取消
        dialog.setMessage(content);//加载显示的信息
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//圆环风格
        dialog.show();
    }

    public MyProgressDialog(Context context, String content, boolean cancelable){
        this.context = context;
        this.content = content;
        dialog = new ProgressDialog(context);
        dialog.setCancelable(cancelable);//是否可以被取消
        dialog.setMessage(content);//加载显示的信息
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//圆环风格
        dialog.show();
    }

    public void stopDialog(){
        if (dialog != null){
//            dialog.cancel();
            dialog.dismiss();
        }
    }

}
