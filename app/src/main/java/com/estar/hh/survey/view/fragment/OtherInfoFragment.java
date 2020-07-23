package com.estar.hh.survey.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.estar.hh.survey.R;
import com.estar.hh.survey.component.StateSelectDialog;
import com.estar.hh.survey.constants.StateSelect;
import com.estar.hh.survey.entity.entity.CarLossInfoDto;
import com.estar.hh.survey.entity.entity.CopyReportInfoDto;
import com.estar.hh.survey.entity.entity.KeyValue;
import com.estar.hh.survey.utils.LogUtils;
import com.estar.hh.survey.view.activity.CaseSurveyActivity;
import com.estar.hh.survey.view.component.MultiSelectDialog;
import com.estar.ocr.common.ValueForKey;
import com.estar.ocr.common.camera.OcrVO;
import com.estar.ocr.dl.NewDLOcrActivity;
import com.estar.utils.CommonUtils;
import com.estar.utils.KeyboardDialog;
import com.estar.utils.StringUtils;
import com.estarview.ToggleButtonView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.estar.hh.survey.utils.TextUtils.setEditTextOnlyForWordAndChar;

/**
 * Created by Administrator on 2017/9/30 0030.
 * 标的信息
 */

public class OtherInfoFragment extends Fragment {

    private ListView otherList;

    private View content;

    public LinearLayout carNoLayout;
    public TextView carNo;
    public TextView model;
    public LinearLayout carNoTypeLayout;
    public TextView carNoType;
    public LinearLayout carTypeLayout;
    public TextView carType;
    public EditText name;
    public TextView nameOcr;
    public TextView creditNo;
    public TextView creditOcr;
    public ToggleButtonView isLoss;
    public ToggleButtonView repairFlag;
    public ToggleButtonView reserveFlag;
    public ToggleButtonView isMiss;
    public EditText amount;

    public LinearLayout dutyTypeLayout;
    public TextView dutyType;
    public LinearLayout ciIndemDutyLayout;
    public TextView ciIndemDuty;
    public LinearLayout noPushReasonLayout;
    public TextView noPushReason;
    public LinearLayout lossPartLayout;
    public TextView lossPart;

    private StateSelectDialog carNoTypeDialog;
    private List<KeyValue> carNoTypeStates = new ArrayList<>();

    private StateSelectDialog dutyTypeDialog;
    private List<KeyValue> dutyTypeStates = new ArrayList<>();
    private StateSelectDialog ciIndemDutyDialog;
    private List<KeyValue> ciIndemDutyStates = new ArrayList<>();
    private StateSelectDialog noPushReasonDialog;
    private List<KeyValue> noPushReasonStates = new ArrayList<>();
    private StateSelectDialog carTypeDialog;
    private List<KeyValue> carTypeStates = new ArrayList<>();
//    private StateSelectDialog lossPartDialog;
    private MultiSelectDialog lossPartDialog;
    private List<KeyValue> lossPartStates = new ArrayList<>();


    private List<Integer> requestCodes = new ArrayList<>();

    private boolean isBeifen = false;

    public LinearLayout frameNoLayout;
    public TextView frameNo;
    public LinearLayout engineNoLayout;
    public TextView engineNo;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View contextView = inflater.inflate(R.layout.other_info_fragment, container, false);

        initView(contextView);
        initData();

        return contextView;
    }


    private void initView(View view){

        otherList = view.findViewById(R.id.other_info_list);
        content = LayoutInflater.from(getActivity()).inflate(R.layout.other_info_content, null);

        /**
         * 添加标的车 车架号、发动机号的展示
         */
        frameNoLayout= content.findViewById(R.id.other_info_frameno_layout);
        frameNo = content.findViewById(R.id.other_info_frameno);
        engineNoLayout= content.findViewById(R.id.other_info_engineno_layout);
        engineNo = content.findViewById(R.id.other_info_engineno);

        carNoLayout = content.findViewById(R.id.other_info_carno_layout);
        carNo = content.findViewById(R.id.other_info_carno);
        model = content.findViewById(R.id.other_info_model);
        carNoTypeLayout = content.findViewById(R.id.other_info_carnotype_layout);
        carNoType = content.findViewById(R.id.other_info_carnotype);
        carTypeLayout = content.findViewById(R.id.other_info_cartype_layout);
        carType = content.findViewById(R.id.other_info_cartype);
        name = content.findViewById(R.id.other_info_name);
        nameOcr = content.findViewById(R.id.other_info_name_ocr);
        creditNo = content.findViewById(R.id.other_info_creditno);
        creditOcr = content.findViewById(R.id.other_info_creditno_ocr);
        isLoss = content.findViewById(R.id.other_info_isloss);
        repairFlag = content.findViewById(R.id.other_info_repairFlag);
        reserveFlag = content.findViewById(R.id.other_info_reserveFlag);
        isMiss = content.findViewById(R.id.other_info_ismiss);
        amount = content.findViewById(R.id.other_info_amount);

        dutyTypeLayout = content.findViewById(R.id.other_info_dutyType_layout);
        dutyType = content.findViewById(R.id.other_info_dutyType);
        ciIndemDutyLayout = content.findViewById(R.id.other_info_ciIndemDuty_layout);
        ciIndemDuty = content.findViewById(R.id.other_info_ciIndemDuty);
        noPushReasonLayout = content.findViewById(R.id.other_info_noPushReason_layout);
        noPushReason = content.findViewById(R.id.other_info_noPushReason);
        lossPartLayout = content.findViewById(R.id.other_info_lossPart_layout);
        lossPart = content.findViewById(R.id.other_info_lossPart);

        dutyTypeLayout.setVisibility(View.GONE);
        ciIndemDutyLayout.setVisibility(View.GONE);

        /**
         * 录入规则
         */
        setEditTextOnlyForWordAndChar(name);

        reserveFlag.setOnToggleChanged(new ToggleButtonView.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on){
                    ((CaseSurveyActivity)getActivity()).carLossInfoDto.setReserveFlag("1");
                }else {
                    ((CaseSurveyActivity)getActivity()).carLossInfoDto.setReserveFlag("0");
                }
            }
        });

        isLoss.setOnToggleChanged(new ToggleButtonView.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on){
                    ((CaseSurveyActivity)getActivity()).carLossInfoDto.setDamageFlag("1");
                }else {
                    ((CaseSurveyActivity)getActivity()).carLossInfoDto.setDamageFlag("0");
                }
            }
        });

        repairFlag.setOnToggleChanged(new ToggleButtonView.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on){
                    ((CaseSurveyActivity)getActivity()).carLossInfoDto.setRepairFlag("1");
                    noPushReasonLayout.setVisibility(View.GONE);
                    noPushReason.setText("");
                    noPushReason.setTag("");
                }else {
                    ((CaseSurveyActivity)getActivity()).carLossInfoDto.setRepairFlag("0");
                    noPushReasonLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        isMiss.setOnToggleChanged(new ToggleButtonView.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on){
                    ((CaseSurveyActivity)getActivity()).carLossInfoDto.setIsFoundOther("1");
                }else {
                    ((CaseSurveyActivity)getActivity()).carLossInfoDto.setIsFoundOther("0");
                }
            }
        });

        otherList.addHeaderView(content);
        otherList.setAdapter(null);

    }

    public boolean OCRstart = false;

    private void initData(){

        requestCodes.add(11);
        requestCodes.add(12);
        requestCodes.add(13);
        requestCodes.add(14);
        requestCodes.add(15);
        requestCodes.add(16);

        creditNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //易星身份证号输入
                CommonUtils.showKeyboardDialog(getActivity() ,creditNo,"身份证号:", KeyboardDialog.INPUT_TYPE.IDNUMBER);
            }
        });

        carNoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //易星纯数字号输入
                CommonUtils.showKeyboardDialog(getActivity() ,carNo,"车牌号:", KeyboardDialog.INPUT_TYPE.CARNO);
            }
        });

        nameOcr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OCRstart = true;
//                startActivityForResult(new Intent(getActivity(), NewSIDOcrActivity.class), 12);
                startActivityForResult(new Intent(getActivity(), NewDLOcrActivity.class), 11);
            }
        });

        creditOcr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OCRstart = true;
//                startActivityForResult(new Intent(getActivity(), NewSIDOcrActivity.class), 12);
                startActivityForResult(new Intent(getActivity(), NewDLOcrActivity.class), 11);
            }
        });

        List<KeyValue> carNoTypes = StateSelect.initArray(getActivity(), R.array.HPZL_value, R.array.HPZL_key);
        if (carNoTypes != null && carNoTypes.size() > 0) {
            carNoTypeStates.clear();
            carNoTypeStates.addAll(carNoTypes);
        }
        carNoTypeDialog = new StateSelectDialog(getActivity(), carNoTypeStates, carNoType);
        carNoTypeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carNoTypeDialog.show();
            }
        });

        List<KeyValue> carTypes = StateSelect.initArray(getActivity(), R.array.CLZL_value, R.array.CLZL_key);
        if (carTypes != null && carTypes.size() > 0) {
            carTypeStates.clear();
            carTypeStates.addAll(carTypes);
        }
        carTypeDialog = new StateSelectDialog(getActivity(), carTypeStates, carType);
        carTypeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carTypeDialog.show();
            }
        });

        List<KeyValue> dutyTypes = StateSelect.initArray(getActivity(), R.array.SYXZR_value, R.array.SYXZR_key);
        if (dutyTypes != null && dutyTypes.size() > 0) {
            dutyTypeStates.clear();
            dutyTypeStates.addAll(dutyTypes);
        }
        dutyTypeDialog = new StateSelectDialog(getActivity(), dutyTypeStates, dutyType);
        dutyTypeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dutyTypeDialog.show();
            }
        });

        List<KeyValue> ciIndemDutys = StateSelect.initArray(getActivity(), R.array.JQXZR_value, R.array.JQXZR_key);
        if (ciIndemDutys != null && ciIndemDutys.size() > 0) {
            ciIndemDutyStates.clear();
            ciIndemDutyStates.addAll(ciIndemDutys);
        }
        ciIndemDutyDialog = new StateSelectDialog(getActivity(), ciIndemDutyStates, ciIndemDuty);
        ciIndemDutyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ciIndemDutyDialog.show();
            }
        });

        List<KeyValue> noPushReasons = StateSelect.initArray(getActivity(), R.array.BTXYY_value, R.array.BTXYY_key);
        if (noPushReasons != null && noPushReasons.size() > 0) {
            noPushReasonStates.clear();
            noPushReasonStates.addAll(noPushReasons);
        }
        noPushReasonDialog = new StateSelectDialog(getActivity(), noPushReasonStates, noPushReason);
        noPushReasonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noPushReasonDialog.show();
            }
        });

        List<KeyValue> lossParts = StateSelect.initArray(getActivity(), R.array.SSBW_value, R.array.SSBW_key);
        if (lossParts != null && lossParts.size() > 0) {
            lossPartStates.clear();
            lossPartStates.addAll(lossParts);
        }


        CarLossInfoDto carLossInfoDto = ((CaseSurveyActivity)getActivity()).carLossInfoDto;
        {

            /**
             * 新增标的车 发动机号、车架号的信息初始
             * add by zhengyg 2018年12月6日17:32:52
             */
            engineNo.setText(carLossInfoDto.getEngineNo());
            frameNo.setText(carLossInfoDto.getFrameNo());

            carNo.setText(carLossInfoDto.getLicenseNo());
            model.setText(carLossInfoDto.getBrandName());

            carNoType.setTag(carLossInfoDto.getLicenseNoType());
            carNoType.setText(StateSelect.getTextValue(carLossInfoDto.getLicenseNoType(), carNoTypeStates));

            carType.setTag(carLossInfoDto.getCarKindCode());
            carType.setText(StateSelect.getTextValue(carLossInfoDto.getCarKindCode(), carTypeStates));

            name.setText(carLossInfoDto.getDriverName());
            creditNo.setText(carLossInfoDto.getIdentifyNumber());
            if (!StringUtils.isEmpty(carLossInfoDto.getDamageFlag()) && carLossInfoDto.getDamageFlag().equals("1")){
                isLoss.setToggleOn();
            }else {
                isLoss.setToggleOff();
            }
            if (!StringUtils.isEmpty(carLossInfoDto.getIsFoundOther()) && carLossInfoDto.getIsFoundOther().equals("1")){
                isMiss.setToggleOn();
            }else {
                isMiss.setToggleOff();
            }
            amount.setText(carLossInfoDto.getExceptSumLossFee());

            ciIndemDuty.setTag(carLossInfoDto.getCiIndemDuty());
            ciIndemDuty.setText(StateSelect.getTextValue(carLossInfoDto.getCiIndemDuty(), ciIndemDutyStates));

            noPushReason.setTag(carLossInfoDto.getNoPushReason());
            noPushReason.setText(StateSelect.getTextValue(carLossInfoDto.getNoPushReason(), noPushReasonStates));
            if (!StringUtils.isEmpty(carLossInfoDto.getRepairFlag()) && carLossInfoDto.getRepairFlag().equals("1")){
                repairFlag.setToggleOn();
                noPushReasonLayout.setVisibility(View.GONE);
                noPushReason.setText("");
                noPushReason.setTag("");
            }else {
                repairFlag.setToggleOff();
                noPushReasonLayout.setVisibility(View.VISIBLE);
            }

            if (!StringUtils.isEmpty(carLossInfoDto.getReserveFlag()) && carLossInfoDto.getReserveFlag().equals("1")){
                reserveFlag.setToggleOn();
            }else {
                reserveFlag.setToggleOff();
            }

            lossPart.setTag(carLossInfoDto.getLossPart());
            lossPart.setText(StateSelect.getMultiTextValue(carLossInfoDto.getLossPart(), lossPartStates));

            dutyType.setTag(carLossInfoDto.getDutyType());
            dutyType.setText(StateSelect.getTextValue(carLossInfoDto.getDutyType(), dutyTypeStates));

        }

        lossPartDialog = new MultiSelectDialog(getActivity(), lossPartStates, lossPart);
        lossPartLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lossPartDialog.show();
            }
        });

        /**
         * 0111（北分公司特殊处理）
         */
        CopyReportInfoDto copyReportInfo = ((CaseSurveyActivity) getActivity()).copyMainInfoDto.getCopyReportInfo();
        System.out.print("获取comCode值====="+copyReportInfo.getComCode());
        if (!StringUtils.isEmpty(copyReportInfo.getComCode()) && copyReportInfo.getComCode().startsWith("0111")){
            isBeifen = true;
            carTypeLayout.setVisibility(View.GONE);
        }

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
//                        name.setText(maps.get("nsName"));
//                        creditNo.setText(maps.get("nsIDNum"));
                        name.setText(maps.get("姓名:"));
                        creditNo.setText(maps.get("证号:"));
                    }
                }
            }
        }
    }
}
