package com.estar.ocr.common;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.estar.ocr.R;
import com.estar.ocr.backcard.bankcode.NewBackOcrActivity;
import com.estar.ocr.common.camera.OcrVO;
import com.estar.ocr.dl.NewDLOcrActivity;
import com.estar.ocr.sid.NewSIDOcrActivity;
import com.estar.ocr.vin.vincode.NewVinOcrActivity;
import com.estar.ocr.vl.NewVLOcrActivity;

import java.util.List;

/**
 * Created by xueliang on 2017/3/9.
 */

public class ShowResultDialog extends Dialog {
    private TableLayout tableLayout;
    private List<ValueForKey> list;
    private LayoutInflater inflater;
    private Context context;
    private String tag;

    public ShowResultDialog(@NonNull Context context,List<ValueForKey> list) {
        super(context,R.style.dialog);
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    public ShowResultDialog(@NonNull Context context,List<ValueForKey> list, String tag) {
        super(context,R.style.dialog);
        this.context = context;
        this.tag = tag;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_result);
        tableLayout = findViewById(R.id.tableLayout);
        if(list!=null){
            for (int i = 0; i < list.size(); i++) {
                tableLayout.addView(getTableRow(list.get(i)));
            }
        }
        findViewById(R.id.dismiss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                /**
                 * 添加网页调用识别返回
                 */
                switch (tag){
                    case "NewBackOcrActivity":{//银行卡识别
                        Intent intent = new Intent();
                        OcrVO ocr = new OcrVO();
                        ocr.setList(list);
                        Bundle mBundle = new Bundle();
                        mBundle.putSerializable("orcData",ocr);
                        intent.putExtras(mBundle);
                        ((NewBackOcrActivity)context).setResult(15, intent);
                        ((NewBackOcrActivity)context).finish();
                    }break;
                    case "NewDLOcrActivity":{//驾驶证识别
                        Intent intent = new Intent();
                        OcrVO ocr = new OcrVO();
                        ocr.setList(list);
                        Bundle mBundle = new Bundle();
                        mBundle.putSerializable("orcData",ocr);
                        intent.putExtras(mBundle);
                        ((NewDLOcrActivity)context).setResult(11, intent);
                        ((NewDLOcrActivity)context).finish();
                    }break;
                    case "NewSIDOcrActivity":{//身份证识别
                        Intent intent = new Intent();
                        OcrVO ocr = new OcrVO();
                        ocr.setList(list);
                        Bundle mBundle = new Bundle();
                        mBundle.putSerializable("orcData",ocr);
                        intent.putExtras(mBundle);
                        ((NewSIDOcrActivity)context).setResult(12, intent);
                        ((NewSIDOcrActivity)context).finish();
                    }break;
                    case "NewVLOcrActivity":{//行驶证识别
                        Intent intent = new Intent();
                        OcrVO ocr = new OcrVO();
                        ocr.setList(list);
                        Bundle mBundle = new Bundle();
                        mBundle.putSerializable("orcData",ocr);
                        intent.putExtras(mBundle);
                        ((NewVLOcrActivity)context).setResult(13, intent);
                        ((NewVLOcrActivity)context).finish();
                    }break;
                    case "NewVinOcrActivity":{//车架号识别
                        Intent intent = new Intent();
                        OcrVO ocr = new OcrVO();
                        ocr.setList(list);
                        Bundle mBundle = new Bundle();
                        mBundle.putSerializable("orcData",ocr);
                        intent.putExtras(mBundle);
                        ((NewVinOcrActivity)context).setResult(16, intent);
                        ((NewVinOcrActivity)context).finish();
                    }break;
                    default:break;
                }
            }
        });
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(params);
    }

    public TableRow getTableRow(ValueForKey valuekey){
        TableRow tabrow = (TableRow) inflater.inflate(R.layout.item_tablerow,null);
        TextView keyView = tabrow.findViewById(R.id.key);
        EditText valueView = tabrow.findViewById(R.id.value);
        keyView.setText(valuekey.getKey());
        valueView.setText(valuekey.getValue());
        return tabrow;
    }
}
