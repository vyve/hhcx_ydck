package com.estar.hh.survey.entity.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/18 0018.
 */

public class ImageUploadDto implements Serializable {

    private String reportno;
    private String taskno;
    private String imgHashCode;
    private String deviceType = "Android";
    private String imageData;
    private String imagePath;//图片本地地址
    private String usercode;

    private String fileName;
    private String picCls;
    private String picDtl;

    private String taskType="";
    private String  uploadType="";//1，PC端截屏图片 (2，移动查勘图片 3，移动视频图片) 4，微信上传 5，客户自助非视频上传 6，客户自助视频上传。

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setPicCls(String picCls) {
        this.picCls = picCls;
    }

    public void setPicDtl(String picDtl) {
        this.picDtl = picDtl;
    }

    public String getFileName() {
        return fileName;
    }

    public String getPicCls() {
        return picCls;
    }

    public String getPicDtl() {
        return picDtl;
    }

    public void setUploadType(String uploadType) {
        this.uploadType = uploadType;
    }

    public String getUploadType() {
        return uploadType;
    }

    public void setReportno(String reportno) {
        this.reportno = reportno;
    }

    public void setTaskno(String taskno) {
        this.taskno = taskno;
    }

    public void setImgHashCode(String imgHashCode) {
        this.imgHashCode = imgHashCode;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getReportno() {
        return reportno;
    }

    public String getTaskno() {
        return taskno;
    }

    public String getImgHashCode() {
        return imgHashCode;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public String getImageData() {
        return imageData;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getUsercode() {
        return usercode;
    }

    public String getTaskType() {
        return taskType;
    }
}
