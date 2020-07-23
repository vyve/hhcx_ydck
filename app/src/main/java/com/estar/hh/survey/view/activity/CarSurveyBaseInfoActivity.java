package com.estar.hh.survey.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bigkoo.pickerview.OptionsPickerView;
import com.estar.hh.survey.R;
import com.estar.hh.survey.component.StateSelectDialog;
import com.estar.hh.survey.constants.StateSelect;
import com.estar.hh.survey.entity.entity.CarLossBaseInfoDto;
import com.estar.hh.survey.entity.entity.CarLossInfoDto;
import com.estar.hh.survey.entity.entity.CopyMainInfoDto;
import com.estar.hh.survey.entity.entity.KeyValue;
import com.estar.hh.survey.entity.entity.Mission;
import com.estar.hh.survey.entity.entity.SurveyAlreadySubmitDto;
import com.estar.hh.survey.entity.vo.addresses.CityModel;
import com.estar.hh.survey.entity.vo.addresses.ProvinceBean;
import com.estar.hh.survey.entity.vo.addresses.ProvinceModel;
import com.estar.hh.survey.service.XmlParserHandler;
import com.estar.hh.survey.utils.ActivityManagerUtils;
import com.estar.hh.survey.utils.LogUtils;
import com.estar.hh.survey.utils.SharedUtils;
import com.estar.hh.survey.utils.TextUtils;
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
import org.litepal.util.SharedUtil;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import static com.estar.hh.survey.utils.TextUtils.setEditTextNoEngChar;
import static com.estar.hh.survey.utils.TextUtils.setEditTextOnlyForNumberAndChar;
import static com.estar.hh.survey.utils.TextUtils.setEditTextOnlyForWordAndChar;

/**
 * Created by Administrator on 2017/10/12 0012.
 * 车辆定损基本信息
 */

public class CarSurveyBaseInfoActivity extends HuangheBaseActivity {

    @ViewInject(R.id.car_survey_base_back)
    private LinearLayout back;

    @ViewInject(R.id.car_survey_base_list)
    private ListView list;

    @ViewInject(R.id.car_survey_base_return)
    private LinearLayout upStep;

    @ViewInject(R.id.car_survey_base_takepic)
    private LinearLayout takePic;

    @ViewInject(R.id.car_survey_base_next_step)
    private LinearLayout nextStep;

    private View content;
    private LinearLayout carNoLayout;
    private TextView carNo;
    private LinearLayout carNoTypeLayout;
    private TextView carNoType;
    public LinearLayout carTypeLayout;
    public TextView carType;
    private EditText driverName;
    private TextView driverCreditNo;
    private EditText driverTelNo;
    private TextView nameOcr;
    private TextView creditOcr;
    private EditText place;
    private LinearLayout placeRefresh;
    private EditText repairePlace;
    private LinearLayout repairePlaceRefresh;
    private LinearLayout lossKindLayout;
    private LinearLayout lossKindLayout2;
    private LinearLayout hasKindLayout;
    private TextView lossKind;
    private TextView lossKind2;
    private TextView hasDuty;//交强险责任
 /*   private ToggleButtonView isCompany;//是否合作修理厂*/
    private ToggleButtonView hasCode;//无机构地区修理
    private LinearLayout factoryCodeLayout;
    private TextView factoryCode;
    private EditText opinion;

    private TextView vinNo;
    private EditText engNo;
    private TextView vinNoOcr;
    private TextView engNoOcr;

    private TextView driverAddr;
    private TextView floorLevel;
    private ToggleButtonView isFloor;
    private ToggleButtonView driverSex;
    private ToggleButtonView isCertain;
    private LinearLayout driverAddrLayout;
    private LinearLayout floorLevelLayout;
    private LinearLayout isFloorLayout;
    private LinearLayout driverSexLayout;
    private LinearLayout isCertainLayout;

    private StateSelectDialog carNoTypeDialog;
    private List<KeyValue> carNoTypeStates = new ArrayList<>();
    private StateSelectDialog carTypeDialog;
    private List<KeyValue> carTypeStates = new ArrayList<>();
    private StateSelectDialog lossKindDialog;
    private StateSelectDialog lossKindDialog2;
    private List<KeyValue> lossKindStates = new ArrayList<>();
    private List<KeyValue> lossKindStates2 = new ArrayList<>();
    //++++++++
    private StateSelectDialog hasKindDialog;
    private List<KeyValue> hasKindStates = new ArrayList<>();

    private StateSelectDialog factoryCodeDialog;
    private List<KeyValue> factoryCodeStates = new ArrayList<>();

    private StateSelectDialog floorLevelDialog;
    private List<KeyValue> floorLevelStates = new ArrayList<>();
    private StateSelectDialog driverAddrDialog;
    private List<KeyValue> driverAddrStates = new ArrayList<>();

    private List<Integer> requestCodes = new ArrayList<>();

    public Mission mission = null;//任务信息
    public CarLossBaseInfoDto carLossBaseInfoDto = null;//查勘信息
    public String reportNo;
    public String taskNo;

    private SurveyAlreadySubmitDto surveyAlreadySubmitDto = null;
    private CopyMainInfoDto copyMainInfo = null;

    private boolean isBeifen = false;
    private String comCode;
    private String repairProvinceCode = "";//维修地点所在省
    private String repairCityCode = "";//维修地点所在市
    private String defProvinceCode = "";//定损地点所在省
    private String defCityCode = "";//定损地点所在市
    private ToggleButtonView case_flag;

    private TextView tv_def;//定损省市
    private TextView tv_repair;//维修省市
    private int isDef = -1; //默认值-1，定损 0，维修 1
    /**
     * 省市区
     */
    public ArrayList<ProvinceBean> options1Items = new ArrayList<ProvinceBean>();
    public ArrayList<ArrayList<ProvinceBean>> options2Items = new ArrayList<ArrayList<ProvinceBean>>();
    private OptionsPickerView pvOptions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_survey_base_info_activity);
        x.view().inject(this);

        initView();
        initData();
        initLocation();

    }

    private void initView() {
        content = LayoutInflater.from(this).inflate(R.layout.car_survey_base_info_content, null);
        carNoLayout = content.findViewById(R.id.car_survey_base_carno_layout);
        carNo = content.findViewById(R.id.car_survey_base_carno);
        carNoTypeLayout = content.findViewById(R.id.car_survey_base_carnotype_layout);
        carNoType = content.findViewById(R.id.car_survey_base_carnotype);
        carTypeLayout = content.findViewById(R.id.car_survey_base_cartype_layout);
        carType = content.findViewById(R.id.car_survey_base_cartype);
        driverName = content.findViewById(R.id.car_survey_base_driver);
        driverCreditNo = content.findViewById(R.id.car_survey_base_creditno);
        driverTelNo = content.findViewById(R.id.car_survey_base_telno);
        nameOcr = content.findViewById(R.id.car_survey_base_driver_ocr);
        creditOcr = content.findViewById(R.id.car_survey_base_creditno_ocr);
        place = content.findViewById(R.id.car_survey_base_place);
        placeRefresh = content.findViewById(R.id.car_survey_base_place_refresh);
        repairePlace = content.findViewById(R.id.car_survey_base_repaireplace);
        repairePlaceRefresh = content.findViewById(R.id.car_survey_base_repaireplace_refresh);
        lossKindLayout = content.findViewById(R.id.car_survey_base_losskind_layout);
        lossKindLayout2 = content.findViewById(R.id.car_survey_base_losskind_layout2);
        hasKindLayout = content.findViewById(R.id.car_survey_base_hasDuty_layout);
        lossKind = content.findViewById(R.id.car_survey_base_losskind);
        lossKind2 = content.findViewById(R.id.car_survey_base_losskind2);
        hasDuty = content.findViewById(R.id.car_survey_base_hasduty);
        //isCompany = (ToggleButtonView) content.findViewById(R.id.car_survey_base_iscompany);
        hasCode = content.findViewById(R.id.car_survey_base_hascode);
        factoryCodeLayout = content.findViewById(R.id.car_survey_base_factorycode_layout);
        factoryCode = content.findViewById(R.id.car_survey_base_factorycode);
        opinion = content.findViewById(R.id.car_survey_base_opinion);

        driverAddr = content.findViewById(R.id.car_survey_base_driveraddress);
        floorLevel = content.findViewById(R.id.survey_info_floorlevel);
        isFloor = content.findViewById(R.id.survey_info_isFloordedCar);
        driverSex = content.findViewById(R.id.survey_info_driversex);
        isCertain = content.findViewById(R.id.survey_info_cetain);

        driverAddrLayout = content.findViewById(R.id.car_survey_base_driveraddress_layout);
        floorLevelLayout = content.findViewById(R.id.survey_info_floorlevel_layout);
        isFloorLayout = content.findViewById(R.id.survey_info_isFloordedCar_layout);
        driverSexLayout = content.findViewById(R.id.survey_info_driversex_layout);
        isCertainLayout = content.findViewById(R.id.survey_info_cetain_layout);

        vinNo = content.findViewById(R.id.car_survey_base_vinno);
        engNo = content.findViewById(R.id.car_survey_base_engineno);
        vinNoOcr = content.findViewById(R.id.car_survey_base_vinno_ocr);
        engNoOcr = content.findViewById(R.id.car_survey_base_engineno_ocr);

        case_flag = content.findViewById(R.id.survey_info_case);
        tv_def = content.findViewById(R.id.car_survey_base_def_pro);
        tv_repair = content.findViewById(R.id.car_survey_base_repair_pro);
        tv_def.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDef = 0;
                pvOptions.show();//省市联动
            }
        });
        tv_repair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDef = 1;
                pvOptions.show();//省市联动
            }
        });
        /**
         * 录入规则
         */
        setEditTextOnlyForNumberAndChar(engNo);
        setEditTextOnlyForWordAndChar(driverName);
        setEditTextNoEngChar(place);
        setEditTextNoEngChar(repairePlace);
        setEditTextNoEngChar(opinion);


        isFloor.setOnToggleChanged(new ToggleButtonView.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    carLossBaseInfoDto.setIsFloodedCar("1");
                    floorLevelLayout.setVisibility(View.VISIBLE);
                } else {
                    carLossBaseInfoDto.setIsFloodedCar("0");
                    floorLevelLayout.setVisibility(View.GONE);
                    floorLevel.setText("");
                    floorLevel.setTag("");
                }
            }
        });
        driverSex.setOnToggleChanged(new ToggleButtonView.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    carLossBaseInfoDto.setDrivingSex("1");
                } else {
                    carLossBaseInfoDto.setDrivingSex("2");
                }
            }
        });
        isCertain.setOnToggleChanged(new ToggleButtonView.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    carLossBaseInfoDto.setCetainlossTypeFlag("1");
                } else {
                    carLossBaseInfoDto.setCetainlossTypeFlag("0");
                }
            }
        });

//        hasDuty.setOnToggleChanged(new ToggleButtonView.OnToggleChanged() {
//            @Override
//            public void onToggle(boolean on) {
//                if (on) {
//                    carLossBaseInfoDto.setCiIndemDuty("100");
//                } else {
//                    carLossBaseInfoDto.setCiIndemDuty("0");
//                }
//            }
//        });

       /* isCompany.setOnToggleChanged(new ToggleButtonView.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    carLossBaseInfoDto.setIsCoopRepair("1");
                } else {
                    carLossBaseInfoDto.setIsCoopRepair("0");
                }
            }
        });*/

        hasCode.setOnToggleChanged(new ToggleButtonView.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    carLossBaseInfoDto.setRepairComcodeFlag("1");
                } else {
                    carLossBaseInfoDto.setRepairComcodeFlag("0");
                }
            }
        });

        case_flag.setOnToggleChanged(new ToggleButtonView.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    carLossBaseInfoDto.setCaseFlag("1");
                } else {
                    carLossBaseInfoDto.setCaseFlag("0");
                }
            }
        });
        list.addHeaderView(content);
        list.setAdapter(null);
    }

    private boolean isOCRStart = false;

    private void initData() {
        initProvinceDatas();
        //选项选择器
        pvOptions = new OptionsPickerView(this);

        //三级联动效果
        pvOptions.setPicker(options1Items, options2Items, null, true);
        //设置选择的三级单位
//        pvOptions.setLabels("省", "市", "区");
//        pvOptions.setTitle("省市");
        pvOptions.setCyclic(false);//设置是否循环滚动

        pvOptions.setTextSize(15);
        //设置默认选中的三级项目
        pvOptions.setSelectOptions(0, 0, 0);

        //监听确定选择按钮
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
//                String tx = options1Items.get(options1).getName()+options1Items.get(options1).getCode()
//                        + options2Items.get(options1).get(option2).getName()+options2Items.get(options1).get(option2).getCode()
//                        + options3Items.get(options1).get(option2).get(options3).getName()+ options3Items.get(options1).get(option2).get(options3).getCode()
//                        ;
                String tx = options1Items.get(options1).getName() + options2Items.get(options1).get(option2).getName();
                if (!android.text.TextUtils.isEmpty(tx)) {
                    if(isDef == 0){
                        tv_def.setText(tx);
                        SharedUtils.saveData(CarSurveyBaseInfoActivity.this,"def",tx);
                        defProvinceCode = options1Items.get(options1).getCode();
                        defCityCode = options2Items.get(options1).get(option2).getCode();
                    }else if(isDef == 1){
                        tv_repair.setText(tx);
                        SharedUtils.saveData(CarSurveyBaseInfoActivity.this,"repair",tx);
                        repairProvinceCode = options1Items.get(options1).getCode();
                        repairCityCode = options2Items.get(options1).get(option2).getCode();
                    }
                }
            }
        });

        requestCodes.add(11);
        requestCodes.add(12);
        requestCodes.add(13);
        requestCodes.add(14);
        requestCodes.add(15);
        requestCodes.add(16);

        driverCreditNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //易星身份证号输入
                CommonUtils.showKeyboardDialog(CarSurveyBaseInfoActivity.this, driverCreditNo, "身份证号:", KeyboardDialog.INPUT_TYPE.IDNUMBER);
            }
        });

        carNoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //车牌号输入
                CommonUtils.showKeyboardDialog(CarSurveyBaseInfoActivity.this, carNo, "车牌号:", KeyboardDialog.INPUT_TYPE.CARNO);
            }
        });

        vinNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //车架号输入
                CommonUtils.showKeyboardDialog(CarSurveyBaseInfoActivity.this, vinNo, "车架号:", KeyboardDialog.INPUT_TYPE.RACKNO);
            }
        });

//        engNo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //发动机号输入
//                CommonUtils.showKeyboardDialog(CarSurveyBaseInfoActivity.this,engNo,"发动机号:", KeyboardDialog.INPUT_TYPE.RACKNO);
//            }
//        });

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
                Intent intent = new Intent(CarSurveyBaseInfoActivity.this, ImageListActivity.class);
                intent.putExtra("reportNo", reportNo);
                intent.putExtra("taskNo", taskNo);
                startActivity(intent);
            }
        });

        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveData();

                if (!completeCheck()) {
                    return;
                }else {
                    if (completeCheck()) {
                        Activity activity = ActivityManagerUtils.getInstance().getActivityclass(TaskCarProcessActivity.class);
                        if (activity != null) {
                            ((TaskCarProcessActivity) activity).changeProcessStyle(((TaskCarProcessActivity) activity).baseInfo);
                            ((TaskCarProcessActivity) activity).carLossMainInfoDto.setBaseInfoFlag(1);
                            ((TaskCarProcessActivity) activity).carLossMainInfoDto.save();
                        }
                    } else {
                        Activity activity = ActivityManagerUtils.getInstance().getActivityclass(TaskCarProcessActivity.class);
                        if (activity != null) {
                            ((TaskCarProcessActivity) activity).returnProcessStyle(((TaskCarProcessActivity) activity).baseInfo);
                            ((TaskCarProcessActivity) activity).carLossMainInfoDto.setBaseInfoFlag(0);
                            ((TaskCarProcessActivity) activity).carLossMainInfoDto.save();
                        }
                    }
                }



                finish();
            }
        });

        nameOcr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOCRStart = true;
//                startActivityForResult(new Intent(CarSurveyBaseInfoActivity.this, NewSIDOcrActivity.class), 12);
                startActivityForResult(new Intent(CarSurveyBaseInfoActivity.this, NewDLOcrActivity.class), 11);
            }
        });

        creditOcr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOCRStart = true;
//                startActivityForResult(new Intent(CarSurveyBaseInfoActivity.this, NewSIDOcrActivity.class), 12);
                startActivityForResult(new Intent(CarSurveyBaseInfoActivity.this, NewDLOcrActivity.class), 11);
            }
        });

        vinNoOcr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOCRStart = true;
//                startActivityForResult(new Intent(CarSurveyBaseInfoActivity.this, NewVinOcrActivity.class), 16);
                startActivityForResult(new Intent(CarSurveyBaseInfoActivity.this, NewVLOcrActivity.class), 13);
            }
        });

        engNoOcr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOCRStart = true;
                startActivityForResult(new Intent(CarSurveyBaseInfoActivity.this, NewVLOcrActivity.class), 13);
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

        List<KeyValue> lossKinds = StateSelect.initArray(this, R.array.DSFS_value1, R.array.DSFS_key1);
        if (lossKinds != null && lossKinds.size() > 0) {
            lossKindStates.clear();
            lossKindStates.addAll(lossKinds);
        }
        lossKindDialog = new StateSelectDialog(this, lossKindStates, lossKind);
        List<KeyValue> lossKinds2 = StateSelect.initArray(this, R.array.returnRepairMode_value, R.array.returnRepairMode_key);
        if (lossKinds2 != null && lossKinds2.size() > 0) {
            lossKindStates2.clear();
            lossKindStates2.addAll(lossKinds2);
        }
        lossKindDialog2 = new StateSelectDialog(this, lossKindStates2, lossKind2);
        lossKindLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lossKindDialog.show();
            }
        });
        //送回修方式
        lossKindLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lossKindDialog2.show();
            }
        });

        //+++++++++++++++
        List<KeyValue> hasKinds = StateSelect.initArray(this, R.array.DSFS2_value1, R.array.DSFS2_key1);
        if (hasKinds != null && hasKinds.size() > 0) {
            hasKindStates.clear();
            hasKindStates.addAll(hasKinds);
        }
        hasKindDialog = new StateSelectDialog(this, hasKindStates, hasDuty);
        hasKindLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hasKindDialog.show();
            }
        });

        List<KeyValue> factoryCodes = StateSelect.initArray(this, R.array.CZCL_value, R.array.CZCL_key);
        if (factoryCodes != null && factoryCodes.size() > 0) {
            factoryCodeStates.clear();
            factoryCodeStates.addAll(factoryCodes);
        }
        factoryCodeDialog = new StateSelectDialog(this, factoryCodeStates, factoryCode);
        factoryCodeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                factoryCodeDialog.show();
            }
        });

        List<KeyValue> floorLevels = StateSelect.initArray(this, R.array.SYDJ_value, R.array.SYDJ_key);
        if (floorLevels != null && floorLevels.size() > 0) {
            floorLevelStates.clear();
            floorLevelStates.addAll(floorLevels);
        }
        floorLevelDialog = new StateSelectDialog(this, floorLevelStates, floorLevel);
        floorLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floorLevelDialog.show();
            }
        });

        List<KeyValue> driverAddrs = StateSelect.initArray(this, R.array.GSD_value, R.array.GSD_key);
        if (driverAddrs != null && driverAddrs.size() > 0) {
            driverAddrStates.clear();
            driverAddrStates.addAll(driverAddrs);
        }
        driverAddrDialog = new StateSelectDialog(this, driverAddrStates, driverAddr);
        driverAddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                driverAddrDialog.show();
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


        /**
         * 数据接收
         */
        surveyAlreadySubmitDto = (SurveyAlreadySubmitDto) getIntent().getSerializableExtra("surveyAlreadySubmitDto");
        mission = (Mission) getIntent().getSerializableExtra("mission");
        copyMainInfo = (CopyMainInfoDto) getIntent().getSerializableExtra("copyMainInfo");
        reportNo = mission.getRptNo();
        taskNo = mission.getTaskId();

        carLossBaseInfoDto = DataSupport.where("reportNo = ? and taskNo = ?", reportNo, taskNo)
                .order("createTime desc").findFirst(CarLossBaseInfoDto.class);


        /**
         * 如下逻辑只有缓存数据丢失才会启用，查勘信息已经保存在本地过得，就用不到这段逻辑
         */
        if (carLossBaseInfoDto == null) {
            carLossBaseInfoDto = new CarLossBaseInfoDto();
            carLossBaseInfoDto.setReportNo(reportNo);
            carLossBaseInfoDto.setTaskNo(taskNo);

            /**
             * 若本地基本信息为空，则从抄单中获取查勘已提交信息
             */
            if (surveyAlreadySubmitDto == null) {
                return;
            }

            String thisCar = mission.getVhl();
            CarLossInfoDto thisCarInfo = null;
            if (StringUtils.isEmpty(thisCar)) {
                return;
            }

            List<CarLossInfoDto> listCarLossInfoDto = surveyAlreadySubmitDto.getListCarLossInfoDto();
            if (listCarLossInfoDto != null && listCarLossInfoDto.size() > 0) {
                for (CarLossInfoDto carLossInfoDto : listCarLossInfoDto) {
                    if (!StringUtils.isEmpty(carLossInfoDto.getLicenseNo()) && carLossInfoDto.getLicenseNo().equals(thisCar)) {
                        thisCarInfo = carLossInfoDto;
                        break;
                    }
                }

                if (thisCarInfo != null) {
                    carLossBaseInfoDto.setLicsnseNo(thisCarInfo.getLicenseNo());
                    carLossBaseInfoDto.setLicsnseNoType(thisCarInfo.getLicenseNoType());
                    carLossBaseInfoDto.setDriverName(thisCarInfo.getDriverName());
                    carLossBaseInfoDto.setIdentifyNumber(thisCarInfo.getIdentifyNumber());
                    carLossBaseInfoDto.setEngineNo(thisCarInfo.getEngineNo());//发动机号赋值
                    carLossBaseInfoDto.setVinNo(thisCarInfo.getFrameNo());//车架号赋值
                    carLossBaseInfoDto.setCarKindCode(thisCarInfo.getCarKindCode());//车辆种类赋值
                    //从抄单中获取默认值
                    if (copyMainInfo != null) {

                        /**
                         * 若为标的车，则自动赋值车架号和发动机号
                         */
                       /* if (mission.getTaskType().equals("1")) {
                            carLossBaseInfoDto.setVinNo(copyMainInfo.getCopyCarInfo().getVinNo());
                            carLossBaseInfoDto.setEngineNo(copyMainInfo.getCopyCarInfo().getEnginNo());
                        }else{

                        }*/

                        if (!StringUtils.isEmpty(copyMainInfo.getCopyCarInfo().getDriverTelNo())) {
                            carLossBaseInfoDto.setDriverTelNo(copyMainInfo.getCopyCarInfo().getDriverTelNo());
                        } else {
                            carLossBaseInfoDto.setDriverTelNo(copyMainInfo.getCopyReportInfo().getReportTelNo());
                        }
                    }
                }
            }

        }

        /**
         * 北分机构判断
         */
        comCode = copyMainInfo.getCopyReportInfo().getComCode();
        if (!StringUtils.isEmpty(comCode) && comCode.startsWith("0111")) {
            isBeifen = true;
            driverAddrLayout.setVisibility(View.VISIBLE);
            isFloorLayout.setVisibility(View.VISIBLE);
            driverSexLayout.setVisibility(View.VISIBLE);
            isCertainLayout.setVisibility(View.VISIBLE);
            carTypeLayout.setVisibility(View.GONE);
        }else{
            carTypeLayout.setVisibility(View.VISIBLE);//非北分机构才展示该信息
        }

        /**
         * 基础信息填写
         */
        {
            System.out.print("号牌种类===================================="+carLossBaseInfoDto.getLicsnseNoType());
            System.out.print(carLossBaseInfoDto.getEngineNo());
            System.out.print(carLossBaseInfoDto.getVinNo());
            System.out.print(carLossBaseInfoDto.getCarKindCode());


            carNo.setText(carLossBaseInfoDto.getLicsnseNo());
            carNoType.setTag(carLossBaseInfoDto.getLicsnseNoType());
            carNoType.setText(StateSelect.getTextValue(carLossBaseInfoDto.getLicsnseNoType(), carNoTypeStates));
            carType.setTag(carLossBaseInfoDto.getCarKindCode());
            carType.setText(StateSelect.getTextValue(carLossBaseInfoDto.getCarKindCode(), carTypeStates));
            driverName.setText(carLossBaseInfoDto.getDriverName());
            driverCreditNo.setText(carLossBaseInfoDto.getIdentifyNumber());
            driverTelNo.setText(carLossBaseInfoDto.getDriverTelNo());
            place.setText(carLossBaseInfoDto.getDefSite());
            repairePlace.setText(carLossBaseInfoDto.getRepairSite());
            lossKind.setTag(carLossBaseInfoDto.getCetainLossType());
            lossKind.setText(StateSelect.getTextValue(carLossBaseInfoDto.getCetainLossType(), lossKindStates));
            lossKind2.setTag(carLossBaseInfoDto.getReturnRepairMode());
            lossKind2.setText(StateSelect.getTextValue(carLossBaseInfoDto.getReturnRepairMode(), lossKindStates2));
            //++++++++
            hasDuty.setTag(carLossBaseInfoDto.getCiIndemDuty());
            hasDuty.setText(StateSelect.getTextValue(carLossBaseInfoDto.getCiIndemDuty(), hasKindStates));
//            if (!StringUtils.isEmpty(carLossBaseInfoDto.getCiIndemDuty()) && carLossBaseInfoDto.getCiIndemDuty().equals("100")) {
//                hasDuty.setToggleOn();
//            } else {
//                hasDuty.setToggleOff();
//            }
            if (!StringUtils.isEmpty(carLossBaseInfoDto.getRepairComcodeFlag()) && carLossBaseInfoDto.getRepairComcodeFlag().equals("1")) {
                hasCode.setToggleOn();
            } else {
                hasCode.setToggleOff();
            }
            factoryCode.setTag(carLossBaseInfoDto.getRepairFactoryCode());
            factoryCode.setText(StateSelect.getTextValue(carLossBaseInfoDto.getRepairFactoryCode(), factoryCodeStates));
            opinion.setText(carLossBaseInfoDto.getLossOpinion());
            vinNo.setText(carLossBaseInfoDto.getVinNo());
            engNo.setText(carLossBaseInfoDto.getEngineNo());

            driverAddr.setText(StateSelect.getTextValue(carLossBaseInfoDto.getDriverApanage(), driverAddrStates));
            floorLevel.setText(StateSelect.getTextValue(carLossBaseInfoDto.getFloodedLevel(), floorLevelStates));
            if (!StringUtils.isEmpty(carLossBaseInfoDto.getIsFloodedCar()) && carLossBaseInfoDto.getIsFloodedCar().equals("1")) {
                isFloor.setToggleOn();
                floorLevelLayout.setVisibility(View.VISIBLE);
            } else {
                isFloor.setToggleOff();
                floorLevelLayout.setVisibility(View.GONE);
            }
            if (!StringUtils.isEmpty(carLossBaseInfoDto.getDrivingSex()) && carLossBaseInfoDto.getDrivingSex().equals("1")) {
                driverSex.setToggleOn();
            } else {
                driverSex.setToggleOff();
            }
            if (!StringUtils.isEmpty(carLossBaseInfoDto.getCetainlossTypeFlag()) && carLossBaseInfoDto.getCetainlossTypeFlag().equals("1")) {
                isCertain.setToggleOn();
            } else {
                isCertain.setToggleOff();
            }
            if (!StringUtils.isEmpty(carLossBaseInfoDto.getCaseFlag()) && carLossBaseInfoDto.getCetainlossTypeFlag().equals("1")) {
                case_flag.setToggleOn();
            } else {
                case_flag.setToggleOff();
            }



            if(!StringUtils.isEmpty(carLossBaseInfoDto.getDriverApanage())){
                String da = (String) SharedUtils.getData(CarSurveyBaseInfoActivity.this,"DriverApanage","");
                driverAddr.setText(da);
                driverAddr.setTag(carLossBaseInfoDto.getDriverApanage());
            }
            if(!StringUtils.isEmpty(carLossBaseInfoDto.getDefProvinceCode()) && !StringUtils.isEmpty(carLossBaseInfoDto.getDefCityCode())){
                defCityCode = carLossBaseInfoDto.getDefCityCode();
                defProvinceCode = carLossBaseInfoDto.getDefProvinceCode();
                String tx = (String) SharedUtils.getData(CarSurveyBaseInfoActivity.this,"def","");
                tv_def.setText(tx);
            }

            if(!StringUtils.isEmpty(carLossBaseInfoDto.getRepairProvinceCode())&&!StringUtils.isEmpty(carLossBaseInfoDto.getRepairCityCode())){
                repairCityCode = carLossBaseInfoDto.getRepairCityCode();
                repairProvinceCode = carLossBaseInfoDto.getRepairProvinceCode();
                String tx = (String) SharedUtils.getData(CarSurveyBaseInfoActivity.this,"repair","");
                tv_repair.setText(tx);
            }

            if(!StringUtils.isEmpty(carLossBaseInfoDto.getDefSite())){
                place.setText(carLossBaseInfoDto.getDefSite());
            }

            if(!StringUtils.isEmpty(carLossBaseInfoDto.getRepairSite())){
                repairePlace.setText(carLossBaseInfoDto.getRepairSite());
            }
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void saveData() {

        carLossBaseInfoDto.setLicsnseNo(carNo.getText().toString());
        if (carNoType.getTag() != null) {
            carLossBaseInfoDto.setLicsnseNoType(carNoType.getTag().toString());
        }
        if (carType.getTag() != null) {
            carLossBaseInfoDto.setCarKindCode(carType.getTag().toString());
        }
        if (floorLevel.getTag() != null) {
            carLossBaseInfoDto.setFloodedLevel(floorLevel.getTag().toString());
        }
        carLossBaseInfoDto.setDriverApanage(driverAddr.getText().toString());
        SharedUtils.saveData(CarSurveyBaseInfoActivity.this,"DriverApanage",driverAddr.getText().toString());
        carLossBaseInfoDto.setVinNo(vinNo.getText().toString());
        carLossBaseInfoDto.setEngineNo(engNo.getText().toString());
        carLossBaseInfoDto.setDriverName(driverName.getText().toString());
        carLossBaseInfoDto.setIdentifyNumber(driverCreditNo.getText().toString());
        carLossBaseInfoDto.setDriverTelNo(driverTelNo.getText().toString());
        carLossBaseInfoDto.setDefSite(place.getText().toString());
        carLossBaseInfoDto.setRepairSite(repairePlace.getText().toString());
        //新增定损省市，维修省市
        carLossBaseInfoDto.setRepairCityCode(repairCityCode);
        carLossBaseInfoDto.setRepairProvinceCode(repairProvinceCode);
        carLossBaseInfoDto.setDefCityCode(defCityCode);
        carLossBaseInfoDto.setDefProvinceCode(defProvinceCode);
        if(lossKind.getTag() !=null){
            carLossBaseInfoDto.setCetainLossType(lossKind.getTag().toString());
        }
        if(lossKind2.getTag() !=null){
            carLossBaseInfoDto.setReturnRepairMode(lossKind2.getTag().toString());
        }
        //+++++++++
        if(hasDuty.getTag() !=null){
            carLossBaseInfoDto.setCiIndemDuty(hasDuty.getTag().toString());
        }

        if (factoryCode.getTag() != null) {
            carLossBaseInfoDto.setRepairFactoryCode(factoryCode.getTag().toString());
        }
        if (floorLevel.getTag() != null) {
            carLossBaseInfoDto.setFloodedLevel(floorLevel.getTag().toString());
        }
        if (driverAddr.getTag() != null) {
            carLossBaseInfoDto.setDriverApanage(driverAddr.getTag().toString());
        }
        carLossBaseInfoDto.setComCode(comCode);
        carLossBaseInfoDto.setLossOpinion(opinion.getText().toString());
        carLossBaseInfoDto.setCreateTime(DateUtil.getTime_YMDMS(new Date()));
        carLossBaseInfoDto.save();

    }

    /**
     * 基本信息是否完成录入检测
     */
    public boolean completeCheck() {

        if (carLossBaseInfoDto == null) {
            showShortToast("请先完成基本信息录入");
            return false;
        } else if (StringUtils.isEmpty(carLossBaseInfoDto.getLicsnseNo())) {
            showShortToast("请先完成基本信息-车牌号录入");
            return false;
        } else if (StringUtils.isEmpty(carLossBaseInfoDto.getLicsnseNoType())) {
            showShortToast("请先完成基本信息-号牌种类录入");
            return false;
        }/*else if (StringUtils.isEmpty(carLossBaseInfoDto.getCarKindCode())) {
            showShortToast("请先完成基本信息-车辆种类录入");
            return false;
        }*/ else if (StringUtils.isEmpty(carLossBaseInfoDto.getVinNo())) {
            showShortToast("请先完成基本信息-车架号录入");
            return false;
        } else if (StringUtils.isEmpty(carLossBaseInfoDto.getEngineNo())) {
            showShortToast("请先完成基本信息-发动机号录入");
            return false;
        } else if (StringUtils.isEmpty(carLossBaseInfoDto.getDriverName())) {
            showShortToast("请先完成基本信息-驾驶人姓名录入");
            return false;
        } else if (StringUtils.isEmpty(carLossBaseInfoDto.getIdentifyNumber())) {
            showShortToast("请先完成基本信息-身份证号录入");
            return false;
        } else if (StringUtils.isEmpty(carLossBaseInfoDto.getDriverTelNo())) {
            showShortToast("请先完成基本信息-手机号录入");
            return false;
        } else if (StringUtils.isEmpty(carLossBaseInfoDto.getDefSite())) {
            showShortToast("请先完成基本信息-定损地点录入");
            return false;
        } else if (StringUtils.isEmpty(carLossBaseInfoDto.getRepairSite())) {
            showShortToast("请先完成基本信息-维修地址录入");
            return false;
        } else if (StringUtils.isEmpty(carLossBaseInfoDto.getLossOpinion())) {
            showShortToast("请先完成基本信息-定损意见录入");
            return false;
        } else if (StringUtils.isEmpty(carLossBaseInfoDto.getCetainLossType())) {
            showShortToast("请先完成基本信息-定损方式录入");
            return false;
        } else if (StringUtils.isEmpty(carLossBaseInfoDto.getReturnRepairMode())) {
            showShortToast("请先完成基本信息-送回修方式录入");
            return false;
        }
        else if (StringUtils.isEmpty(carLossBaseInfoDto.getCiIndemDuty())) {
            showShortToast("请先完成基本信息-交强险责任类型录入");
            return false;
        }else {
            return true;
        }
    }


    private LocationClient mLocationClient = null;
    private boolean isFirstPlaceRefresh = true;
    private boolean isFirstRepairPlaceRefresh = true;

    private void initLocation() {

        mLocationClient = new LocationClient(this);
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

        placeRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshType = 0;
                mLocationClient.requestLocation();
            }
        });

        repairePlaceRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshType = 1;
                mLocationClient.requestLocation();
            }
        });

    }

    private int refreshType = 2;//0定损地点  1维修地址 2全部

    private class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {

            if (bdLocation.getLocType() != BDLocation.TypeServerError) {
                LogUtils.i("locationUploadService", "定位成功");
                LogUtils.i("locationUploadService", "--------------->latitude:" + bdLocation.getLatitude() + "    longitude:" + bdLocation.getLongitude());

                if (bdLocation != null) {
                    if (refreshType == 0) {
                        if (isFirstPlaceRefresh){
                            isFirstPlaceRefresh = false;
                            return;
                        }
                        place.setText(bdLocation.getAddrStr());
                    } else if (refreshType == 1) {
                        if (isFirstRepairPlaceRefresh){
                            isFirstRepairPlaceRefresh = false;
                            return;
                        }
                        repairePlace.setText(bdLocation.getAddrStr());
                    } else {
                        if (isFirstPlaceRefresh && isFirstRepairPlaceRefresh){
                            isFirstPlaceRefresh = false;
                            isFirstRepairPlaceRefresh = false;
                            return;
                        }
                        place.setText(bdLocation.getAddrStr());
                        repairePlace.setText(bdLocation.getAddrStr());
                    }
                } else {
                    ToastUtils.showShort(CarSurveyBaseInfoActivity.this, "未找到当前位置");
                }

            } else if (bdLocation.getLocType() == BDLocation.TypeCriteriaException) {
                //当前缺少定位依据，可能是用户没有授权，建议弹出提示框让用户开启权限
                //可进一步参考onLocDiagnosticMessage中的错误返回码
                ToastUtils.showShort(CarSurveyBaseInfoActivity.this, "地理位置更新失败");
            } else {
                ToastUtils.showShort(CarSurveyBaseInfoActivity.this, "地理位置更新失败");
            }
        }
    }

    @Override
    protected void onStop() {

        if (isOCRStart) {
            isOCRStart = false;
            super.onStop();
            return;
        }

        saveData();
        /**
         * 改变流程图颜色
         */
        if (completeCheck()) {
            Activity activity = ActivityManagerUtils.getInstance().getActivityclass(TaskCarProcessActivity.class);
            if (activity != null) {
                ((TaskCarProcessActivity) activity).changeProcessStyle(((TaskCarProcessActivity) activity).baseInfo);
                ((TaskCarProcessActivity) activity).carLossMainInfoDto.setBaseInfoFlag(1);
                ((TaskCarProcessActivity) activity).carLossMainInfoDto.save();
            }
        } else {
            Activity activity = ActivityManagerUtils.getInstance().getActivityclass(TaskCarProcessActivity.class);
            if (activity != null) {
                ((TaskCarProcessActivity) activity).returnProcessStyle(((TaskCarProcessActivity) activity).baseInfo);
                ((TaskCarProcessActivity) activity).carLossMainInfoDto.setBaseInfoFlag(0);
                ((TaskCarProcessActivity) activity).carLossMainInfoDto.save();
            }
        }

        super.onStop();
    }

    @Override
    public void onDestroy() {

        if (mLocationClient != null) {
            mLocationClient.stop();
            mLocationClient = null;
        }

        super.onDestroy();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                engNo.setText(maps.get("nsEngineNo"));
                vinNo.setText(maps.get("nsVIN"));
            } else if (resultCode == 11) {//驾驶证识别

                driverName.setText(maps.get("姓名:"));
                driverCreditNo.setText(maps.get("证号:"));

            } else if (resultCode == 12) {//身份证识别

                driverName.setText(maps.get("nsName"));
                driverCreditNo.setText(maps.get("nsIDNum"));

            } else if (resultCode == 16) {//车架号识别
                vinNo.setText(maps.get("车架号:"));
            }
        }
    }

    /**
     * 解析省市区的XML数据
     */
    protected void initProvinceDatas() {
        List<ProvinceModel> provinceList = null;
        AssetManager asset = getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();

            for (ProvinceModel provinceModel : provinceList) {//省

                ArrayList<ProvinceBean> options2Item = new ArrayList<ProvinceBean>();// 市
//            ArrayList<ArrayList<ProvinceBean>> options3Item = new ArrayList<ArrayList<ProvinceBean>>();//区县
                for (CityModel cityModel : provinceModel.getCityList()) {//市
                    ProvinceBean vo2 = new ProvinceBean();
                    vo2.setName(cityModel.getName());
                    vo2.setCode(cityModel.getZipcode());
                    //选项2
                    options2Item.add(vo2);
//                ArrayList<ProvinceBean> options3Items_01_01=new ArrayList<ProvinceBean>();
//                for (DistrictModel districtModel: cityModel.getDistrictList()) {//区县
//                    ProvinceBean vo3=new ProvinceBean();
//                    vo3.setName(districtModel.getName());
//                    vo3.setCode(districtModel.getZipcode());
//                    //选项3
//                    options3Items_01_01.add(vo3);
//                }
//                options3Item.add(options3Items_01_01);
                }
                ProvinceBean vo1 = new ProvinceBean();
                vo1.setName(provinceModel.getName());
                vo1.setCode(provinceModel.getZipcode());
                options1Items.add(vo1);// 省
                options2Items.add(options2Item);// 市
//            options3Items.add(options3Item);//区县

            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
    }
}
