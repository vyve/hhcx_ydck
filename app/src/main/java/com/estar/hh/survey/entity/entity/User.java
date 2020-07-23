package com.estar.hh.survey.entity.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/4 0004.
 */

public class User extends DataSupport implements Serializable {

    private String realName;
    private String telNo;
    private String empCde;//工号
    private String userName;//用户名
    private String userIdNo;//证件号码
    private String passWord;
    private String headUrl;
    private String sex;
    private String orgCode;
    private String orgName;
    private String sign;
    private String score;
    private String aTaskCount;
    private String sTaskCount;
    private String rTaskCount;
    private String mTaskCount;
    private String monthMileage;
    private String sumMileage;

    private String sumSurvyTaskCount;//总查勘单量
    private String monthSurvyTaskCount;//月查勘单量
    private String sumLossTaskCount;//总定损单量
    private String monthLossTaskCount;//月定损单量

    private String branchComCode = "";// 机构代码(中支)
    private String branchComName = "";// 机构名称(中支)
    private String userToken = "";
//    private String pwdState = "";
//    private String pwdTip = "";

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

//    public String getPwdState() {
//        return pwdState;
//    }
//
//    public void setPwdState(String pwdState) {
//        this.pwdState = pwdState;
//    }
//
//    public String getPwdTip() {
//        return pwdTip;
//    }
//
//    public void setPwdTip(String pwdTip) {
//        this.pwdTip = pwdTip;
//    }

    public String getUsertoken() {
        return userToken;
    }

    public void setUsertoken(String usertoken) {
        this.userToken = usertoken;
    }

    public void setSumSurvyTaskCount(String sumSurvyTaskCount) {
        this.sumSurvyTaskCount = sumSurvyTaskCount;
    }

    public void setMonthSurvyTaskCount(String monthSurvyTaskCount) {
        this.monthSurvyTaskCount = monthSurvyTaskCount;
    }

    public void setSumLossTaskCount(String sumLossTaskCount) {
        this.sumLossTaskCount = sumLossTaskCount;
    }

    public void setMonthLossTaskCount(String monthLossTaskCount) {
        this.monthLossTaskCount = monthLossTaskCount;
    }

    public String getSumSurvyTaskCount() {
        return sumSurvyTaskCount;
    }

    public String getMonthSurvyTaskCount() {
        return monthSurvyTaskCount;
    }

    public String getSumLossTaskCount() {
        return sumLossTaskCount;
    }

    public String getMonthLossTaskCount() {
        return monthLossTaskCount;
    }

    public void setUserIdNo(String userIdNo) {
        this.userIdNo = userIdNo;
    }

    public String getUserIdNo() {
        return userIdNo;
    }

    public void setEmpCde(String empCde) {
        this.empCde = empCde;
    }

    public String getEmpCde() {
        return empCde;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public void setaTaskCount(String aTaskCount) {
        this.aTaskCount = aTaskCount;
    }

    public void setsTaskCount(String sTaskCount) {
        this.sTaskCount = sTaskCount;
    }

    public void setrTaskCount(String rTaskCount) {
        this.rTaskCount = rTaskCount;
    }

    public void setmTaskCount(String mTaskCount) {
        this.mTaskCount = mTaskCount;
    }

    public void setMonthMileage(String monthMileage) {
        this.monthMileage = monthMileage;
    }

    public void setSumMileage(String sumMileage) {
        this.sumMileage = sumMileage;
    }

    public String getRealName() {
        return realName;
    }

    public String getTelNo() {
        return telNo;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public String getSex() {
        return sex;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public String getSign() {
        return sign;
    }

    public String getScore() {
        return score;
    }

    public String getaTaskCount() {
        return aTaskCount;
    }

    public String getsTaskCount() {
        return sTaskCount;
    }

    public String getrTaskCount() {
        return rTaskCount;
    }

    public String getmTaskCount() {
        return mTaskCount;
    }

    public String getMonthMileage() {
        return monthMileage;
    }

    public String getSumMileage() {
        return sumMileage;
    }

    public String getBranchComCode() {
        return branchComCode;
    }

    public void setBranchComCode(String branchComCode) {
        this.branchComCode = branchComCode;
    }

    public String getBranchComName() {
        return branchComName;
    }

    public void setBranchComName(String branchComName) {
        this.branchComName = branchComName;
    }
}
