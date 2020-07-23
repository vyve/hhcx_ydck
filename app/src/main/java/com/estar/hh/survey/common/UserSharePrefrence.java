package com.estar.hh.survey.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.estar.hh.survey.entity.entity.User;
import com.estar.hh.survey.utils.AesUtil;



/**
 * Created by Administrator on 2017/11/1 0001.
 * 用户数据
 */

public class UserSharePrefrence {

    private SharedPreferences user;
    private SharedPreferences.Editor userEditor;

    private static final String SPNAME = "user";

    public UserSharePrefrence(Context context) {
        user = context.getSharedPreferences(SPNAME, Context.MODE_PRIVATE);
        userEditor = user.edit();
    }

    public User getUserEntity() {
        User entity = new User();
        entity.setRealName(getString("realName"));
        entity.setTelNo(getString("telNo"));
        entity.setUserName(getString("userName"));
        entity.setUserIdNo(getString("userIdNo"));
        entity.setEmpCde(getString("empCde"));
        entity.setPassWord(getString("passWord"));
        entity.setHeadUrl(getString("headUrl"));
        entity.setSex(getString("sex"));
        entity.setOrgCode(getString("orgCode"));
        entity.setOrgName(getString("orgName"));
        entity.setSign(getString("sign"));
        entity.setScore(getString("score"));
        entity.setaTaskCount(getString("aTaskCount"));
        entity.setsTaskCount(getString("sTaskCount"));
        entity.setrTaskCount(getString("rTaskCount"));
        entity.setmTaskCount(getString("mTaskCount"));
        entity.setMonthMileage(getString("monthMileage"));
        entity.setSumMileage(getString("sumMileage"));

        entity.setSumSurvyTaskCount(getString("sumSurvyTaskCount"));
        entity.setMonthSurvyTaskCount(getString("monthSurvyTaskCount"));
        entity.setSumLossTaskCount(getString("sumLossTaskCount"));
        entity.setMonthLossTaskCount(getString("monthLossTaskCount"));

        entity.setBranchComCode(getString("branchComCode"));
        entity.setBranchComName(getString("branchComName"));
//        entity.setPwdState(getString("pwdState",""));
//        entity.setPwdTip(getString("pwdTip",""));
        return entity;
    }

    public void setUserEntity(User entity) {
        //        userEditor.putString("pwdState", entity.getPwdState());
//        userEditor.putString("pwdTip", entity.getPwdTip());
        putString("realName", entity.getRealName());
        putString("telNo", entity.getTelNo());
        putString("userIdNo", entity.getUserIdNo());
        //        userEditor.putString("userName", entity.getUserName());
        putString("empCde", entity.getEmpCde());
        //        userEditor.putString("passWord", entity.getPassWord());
        putString("headUrl", entity.getHeadUrl());
        putString("sex", entity.getSex());
        putString("orgCode", entity.getOrgCode());
        putString("orgName", entity.getOrgName());
        putString("sign", entity.getSign());
        putString("score", entity.getScore());
        putString("usertoken", entity.getUsertoken());
        putString("aTaskCount", entity.getaTaskCount());
        putString("sTaskCount", entity.getsTaskCount());
        putString("rTaskCount", entity.getrTaskCount());
        putString("mTaskCount", entity.getmTaskCount());
        putString("monthMileage", entity.getMonthMileage());
        putString("sumMileage", entity.getSumMileage());

        putString("sumSurvyTaskCount", entity.getSumSurvyTaskCount());
        putString("monthSurvyTaskCount", entity.getMonthSurvyTaskCount());
        putString("sumLossTaskCount", entity.getSumLossTaskCount());
        putString("monthLossTaskCount", entity.getMonthLossTaskCount());
        if (android.text.TextUtils.isEmpty(entity.getBranchComCode())) {
            putString("branchComCode", entity.getOrgCode());
        } else {
            putString("branchComCode", entity.getBranchComCode());
        }
        if (android.text.TextUtils.isEmpty(entity.getBranchComName())) {
            putString("branchComName", entity.getOrgName());
        } else {
            putString("branchComName", entity.getBranchComName());
        }

//        commitData();
    }

    public String getString(String key) {
        String value = user.getString(key, "");
        if (TextUtils.isEmpty(value)){
            return "";
        }
        AesUtil aesUtil = new AesUtil();
        String encrypt = aesUtil.decrypt(value);
        return encrypt;
    }

    public boolean getBoolean(String key) {
        return user.getBoolean(key, false);
    }

    public void putString(String key, String value) {
        if (TextUtils.isEmpty(value)){
            return;
        }
        AesUtil aesUtil = new AesUtil();
        String encrypt = aesUtil.encrypt(value);
        userEditor.putString(key, encrypt);
        userEditor.commit();
    }

    public void commitData() {
        userEditor.commit();
    }

}
