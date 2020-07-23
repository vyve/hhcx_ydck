package com.estar.hh.survey.entity.vo;

import android.os.Parcel;
import android.os.Parcelable;

public class UploadImageDTO implements Parcelable {
	private String taskno="";// 任务号     a
	private String picDtl="";// 大类        a    1、基础类  2、现场类  3、损失类
	private String reportno="";// 报案号      a
	private String uploadType = "";// 上传类型(1,PC端截屏图片 2,移动查勘图片 3移动视频图片，)        a
	private String imageData="";// 图片64位码      a
	private String imgid="";// 图片id
	private String url="";// 图片地址
	private int longitude=0;// 经度
	private int latitude=0;// 纬度
	private String address="";// 地址
	private String imgdate="";// 图片日期
	private String usercode="";// 用户代码
	// private InputStream streamPic;// 图片流
	private String orderFid="";// 排序字段
	private String orderType="";// 排序类型 asc desc
	private String picCls="";// 小类
	private String upCliamFlag="";// 是否上传理赔
	private String lossName="";// 损失名称
	private String taskType = "";// 任务类型(0：现场查勘1：主车车损2：三者车损3：主车货物4：三者车货物5：其它三者财产)
	private String uploadTimes="";// 上传次数
	private String updateTm="";// 更新时间
	private String dptCde="";// 查勘员机构代码
	private String fileName="";// 图片名称
	private String mark;// 标记(备注，中文名字)
	private String isFirstUpload;//手机是否首次上传 0首次 1补传
	private String imgLength;//图片Base64位码长度
	private String imgHashCode;//图片hash值

	public String getImgHashCode() {
		return imgHashCode;
	}

	public void setImgHashCode(String imgHashCode) {
		this.imgHashCode = imgHashCode;
	}

	public String getImgLength() {
		return imgLength;
	}

	public void setImgLength(String imgLength) {
		this.imgLength = imgLength;
	}

	public String getIsFirstUpload() {
		return isFirstUpload;
	}

	public void setIsFirstUpload(String isFirstUpload) {
		this.isFirstUpload = isFirstUpload;
	}

	public String getTaskno() {
		return taskno;
	}

	public void setTaskno(String taskno) {
		this.taskno = taskno;
	}

	public String getPicDtl() {
		return picDtl;
	}

	public void setPicDtl(String picDtl) {
		this.picDtl = picDtl;
	}

	public String getReportno() {
		return reportno;
	}

	public void setReportno(String reportno) {
		this.reportno = reportno;
	}

	public String getUploadType() {
		return uploadType;
	}

	public void setUploadType(String uploadType) {
		this.uploadType = uploadType;
	}

	public String getImageData() {
		return imageData;
	}

	public void setImageData(String imageData) {
		this.imageData = imageData;
	}

	public String getImgid() {
		return imgid;
	}

	public void setImgid(String imgid) {
		this.imgid = imgid;
	}

	public int getLongitude() {
		return longitude;
	}

	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getLatitude() {
		return latitude;
	}

	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getImgdate() {
		return imgdate;
	}

	public void setImgdate(String imgdate) {
		this.imgdate = imgdate;
	}

	public String getUsercode() {
		return usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getOrderFid() {
		return orderFid;
	}

	public void setOrderFid(String orderFid) {
		this.orderFid = orderFid;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getPicCls() {
		return picCls;
	}

	public void setPicCls(String picCls) {
		this.picCls = picCls;
	}

	public String getUpCliamFlag() {
		return upCliamFlag;
	}

	public void setUpCliamFlag(String upCliamFlag) {
		this.upCliamFlag = upCliamFlag;
	}

	public String getLossName() {
		return lossName;
	}

	public void setLossName(String lossName) {
		this.lossName = lossName;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getUploadTimes() {
		return uploadTimes;
	}

	public void setUploadTimes(String uploadTimes) {
		this.uploadTimes = uploadTimes;
	}

	public String getUpdateTm() {
		return updateTm;
	}

	public void setUpdateTm(String updateTm) {
		this.updateTm = updateTm;
	}

	public String getDptCde() {
		return dptCde;
	}

	public void setDptCde(String dptCde) {
		this.dptCde = dptCde;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.taskno);
		dest.writeString(this.picDtl);
		dest.writeString(this.reportno);
		dest.writeString(this.uploadType);
		dest.writeString(this.imageData);
		dest.writeString(this.imgid);
		dest.writeString(this.url);
		dest.writeInt(this.longitude);
		dest.writeInt(this.latitude);
		dest.writeString(this.address);
		dest.writeString(this.imgdate);
		dest.writeString(this.usercode);
		dest.writeString(this.orderFid);
		dest.writeString(this.orderType);
		dest.writeString(this.picCls);
		dest.writeString(this.upCliamFlag);
		dest.writeString(this.lossName);
		dest.writeString(this.taskType);
		dest.writeString(this.uploadTimes);
		dest.writeString(this.updateTm);
		dest.writeString(this.dptCde);
		dest.writeString(this.fileName);
		dest.writeString(this.mark);
		dest.writeString(this.isFirstUpload);
		dest.writeString(this.imgLength);
		dest.writeString(this.imgHashCode);
	}

	public UploadImageDTO() {
	}

	protected UploadImageDTO(Parcel in) {
		this.taskno = in.readString();
		this.picDtl = in.readString();
		this.reportno = in.readString();
		this.uploadType = in.readString();
		this.imageData = in.readString();
		this.imgid = in.readString();
		this.url = in.readString();
		this.longitude = in.readInt();
		this.latitude = in.readInt();
		this.address = in.readString();
		this.imgdate = in.readString();
		this.usercode = in.readString();
		this.orderFid = in.readString();
		this.orderType = in.readString();
		this.picCls = in.readString();
		this.upCliamFlag = in.readString();
		this.lossName = in.readString();
		this.taskType = in.readString();
		this.uploadTimes = in.readString();
		this.updateTm = in.readString();
		this.dptCde = in.readString();
		this.fileName = in.readString();
		this.mark = in.readString();
		this.isFirstUpload = in.readString();
		this.imgLength = in.readString();
		this.imgHashCode = in.readString();
	}

	public static final Creator<UploadImageDTO> CREATOR = new Creator<UploadImageDTO>() {
		public UploadImageDTO createFromParcel(Parcel source) {
			return new UploadImageDTO(source);
		}

		public UploadImageDTO[] newArray(int size) {
			return new UploadImageDTO[size];
		}
	};
}
