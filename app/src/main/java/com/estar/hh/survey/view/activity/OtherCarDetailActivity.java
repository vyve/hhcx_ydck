package com.estar.hh.survey.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.estar.hh.survey.R;
import com.estar.hh.survey.component.StateSelectDialog;
import com.estar.hh.survey.constants.StateSelect;
import com.estar.hh.survey.entity.entity.CarLossInfoDto;
import com.estar.hh.survey.entity.entity.KeyValue;
import com.estar.hh.survey.utils.LogUtils;
import com.estar.hh.survey.view.component.MultiSelectDialog;
import com.estar.ocr.common.ValueForKey;
import com.estar.ocr.common.camera.OcrVO;
import com.estar.ocr.dl.NewDLOcrActivity;
import com.estar.ocr.vl.NewVLOcrActivity;
import com.estar.utils.CommonUtils;
import com.estar.utils.DateUtil;
import com.estar.utils.KeyboardDialog;
import com.estar.utils.StringUtils;
import com.estar.utils.ToastUtils;
import com.estarview.ToggleButtonView;
import com.google.gson.Gson;

import org.litepal.crud.DataSupport;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.estar.hh.survey.entity.entity.CarLossInfoDto.LOSSTYPEOTHERCAR;
import static com.estar.hh.survey.utils.TextUtils.setEditTextForChar;
import static com.estar.hh.survey.utils.TextUtils.setEditTextOnlyForNumberAndChar;
import static com.estar.hh.survey.utils.TextUtils.setEditTextOnlyForWordAndChar;

/**
 * Created by Administrator on 2017/9/30 0030.
 * 三者车详情
 */

public class OtherCarDetailActivity extends HuangheBaseActivity {

    @ViewInject(R.id.other_car_detail_back)
    private LinearLayout back;

    @ViewInject(R.id.other_car_detail_list)
    private ListView otherList;

    @ViewInject(R.id.other_car_detail_up_step)
    private LinearLayout upStep;

    @ViewInject(R.id.other_car_detail_take_pic)
    private LinearLayout takePic;

    @ViewInject(R.id.other_car_detail_next_step)
    private LinearLayout nextStep;

    private View content;

    private LinearLayout carNoLayout;
    private TextView carNo;
    private EditText model;
    private LinearLayout carNoTypeLayout;
    private TextView carNoType;
    public LinearLayout carTypeLayout;
    public TextView carType;
    private EditText name;
    private TextView nameOcr;
    private TextView creditNo;
    private TextView creditOcr;
    private ToggleButtonView hasDuty;
    private ToggleButtonView isLoss;
    private EditText amount;

    private EditText engineNo;
    private TextView frameNo;

    private TextView engineNoOcr;
    private TextView frameNoOcr;

    public ToggleButtonView repairFlag;
    public ToggleButtonView reserveFlag;
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
    //    private StateSelectDialog lossPartDialog;
    private MultiSelectDialog lossPartDialog;
    private List<KeyValue> lossPartStates = new ArrayList<>();
    private StateSelectDialog carTypeDialog;
    private List<KeyValue> carTypeStates = new ArrayList<>();


    private List<Integer> requestCodes = new ArrayList<>();

    private CarLossInfoDto car = null;
    private String reportNo = null;
    private String taskNo = null;

    private boolean isBeifen = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_car_detail_activity);
        x.view().inject(this);

        initView();
        initData();

    }


    private void initView() {


        content = LayoutInflater.from(this).inflate(R.layout.other_car_detail_content, null);

        carNoLayout = content.findViewById(R.id.other_car_detail_carno_layout);
        carNo = content.findViewById(R.id.other_car_detail_carno);
        model = content.findViewById(R.id.other_car_detail_model);
        carNoTypeLayout = content.findViewById(R.id.other_car_detail_carnotype_layout);
        carNoType = content.findViewById(R.id.other_car_detail_carnotype);
        carTypeLayout = content.findViewById(R.id.other_car_detail_cartype_layout);
        carType = content.findViewById(R.id.other_car_detail_cartype);
        name = content.findViewById(R.id.other_car_detail_name);
        nameOcr = content.findViewById(R.id.other_car_detail_name_ocr);
        creditNo = content.findViewById(R.id.other_car_detail_creditno);
        creditOcr = content.findViewById(R.id.other_car_detail_creditno_ocr);
        hasDuty = content.findViewById(R.id.other_car_detail_hasduty);
        isLoss = content.findViewById(R.id.other_car_detail_isloss);
        amount = content.findViewById(R.id.other_car_detail_amount);

        /**
         * add by zhengyg
         * 2018年11月25日18:50:06
         * 发动机号、车架号 添加OCR识别
         */
        frameNo = content.findViewById(R.id.other_car_detail_vinNo);
        frameNoOcr = content.findViewById(R.id.other_car_detail_vinNo_Ocr);
        engineNo = content.findViewById(R.id.other_car_detail_engineNo);
        engineNoOcr = content.findViewById(R.id.other_car_detail_engineNo_Ocr);

        repairFlag = content.findViewById(R.id.other_info_repairFlag);
        reserveFlag = content.findViewById(R.id.other_info_reserveFlag);

        dutyTypeLayout = content.findViewById(R.id.other_info_dutyType_layout);
        dutyType = content.findViewById(R.id.other_info_dutyType);
        ciIndemDutyLayout = content.findViewById(R.id.other_info_ciIndemDuty_layout);
        ciIndemDuty = content.findViewById(R.id.other_info_ciIndemDuty);
        noPushReasonLayout = content.findViewById(R.id.other_info_noPushReason_layout);
        noPushReason = content.findViewById(R.id.other_info_noPushReason);
        lossPartLayout = content.findViewById(R.id.other_info_lossPart_layout);
        lossPart = content.findViewById(R.id.other_info_lossPart);

        dutyTypeLayout.setVisibility(View.GONE);

        otherList.addHeaderView(content);
        otherList.setAdapter(null);


        /**
         * 录入规则
         */
        setEditTextOnlyForWordAndChar(name);
        setEditTextOnlyForNumberAndChar(engineNo);
        setEditTextForChar(model);

    }

    private void initData() {

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
                CommonUtils.showKeyboardDialog(OtherCarDetailActivity.this, creditNo, "身份证号:", KeyboardDialog.INPUT_TYPE.IDNUMBER);
            }
        });

        frameNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //易星身份证号输入
                CommonUtils.showKeyboardDialog(OtherCarDetailActivity.this, frameNo, "车架号:", KeyboardDialog.INPUT_TYPE.RACKNO);
            }
        });

        carNoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //易星纯数字号输入
                CommonUtils.showKeyboardDialog(OtherCarDetailActivity.this, carNo, "车牌号:", KeyboardDialog.INPUT_TYPE.CARNO);
            }
        });

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
                Intent intent = new Intent(OtherCarDetailActivity.this, ImageListActivity.class);
                intent.putExtra("reportNo", reportNo);
                intent.putExtra("taskNo", taskNo);
                startActivity(intent);
            }
        });

        //保存
        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (car != null) {
                    saveData();
                } else {
                    showShortToast("车辆信息为空");
                    return;
                }

                if (completeCheck()) {
                    car.setCreateTime(DateUtil.getTime_YMDMS(new Date()));
                    car.save();
                    finish();
                }

            }
        });

        hasDuty.setOnToggleChanged(new ToggleButtonView.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    car.setIsHaveDuty("1");
                } else {
                    car.setIsHaveDuty("0");
                }
            }
        });

        reserveFlag.setOnToggleChanged(new ToggleButtonView.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    car.setReserveFlag("1");
                } else {
                    car.setReserveFlag("0");
                }
            }
        });

        repairFlag.setOnToggleChanged(new ToggleButtonView.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    car.setRepairFlag("1");
                    noPushReasonLayout.setVisibility(View.GONE);
                    noPushReason.setText("");
                    noPushReason.setTag("");
                } else {
                    car.setRepairFlag("0");
                    noPushReasonLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        isLoss.setOnToggleChanged(new ToggleButtonView.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    car.setDamageFlag("1");
                } else {
                    car.setDamageFlag("0");
                }
            }
        });

        nameOcr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivityForResult(new Intent(OtherCarDetailActivity.this, NewSIDOcrActivity.class), 12);
                startActivityForResult(new Intent(OtherCarDetailActivity.this, NewDLOcrActivity.class), 11);
            }
        });

        creditOcr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivityForResult(new Intent(OtherCarDetailActivity.this, NewSIDOcrActivity.class), 12);
                startActivityForResult(new Intent(OtherCarDetailActivity.this, NewDLOcrActivity.class), 11);
            }
        });

        engineNoOcr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(OtherCarDetailActivity.this, NewVLOcrActivity.class), 13);
            }
        });

        frameNoOcr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(OtherCarDetailActivity.this, NewVLOcrActivity.class), 13);
            }
        });

        List<KeyValue> carNoTypes = StateSelect.initArray(this, R.array.HPZL_value, R.array.HPZL_key);
        if (carNoTypes != null && carNoTypes.size() > 0) {
            carNoTypeStates.clear();
            carNoTypeStates.addAll(carNoTypes);
        }
        carNoTypeDialog = new StateSelectDialog(this, carNoTypeStates, carNoType);
        carNoTypeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carNoTypeDialog.show();
            }
        });

        List<KeyValue> carTypes = StateSelect.initArray(this, R.array.CLZL_value, R.array.CLZL_key);
        if (carTypes != null && carTypes.size() > 0) {
            carTypeStates.clear();
            carTypeStates.addAll(carTypes);
        }
        carTypeDialog = new StateSelectDialog(this, carTypeStates, carType);
        carTypeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carTypeDialog.show();
            }
        });

        List<KeyValue> dutyTypes = StateSelect.initArray(this, R.array.SYXZR_value, R.array.SYXZR_key);
        if (dutyTypes != null && dutyTypes.size() > 0) {
            dutyTypeStates.clear();
            dutyTypeStates.addAll(dutyTypes);
        }
        dutyTypeDialog = new StateSelectDialog(this, dutyTypeStates, dutyType);
        dutyTypeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dutyTypeDialog.show();
            }
        });

        List<KeyValue> ciIndemDutys = StateSelect.initArray(this, R.array.JQXZR_value, R.array.JQXZR_key);
        if (ciIndemDutys != null && ciIndemDutys.size() > 0) {
            ciIndemDutyStates.clear();
            ciIndemDutyStates.addAll(ciIndemDutys);
        }
        ciIndemDutyDialog = new StateSelectDialog(this, ciIndemDutyStates, ciIndemDuty);
        ciIndemDutyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ciIndemDutyDialog.show();
            }
        });

        List<KeyValue> noPushReasons = StateSelect.initArray(this, R.array.BTXYY_value, R.array.BTXYY_key);
        if (noPushReasons != null && noPushReasons.size() > 0) {
            noPushReasonStates.clear();
            noPushReasonStates.addAll(noPushReasons);
        }
        noPushReasonDialog = new StateSelectDialog(this, noPushReasonStates, noPushReason);
        noPushReasonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noPushReasonDialog.show();
            }
        });

        List<KeyValue> lossParts = StateSelect.initArray(this, R.array.SSBW_value, R.array.SSBW_key);
        if (lossParts != null && lossParts.size() > 0) {
            lossPartStates.clear();
            lossPartStates.addAll(lossParts);
        }


        //获取Activity传递过来的参数
        CarLossInfoDto carDto = (CarLossInfoDto) getIntent().getSerializableExtra("carLossInfoDto");
        reportNo = getIntent().getStringExtra("reportNo");
        taskNo = getIntent().getStringExtra("taskNo");

        /**
         * 刷新页面数据
         */
        if (carDto != null) {
            car = DataSupport.where("reportNo = ? and taskNo = ? and lossItemType = ? and createTime = ?", carDto.getReportNo(), carDto.getTaskNo(), LOSSTYPEOTHERCAR, carDto.getCreateTime())
                    .order("createTime desc").findFirst(CarLossInfoDto.class);

        } else {

            car = new CarLossInfoDto();
            car.setReportNo(reportNo);
            car.setTaskNo(taskNo);
            car.setLossItemType(LOSSTYPEOTHERCAR);//三者车

        }

        {
            carNo.setText(car.getLicenseNo());
            model.setText(car.getBrandName());

            frameNo.setText(car.getFrameNo());
            engineNo.setText(car.getEngineNo());

            carNoType.setTag(car.getLicenseNoType());
            carNoType.setText(StateSelect.getTextValue(car.getLicenseNoType(), carNoTypeStates));
            carType.setTag(car.getCarKindCode());
            carType.setText(StateSelect.getTextValue(car.getCarKindCode(), carTypeStates));
            name.setText(car.getDriverName());
            creditNo.setText(car.getIdentifyNumber());
            if (!StringUtils.isEmpty(car.getIsHaveDuty()) && car.getIsHaveDuty().equals("1")) {
                hasDuty.setToggleOn();
            } else {
                hasDuty.setToggleOff();
            }
            if (!StringUtils.isEmpty(car.getDamageFlag()) && car.getDamageFlag().equals("1")) {
                isLoss.setToggleOn();
            } else {
                isLoss.setToggleOff();
            }
            amount.setText(car.getExceptSumLossFee());

            ciIndemDuty.setTag(car.getCiIndemDuty());
            ciIndemDuty.setText(StateSelect.getTextValue(car.getCiIndemDuty(), ciIndemDutyStates));

            if (!StringUtils.isEmpty(car.getRepairFlag()) && car.getRepairFlag().equals("1")) {
                repairFlag.setToggleOn();
                noPushReasonLayout.setVisibility(View.GONE);
                noPushReason.setText("");
                noPushReason.setTag("");
            } else {
                repairFlag.setToggleOff();
                noPushReasonLayout.setVisibility(View.VISIBLE);
            }
            noPushReason.setTag(car.getNoPushReason());
            noPushReason.setText(StateSelect.getTextValue(car.getNoPushReason(), noPushReasonStates));

            if (!StringUtils.isEmpty(car.getReserveFlag()) && car.getReserveFlag().equals("1")) {
                reserveFlag.setToggleOn();
            } else {
                reserveFlag.setToggleOff();
            }

            lossPart.setTag(car.getLossPart());
            lossPart.setText(StateSelect.getMultiTextValue(car.getLossPart(), lossPartStates));

            dutyType.setTag(car.getDutyType());
            dutyType.setText(StateSelect.getTextValue(car.getDutyType(), dutyTypeStates));

        }

        lossPartDialog = new MultiSelectDialog(this, lossPartStates, lossPart);
        lossPartLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lossPartDialog.show();
            }
        });
        System.out.print("获取reportNo值====="+reportNo);
        String comCode = reportNo.substring(9, 13);
        System.out.print("获取comCode值=====" + comCode);
        if (!StringUtils.isEmpty(comCode) && comCode.equals("0111")) {
            isBeifen = true;
            carTypeLayout.setVisibility(View.GONE);
        } else {
            carTypeLayout.setVisibility(View.VISIBLE);//非北京案件要有该字段的展示
        }
    }


    private boolean completeCheck() {

        if (car == null) {
            return false;
        } else if (StringUtils.isEmpty(car.getLicenseNo())) {
            showShortToast("请输入车牌号");
            return false;
        } else if (StringUtils.isEmpty(car.getBrandName())) {
            showShortToast("请填写车型名称");
            return false;
        } else if (StringUtils.isEmpty(car.getEngineNo())) {
            showShortToast("请录入发动机号");
            return false;
        } else if (StringUtils.isEmpty(car.getFrameNo())) {
            showShortToast("请录入车架号");
            return false;
        } else if (StringUtils.isEmpty(car.getLicenseNoType())) {
            showShortToast("请选择号牌种类");
            return false;
        } else if (!isBeifen && StringUtils.isEmpty(car.getCarKindCode())) {
            showShortToast("请输入车辆种类");
            return false;
        } else if (StringUtils.isEmpty(car.getDriverName())) {
            showShortToast("请输入驾驶人姓名");
            return false;
        } else if (StringUtils.isEmpty(car.getIdentifyNumber())) {
            showShortToast("请输入驾驶人身份证号");
            return false;
        }
//        else if (StringUtils.isEmpty(car.getDutyType())){
//            showShortToast("请选择商业险赔偿责任");
//            return false;
//        }
        else if (StringUtils.isEmpty(car.getCiIndemDuty())) {
            showShortToast("请选择交强险责任类型");
            return false;
        } else if (car.getRepairFlag().equals("0") && StringUtils.isEmpty(car.getNoPushReason())) {//如果不维修,则必须有不维修原因
            showShortToast("请选择不推修原因");
            return false;
        } else if ((car.getDamageFlag().equals("1") || car.getRepairFlag().equals("1")) && StringUtils.isEmpty(car.getLossPart())) {
            showShortToast("请填写标的信息-损失部位");
            return false;
        } else if (StringUtils.isEmpty(car.getExceptSumLossFee())) {
            showShortToast("请填写标的信息-预估金额");
            return false;
        } else {
            return true;
        }
    }


    private void saveData() {

        car.setLicenseNo(carNo.getText().toString());
        car.setBrandName(model.getText().toString());
        car.setFrameNo(frameNo.getText().toString());
        car.setEngineNo(engineNo.getText().toString());
        if (carNoType.getTag() != null) {
            car.setLicenseNoType(carNoType.getTag().toString());
        }
        if (carType.getTag() != null) {
            car.setCarKindCode(carType.getTag().toString());
        }
        car.setDriverName(name.getText().toString());
        car.setIdentifyNumber(creditNo.getText().toString());
        car.setDrivingLicenseNo(creditNo.getText().toString());
        car.setLossMoney(amount.getText().toString());

        car.setExceptSumLossFee(amount.getText().toString());
        if (noPushReason.getTag() != null) {
            car.setNoPushReason(noPushReason.getTag().toString());
        }
        if (lossPart.getTag() != null) {
            car.setLossPart(lossPart.getTag().toString());
        }
        if (dutyType.getTag() != null) {
            car.setDutyType(dutyType.getTag().toString());
        }
        if (ciIndemDuty.getTag() != null) {
            car.setCiIndemDuty(ciIndemDuty.getTag().toString());
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 若为识别调用请求
         */
        /**
         * 若为识别调用请求
         */
        if (requestCodes.contains(requestCode)) {

            if (data == null) {
                return;
            }

            OcrVO ocrVO = (OcrVO) data.getExtras().get("orcData");
            if (ocrVO == null) {
                return;
            }

            List<ValueForKey> values = ocrVO.getList();
            if (values == null || values.size() == 0) {
                return;
            }

            Map<String, String> maps = new HashMap<>();
            for (int i = 0; i < values.size(); i++) {
                maps.put(values.get(i).getKey(), values.get(i).getValue());
            }
            String result = new Gson().toJson(maps);
            LogUtils.e("result", "-------------------------->\n" + result);


            if (resultCode == 13) {//行驶证识别
                engineNo.setText(maps.get("nsEngineNo"));
                frameNo.setText(maps.get("nsVIN"));
            } else if (resultCode == 11) {//驾驶证识别
                name.setText(maps.get("姓名:"));
                creditNo.setText(maps.get("证号:"));
            }
        }
    }

}
