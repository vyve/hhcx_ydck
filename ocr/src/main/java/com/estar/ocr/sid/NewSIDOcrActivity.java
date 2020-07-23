package com.estar.ocr.sid;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.AsyncQueryHandler;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.estar.ocr.BaseActivitity;
import com.estar.ocr.Config;
import com.estar.ocr.R;
import com.estar.ocr.common.ShowResultDialog;
import com.estar.ocr.common.ValueForKey;
import com.estar.ocr.dl.NewDLOcrActivity;
import com.etop.SIDCard.SIDCardAPI;
import com.flurgle.camerakit.CameraKit;
import com.flurgle.camerakit.CameraListener;
import com.flurgle.camerakit.CameraView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NewSIDOcrActivity extends BaseActivitity {

    private CameraView cameraView;
    private SIDViewfinderView myView;
    private RelativeLayout re_c;

    private SIDCardAPI api = null;
    private boolean bInitKernal = false;
    private TimerTask timer;

    private boolean isRun = false;
    private boolean isFatty = false;

    private long joinTime;
    Timer time = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_new_sidocr);
        if (!bInitKernal) {
            if (api == null) {
                api = new SIDCardAPI();
            }
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            int nRet = api.SIDCardKernalInit("", Config.LIC_PATH, Config.LIC_NAME, 0x02, 0x02, telephonyManager, this);
            if (nRet != 0) {
                Toast.makeText(getApplicationContext(), "激活失败" + nRet, Toast.LENGTH_SHORT).show();
                System.out.print("nRet=" + nRet);
                bInitKernal = false;
            } else {
                System.out.print("nRet=" + nRet);
                bInitKernal = true;
                api.SIDCardSetRecogType(0);
            }
        }
        joinTime = System.currentTimeMillis();
        init();
    }

    private void init() {
        cameraView = findViewById(R.id.cameraView);
        re_c = findViewById(R.id.re_c);
        cameraView.setZoom(2);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels; // 屏幕宽度（像素）
        int height = metric.heightPixels; // 屏幕高度（像素）
        if (width * 3 == height * 4) {
            isFatty = true;
        }
        if (isFatty)
            myView = new SIDViewfinderView(this, width, height, isFatty);
        else
            myView = new SIDViewfinderView(this, width, height);
        try {
            re_c.addView(myView);
        } catch (Exception e) {
            e.printStackTrace();
        }


        findViewById(R.id.select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSelectPic();
            }
        });
        cameraView.setCameraListener(new CameraListener() {
            @Override
            public void onPreviewCallback(byte[] jpeg) {

                long currentTime = System.currentTimeMillis();
                if (currentTime - joinTime < 2 * 1000) {
                    return;
                }
                if (isRun) {

                    return;
                } else {
                    isRun = true;
                    new ShibieTask(jpeg, cameraView.getPreviewSize().getWidth(), cameraView.getPreviewSize().getHeight()).execute();
                    return;
                }


            }


        });
    }

    @Override
    public void selectResultPicPath(String picPath) {
        super.selectResultPicPath(picPath);
        isRun = true;
        new ShibieTask(picPath).execute();
    }

    public void takePicture() {

//        cameraView.captureImage();
    }

    @Override
    protected void onResume() {
        super.onResume();

//        if (timer!=null) {
            time.schedule(timer = new TimerTask() {
                @Override
                public void run() {
                    if (cameraView != null) {
                        try {
                            cameraView.autoFocus(new Camera.AutoFocusCallback() {
                                public void onAutoFocus(boolean success, Camera camera) {
                                    // isSuccess=success;
//                                    Toast.makeText(NewSIDOcrActivity.this,"9999999",0).show();
//                                    takePicture();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, 1000, 2500);

//        }
        cameraView.setDisplayOrientation(0);
        cameraView.start();
    }

    @Override
    protected void onPause() {
        cameraView.stop();
        super.onPause();
        if (timer!=null) {
            timer.cancel();
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bInitKernal) {
            api.SIDCardKernalUnInit();
            bInitKernal = false;
            api = null;
        }
//        timer.cancel();
//        cameraView.stop();
    }


    class ShibieTask extends AsyncTask<Void, Void, String> {

        private byte[] bytes;
        private int width, height;
        List<ValueForKey> list = new ArrayList<>();
        private String filePath = null;
        private  ProgressDialog dialog;

        public ShibieTask(byte[] bytes, int width, int height) {
            this.bytes = bytes;
            this.width = width;
            this.height = height;
            list.clear();
        }

        public ShibieTask(String filePath) {
            this.filePath = filePath;

            list.clear();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (filePath!=null&& !"".equals(filePath)) {
                dialog = ProgressDialog.show(NewSIDOcrActivity.this,null,"正在识别中");
                dialog.show();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            int buffl = 256;
            char[] recogval = new char[buffl];
            Date dt = new Date();
            Long timeStart = System.currentTimeMillis();
            int r = -1;
            if (filePath!=null&& !"".equals(filePath)) {
                r = api.SIDCardRecogImgaeFile(filePath);
            }else {
                r = api.SIDCardRecognizeNV21(bytes, width,
                        height, recogval, buffl);
            }
            Long endTiem = System.currentTimeMillis();
            String str = "";
            if (r == 0) {
                int nRecog = api.SIDCardGetRecogType();
//                List<ValueForKey> list = new ArrayList<>();
                ValueForKey valueForKey = null;
                if (nRecog == 1) {
                    for (int i = 0; i < 6; i++) {
                        valueForKey = new ValueForKey();
                        str= api.SIDCardGetResult(i);
                        valueForKey.setValue(str);
                        valueForKey.setKey(getValueByKey(ZJ_TYPE.SID,i+""));
                        list.add(valueForKey);
                    }
                } else if (nRecog == 2) {
                    for (int i = 6; i < 8; i++) {
                        str = api.SIDCardGetResult(i);
                        valueForKey = new ValueForKey();
                        valueForKey.setValue(str);
                        valueForKey.setKey(getValueByKey(ZJ_TYPE.SID,i+""));
                        list.add(valueForKey);
                    }
                }
            }

            return str;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);

            if (dialog!=null&&dialog.isShowing()) {
                dialog.dismiss();
                dialog = null;
            }

            if (!"".equals(aVoid)) {
//                Toast.makeText(NewSIDOcrActivity.this, aVoid, 0).show();
                Dialog dialog =  new ShowResultDialog(NewSIDOcrActivity.this,list, "NewSIDOcrActivity");
                dialog.show();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        isRun = false;
                    }
                });
            }else{
                if (filePath!=null&& !"".equals(filePath)) {
                    Toast.makeText(NewSIDOcrActivity.this,"识别失败！",Toast.LENGTH_SHORT).show();
                }
                isRun = false;
            }
        }
    }
}
