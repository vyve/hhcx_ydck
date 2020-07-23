package com.estar.hh.survey.view.activity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.estar.hh.survey.R;
import com.estar.hh.survey.component.StateSelectDialog;
import com.estar.hh.survey.constants.StateSelect;
import com.estar.hh.survey.entity.entity.KeyValue;
import com.estar.hh.survey.entity.vo.addresses.CityModel;
import com.estar.hh.survey.entity.vo.addresses.ProvinceBean;
import com.estar.hh.survey.entity.vo.addresses.ProvinceModel;
import com.estar.hh.survey.service.XmlParserHandler;
import com.estar.hh.survey.utils.LogUtils;
import com.estar.ocr.backcard.bankcode.NewBackOcrActivity;
import com.estar.ocr.common.ValueForKey;
import com.estar.ocr.common.camera.OcrVO;
import com.google.gson.Gson;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Administrator on 2017/9/30 0030.
 * 收款账户详情
 */

public class InCountDetailActivity extends HuangheBaseActivity {

    @ViewInject(R.id.in_count_detail_back)
    private LinearLayout back;

    @ViewInject(R.id.in_count_detail_list)
    private ListView incountList;

    @ViewInject(R.id.in_count_detail_up_step)
    private LinearLayout upStep;

    @ViewInject(R.id.in_count_detail_take_pic)
    private LinearLayout takePic;

    @ViewInject(R.id.in_count_detail_next_step)
    private LinearLayout nextStep;

    private View content;

    private LinearLayout typeLayout;
    private TextView type;
    private EditText riskType;
    private LinearLayout bankTypeLayout;
    private TextView bankType;
    private TextView bankAddress;
    private EditText bankName;
    private EditText bankNo;
    private TextView bankNoOcr;
    private EditText manName;
    private LinearLayout manClassLayout;
    private TextView manClass;
    private LinearLayout manKindLayout;
    private TextView manKind;
    private LinearLayout cardKindLayout;
    private TextView cardKind;
    private EditText cardNo;
    private EditText telNo;

    /**省市区*/
    public ArrayList<ProvinceBean> options1Items = new ArrayList<ProvinceBean>();
    public ArrayList<ArrayList<ProvinceBean>> options2Items = new ArrayList<ArrayList<ProvinceBean>>();
    private OptionsPickerView pvOptions;


    private StateSelectDialog typeDialog;
    private List<KeyValue> typeStates = new ArrayList<>();
    private StateSelectDialog bankTypeDialog;
    private List<KeyValue> bankTypeStates = new ArrayList<>();
    private StateSelectDialog manClassDialog;
    private List<KeyValue> manClassStates = new ArrayList<>();
    private StateSelectDialog manKindDialog;
    private List<KeyValue> manKindStates = new ArrayList<>();
    private StateSelectDialog cardKindDialog;
    private List<KeyValue> cardKindStates = new ArrayList<>();

    private List<Integer> requestCodes = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_count_detail_activity);
        x.view().inject(this);

        initView();
        initData();

    }


    private void initView(){

        content = LayoutInflater.from(this).inflate(R.layout.in_count_detail_content, null);

        typeLayout = content.findViewById(R.id.in_count_detail_type_layout);
        type = content.findViewById(R.id.in_count_detail_type);
        riskType = content.findViewById(R.id.in_count_detail_risktype);
        bankTypeLayout = content.findViewById(R.id.in_count_detail_banktype_layout);
        bankType = content.findViewById(R.id.in_count_detail_banktype);
        bankAddress = content.findViewById(R.id.in_count_detail_bankaddress);
        bankName = content.findViewById(R.id.in_count_detail_bankname);
        bankNo = content.findViewById(R.id.in_count_detail_bankno);
        bankNoOcr = content.findViewById(R.id.in_count_detail_bankno_ocr);
        manName = content.findViewById(R.id.in_count_detail_man);
        manClassLayout = content.findViewById(R.id.in_count_detail_manclass_layout);
        manClass = content.findViewById(R.id.in_count_detail_manclass);
        manKindLayout = content.findViewById(R.id.in_count_detail_mankind_layout);
        manKind = content.findViewById(R.id.in_count_detail_mankind);
        cardKindLayout = content.findViewById(R.id.in_count_detail_cardkind_layout);
        cardKind = content.findViewById(R.id.in_count_detail_cardkind);
        cardNo = content.findViewById(R.id.in_count_detail_cardno);
        telNo = content.findViewById(R.id.in_count_detail_telno);

        bankAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvOptions.show();//省市联动
            }
        });

        incountList.addHeaderView(content);
        incountList.setAdapter(null);

    }

    private void initData(){
        //获取Activity传递过来的参数

        requestCodes.add(11);
        requestCodes.add(12);
        requestCodes.add(13);
        requestCodes.add(14);
        requestCodes.add(15);
        requestCodes.add(16);

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
                bankAddress.setText(tx);

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
                Intent intent = new Intent(InCountDetailActivity.this, ImageListActivity.class);
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

        bankNoOcr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(InCountDetailActivity.this, NewBackOcrActivity.class), 15);
            }
        });

        List<KeyValue> types = StateSelect.initArray(this, R.array.CZCL_value, R.array.CZCL_key);
        if (types != null && types.size() > 0) {
            typeStates.clear();
            typeStates.addAll(types);
        }
        typeDialog = new StateSelectDialog(this, typeStates, type);
        typeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeDialog.show();
            }
        });

        List<KeyValue> bankTypes = StateSelect.initArray(this, R.array.CZCL_value, R.array.CZCL_key);
        if (bankTypes != null && bankTypes.size() > 0) {
            bankTypeStates.clear();
            bankTypeStates.addAll(bankTypes);
        }
        bankTypeDialog = new StateSelectDialog(this, bankTypeStates, bankType);
        bankTypeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bankTypeDialog.show();
            }
        });

        List<KeyValue> manClasses = StateSelect.initArray(this, R.array.CZCL_value, R.array.CZCL_key);
        if (manClasses != null && manClasses.size() > 0) {
            manClassStates.clear();
            manClassStates.addAll(manClasses);
        }
        manClassDialog = new StateSelectDialog(this, manClassStates, manClass);
        manClassLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manClassDialog.show();
            }
        });

        List<KeyValue> manKinds = StateSelect.initArray(this, R.array.CZCL_value, R.array.CZCL_key);
        if (manKinds != null && manKinds.size() > 0) {
            manKindStates.clear();
            manKindStates.addAll(manKinds);
        }
        manKindDialog = new StateSelectDialog(this, manKindStates, manKind);
        manKindLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manKindDialog.show();
            }
        });

        List<KeyValue> cardKinds = StateSelect.initArray(this, R.array.CZCL_value, R.array.CZCL_key);
        if (cardKinds != null && cardKinds.size() > 0) {
            cardKindStates.clear();
            cardKindStates.addAll(cardKinds);
        }
        cardKindDialog = new StateSelectDialog(this, cardKindStates, cardKind);
        cardKindLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardKindDialog.show();
            }
        });

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
                    }
                }
            }
        }
    }

}
