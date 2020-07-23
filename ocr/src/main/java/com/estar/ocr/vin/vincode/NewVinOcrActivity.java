package com.estar.ocr.vin.vincode;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.estar.ocr.BaseActivitity;
import com.estar.ocr.Config;
import com.estar.ocr.R;
import com.estar.ocr.common.ShowResultDialog;
import com.estar.ocr.common.ValueForKey;
import com.estar.ocr.vl.NewVLOcrActivity;
import com.etop.vin.VINAPI;
import com.flurgle.camerakit.CameraKit;
import com.flurgle.camerakit.CameraListener;
import com.flurgle.camerakit.CameraView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NewVinOcrActivity extends BaseActivitity {

    private RelativeLayout re_c;
    private VinViewfinderView myView;
    private CameraView cameraView;

    private VINAPI api;
    private TimerTask timer;
    private Vibrator mVibrator;


    private boolean  bInitKernal=false;
    private boolean isFatty = false;
    private boolean isRun = false;
    private long joinTime;
    private boolean isROI = false;


    private int height;
    private int width;
    private int[] m_ROI={0,0,0,0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        // // 屏幕常亮
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_new_vin_ocr);

        if(!bInitKernal)
        {
            api =new VINAPI();
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            int nRet = api.VinKernalInit("", Config.LIC_PATH_VIN,Config.LIC_NAME_VIN,0x01,0x02,telephonyManager, this);
            if(nRet!=0)
            {
                Toast.makeText(getApplicationContext(), "激活失败"+nRet, Toast.LENGTH_SHORT).show();
            }
            else
            {
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
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        width = metric.widthPixels; // 屏幕宽度（像素）
        height = metric.heightPixels; // 屏幕高度（像素）
        if (width * 3 == height * 4) {
            isFatty = true;
        }
        findViewById(R.id.select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSelectPic();
            }
        });
        findViewById(R.id.flash_camera).setOnClickListener(new View.OnClickListener() {
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
        findViewById(R.id.tackPic_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void init(){
        cameraView.setCameraListener(new CameraListener() {
            @Override
            public void onPreviewCallback(byte[] jpeg) {
//                if (isPhoto) {
//                    return;
//                }
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

    @Override
    protected void onResume() {
        super.onResume();

        cameraView.setDisplayOrientation(0);
        cameraView.start();
        int preWidth = cameraView.getPreviewSize().getWidth();
        int preHeight = cameraView.getPreviewSize().getHeight();
        if (!isROI) {
            int $t = height / 10;
            int ntmp = height*3 / 10;
            int t = ntmp;
            int b = height - ntmp;
            int $l = (int) ((height-$t-$t) * 1.585);
            int l = (width - $l) / 2;
            int r = width - l;
            l = l + 30;
            t = t + 19;
            r = r - 30;
            b = b - 19;
            if (isFatty) {
                $t = height / 5;
                ntmp = height*2 / 5;
                t = ntmp;
                b = height - t;
                $l = (int) ((height - $t-$t) * 1.585);
                l = (width - $l) / 2;
                r = width - l;
            }
            double proportion = (double) width / (double) preWidth;
            double hproportion=(double)height/(double)  preHeight;
            l = (int) (l /proportion);
            t = (int) (t /hproportion);
            r = (int) (r /proportion);
            b = (int) (b / hproportion);
            int[] borders = { l, t, r, b };
            m_ROI[0]=l;
            m_ROI[1]=t;
            m_ROI[2]=r;
            m_ROI[3]=b;
            api.VinSetROI(borders, preWidth, preHeight);
            isROI = true;
            if (isFatty)
                myView = new VinViewfinderView(this, width, height, isFatty);
            else
                myView = new VinViewfinderView(this, width, height);
            re_c.addView(myView);
        }
//        if (!isAddMyView) {
//            if (isFatty)
//                myView = new BankViewfinderView(this, cameraView.getPreviewSize().getWidth(), cameraView.getPreviewSize().getHeight(), isFatty);
//            else
//                myView = new BankViewfinderView(this, cameraView.getPreviewSize().getWidth(), cameraView.getPreviewSize().getHeight());
//            re_c.addView(myView);
//        }
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
            api.VinKernalUnInit();
            api = null;
        }
    }

    class ShibieTask extends AsyncTask<Void, Void, String> {

        private byte[] bytes;
        private int width, height;
        List<ValueForKey> list = new ArrayList<>();
        private String filePath = null;
        private ProgressDialog dialog;
        private NewVLOcrActivity.TYPE type;
        public ShibieTask(byte[] bytes, int width, int height) {
            this.bytes = bytes;
            this.width = width;
            this.height = height;
            list.clear();
            type = BaseActivitity.TYPE.PREVIEW;
        }

        public ShibieTask(String filePath) {
            this.filePath = filePath;

            list.clear();
            type = BaseActivitity.TYPE.PATH;
        }

        public ShibieTask(byte[] bytes){
            this.bytes = bytes;
            type = BaseActivitity.TYPE.PICTURE;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (type== NewVLOcrActivity.TYPE.PATH||type== NewVLOcrActivity.TYPE.PICTURE) {
                dialog = ProgressDialog.show(NewVinOcrActivity.this,null,"正在识别中");
                dialog.show();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            int buffl = 256;
            char[] recogval = new char[buffl];
            Date dt = new Date();
            Long timeStart = System.currentTimeMillis();
            int[] line =new int[4];
            line[0]=0;
            line[1]=0;
            line[2]=0;
            line[3]=0;
            int[] pLineWarp = new int[32000];
            int r = -1;
            if (type == NewVLOcrActivity.TYPE.PATH) {
                r = api.VinSaveImage(filePath);
            }else {
                r = api.VinRecognizeNV21(bytes, width,
                        height,  recogval,buffl);
            }
            Long endTiem = System.currentTimeMillis();
            String str = "";
            if (r == 0) {
//                List<ValueForKey> list = new ArrayList<>();
                ValueForKey valueForKey = null;
               String result = api.VinGetResult();
                valueForKey = new ValueForKey("车架号:",result);
                list.add(valueForKey);
                str = result;
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
                Dialog dialog =  new ShowResultDialog(NewVinOcrActivity.this,list, "NewVinOcrActivity");
                dialog.show();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        isRun = false;
                    }
                });
            }else{
                if (type== BaseActivitity.TYPE.PATH||type== BaseActivitity.TYPE.PICTURE) {
                    Toast.makeText(NewVinOcrActivity.this,"识别失败",Toast.LENGTH_SHORT).show();
                }
                isRun = false;
            }
        }
    }



}
