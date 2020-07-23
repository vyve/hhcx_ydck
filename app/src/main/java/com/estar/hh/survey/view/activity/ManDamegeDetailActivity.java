package com.estar.hh.survey.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.estar.hh.survey.R;
import com.estar.hh.survey.component.StateSelectDialog;
import com.estar.hh.survey.constants.StateSelect;
import com.estar.hh.survey.entity.entity.KeyValue;
import com.estar.hh.survey.utils.LogUtils;
import com.estar.ocr.common.ValueForKey;
import com.estar.ocr.common.camera.OcrVO;
import com.estar.ocr.sid.NewSIDOcrActivity;
import com.google.gson.Gson;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/29 0029.
 * 人伤详细信息
 */

public class ManDamegeDetailActivity extends HuangheBaseActivity {

    @ViewInject(R.id.man_damege_detail_back)
    private LinearLayout back;

    @ViewInject(R.id.man_damege_detail_list)
    private ListView list;

    @ViewInject(R.id.man_damege_detail_up_step)
    private LinearLayout upStep;

    @ViewInject(R.id.man_damege_detail_take_pic)
    private LinearLayout takePic;

    @ViewInject(R.id.man_damege_detail_next_step)
    private LinearLayout nextStep;

    private View content;
    private LinearLayout lossTypeLayout;
    private TextView lossType;
    private LinearLayout damegeTypeLayout;
    private TextView damegeType;
    private LinearLayout damegePlaceLayout;
    private TextView damegePlace;
    private LinearLayout lossManLayout;
    private TextView lossMan;
    private EditText name;
    private EditText creditNo;
    private EditText telNo;
    private TextView nameOcr;
    private TextView creditOcr;
    private TextView amount;

    private StateSelectDialog lossTypeDialog;
    private List<KeyValue> lossTypeStates = new ArrayList<>();
    private StateSelectDialog damegeTypeDialog;
    private List<KeyValue> damegeTypeStates = new ArrayList<>();
    private StateSelectDialog damegePlaceDialog;
    private List<KeyValue> damegePlaceStates = new ArrayList<>();
    private StateSelectDialog lossManDialog;
    private List<KeyValue> lossManStates = new ArrayList<>();

    private List<Integer> requestCodes = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.man_damege_detail_activity);
        x.view().inject(this);

        initView();
        initData();

    }

    private void initView(){

        content = LayoutInflater.from(this).inflate(R.layout.man_damege_detail_content, null);
        lossTypeLayout = content.findViewById(R.id.man_damege_detail_losstype_layout);
        lossType = content.findViewById(R.id.man_damege_detail_losstype);
        damegeTypeLayout = content.findViewById(R.id.man_damege_detail_damegetype_layout);
        damegeType = content.findViewById(R.id.man_damege_detail_damegetype);
        damegePlaceLayout = content.findViewById(R.id.man_damege_detail_damegeplace_layout);
        damegePlace = content.findViewById(R.id.man_damege_detail_damegeplace);
        amount = (EditText)content.findViewById(R.id.man_damege_detail_amount);
        name = content.findViewById(R.id.man_damege_detail_name);
        creditNo = content.findViewById(R.id.man_damege_detail_creditno);
        telNo = content.findViewById(R.id.man_damege_detail_telno);
        lossManLayout = content.findViewById(R.id.man_damege_detail_lossman_layout);
        lossMan = content.findViewById(R.id.man_damege_detail_lossman);
        nameOcr = content.findViewById(R.id.man_damege_detail_name_ocr);
        creditOcr = content.findViewById(R.id.man_damege_detail_creditno_ocr);

        list.addHeaderView(content);
        list.setAdapter(null);

    }

    private void initData(){

        requestCodes.add(11);
        requestCodes.add(12);
        requestCodes.add(13);
        requestCodes.add(14);
        requestCodes.add(15);
        requestCodes.add(16);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        upStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        takePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManDamegeDetailActivity.this, ImageListActivity.class);
                intent.putExtra("reportNo", "123");
                intent.putExtra("taskNo", "123");
                startActivity(intent);
            }
        });

        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        nameOcr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ManDamegeDetailActivity.this, NewSIDOcrActivity.class), 12);
            }
        });

        creditOcr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ManDamegeDetailActivity.this, NewSIDOcrActivity.class), 12);
            }
        });

        List<KeyValue> lossTypes = StateSelect.initArray(this, R.array.CZCL_value, R.array.CZCL_key);
        if (lossTypes != null && lossTypes.size() > 0) {
            lossTypeStates.clear();
            lossTypeStates.addAll(lossTypes);
        }
        lossTypeDialog = new StateSelectDialog(this, lossTypeStates, lossType);
        lossTypeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lossTypeDialog.show();
            }
        });

        List<KeyValue> damegeTypes = StateSelect.initArray(this, R.array.CZCL_value, R.array.CZCL_key);
        if (damegeTypes != null && damegeTypes.size() > 0) {
            damegeTypeStates.clear();
            damegeTypeStates.addAll(damegeTypes);
        }
        damegeTypeDialog = new StateSelectDialog(this, damegeTypeStates, damegeType);
        damegeTypeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                damegeTypeDialog.show();
            }
        });

        List<KeyValue> damegePlaces = StateSelect.initArray(this, R.array.CZCL_value, R.array.CZCL_key);
        if (damegePlaces != null && damegePlaces.size() > 0) {
            damegePlaceStates.clear();
            damegePlaceStates.addAll(damegePlaces);
        }
        damegePlaceDialog = new StateSelectDialog(this, damegePlaceStates, damegePlace);
        damegePlaceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                damegePlaceDialog.show();
            }
        });

        List<KeyValue> lossMans = StateSelect.initArray(this, R.array.CZCL_value, R.array.CZCL_key);
        if (lossMans != null && lossMans.size() > 0) {
            lossManStates.clear();
            lossManStates.addAll(lossMans);
        }
        lossManDialog = new StateSelectDialog(this, lossManStates, lossMan);
        lossManLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lossManDialog.show();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 若为识别调用请求
         */
        if (requestCodes.contains(requestCode)) {
            if (data != null) {
                OcrVO ocrVO = (OcrVO) data.getExtras().get("orcData");
                if (ocrVO != null) {
                    List<ValueForKey> values = ocrVO.getList();
                    if (values != null && values.size() > 0) {

                        Map<String, String> maps = new HashMap<>();
                        for (int i = 0; i < values.size(); i++) {
                            maps.put(values.get(i).getKey(), values.get(i).getValue());
                        }
                        String result = new Gson().toJson(maps);
                        LogUtils.e("result", "-------------------------->\n" + result);
                        name.setText(maps.get("nsName"));
                        creditNo.setText(maps.get("nsIDNum"));
                    }
                }
            }
        }
    }

}
