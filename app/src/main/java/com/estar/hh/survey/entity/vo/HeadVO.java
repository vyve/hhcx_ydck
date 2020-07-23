package com.estar.hh.survey.entity.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 */
public class HeadVO implements Parcelable {
    private String function=""; //功能
    private String method="";   //方法名
    private String userCode="";   //
    private String appVersion= "";   //
    private String clientSystemInfo="android";   //
    private String token="";   //

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getClientSystemInfo() {
        return clientSystemInfo;
    }

    public void setClientSystemInfo(String clientSystemInfo) {
        this.clientSystemInfo = clientSystemInfo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.function);
        dest.writeString(this.method);
        dest.writeString(this.userCode);
        dest.writeString(this.appVersion);
        dest.writeString(this.clientSystemInfo);
        dest.writeString(this.token);
    }

    public HeadVO() {
    }

    protected HeadVO(Parcel in) {
        this.function = in.readString();
        this.method = in.readString();
        this.userCode = in.readString();
        this.appVersion = in.readString();
        this.clientSystemInfo = in.readString();
        this.token = in.readString();
    }

    public static final Creator<HeadVO> CREATOR = new Creator<HeadVO>() {
        @Override
        public HeadVO createFromParcel(Parcel source) {
            return new HeadVO(source);
        }

        @Override
        public HeadVO[] newArray(int size) {
            return new HeadVO[size];
        }
    };
}
