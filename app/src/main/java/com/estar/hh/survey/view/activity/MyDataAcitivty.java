package com.estar.hh.survey.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.estar.hh.survey.R;
import com.estar.hh.survey.common.UserSharePrefrence;
import com.estar.hh.survey.constants.Constants;
import com.estar.hh.survey.entity.entity.User;
import com.estar.hh.survey.entity.request.SaveDataRequest;
import com.estar.hh.survey.entity.response.SaveDataResponse;
import com.estar.hh.survey.utils.HttpClientUtil;
import com.estar.hh.survey.utils.LogUtils;
import com.estar.hh.survey.view.component.MyProgressDialog;
import com.estar.utils.StringUtils;
import com.google.gson.Gson;
import com.yongchun.library.view.ImageSelectorActivity;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.util.List;

import static com.estar.hh.survey.service.MyIntentService.ConvertBase64HH;
import static com.estar.hh.survey.utils.TextUtils.setEditTextOnlyForWordAndChar;

/**
 * Created by Administrator on 2017/8/9.
 * 个人资料界面
 */

public class MyDataAcitivty extends HuangheBaseActivity{

    @ViewInject(R.id.my_data_back)
    private LinearLayout back;

    @ViewInject(R.id.my_data_icon_select)
    private LinearLayout icon_select;

    @ViewInject(R.id.my_data_icon)
    private ImageView icon;

    @ViewInject(R.id.my_data_name)
    private EditText name;

    @ViewInject(R.id.my_data_gender)
    private RadioGroup sexGroup;

    @ViewInject(R.id.my_data_gender_male)
    private RadioButton male;

    @ViewInject(R.id.my_data_gender_female)
    private RadioButton female;

    @ViewInject(R.id.my_data_submit)
    private Button submit;

    private String imageUri;//保存当前图片地址
    private String fileData;//图片文件BASE64码
    private String sex = "0";//性别选择
    private UserSharePrefrence userSharePrefrence = null;
    private User user = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_data_activity);
        x.view().inject(this);

        initView();
        initData();

    }

    private void initView(){

        setEditTextOnlyForWordAndChar(name);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        icon_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * @param context
                 * @param maxselectNum 最大照片选择数
                 * @param mode 模式 1-多重模式  2-单个模式
                 * @param isShow 是否展示
                 * @param enablePreview 是否支持预览图
                 * @param enableCrop 是否支持裁剪
                 */
                ImageSelectorActivity.start(MyDataAcitivty.this, 1, 2, true, true, true);
            }
        });

        sexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.my_data_gender_male){
                    sex = "1";
                }else if (checkedId == R.id.my_data_gender_female){
                    sex = "2";
                }else {
                    sex = "0";
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!StringUtils.isEmpty(imageUri)){
                    try {
                        fileData = ConvertBase64HH(imageUri).replace("+", "%2B");//获取图片base64码
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                submitData();
            }
        });
    }

    private void initData(){

        userSharePrefrence = new UserSharePrefrence(this);
        user = userSharePrefrence.getUserEntity();

        {

            Glide.with(this)
                    .load(Constants.BASE_IP + user.getHeadUrl())
                    .error(R.drawable.logo)
                    .centerCrop()
                    .into(icon);

            name.setText(user.getRealName());

            if (!StringUtils.isEmpty(user.getSex())){
                switch (user.getSex()){
                    case "0":{
                        sex = "0";
                    }break;
                    case "1":{
                        sex = "1";
                        male.performClick();
                    }break;
                    case "2":{
                        sex = "2";
                        female.performClick();
                    }break;
                }
            }
        }

        getMyData();

    }

    /**
     * 获取个人数据
     */
    private void getMyData(){

    }

    /**
     * 保存提交个人数据
     */
    private void submitData(){

        final MyProgressDialog dialog = new MyProgressDialog(this, "资料提交中...");

        SaveDataRequest request = new SaveDataRequest();
        request.getData().setRealName(name.getText().toString());
        request.getData().setSex(sex);
        request.getData().setFileData(fileData);
        request.getData().setTelNo(user.getEmpCde());
        final Gson gson = new Gson();
        String reqMsg = gson.toJson(request);

//        RequestParams params = HttpUtils.buildRequestParam(Constants.SAVEMYDATA, reqMsg);
        RequestParams params = HttpClientUtil.getHttpRequestParam(Constants.SAVEMYDATA, reqMsg);

        LogUtils.i("提交个人数据请求参数为:", "-------------------------------------------\n" + reqMsg);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
                LogUtils.i("提交个人数据返回参数为:", "--------------------------------------------\n" + result);
                SaveDataResponse response = gson.fromJson(result, SaveDataResponse.class);
                if (response.getSuccess()){
                    user.setSex(response.getObj().getSex());
                    user.setHeadUrl(response.getObj().getHeadUrl());
                    user.setRealName(response.getObj().getRealName());
                    userSharePrefrence.setUserEntity(user);
                    showShortToast("个人信息提交成功");
                    finish();
                }else{
                    showShortToast(response.getMsg());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.i("提交个人数据请求错误为:", "--------------------------------------------\n" + ex.getMessage());
                showShortToast(ex.getLocalizedMessage());
                ex.printStackTrace();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.i("提交个人数据请求取消为:", "--------------------------------------------\n" + cex.getMessage());
                showShortToast(cex.getLocalizedMessage());
                cex.printStackTrace();
            }

            @Override
            public void onFinished() {
                dialog.stopDialog();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){//处理图片选择返回
            //拿到返回的图片地址清单
            List<String> images = data.getStringArrayListExtra(ImageSelectorActivity.REQUEST_OUTPUT);
            if (images != null && images.size() > 0){
                imageUri = images.get(0);
                Glide.with(this).load(images.get(0)).into(icon);
            }
        }
    }
}
