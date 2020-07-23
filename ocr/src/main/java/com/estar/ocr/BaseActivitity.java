package com.estar.ocr;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.flurgle.camerakit.CameraKit;
import com.flurgle.camerakit.CameraListener;
import com.flurgle.camerakit.CameraView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by xueliang on 2017/2/23.
 */

public class BaseActivitity extends Activity {

//    private CameraView cameraView;
//    private TimerTask timer;

//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.base_camera);
//        cameraView = (CameraView) findViewById(R.id.cameraView);
//        Timer time = new Timer();
//        if (timer == null) {
//            timer = new TimerTask() {
//                public void run() {
//                    //isSuccess=false;
//                    if (cameraView != null) {
//                        try {
//                            cameraView.autoFocus(new Camera.AutoFocusCallback() {
//                                public void onAutoFocus(boolean success, Camera camera) {
//                                    // isSuccess=success;
////                                    Toast.makeText(BaseActivitity.this,"9999999",0).show();
//
//                                }
//                            });
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                };
//            };
//        }
//        time.schedule(timer, 500, 2500);
//        cameraView.setCameraListener(new CameraListener() {
//            @Override
//            public void onPreviewCallback(byte[] jpeg) {
//                super.onPreviewCallback(jpeg);
//
//                Toast.makeText(BaseActivitity.this,jpeg.length+"",0).show();
//            }
//        });
//        cameraView.setFocus(CameraKit.Constants.FOCUS_CONTINUOUS);
//    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        cameraView.start();
//    }
//
//    @Override
//    protected void onPause() {
//        cameraView.stop();
//        super.onPause();
//    }

    public void openSelectPic() {
        Intent intent = new Intent();
                /* 开启Pictures画面Type设定为image */
        intent.setType("image/*");
                /* 使用Intent.ACTION_GET_CONTENT这个Action */
        intent.setAction(Intent.ACTION_GET_CONTENT);
                /* 取得相片后返回本画面 */
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
            String filePath = "";
            if (cursor != null) {
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                filePath = cursor.getString(columnIndex);
                cursor.close();
            }

            selectResultPicPath(filePath);
        }
    }

    public void selectResultPicPath(String picPath){

    }
    public void show(byte[] bt) {

    }

    public String getValueByKey(ZJ_TYPE type,String key){
        String[] keys= null;
        String[] values = null;
        if(type==ZJ_TYPE.SID) {
            keys = getResources().getStringArray(R.array.arr_sid_key);
            values = getResources().getStringArray(R.array.arr_sid_value);
        }else if(type==ZJ_TYPE.VL){
            keys = getResources().getStringArray(R.array.arr_vl_key);
            values = getResources().getStringArray(R.array.arr_vl_value);
        }else if(type==ZJ_TYPE.DL){
            keys = getResources().getStringArray(R.array.arr_dl_key);
            values = getResources().getStringArray(R.array.arr_dl_value);
        }
        if(keys==null&&values==null){
            return "";
        }
        for (int i = 0; i < keys.length; i++) {
            if(key.equals(keys[i])){
                return values[i];
            }
        }
        return "";
    }

    public enum ZJ_TYPE{
        SID,VL,DL,BANK
    }

    public enum TYPE{
        PREVIEW,PICTURE,PATH
    }
}
