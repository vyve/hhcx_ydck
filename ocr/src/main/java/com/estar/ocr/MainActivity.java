package com.estar.ocr;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.estar.ocr.backcard.bankcode.BankScanCamera;
import com.estar.ocr.backcard.bankcode.NewBackOcrActivity;
import com.estar.ocr.dl.DLOcrActivity;
import com.estar.ocr.dl.NewDLOcrActivity;
import com.estar.ocr.plate.PLCardActivity;
import com.estar.ocr.sid.NewSIDOcrActivity;
import com.estar.ocr.sid.SIDOcrActivity2;
import com.estar.ocr.vin.vincode.NewVinOcrActivity;
import com.estar.ocr.vin.vincode.VinScanCamera;
import com.estar.ocr.vl.NewVLOcrActivity;
import com.estar.ocr.vl.VLOcrActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//
//        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, NewDLOcrActivity.class);//驾驶证识别
//                startActivity(intent);
//            }
//        });
//        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, NewSIDOcrActivity.class);//身份证识别
//                startActivity(intent);
//            }
//        });
//        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, NewVLOcrActivity.class);//行驶证识别
//                startActivity(intent);
//            }
//
//        });
//        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, PLCardActivity.class);//车牌号识别
//                startActivity(intent);
//            }
//        });
//        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, NewBackOcrActivity.class);//银行卡识别
//                startActivity(intent);
//            }
//        });
//        findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, NewVinOcrActivity.class);//车架号识别
//                startActivity(intent);
//            }
//
//        });
        try {
            copyDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void copyDataBase() throws IOException {
        //  Common common = new Common();
        String dst = Environment.getExternalStorageDirectory().toString() + "/9AF68E014442E5AA58EF.lic";

        File file = new File(dst);
        if (!file.exists()) {
            // file.createNewFile();
        } else {
            file.delete();
        }

        try {
            InputStream myInput = getAssets().open("9AF68E014442E5AA58EF.lic");
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
            System.out.println("9AF68E014442E5AA58EF.lic" + "is not found");
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
