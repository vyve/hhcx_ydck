package com.estar.ocr.sid;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.format.Time;
import android.view.KeyEvent;
import android.widget.Toast;

import com.estar.ocr.Config;
import com.estar.ocr.R;
import com.etop.SIDCard.SIDCardAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SIDPhotoResultActivity extends AppCompatActivity {

    private SIDCardAPI api;

    private String filePath;

    private static final String PATH = Environment.getExternalStorageDirectory().toString() + "/alpha/SID/";

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sidphoto_result);

        try {
            copyDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        init();


    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

    public void copyDataBase() throws IOException {
        //  Common common = new Common();
        String dst = Environment.getExternalStorageDirectory().toString() + "/" + Config.LIC_NAME + ".lic";

        File file = new File(dst);
        if (!file.exists()) {
            // file.createNewFile();
        } else {
            file.delete();
        }

        try {
            InputStream myInput = getAssets().open(Config.LIC_NAME + ".lic");
            OutputStream myOutput = new FileOutputStream(dst);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (Exception e) {
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (api != null) {
                api.SIDCardKernalUnInit();
                api = null;
            }
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void init() {
        filePath = getIntent().getStringExtra("data");

        File file = new File(Config.LIC_PATH);
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
        if (api == null) {
            api = new SIDCardAPI();
            String FilePath = Environment.getExternalStorageDirectory().toString() + "/" + Config.LIC_NAME + ".lic";
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            int nRet = api.SIDCardKernalInit("", FilePath, Config.LIC_NAME, 0x02, 0x02, telephonyManager, this);
            if (nRet != 0) {
                Toast.makeText(getApplicationContext(), "激活失败", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }
        dialog = ProgressDialog.show(this, "", "正在识别中", true, false);

        new Thread(new Runnable() {
            @Override
            public void run() {


                try {

                    if (api != null) {

                        api.SIDCardSetRecogType(0);
                        int nRet = api.SIDCardRecogImgaeFile(filePath);
                        //String resultStr = "";
                        if (nRet == 0) {
                            String strCaptureFilePath = PATH + "_SIDCard_" + pictureName() + ".jpg";
                            String strCaptureFileHeadPath = PATH + "_SIDCard_Head_" + pictureName() + ".jpg";
                            api.SIDCardSaveCardImage(strCaptureFilePath);
                            nRet = api.SIDCardSaveHeadImage(strCaptureFileHeadPath);
                            //int ncheckcopy=api.SIDCardCheckIsCopy();//0表示不是，1表示是，-1错误
                            String result = "";

                            int nRecog = api.SIDCardGetRecogType();
                            if (nRecog == 1) {
                                for (int i = 0; i < 6; i++) {
                                    result += api.SIDCardGetResult(i);
                                    result += "\r\n";
                                }
                            } else if (nRecog == 2) {
                                for (int i = 6; i < 8; i++) {
                                    result += api.SIDCardGetResult(i);
                                    result += "\r\n";
                                }
                            }
                            final String rStr = result;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(SIDPhotoResultActivity.this, rStr + "", Toast.LENGTH_SHORT).show();
                                    if (dialog != null && dialog.isShowing()) {
                                        dialog.dismiss();
                                    }
                                }
                            });
                        } else {
//                    result = "识别失败，请重新选择图片...";
                        }
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public String pictureName() {
        String str = "";
        Time t = new Time();
        t.setToNow(); // 取得系统时间。
        int year = t.year;
        int month = t.month + 1;
        int date = t.monthDay;
        int hour = t.hour; // 0-23
        int minute = t.minute;
        int second = t.second;
        if (month < 10)
            str = year + "0" + month;
        else {
            str = String.valueOf(year) + month;
        }
        if (date < 10)
            str = str + "0" + date + "_";
        else {
            str = str + date + "_";
        }
        if (hour < 10)
            str = str + "0" + hour;
        else {
            str = str + hour;
        }
        if (minute < 10)
            str = str + "0" + minute;
        else {
            str = str + minute;
        }
        if (second < 10)
            str = str + "0" + second;
        else {
            str = str + second;
        }
        return str;
    }


}
