package com.estar.hh.survey.common;

import com.estar.hh.survey.MyApplication;
import com.estar.hh.survey.utils.AesUtil;

import org.xutils.common.Callback;

public abstract class MyStringCallback implements Callback.CacheCallback<String> {
    @Override
    public void onSuccess(String result) {
        boolean isAesString = MyApplication.isAesString();
        if(isAesString){
            String resultAes = new AesUtil().encrypt(result);
            onMySuccess(resultAes);
        }else {
            onMySuccess(result);
        }
    }
    public abstract void onMySuccess(String result);
}
