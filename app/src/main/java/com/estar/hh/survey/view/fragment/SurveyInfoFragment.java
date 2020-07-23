package com.estar.hh.survey.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.bigkoo.pickerview.TimePickerView;
import com.estar.hh.survey.R;
import com.estar.hh.survey.common.UserSharePrefrence;
import com.estar.hh.survey.component.StateSelectDialog;
import com.estar.hh.survey.constants.StateSelect;
import com.estar.hh.survey.entity.entity.CopyMainInfoDto;
import com.estar.hh.survey.entity.entity.CopyPolicyDto;
import com.estar.hh.survey.entity.entity.CopyReportInfoDto;
import com.estar.hh.survey.entity.entity.KeyValue;
import com.estar.hh.survey.entity.entity.SurvBaseInfoDto;
import com.estar.hh.survey.utils.LogUtils;
import com.estar.hh.survey.view.activity.CaseSurveyActivity;
import com.estar.hh.survey.view.component.MyProgressDialog;
import com.estar.ocr.common.ValueForKey;
import com.estar.ocr.common.camera.OcrVO;
import com.estar.ocr.sid.NewSIDOcrActivity;
import com.estar.utils.CommonUtils;
import com.estar.utils.DateUtil;
import com.estar.utils.KeyboardDialog;
import com.estar.utils.StringUtils;
import com.estar.utils.ToastUtils;
import com.estarview.ToggleButtonView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.estar.hh.survey.utils.TextUtils.setEditTextNoEngChar;

/**
 * Created by Administrator on 2017/9/30 0030.
 * 查勘信息
 */

public class SurveyInfoFragment extends Fragment {

    private ListView surveyList;
    private View content;

    public LinearLayout lossTypeLayout;
    public TextView lossType;//损失类别
    public LinearLayout riskReasonLayout;
    public TextView riskReason;//出险原因
    public LinearLayout classTypeLayout;
    public TextView classType;//事故类型
    public LinearLayout riskTypeLayout;
    public TextView riskType;//事故类型
    public LinearLayout riskTimeLayout;
    public TextView riskTime;//查勘日期
    public EditText sryAddress;//查勘地点
    public TextView sryIdcard;//查勘人身份证号
    public ImageView sryIdcardOCR;//查勘人身份证号证件识别
    public LinearLayout sryAddressRefresh;//查勘地点
    public LinearLayout bussinessLayout;//商业险
    public ToggleButtonView isBussiness;//是否商业责任
    public LinearLayout busDutyLayout;
    public TextView busDuty;//商业险责任
    public LinearLayout usualLayout;//交强险
    public ToggleButtonView isUsual;//是否交强险责任
    public LinearLayout usualDutyLayout;
    public ToggleButtonView usualDuty;//交强险责任
    public ToggleButtonView isReserve;//重大赔案标识
    public LinearLayout isFirstPlaceLayout;
    public TextView isFirstPlace;//是否第一现场
    public ToggleButtonView paySelf;//互碰自赔
   /* public ToggleButtonView sign;//代为标识
    public ToggleButtonView beSign;//被代为标识*/
    public EditText amount;//预估金额
    public EditText opinion;//查勘意见

    public ToggleButtonView isFloorCar;//是否水淹车
    public LinearLayout floorLevelLayout;
    public TextView floorLevel;//水淹等级
    public LinearLayout caseTypeLayout;
    public TextView caseType;
    public ToggleButtonView personAdd;//是否新增人伤

    /**
     * add by zhengyg
     * 2018年11月23日15:35:08
     * 新增控件 有无交管事故书
     */
    public ToggleButtonView haveBook;//有无交管事故书

    private StateSelectDialog lossTypeDialog;
    private List<KeyValue> lossTypeStates = new ArrayList<>();
    private StateSelectDialog riskReasonDialog;
    private List<KeyValue> riskReasonStates = new ArrayList<>();
    private StateSelectDialog riskTypeDialog;
    private List<KeyValue> riskTypeStates = new ArrayList<>();
    private StateSelectDialog busDutyDialog;
    private List<KeyValue> busDutyStates = new ArrayList<>();
    private StateSelectDialog isFirstPlaceDialog;
    private List<KeyValue> isFirstPlaceStates = new ArrayList<>();

    private StateSelectDialog floorLevelDialog;
    private List<KeyValue> floorLevelStates = new ArrayList<>();
    private StateSelectDialog classTypeDialog;
    private List<KeyValue> classTypeStates = new ArrayList<>();
    private StateSelectDialog caseTypeDialog;
    private List<KeyValue> caseTypeStates = new ArrayList<>();



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View contextView = inflater.inflate(R.layout.survey_info_fragment, container, false);

        initView(contextView);
        initData();
        initLocation();

        return contextView;
    }

    private void initView(View view) {

        surveyList = view.findViewById(R.id.survey_info_list);
        content = LayoutInflater.from(getActivity()).inflate(R.layout.survey_info_content, null);

        lossTypeLayout = content.findViewById(R.id.survey_info_loss_type_layout);
        lossType = content.findViewById(R.id.survey_info_loss_type);
        riskReasonLayout = content.findViewById(R.id.survey_info_risk_reason_layout);
        riskReason = content.findViewById(R.id.survey_info_risk_reason);
        classTypeLayout = content.findViewById(R.id.survey_info_class_type_layout);
        classType = content.findViewById(R.id.survey_info_class_type);
        riskTypeLayout = content.findViewById(R.id.survey_info_risk_type_layout);
        riskType = content.findViewById(R.id.survey_info_risk_type);
        riskTimeLayout = content.findViewById(R.id.survey_info_risktime_layout);
        riskTime = content.findViewById(R.id.survey_info_risktime);
        sryAddress = content.findViewById(R.id.survey_info_survey_address);
        sryAddressRefresh = content.findViewById(R.id.survey_info_survey_address_refresh);
        sryIdcard = content.findViewById(R.id.survey_info_creditno);
        sryIdcardOCR = content.findViewById(R.id.survey_info_creditno_ocr);
        isBussiness = content.findViewById(R.id.survey_info_isbussiness);
        busDutyLayout = content.findViewById(R.id.survey_info_bussiness_duty_layout);
        busDuty = content.findViewById(R.id.survey_info_bussiness_duty);
        isUsual = content.findViewById(R.id.survey_info_isusual);
        usualDutyLayout = content.findViewById(R.id.survey_info_usual_duty_layout);
        usualDuty = content.findViewById(R.id.survey_info_usual_duty);
        isReserve = content.findViewById(R.id.survey_info_reserveflag);
        isFirstPlaceLayout = content.findViewById(R.id.survey_info_isfirstplace_layout);
        isFirstPlace = content.findViewById(R.id.survey_info_isfirstplace);
        paySelf = content.findViewById(R.id.survey_info_payself);
     /*   sign = (ToggleButtonView) content.findViewById(R.id.survey_info_sign);
        beSign = (ToggleButtonView) content.findViewById(R.id.survey_info_besign);*/
        opinion = content.findViewById(R.id.survey_info_survey_optition);
        amount = content.findViewById(R.id.survey_info_survey_amount);

        bussinessLayout = content.findViewById(R.id.survey_info_bussiness_layout);
        usualLayout = content.findViewById(R.id.survey_info_usual_layout);

        personAdd = content.findViewById(R.id.survey_info_personadd);
        isFloorCar = content.findViewById(R.id.survey_info_isFloordedCar);
        floorLevelLayout = content.findViewById(R.id.survey_info_floorlevel_layout);
        floorLevel = content.findViewById(R.id.survey_info_floorlevel);

        caseTypeLayout = content.findViewById(R.id.survey_info_case_type_layout);
        caseType = content.findViewById(R.id.survey_info_case_type);

        /**
         * add by zhengyg
         * 2018年11月23日15:17:19
         * 新增 北分所需字段，有无交管事故书
         */
        haveBook = content.findViewById(R.id.survey_info_haveBook);
        /**
         * 设置录入规则
         */
        setEditTextNoEngChar(sryAddress);
        setEditTextNoEngChar(opinion);

        sryIdcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //易星身份证号输入
                CommonUtils.showKeyboardDialog(getActivity() ,sryIdcard,"身份证号:", KeyboardDialog.INPUT_TYPE.IDNUMBER);
            }
        });

        personAdd.setOnToggleChanged(new ToggleButtonView.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    ((CaseSurveyActivity) getActivity()).survBaseInfoDto.setIsPerson("1");
                } else {
                    ((CaseSurveyActivity) getActivity()).survBaseInfoDto.setIsPerson("0");
                }
            }
        });


        haveBook.setOnToggleChanged(new ToggleButtonView.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    ((CaseSurveyActivity) getActivity()).survBaseInfoDto.setHaveBook("1");
                } else {
                    ((CaseSurveyActivity) getActivity()).survBaseInfoDto.setHaveBook("0");
                }
            }
        });


        isFloorCar.setOnToggleChanged(new ToggleButtonView.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    ((CaseSurveyActivity) getActivity()).survBaseInfoDto.setIsFloodedCar("1");
                    floorLevelLayout.setVisibility(View.VISIBLE);
                } else {
                    ((CaseSurveyActivity) getActivity()).survBaseInfoDto.setIsFloodedCar("0");
                    floorLevel.setText("");
                    floorLevel.setTag("");
                    floorLevelLayout.setVisibility(View.GONE);
                }
            }
        });

        isBussiness.setOnToggleChanged(new ToggleButtonView.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    ((CaseSurveyActivity) getActivity()).survBaseInfoDto.setClaimFlag("1");
                    busDutyLayout.setVisibility(View.VISIBLE);
                } else {
                    ((CaseSurveyActivity) getActivity()).survBaseInfoDto.setClaimFlag("0");
                    busDuty.setText("无责");
                    busDuty.setTag("4");
                    busDutyLayout.setVisibility(View.GONE);
                }
            }
        });

        isUsual.setOnToggleChanged(new ToggleButtonView.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    ((CaseSurveyActivity) getActivity()).survBaseInfoDto.setCiIndemDuty("1");
                    usualDutyLayout.setVisibility(View.VISIBLE);
                } else {
                    ((CaseSurveyActivity) getActivity()).survBaseInfoDto.setCiIndemDuty("0");
                    usualDuty.setToggleOff();
                    usualDutyLayout.setVisibility(View.GONE);
                }
            }
        });

        usualDuty.setOnToggleChanged(new ToggleButtonView.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    ((CaseSurveyActivity) getActivity()).survBaseInfoDto.setCiDutyFlag("100");
                } else {
                    ((CaseSurveyActivity) getActivity()).survBaseInfoDto.setCiDutyFlag("0");
                }
            }
        });

        isReserve.setOnToggleChanged(new ToggleButtonView.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    ((CaseSurveyActivity) getActivity()).survBaseInfoDto.setReserveFlag("1");
                } else {
                    ((CaseSurveyActivity) getActivity()).survBaseInfoDto.setReserveFlag("0");
                }
            }
        });

        paySelf.setOnToggleChanged(new ToggleButtonView.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    ((CaseSurveyActivity) getActivity()).survBaseInfoDto.setCaseFlag("1");
                } else {
                    ((CaseSurveyActivity) getActivity()).survBaseInfoDto.setCaseFlag("0");
                }
            }
        });

        /*sign.setOnToggleChanged(new ToggleButtonView.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    ((CaseSurveyActivity) getActivity()).survBaseInfoDto.setIsSubrogationMark("1");
                } else {
                    ((CaseSurveyActivity) getActivity()).survBaseInfoDto.setIsSubrogationMark("0");
                }
            }
        });

        beSign.setOnToggleChanged(new ToggleButtonView.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    ((CaseSurveyActivity) getActivity()).survBaseInfoDto.setIsCoverSubrogationMark("1");
                } else {
                    ((CaseSurveyActivity) getActivity()).survBaseInfoDto.setIsCoverSubrogationMark("0");
                }
            }
        });*/

        sryIdcardOCR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OCRstart = true;
                startActivityForResult(new Intent(getActivity(), NewSIDOcrActivity.class), 12);
            }
        });

        isBussiness.setToggleOff();
        isUsual.setToggleOff();

        surveyList.addHeaderView(content);
        surveyList.setAdapter(null);

        initRiskLayout();

    }


    private boolean isBeifen = false;

    /**
     * 根据保单信息判断是否需要隐藏交强险或商业险
     */
    private void initRiskLayout(){

        boolean bussInsFlag = false;//是否有商业险保单
        boolean usualInsFlag = false;//是否有交强险保单

        if (((CaseSurveyActivity) getActivity()).copyMainInfoDto != null) {
            List<CopyPolicyDto> policys = ((CaseSurveyActivity) getActivity()).copyMainInfoDto.getListPolicy();
            if (policys != null && policys.size() > 0){
                for (CopyPolicyDto copyPolicyDto : policys){
                    if (copyPolicyDto.getPolicyType().contains("交强")){
                        usualInsFlag = true;
                    }

                    if (copyPolicyDto.getPolicyType().contains("商业")){
                        bussInsFlag = true;
                    }
                }

                if (!bussInsFlag){
                    bussinessLayout.setVisibility(View.GONE);
                }else {
                    bussinessLayout.setVisibility(View.VISIBLE);
                    ((CaseSurveyActivity) getActivity()).survBaseInfoDto.setClaimFlag("1");
                }

                if (!usualInsFlag){
                    usualLayout.setVisibility(View.GONE);
                }else {
                    usualLayout.setVisibility(View.VISIBLE);
                    ((CaseSurveyActivity) getActivity()).survBaseInfoDto.setCiIndemDuty("1");
                    ((CaseSurveyActivity) getActivity()).survBaseInfoDto.setCiDutyFlag("100");
                }
            }
        }
    }

    private TimePickerView pvTime;//时间选择器
    private Date nowTime;
    private List<Integer> requestCodes = new ArrayList<>();

    public boolean OCRstart = false;

    private void initData() {

        requestCodes.add(11);
        requestCodes.add(12);
        requestCodes.add(13);
        requestCodes.add(14);
        requestCodes.add(15);
        requestCodes.add(16);

        List<KeyValue> lossTypes = StateSelect.initArray(getActivity(), R.array.SSLB_value, R.array.SSLB_key);
        if (lossTypes != null && lossTypes.size() > 0) {
            lossTypeStates.clear();
            lossTypeStates.addAll(lossTypes);
        }
        lossTypeDialog = new StateSelectDialog(getActivity(), lossTypeStates, lossType);
        lossTypeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lossTypeDialog.show();
            }
        });


        List<KeyValue> riskReasons = StateSelect.initArray(getActivity(), R.array.CXYY_value, R.array.CXYY_key);
        if (riskReasons != null && riskReasons.size() > 0) {
            riskReasonStates.clear();
            riskReasonStates.addAll(riskReasons);
        }
        riskReasonDialog = new StateSelectDialog(getActivity(), riskReasonStates, riskReason);
        riskReasonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                riskReasonDialog.show();
            }
        });


        List<KeyValue> riskTypes = StateSelect.initArray(getActivity(), R.array.SGLX_value, R.array.SGLX_key);
        if (riskTypes != null && riskTypes.size() > 0) {
            riskTypeStates.clear();
            riskTypeStates.addAll(riskTypes);
        }
        riskTypeDialog = new StateSelectDialog(getActivity(), riskTypeStates, riskType);
        riskTypeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                riskTypeDialog.show();
            }
        });


        List<KeyValue> busDutys = StateSelect.initArray(getActivity(), R.array.SYXZR_value, R.array.SYXZR_key);
        if (busDutys != null && busDutys.size() > 0) {
            busDutyStates.clear();
            busDutyStates.addAll(busDutys);
        }
        busDutyDialog = new StateSelectDialog(getActivity(), busDutyStates, busDuty);
        busDutyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                busDutyDialog.show();
            }
        });

        List<KeyValue> floorLevels = StateSelect.initArray(getActivity(), R.array.SYDJ_value, R.array.SYDJ_key);
        if (floorLevels != null && floorLevels.size() > 0) {
            floorLevelStates.clear();
            floorLevelStates.addAll(floorLevels);
        }
        floorLevelDialog = new StateSelectDialog(getActivity(), floorLevelStates, floorLevel);
        floorLevelLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floorLevelDialog.show();
            }
        });

        List<KeyValue> classTypes = StateSelect.initArray(getActivity(), R.array.BXSGFL_value, R.array.BXSGFL_key);
        if (classTypes != null && classTypes.size() > 0) {
            classTypeStates.clear();
            classTypeStates.addAll(classTypes);
        }
        classTypeDialog = new StateSelectDialog(getActivity(), classTypeStates, classType);
        classTypeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classTypeDialog.show();
            }
        });


        List<KeyValue> isFirstPlaces = StateSelect.initArray(getActivity(), R.array.DYXC_value, R.array.DYXC_key);
        if (isFirstPlaces != null && isFirstPlaces.size() > 0) {
            isFirstPlaceStates.clear();
            isFirstPlaceStates.addAll(isFirstPlaces);
        }
        isFirstPlaceDialog = new StateSelectDialog(getActivity(), isFirstPlaceStates, isFirstPlace);
        isFirstPlaceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFirstPlaceDialog.show();
            }
        });

        List<KeyValue> caseTypes = StateSelect.initArray(getActivity(), R.array.SGYY_value, R.array.SGYY_key);
        if (caseTypes != null && caseTypes.size() > 0) {
            caseTypeStates.clear();
            caseTypeStates.addAll(caseTypes);
        }
        caseTypeDialog = new StateSelectDialog(getActivity(), caseTypeStates, caseType);
        caseTypeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                caseTypeDialog.show();
            }
        });

        nowTime = new Date();

        //时间选择器
        pvTime = new TimePickerView(getActivity(), TimePickerView.Type.YEAR_MONTH_DAY);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                if (date.after(nowTime)) {
                    ToastUtils.showShort(getActivity(), "时间不能晚于当前日期");
                    return;
                }

                if (!StringUtils.isEmpty(((CaseSurveyActivity) getActivity()).copyMainInfoDto.getCopyReportInfo().getDamageDate())
                        && date.before(DateUtil.getDate(((CaseSurveyActivity) getActivity()).copyMainInfoDto.getCopyReportInfo().getDamageDate()))){
                    ToastUtils.showShort(getActivity(), "时间不能早于出险日期");
                    return;
                }

                riskTime.setText(DateUtil.getTime(date));
            }
        });
        riskTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvTime.setTime(TimePickerView.Type.YEAR_MONTH_DAY, DateUtil.getDate(riskTime.getText().toString()));
                pvTime.show();
            }
        });


        /**
         * 0111（北分公司特殊处理）
         */
        CopyReportInfoDto copyReportInfo = ((CaseSurveyActivity) getActivity()).copyMainInfoDto.getCopyReportInfo();
        if (!StringUtils.isEmpty(copyReportInfo.getComCode()) && copyReportInfo.getComCode().startsWith("0111")){
            isBeifen = true;
            caseTypeLayout.setVisibility(View.VISIBLE);
            /**
             * add by zhengyg
             * 2018年11月23日15:17:19
             * 新增 北分所需字段，有无交管事故书
             */
            haveBook.setVisibility(View.VISIBLE);
        }


        /**
         * 初始化界面
         */
        SurvBaseInfoDto survBaseInfoDto = ((CaseSurveyActivity) getActivity()).survBaseInfoDto;
        {
            lossType.setTag(survBaseInfoDto.getLossType());
            lossType.setText(StateSelect.getTextValue(survBaseInfoDto.getLossType(), lossTypeStates));

            riskReason.setTag(survBaseInfoDto.getDamageCode());
            riskReason.setText(StateSelect.getTextValue(survBaseInfoDto.getDamageCode(), riskReasonStates));

            riskType.setTag(survBaseInfoDto.getDamageCaseCode());
            riskType.setText(StateSelect.getTextValue(survBaseInfoDto.getDamageCaseCode(), riskTypeStates));

            amount.setText(survBaseInfoDto.getSumLossFee());

            sryAddress.setText(survBaseInfoDto.getCheckSite());
            if (!StringUtils.isEmpty(survBaseInfoDto.getClaimFlag()) && survBaseInfoDto.getClaimFlag().equals("1")) {
                isBussiness.setToggleOn();
                busDutyLayout.setVisibility(View.VISIBLE);
            } else {
                isBussiness.setToggleOff();
                busDutyLayout.setVisibility(View.GONE);
            }

            busDuty.setTag(survBaseInfoDto.getIndemnityDuty());
            busDuty.setText(StateSelect.getTextValue(survBaseInfoDto.getIndemnityDuty(), busDutyStates));

            isFirstPlace.setTag(survBaseInfoDto.getFirstSiteFlagQuery());
            isFirstPlace.setText(StateSelect.getTextValue(survBaseInfoDto.getFirstSiteFlagQuery(), isFirstPlaceStates));

            caseType.setTag(survBaseInfoDto.getDamageTypeCode());
            caseType.setText(StateSelect.getTextValue(survBaseInfoDto.getDamageTypeCode(), caseTypeStates));

            classType.setTag(survBaseInfoDto.getInsureAccident());
            classType.setText(StateSelect.getTextValue(survBaseInfoDto.getInsureAccident(), classTypeStates));

            floorLevel.setTag(survBaseInfoDto.getFloodedLevel());
            floorLevel.setText(StateSelect.getTextValue(survBaseInfoDto.getFloodedLevel(), floorLevelStates));

            if (StringUtils.isEmpty(survBaseInfoDto.getCheckDate())) {
                riskTime.setText(survBaseInfoDto.getCheckDate());
            }else {
                riskTime.setText(DateUtil.getTime(nowTime));
            }

            if (!StringUtils.isEmpty(survBaseInfoDto.getChecker1IdentNum())) {
                sryIdcard.setText(survBaseInfoDto.getChecker1IdentNum());
            }else {
                sryIdcard.setText(new UserSharePrefrence(getActivity()).getUserEntity().getUserIdNo());
            }

            if (!StringUtils.isEmpty(survBaseInfoDto.getIsFloodedCar()) && survBaseInfoDto.getIsFloodedCar().equals("1")) {
                isFloorCar.setToggleOn();
                floorLevelLayout.setVisibility(View.VISIBLE);
            } else {
                isFloorCar.setToggleOff();
                floorLevelLayout.setVisibility(View.GONE);
            }

            if (!StringUtils.isEmpty(survBaseInfoDto.getReserveFlag()) && survBaseInfoDto.getReserveFlag().equals("1")) {
                isReserve.setToggleOn();
            } else {
                isReserve.setToggleOff();
            }

            if (!StringUtils.isEmpty(survBaseInfoDto.getIsPerson()) && survBaseInfoDto.getIsPerson().equals("1")) {
                personAdd.setToggleOn();
            } else {
                personAdd.setToggleOff();
            }

            if (!StringUtils.isEmpty(survBaseInfoDto.getCiIndemDuty()) && survBaseInfoDto.getCiIndemDuty().equals("1")) {
                isUsual.setToggleOn();
                usualDutyLayout.setVisibility(View.VISIBLE);
            } else {
                isUsual.setToggleOff();
                usualDutyLayout.setVisibility(View.GONE);
            }
            if (!StringUtils.isEmpty(survBaseInfoDto.getCiDutyFlag()) && survBaseInfoDto.getCiDutyFlag().equals("100")) {
                usualDuty.setToggleOn();
            } else {
                usualDuty.setToggleOff();
            }
            if (!StringUtils.isEmpty(survBaseInfoDto.getCaseFlag()) && survBaseInfoDto.getCaseFlag().equals("1")) {
                paySelf.setToggleOn();
            } else {
                paySelf.setToggleOff();
            }
          /*  if (!StringUtils.isEmpty(survBaseInfoDto.getIsSubrogationMark()) && survBaseInfoDto.getIsSubrogationMark().equals("1")) {
                sign.setToggleOn();
            } else {
                sign.setToggleOff();
            }
            if (!StringUtils.isEmpty(survBaseInfoDto.getIsCoverSubrogationMark()) && survBaseInfoDto.getIsCoverSubrogationMark().equals("1")) {
                beSign.setToggleOn();
            } else {
                beSign.setToggleOff();
            }*/
            if(!StringUtils.isEmpty(survBaseInfoDto.getCheckSite())){
                sryAddress.setText(survBaseInfoDto.getCheckSite());
            }

            if (!StringUtils.isEmpty(survBaseInfoDto.getHaveBook()) && survBaseInfoDto.getHaveBook().equals("1")) {
                haveBook.setToggleOn();
            } else {
                haveBook.setToggleOff();
            }

            opinion.setText(survBaseInfoDto.getContext());
        }

    }

//    private MyProgressDialog addressDialog = null;
    private LocationClient mLocationClient = null;

    private void initLocation() {

        mLocationClient = new LocationClient(getActivity());
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
//        int span=10*1000;
//        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
//        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果(目前这种方式使setScanSpan有效)
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(new MyLocationListener());
        mLocationClient.start();

//        /**
//         * 若地址为空则自动刷新
//         */
//        if (StringUtils.isEmpty(sryAddress.getText().toString())){
//            addressDialog =  new MyProgressDialog(getActivity(), "正在刷新地理位置");
//        }

        sryAddressRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                addressDialog = new MyProgressDialog(getActivity(), "正在刷新地理位置", true);
                mLocationClient.requestLocation();
            }
        });

    }

    private boolean isFirstAddrRefresh = true;

    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {

//            if (addressDialog != null) {
//                addressDialog.stopDialog();
//            }

            if (bdLocation.getLocType() != BDLocation.TypeServerError) {
                LogUtils.i("locationUploadService", "定位成功");
                LogUtils.i("locationUploadService", "--------------->latitude:" + bdLocation.getLatitude() + "    longitude:" + bdLocation.getLongitude());

                if (bdLocation != null) {
                    if (isFirstAddrRefresh){
                        isFirstAddrRefresh = false;
                        return;
                    }
                    sryAddress.setText(bdLocation.getAddrStr());
                } else {
                    ToastUtils.showShort(getActivity(), "未找到当前位置");
                }

            } else if (bdLocation.getLocType() == BDLocation.TypeCriteriaException) {
                //当前缺少定位依据，可能是用户没有授权，建议弹出提示框让用户开启权限
                //可进一步参考onLocDiagnosticMessage中的错误返回码
                ToastUtils.showShort(getActivity(), "地理位置更新失败");
            } else {
                ToastUtils.showShort(getActivity(), "地理位置更新失败");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.stop();
            mLocationClient = null;
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
                        sryIdcard.setText(maps.get("nsIDNum"));
                    }
                }
            }
        }
    }

}
