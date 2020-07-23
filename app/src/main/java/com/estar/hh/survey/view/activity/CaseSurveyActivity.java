package com.estar.hh.survey.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.estar.hh.survey.R;
import com.estar.hh.survey.constants.StateSelect;
import com.estar.hh.survey.entity.entity.CarLossInfoDto;
import com.estar.hh.survey.entity.entity.CopyMainInfoDto;
import com.estar.hh.survey.entity.entity.CopyReportInfoDto;
import com.estar.hh.survey.entity.entity.KeyValue;
import com.estar.hh.survey.entity.entity.Mission;
import com.estar.hh.survey.entity.entity.SubmitSurvMainInfoDto;
import com.estar.hh.survey.entity.entity.SurvBaseInfoDto;
import com.estar.hh.survey.utils.ActivityManagerUtils;
import com.estar.hh.survey.view.fragment.OtherInfoFragment;
import com.estar.hh.survey.view.fragment.SurveyInfoFragment;
import com.estar.utils.DateUtil;
import com.estar.utils.StringUtils;
import com.estar.utils.ToastUtils;
import com.viewpagerindicator.TabPageIndicator;
import com.viewpagerindicator.UnderlinePageIndicator;

import org.litepal.crud.DataSupport;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Date;
import java.util.List;

import static com.estar.hh.survey.entity.entity.CarLossInfoDto.LOSSTYPECAR;

/**
 * Created by Administrator on 2017/9/29 0029.
 * 现场查勘
 */

public class CaseSurveyActivity extends FragmentActivity {

    @ViewInject(R.id.case_survey_back)
    private LinearLayout back;

    @ViewInject(R.id.case_survey_up_step)
    private LinearLayout upStep;

    @ViewInject(R.id.case_survey_take_pic)
    private LinearLayout takePic;

    @ViewInject(R.id.case_survey_next_step)
    private LinearLayout nextStep;

    @ViewInject(R.id.case_survey_indicator)
    private TabPageIndicator indicator;

    @ViewInject(R.id.case_survey_underline)
    private UnderlinePageIndicator underline;

    @ViewInject(R.id.case_survey_pager)
    private ViewPager pager;

    /**
     * Tab标题
     */
    private static final String[] INDICATORTITLE = new String[]{"查勘信息", "标的信息"};
    private TabPageIndicatorAdapter adapter;

    public Mission mission = null;//任务信息
    public SurvBaseInfoDto survBaseInfoDto = null;//查勘信息
    public CarLossInfoDto carLossInfoDto = null;//标的信息
    public CopyMainInfoDto copyMainInfoDto = null;//抄单信息

    public String reportNo;
    public String taskNo;

    private boolean isBeifen = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.case_survey_activity);
        x.view().inject(this);

        initData();
        initView();

    }

    private void saveData() {

        SurveyInfoFragment surveyInfoFragment = (SurveyInfoFragment) adapter.getSurveyInfoFragment();
        if (surveyInfoFragment.lossType.getTag() != null) {
            survBaseInfoDto.setLossType(surveyInfoFragment.lossType.getTag().toString());// 损失类别
        }
        if (surveyInfoFragment.riskReason.getTag() != null) {
            survBaseInfoDto.setDamageCode(surveyInfoFragment.riskReason.getTag().toString());// 出险原因
        }
        if (surveyInfoFragment.riskType.getTag() != null) {
            survBaseInfoDto.setDamageCaseCode(surveyInfoFragment.riskType.getTag().toString());// 事故类型
        }
        if (surveyInfoFragment.busDuty.getTag() != null) {
            survBaseInfoDto.setIndemnityDuty(surveyInfoFragment.busDuty.getTag().toString());// 商业险赔偿责任
        }
        if (surveyInfoFragment.classType.getTag() != null) {
            survBaseInfoDto.setInsureAccident(surveyInfoFragment.classType.getTag().toString());// 保险事故分类
        }
        if (surveyInfoFragment.floorLevel.getTag() != null) {
            survBaseInfoDto.setFloodedLevel(surveyInfoFragment.floorLevel.getTag().toString());// 水淹等级
        }
        if (surveyInfoFragment.isFirstPlace.getTag() != null) {
            survBaseInfoDto.setFirstSiteFlagQuery(surveyInfoFragment.isFirstPlace.getTag().toString());// 是否第一现场
        }
        if (surveyInfoFragment.caseType.getTag() != null) {
            survBaseInfoDto.setDamageTypeCode(surveyInfoFragment.caseType.getTag().toString());// 事故原因
        }
        /**
         * add by zhengyg
         * 2018年11月23日15:17:19
         * 新增 北分所需字段，有无交管事故书
         */
        if (surveyInfoFragment.haveBook.getTag() != null) {
            survBaseInfoDto.setDamageTypeCode(surveyInfoFragment.haveBook.getTag().toString());// 有无交管事故书
        }

        survBaseInfoDto.setCheckSite(surveyInfoFragment.sryAddress.getText().toString());// 查勘地点
        survBaseInfoDto.setContext(surveyInfoFragment.opinion.getText().toString());// 查勘意见
        survBaseInfoDto.setChecker1IdentNum(surveyInfoFragment.sryIdcard.getText().toString());// 查勘人员身份证号
        survBaseInfoDto.setCheckDate(surveyInfoFragment.riskTime.getText().toString());// 查勘日期
        survBaseInfoDto.setSumLossFee(surveyInfoFragment.amount.getText().toString());// 预估金额

        survBaseInfoDto.setCreateTime(DateUtil.getTime_YMDMS(new Date()));
        survBaseInfoDto.save();


        OtherInfoFragment otherInfoFragment = (OtherInfoFragment) adapter.getOtherInfoFragment();
        carLossInfoDto.setLicenseNo(otherInfoFragment.carNo.getText().toString());// 车牌号
        carLossInfoDto.setBrandName(otherInfoFragment.model.getText().toString());// 车型名称
        if (otherInfoFragment.carNoType.getTag() != null) {
            carLossInfoDto.setLicenseNoType(otherInfoFragment.carNoType.getTag().toString());// 号牌种类
        }
        if (otherInfoFragment.carType.getTag() != null) {
            carLossInfoDto.setCarKindCode(otherInfoFragment.carType.getTag().toString());// 车辆种类
        }
        carLossInfoDto.setDriverName(otherInfoFragment.name.getText().toString());// 驾驶人姓名
        carLossInfoDto.setIdentifyNumber(otherInfoFragment.creditNo.getText().toString());// 证件号码
        carLossInfoDto.setDrivingLicenseNo(otherInfoFragment.creditNo.getText().toString());// 驾驶证号码(和身份证一致)
        carLossInfoDto.setLossMoney(otherInfoFragment.amount.getText().toString());// 损失预估金额
        carLossInfoDto.setExceptSumLossFee(otherInfoFragment.amount.getText().toString());// 损失预估金额

        if (otherInfoFragment.ciIndemDuty.getTag() != null) {
            carLossInfoDto.setCiIndemDuty(otherInfoFragment.ciIndemDuty.getTag().toString());// 交强险责任
        }
        if (otherInfoFragment.noPushReason.getTag() != null) {
            carLossInfoDto.setNoPushReason(otherInfoFragment.noPushReason.getTag().toString());// 不推修原因
        }
        if (otherInfoFragment.lossPart.getTag() != null) {
            carLossInfoDto.setLossPart(otherInfoFragment.lossPart.getTag().toString());// 受损部位
        }
        if (otherInfoFragment.dutyType.getTag() != null) {
            carLossInfoDto.setDutyType(otherInfoFragment.dutyType.getTag().toString());// 商业险责任
        }

        carLossInfoDto.setCreateTime(DateUtil.getTime_YMDMS(new Date()));
        carLossInfoDto.save();
    }

    private void initView() {

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
                Intent intent = new Intent(CaseSurveyActivity.this, ImageListActivity.class);
                intent.putExtra("reportNo", reportNo);
                intent.putExtra("taskNo", taskNo);
                startActivity(intent);
            }
        });

        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pager.getCurrentItem() == 0) {//若当前页面在基本信息页，则执行到标的页的跳转

                    saveData();

                    if (!baseInfoCompleteCheck()) {
                        return;
                    }

                    pager.setCurrentItem(1);

                } else if (pager.getCurrentItem() == 1) {//若当前页面在标的页，则执行向三者车页的跳转

                    saveData();

                    if (!completeCheck()) {
                        return;
                    }

                    Intent intent = new Intent(CaseSurveyActivity.this, OtherCarActivity.class);
                    intent.putExtra("copyMainInfo", copyMainInfoDto);
                    intent.putExtra("mission", mission);
                    startActivity(intent);
                    finish();

                }
            }
        });

        //ViewPager的adapter
        adapter = new TabPageIndicatorAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);

        underline.setViewPager(pager);
        underline.setFades(false);

        //实例化TabPageIndicator然后设置ViewPager与之关联
        indicator.setViewPager(pager);
        indicator.setOnPageChangeListener(underline);


        //如果我们要对ViewPager设置监听，用indicator设置就行了
        underline.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    private void initData() {

        mission = (Mission) getIntent().getSerializableExtra("mission");
        copyMainInfoDto = (CopyMainInfoDto) getIntent().getSerializableExtra("copyMainInfo");
        reportNo = mission.getRptNo();
        taskNo = mission.getTaskId();

        survBaseInfoDto = DataSupport.where("reportNo = ? and taskNo = ?", reportNo, taskNo)
                .order("createTime desc").findFirst(SurvBaseInfoDto.class);
        if(copyMainInfoDto.getSubmitSurvMainInfoDto()!=null){
            survBaseInfoDto = copyMainInfoDto.getSubmitSurvMainInfoDto().getSurvBaseInfoDto();//获取基本信息回显
            survBaseInfoDto.setReportNo(reportNo);
            survBaseInfoDto.setTaskNo(taskNo);
        }
        if (survBaseInfoDto == null) {
            survBaseInfoDto = new SurvBaseInfoDto();
            survBaseInfoDto.setReportNo(reportNo);
            survBaseInfoDto.setTaskNo(taskNo);
            if (!StringUtils.isEmpty(copyMainInfoDto.getCopyReportInfo().getDamageReason())) {
                List<KeyValue> keyValues = StateSelect.initArray(this, R.array.CXYY_value, R.array.CXYY_key);
                survBaseInfoDto.setDamageCode(StateSelect.getTextKey(copyMainInfoDto.getCopyReportInfo().getDamageReason(), keyValues));
                //初始化是否互碰自赔。
                survBaseInfoDto.setCaseFlag(copyMainInfoDto.getCopyReportInfo().getIsSelfCompenClaim());
            }
        }

        carLossInfoDto = DataSupport.where("reportNo = ? and taskNo = ? and lossItemType = ?", reportNo, taskNo, LOSSTYPECAR)
                .order("createTime desc").findFirst(CarLossInfoDto.class);//标的信息
        if(copyMainInfoDto.getSubmitSurvMainInfoDto()!=null){
            carLossInfoDto = copyMainInfoDto.getSubmitSurvMainInfoDto().getCarLossInfoDto();//获取标的信息回显
            carLossInfoDto.setReportNo(reportNo);
            carLossInfoDto.setTaskNo(taskNo);
        }
        if (carLossInfoDto == null) {
            carLossInfoDto = new CarLossInfoDto();
            carLossInfoDto.setReportNo(reportNo);
            carLossInfoDto.setTaskNo(taskNo);
            carLossInfoDto.setLossItemType(LOSSTYPECAR);//标的信息

            Activity activity = ActivityManagerUtils.getInstance().getActivityclass(TaskProcessActivity.class);
            if (activity != null) {
                CopyMainInfoDto copyMainInfoDto = ((TaskProcessActivity) activity).copyMainInfo;
                if (copyMainInfoDto != null) {
                    carLossInfoDto.setLicenseNo(copyMainInfoDto.getCopyCarInfo().getCarNo());
                    carLossInfoDto.setBrandName(copyMainInfoDto.getCopyCarInfo().getBrandModels());
                    carLossInfoDto.setLicenseNoType(copyMainInfoDto.getCopyCarInfo().getCarNoType());
                    //初始化驾驶人姓名、车架号、发动机号 add by zhengyg 2018年11月27日19:24:22
                    carLossInfoDto.setDriverName(copyMainInfoDto.getCopyReportInfo().getDriverName());
                    carLossInfoDto.setFrameNo(copyMainInfoDto.getCopyCarInfo().getVinNo());
                    carLossInfoDto.setEngineNo(copyMainInfoDto.getCopyCarInfo().getEnginNo());
                    carLossInfoDto.setBrandName(copyMainInfoDto.getListPolicy().get(0).getBrandName());
                }
            }
        }

        CopyReportInfoDto copyReportInfo = (this).copyMainInfoDto.getCopyReportInfo();
        if (!StringUtils.isEmpty(copyReportInfo.getComCode()) && copyReportInfo.getComCode().startsWith("0111")) {
            isBeifen = true;
        }
    }

    /**
     * 基本信息完成检测
     *
     * @return
     */
    public boolean baseInfoCompleteCheck() {
        //查勘信息校验
        if (survBaseInfoDto == null) {
            ToastUtils.showShort(this, "查勘信息为空");
            return false;
        } else if (StringUtils.isEmpty(survBaseInfoDto.getLossType())) {
            ToastUtils.showShort(this, "请选择查勘信息-损失类别");
            return false;
        } else if (StringUtils.isEmpty(survBaseInfoDto.getDamageCode())) {
            ToastUtils.showShort(this, "请选择查勘信息-出险原因");
            return false;
        } else if (StringUtils.isEmpty(survBaseInfoDto.getDamageCaseCode())) {
            ToastUtils.showShort(this, "请选择查勘信息-事故类型");
            return false;
        } else if (StringUtils.isEmpty(survBaseInfoDto.getInsureAccident())) {
            ToastUtils.showShort(this, "请选择查勘信息-保险事故分类");
            return false;
        } else if (StringUtils.isEmpty(survBaseInfoDto.getCheckDate())) {
            ToastUtils.showShort(this, "请填写查勘信息-查勘日期");
            return false;
        } else if (StringUtils.isEmpty(survBaseInfoDto.getCheckSite())) {
            ToastUtils.showShort(this, "请填写查勘信息-查勘地点");
            return false;
        }
//        else if (StringUtils.isEmpty(survBaseInfoDto.getChecker1IdentNum())){
//            ToastUtils.showShort(this, "请录入查勘信息-查勘员身份证号码");
//            return false;
//        }
        else if (survBaseInfoDto.getClaimFlag().equals("1") && StringUtils.isEmpty(survBaseInfoDto.getIndemnityDuty())) {//如果为商业险责任，则必选
            ToastUtils.showShort(this, "请录入查勘信息-商业险赔偿责任");
            return false;
        } else if (survBaseInfoDto.getIsFloodedCar().equals("1") && StringUtils.isEmpty(survBaseInfoDto.getFloodedLevel())) {//如果为水淹，则必选水淹等级
            ToastUtils.showShort(this, "请录入查勘信息-水淹等级");
            return false;
        } else if (StringUtils.isEmpty(survBaseInfoDto.getFirstSiteFlagQuery())) {
            ToastUtils.showShort(this, "请录入查勘信息-是否第一现场");
            return false;
        } else if (StringUtils.isEmpty(survBaseInfoDto.getSumLossFee())) {
            ToastUtils.showShort(this, "请录入查勘信息-预估金额");
            return false;
        } else if (StringUtils.isEmpty(survBaseInfoDto.getContext())) {
            ToastUtils.showShort(this, "请填写查勘信息-查勘意见");
            return false;
        } else {
            return true;
        }
    }


    /**
     * 必录项填写校验
     *
     * @return
     */
    public boolean completeCheck() {
        String cph = carLossInfoDto.getLicenseNo().substring(carLossInfoDto.getLicenseNo().length() - 1);//车牌号最后一位
        String cph2 = carLossInfoDto.getLicenseNo().substring(0,1);//车牌号第一位
        //查勘信息校验
        if (survBaseInfoDto == null) {
            ToastUtils.showShort(this, "查勘信息为空");
            return false;
        } else if (StringUtils.isEmpty(survBaseInfoDto.getLossType())) {
            ToastUtils.showShort(this, "请选择查勘信息-损失类别");
            return false;
        } else if (StringUtils.isEmpty(survBaseInfoDto.getDamageCode())) {
            ToastUtils.showShort(this, "请选择查勘信息-出险原因");
            return false;
        } else if (StringUtils.isEmpty(survBaseInfoDto.getDamageCaseCode())) {
            ToastUtils.showShort(this, "请选择查勘信息-事故类型");
            return false;
        } else if (StringUtils.isEmpty(survBaseInfoDto.getInsureAccident())) {
            ToastUtils.showShort(this, "请选择查勘信息-保险事故分类");
            return false;
        } else if (StringUtils.isEmpty(survBaseInfoDto.getCheckDate())) {
            ToastUtils.showShort(this, "请填写查勘信息-查勘日期");
            return false;
        } else if (StringUtils.isEmpty(survBaseInfoDto.getCheckSite())) {
            ToastUtils.showShort(this, "请填写查勘信息-查勘地点");
            return false;
        }
//        else if (StringUtils.isEmpty(survBaseInfoDto.getChecker1IdentNum())){
//            ToastUtils.showShort(this, "请录入查勘信息-查勘员身份证号码");
//            return false;
//        }
        else if (survBaseInfoDto.getClaimFlag().equals("1") && StringUtils.isEmpty(survBaseInfoDto.getIndemnityDuty())) {//如果为商业险责任，则必选
            ToastUtils.showShort(this, "请录入查勘信息-商业险赔偿责任");
            return false;
        } else if (survBaseInfoDto.getIsFloodedCar().equals("1") && StringUtils.isEmpty(survBaseInfoDto.getFloodedLevel())) {//如果为水淹，则必选水淹等级
            ToastUtils.showShort(this, "请录入查勘信息-水淹等级");
            return false;
        } else if (StringUtils.isEmpty(survBaseInfoDto.getFirstSiteFlagQuery())) {
            ToastUtils.showShort(this, "请录入查勘信息-是否第一现场");
            return false;
        } else if (StringUtils.isEmpty(survBaseInfoDto.getSumLossFee())) {
            ToastUtils.showShort(this, "请录入查勘信息-预估金额");
            return false;
        } else if (StringUtils.isEmpty(survBaseInfoDto.getContext())) {
            ToastUtils.showShort(this, "请填写查勘信息-查勘意见");
            return false;
        }
        //标的信息校验
        else if (carLossInfoDto == null) {
            ToastUtils.showShort(this, "标的信息为空");
            return false;
        } else if (StringUtils.isEmpty(carLossInfoDto.getLicenseNo())) {
            ToastUtils.showShort(this, "请填写标的信息-车牌号");
            return false;
        }

        /**
         * //对车牌号进行检验
         *  1.非使/领馆车规则：
         *  1)   仅允许录入以汉字、   两位大写英文字母开头的车牌号。
         *  2)   黑龙江允许录入以“08”或“38”开头的车牌号。
         *
         */
//        else if ((!cph.equals("使") && !cph.equals("领") && !cph2.equals("领")) && !cph2.equals("黑") && (!isAcronym(carLossInfoDto.getLicenseNo().substring(1, 3)))) {
//            ToastUtils.showShort(this, "仅允许录入以汉字、两位大写英文字母开头的车牌号");
//            return false;
//        }
//        else if (cph2.equals("黑") && (!carLossInfoDto.getLicenseNo().substring(1, 3).equals("08") && !carLossInfoDto.getLicenseNo().substring(1, 3).equals("38")) && (!isAcronym(carLossInfoDto.getLicenseNo().substring(1, 3)))) {
//            ToastUtils.showShort(this, "请输入正确的黑龙江车牌号");
//            return false;
//        }




        /*else if (StringUtils.isEmpty(carLossInfoDto.getBrandName())){
            ToastUtils.showShort(this, "请填写标的信息-车型名称");
            return false;
        }*/
        else if (StringUtils.isEmpty(carLossInfoDto.getLicenseNoType())) {
            ToastUtils.showShort(this, "请选择标的信息-号牌种类");
            return false;
        } else if (!isBeifen && StringUtils.isEmpty(carLossInfoDto.getCarKindCode())) {
            ToastUtils.showShort(this, "请选择标的信息-车辆种类");
            return false;
        } else if (StringUtils.isEmpty(carLossInfoDto.getDriverName())) {
            ToastUtils.showShort(this, "请填写标的信息-驾驶人姓名");
            return false;
        } else if (StringUtils.isEmpty(carLossInfoDto.getIdentifyNumber())) {
            ToastUtils.showShort(this, "请填写标的信息-身份证号");
            return false;
        }
//        else if (StringUtils.isEmpty(carLossInfoDto.getDutyType())){
//            ToastUtils.showShort(this, "请填写标的信息-商业险赔偿责任");
//            return false;
//        }else if (StringUtils.isEmpty(carLossInfoDto.getCiIndemDuty())){
//            ToastUtils.showShort(this, "请填写标的信息-交强险责任类型");
//            return false;
//        }
        else if (carLossInfoDto.getRepairFlag().equals("0") && StringUtils.isEmpty(carLossInfoDto.getNoPushReason())) {//如果不维修,则必须有不维修原因
            ToastUtils.showShort(this, "请填写标的信息-不推修原因");
            return false;
        } else if ((carLossInfoDto.getDamageFlag().equals("1") || carLossInfoDto.getRepairFlag().equals("1")) && StringUtils.isEmpty(carLossInfoDto.getLossPart())) {
            ToastUtils.showShort(this, "请填写标的信息-受损部位");
            return false;
        } else if (StringUtils.isEmpty(carLossInfoDto.getExceptSumLossFee())) {
            ToastUtils.showShort(this, "请填写标的信息-预估金额");
            return false;
        }
        //全部满足则完成检测
        else {
            return true;
        }

    }


    @Override
    protected void onStop() {

        if (((SurveyInfoFragment) adapter.getSurveyInfoFragment()).OCRstart) {
            ((SurveyInfoFragment) adapter.getSurveyInfoFragment()).OCRstart = false;
            super.onStop();
            return;
        }

        if (((OtherInfoFragment) adapter.getOtherInfoFragment()).OCRstart) {
            ((OtherInfoFragment) adapter.getOtherInfoFragment()).OCRstart = false;
            super.onStop();
            return;
        }

        saveData();
        /**
         * 改变流程图颜色
         */
        if (completeCheck()) {
            Activity activity = ActivityManagerUtils.getInstance().getActivityclass(TaskProcessActivity.class);
            if (activity != null) {
                ((TaskProcessActivity) activity).changeProcessStyle(((TaskProcessActivity) activity).caseSurvey);
                ((TaskProcessActivity) activity).survMainInfoDto.setBaseInfoFlag(1);
                ((TaskProcessActivity) activity).survMainInfoDto.save();
            }
        } else {
            Activity activity = ActivityManagerUtils.getInstance().getActivityclass(TaskProcessActivity.class);
            if (activity != null) {
                ((TaskProcessActivity) activity).returnProcessStyle(((TaskProcessActivity) activity).caseSurvey);
                ((TaskProcessActivity) activity).survMainInfoDto.setBaseInfoFlag(0);
                ((TaskProcessActivity) activity).survMainInfoDto.save();
            }
        }

        super.onStop();
    }

    /**
     * ViewPager适配器
     *
     * @author len
     */
    public class TabPageIndicatorAdapter extends FragmentPagerAdapter {

        private Fragment surveyInfoFragment;
        private Fragment otherInfoFragment;

        public TabPageIndicatorAdapter(FragmentManager fm) {
            super(fm);
            surveyInfoFragment = new SurveyInfoFragment();
            otherInfoFragment = new OtherInfoFragment();
        }

        public Fragment getSurveyInfoFragment() {
            return surveyInfoFragment;
        }

        public Fragment getOtherInfoFragment() {
            return otherInfoFragment;
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = null;

            switch (position) {
                case 0: {
                    fragment = surveyInfoFragment;
                }
                break;
                case 1: {
                    fragment = otherInfoFragment;
                }
                break;
                default:
                    break;
            }

            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return INDICATORTITLE[position % INDICATORTITLE.length];
        }

        @Override
        public int getCount() {
            return INDICATORTITLE.length;
        }
    }


    //判断大小写
    public static boolean isAcronym(String word) {
        boolean b = false;
        for (int i = 0; i < word.length(); i++) {
            // Complete this case
            b = Character.isUpperCase(word.charAt(i));
        }
        return b;
    }
}
