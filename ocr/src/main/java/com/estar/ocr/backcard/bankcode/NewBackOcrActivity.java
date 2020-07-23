package com.estar.ocr.backcard.bankcode;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.estar.ocr.BaseActivitity;
import com.estar.ocr.Config;
import com.estar.ocr.R;
import com.estar.ocr.backcard.view.BankViewfinderView;
import com.estar.ocr.common.ShowResultDialog;
import com.estar.ocr.common.ValueForKey;
import com.estar.ocr.dl.DLViewfinderView;
import com.estar.ocr.dl.NewDLOcrActivity;
import com.estar.ocr.vl.NewVLOcrActivity;
import com.etop.BankCard.BankCardAPI;
import com.etop.BankCard.BankCardInfoAPI;
import com.flurgle.camerakit.CameraKit;
import com.flurgle.camerakit.CameraListener;
import com.flurgle.camerakit.CameraView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 银行卡识别
 */
public class NewBackOcrActivity extends BaseActivitity {

    private CameraView cameraView;
    private BankViewfinderView myView;
    private RelativeLayout re_c;

    private BankCardAPI api =null;
    private BankCardInfoAPI cardinfoapi=null;
    private TimerTask timer;
    private Vibrator mVibrator;

    private boolean isFatty = false;
    private boolean isRun = false;
    private boolean isAddMyView = false;
    private boolean isPhoto = false;

    private long joinTime;
    private int width;
    private int height;

    private boolean isROI = false;
    private int[] m_ROI={0,0,0,0};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        setContentView(R.layout.activity_new_back_ocr);
        if(api==null){
            api =new BankCardAPI();
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            int nRet = api.ScanStart("", Config.LIC_PATH,Config.LIC_NAME,0x04,0x02,telephonyManager, this);
            if(nRet!=0)
            {
                Toast.makeText(getApplicationContext(), "激活失败"+nRet, Toast.LENGTH_SHORT).show();
            }
            if(cardinfoapi==null)
            {
                cardinfoapi =new BankCardInfoAPI();
                cardinfoapi.InitCardInfo();
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
        findViewById(R.id.back_camera).setOnClickListener(new View.OnClickListener() {
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
            int t = $t;
            int b = height - t;
            int $l = (int) ((b - t) * 1.58577);
            int l = (width - $l) / 2;
            int r = width - l;
            if (isFatty) {
                $t = height / 5;
                t = $t;
                b = height - t;
                $l = (int) ((b - t) * 1.58577);
                l = (width - $l) / 2;
                r = width - l;
            }
            double proportion = (double) width / (double) preWidth;
            double hproportion=(double)height/(double)  preHeight;
            l = (int) (l /proportion);
            t = (int) (t /hproportion);
            r = (int) (r /proportion);
            b = (int) (b / hproportion);
            m_ROI[0]=l;
            m_ROI[1]=t;
            m_ROI[2]=r;
            m_ROI[3]=b;
            api.SetRegion(l, t, r, b);
            isROI = true;
            if (isFatty)
                myView = new BankViewfinderView(this, width, height, isFatty);
            else
                myView = new BankViewfinderView(this, width, height);
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
            api.ScanEnd();
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
            if (type== NewVLOcrActivity.TYPE.PATH||type== NewVLOcrActivity.TYPE.PICTURE) {
                dialog = ProgressDialog.show(NewBackOcrActivity.this,null,"正在识别中");
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
                r = api.ScanImage(filePath,recogval);
            }else {
                r = api.ScanStreamNV21(bytes, width,
                        height, line, recogval,pLineWarp);
            }
            Long endTiem = System.currentTimeMillis();
            String str = "";
            if (r == 0) {
//                List<ValueForKey> list = new ArrayList<>();
                ValueForKey valueForKey = null;
                String[] cardinfo =new String[4];
                cardinfoapi.GetCardInfo(recogval, cardinfo);
                String CardNo = String.valueOf(recogval);
                valueForKey = new ValueForKey("银行卡号:",CardNo);
                list.add(valueForKey);
                valueForKey = new ValueForKey("银行名称:",cardinfo[0]);
                list.add(valueForKey);
                valueForKey = new ValueForKey("卡名称:",cardinfo[1]);
                list.add(valueForKey);
                valueForKey = new ValueForKey("银行类型:",cardinfo[2]);
                list.add(valueForKey);
                valueForKey = new ValueForKey("卡类型:",cardinfo[3]);
                list.add(valueForKey);
                str = CardNo;
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
                Dialog dialog =  new ShowResultDialog(NewBackOcrActivity.this,list, "NewBackOcrActivity");
                dialog.show();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        isRun = false;
                    }
                });
            }else{
                if (type== TYPE.PATH||type== TYPE.PICTURE) {
                    Toast.makeText(NewBackOcrActivity.this,"识别失败",Toast.LENGTH_SHORT).show();
                }
                isRun = false;
            }
        }
    }



}
