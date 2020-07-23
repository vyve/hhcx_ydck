package com.estar.ocr.vl;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.estar.ocr.BaseActivitity;
import com.estar.ocr.Config;
import com.estar.ocr.R;
import com.estar.ocr.common.ShowResultDialog;
import com.estar.ocr.common.ValueForKey;
import com.estar.ocr.common.camera.CameraFragment;
import com.estar.ocr.sid.NewSIDOcrActivity;
import com.etop.VL.VLCardAPI;
import com.flurgle.camerakit.CameraKit;
import com.flurgle.camerakit.CameraListener;
import com.flurgle.camerakit.CameraView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 行驶证识别
 */
public class NewVLOcrActivity extends BaseActivitity {

    private CameraView cameraView;
    private VLViewfinderView myView;
    private RelativeLayout re_c;
    private TextView mytext;
    private ImageButton take_pic;

    private VLCardAPI   api=null;
    private TimerTask timer;
    private Vibrator mVibrator;

    private boolean isFatty = false;
    private boolean  bInitKernal=false;
    private boolean isRun = false;
    private boolean isAddMyView = false;
    private boolean isPhoto = false;

    private long joinTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        setContentView(R.layout.activity_new_vlocr);
        if(!bInitKernal)
        {
            if(api==null)
            {
                api= new VLCardAPI();
            }
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            int nRet = api.VLKernalInit("", Config.LIC_PATH,Config.LIC_NAME,0x05,0x02,telephonyManager,this);
            if(nRet!=0)
            {
                Toast.makeText(getApplicationContext(), "激活失败", Toast.LENGTH_SHORT).show();
                System.out.print("nRet="+nRet);
                bInitKernal =false;
            }
            else
            {
                System.out.print("nRet="+nRet);
                bInitKernal=true;
            }
        }
        joinTime = System.currentTimeMillis();

        initView();
        init();
    }

    private void initView(){
        cameraView = findViewById(R.id.cameraView);
        re_c = findViewById(R.id.re_c);
        mytext = findViewById(R.id.mytext);
        take_pic = findViewById(R.id.take_pic);
        mytext.setText("当前为视频流识别模式,可点击切换按钮切换识别模式");

        findViewById(R.id.select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSelectPic();
            }
        });
        findViewById(R.id.change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPhoto){
                    mytext.setText("当前为视频流识别模式,可点击切换按钮切换识别模式");
                    take_pic.setVisibility(View.GONE);
                    isPhoto = false;
                }else{
                    isPhoto= true;
                    mytext.setText("当前为拍照识别模式,可点击切换按钮切换识别模式");
                    take_pic.setVisibility(View.VISIBLE);
                }
            }
        });

        findViewById(R.id.flash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String flashMode = cameraView.getFlash();
                if (flashMode.equals(Camera.Parameters.FLASH_MODE_TORCH)) {
                   cameraView.setFlash(CameraKit.Constants.FLASH_OFF);
                } else {
                   cameraView.setFlash(CameraKit.Constants.FLASH_ON);
                }
            }
        });
        take_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraView.captureImage();
            }
        });
    }

    private void init(){
        cameraView.setCameraListener(new CameraListener() {
            @Override
            public void onPreviewCallback(byte[] jpeg) {
                if (isPhoto) {
                    return;
                }
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

            @Override
            public void onPictureTaken(byte[] jpeg) {
                super.onPictureTaken(jpeg);
                long currentTime = System.currentTimeMillis();
                if (currentTime - joinTime < 2 * 1000) {
                    return;
                }
                if (isRun) {

                    return;
                } else {
                    isRun = true;
                    new ShibieTask(jpeg).execute();
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

    @Override
    protected void onResume() {
        super.onResume();

        cameraView.setDisplayOrientation(0);
        cameraView.start();

        if (!isAddMyView) {
            if (isFatty)
                myView = new VLViewfinderView(this, cameraView.getPreviewSize().getWidth(), cameraView.getPreviewSize().getHeight(), isFatty);
            else
                myView = new VLViewfinderView(this, cameraView.getPreviewSize().getWidth(), cameraView.getPreviewSize().getHeight());
            re_c.addView(myView);
        }
//        if (timer!=null) {
            Timer time = new Timer();
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
        if (api!=null) {
            api.VLKernalUnInit();
            api = null;
        }
    }

    class ShibieTask extends AsyncTask<Void, Void, String> {

        private byte[] bytes;
        private int width, height;
        List<ValueForKey> list = new ArrayList<>();
        private String filePath = null;
        private ProgressDialog dialog;
        private TYPE type;
        public ShibieTask(byte[] bytes, int width, int height) {
            this.bytes = bytes;
            this.width = width;
            this.height = height;
            list.clear();
            type = TYPE.PREVIEW;
        }

        public ShibieTask(String filePath) {
            this.filePath = filePath;

            list.clear();
            type = TYPE.PATH;
        }

        public ShibieTask(byte[] bytes){
            this.bytes = bytes;
            type = TYPE.PICTURE;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (type==TYPE.PATH||type==TYPE.PICTURE) {
                dialog = ProgressDialog.show(NewVLOcrActivity.this,null,"正在识别中");
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
            if (type == TYPE.PATH) {
                r = api.VLRecognizeImageFileW(filePath);
            }else if(type == TYPE.PICTURE){
                r = api.VLRecognizePhoto(bytes,bytes.length);
            }else {
                r = api.VLRecognizeNV21(bytes, width,
                        height, recogval, buffl);
            }
            Long endTiem = System.currentTimeMillis();
            String str = "";
            if (r == 0) {
//                List<ValueForKey> list = new ArrayList<>();
                ValueForKey valueForKey = null;
                    for (int i = 0; i < 10; i++) {
                        valueForKey = new ValueForKey();
                        str= api.VLGetResult(i);
                        valueForKey.setValue(str);
                        valueForKey.setKey(getValueByKey(ZJ_TYPE.VL,i+""));
                        list.add(valueForKey);
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
                mVibrator = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
                mVibrator.vibrate(50);
//                Toast.makeText(NewSIDOcrActivity.this, aVoid, 0).show();
                Dialog dialog =  new ShowResultDialog(NewVLOcrActivity.this,list, "NewVLOcrActivity");
                dialog.show();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        isRun = false;
                    }
                });
            }else{
                if (type==TYPE.PATH||type==TYPE.PICTURE) {
                    Toast.makeText(NewVLOcrActivity.this,"识别失败",Toast.LENGTH_SHORT).show();
                }
                isRun = false;
            }
        }
    }



}
