package com.estar.hh.survey.entity.vo;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * 图片数据
 */
public class ImageVO extends DataSupport implements Serializable {

	private String usercode;
	private String reportno;
	private String taskno;
	private String taskid="";//任务id
	private String imgHashCode;
	private String imageData;
	private String imagePath;//图片本地地址
	private String imagename="";// 图片名称
	private String msg="";//记录返回的错误信息
	private String mark="未命名";// 标记(备注，中文名字)
	private String taskType="";

	private String isFirstUpload="0";//手机是否首次上传 0首次 1补传
	private int upcliamflag=0;// 是否上传理赔 0 为初始状态， 1 为上传状态 ，2 为已上传成功 ，3 为上传失败  每张图片最多上传3次
	private int uploadtimes=0;// 上传次数
	private int checked=0;// 选中为1 未选中为0
	private int dele=0;// 删除为1 未删除为0

	/**
	 * 上传类型（默认为移动查勘图片）
	 * 1.PC端截屏图片
	 * 2.移动查勘图片
	 * 3.移动视频图片
	 * 4.微信上传
	 * 5.客户自助非视频上传
	 * 6.客户自助视频上传
	 */
	private String uploadtype = "2";

	private String picCls = "";
	private String picDtl = "";

	public void setPicCls(String picCls) {
		this.picCls = picCls;
	}

	public String getPicCls() {
		return picCls;
	}

	public void setPicDtl(String picDtl) {
		this.picDtl = picDtl;
	}

	public String getPicDtl() {
		return picDtl;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getMark() {
		return mark;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setImagename(String imagename) {
		this.imagename = imagename;
	}

	public String getImagename() {
		return imagename;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setUploadtype(String uploadtype) {
		this.uploadtype = uploadtype;
	}

	public String getUploadtype() {
		return uploadtype;
	}

	public void setUploadtimes(int uploadtimes) {
		this.uploadtimes = uploadtimes;
	}

	public void setChecked(int checked) {
		this.checked = checked;
	}

	public void setDele(int dele) {
		this.dele = dele;
	}

	public int getUploadtimes() {
		return uploadtimes;
	}

	public int getChecked() {
		return checked;
	}

	public int getDele() {
		return dele;
	}

	public void setIsFirstUpload(String isFirstUpload) {
		this.isFirstUpload = isFirstUpload;
	}

	public String getIsFirstUpload() {
		return isFirstUpload;
	}

	public void setUpcliamflag(int upcliamflag) {
		this.upcliamflag = upcliamflag;
	}

	public int getUpcliamflag() {
		return upcliamflag;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}

	public String getUsercode() {
		return usercode;
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

	public void setImageData(String imageData) {
		this.imageData = imageData;
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

	public String getImageData() {
		return imageData;
	}

	//	private long id;
//	private String path="";//路径
//	private String lossitemserialno="";//损失序列号
//	private String taskid="";//任务id
//	private String taskno="";// 任务号
//	private String imagename="";// 图片名称
//	private String imgdate="";// 图片日期 yyyy-MM-dd
//	private String usercode="";// 用户代码
//	private String piccls="";// 大类           1、基础类  2、现场类  3、损失类
//	private String picDtl="";// 小类
//	private int upcliamflag=0;// 是否上传理赔 0 为初始状态， 1 为上传状态 ，2 为已上传成功 ，3 为上传失败  每张图片最多上传3次
//	private String reportno="";// 报案号
//	private String uploadtype = "2";// 上传类型(1,PC端截屏图片 2,移动查勘图片 3移动视频图片，)        a
//	private int uploadtimes=0;// 上传次数
//	private int checked=0;// 选中为1 未选中为0
//	private int dele=0;// 删除为1 未删除为0
//	private String msg="";//记录返回的错误信息
//	private double imagesize=0;//照片大小 B
//	private String mark="未命名";// 标记(备注，中文名字)
//	private String isFirstUpload="0";//手机是否首次上传 0首次 1补传
//	private String taskType="";
//	private String dptCde="";					 //所属保险公司机构代码
//	private String imageTaskType = ""; 	     //任务类型 121现在查勘 ，122：人伤查勘，131：车辆定损，132：财产定损
//	private String picClsName="";//图片大类名称
//	private String picDtlName="";//图片小类名称
//	private String picCategory="";//上传类型 1其他 2单证收集
//
//	public long getId() {
//		return id;
//	}
//
//	public void setId(long id) {
//		this.id = id;
//	}
//
//	public String getPath() {
//		return path;
//	}
//
//	public void setPath(String path) {
//		this.path = path;
//	}
//
//	public String getLossitemserialno() {
//		return lossitemserialno;
//	}
//
//	public void setLossitemserialno(String lossitemserialno) {
//		this.lossitemserialno = lossitemserialno;
//	}
//
//	public String getTaskid() {
//		return taskid;
//	}
//
//	public void setTaskid(String taskid) {
//		this.taskid = taskid;
//	}
//
//	public String getTaskno() {
//		return taskno;
//	}
//
//	public void setTaskno(String taskno) {
//		this.taskno = taskno;
//	}
//
//	public String getImagename() {
//		return imagename;
//	}
//
//	public void setImagename(String imagename) {
//		this.imagename = imagename;
//	}
//
//	public String getImgdate() {
//		return imgdate;
//	}
//
//	public void setImgdate(String imgdate) {
//		this.imgdate = imgdate;
//	}
//
//	public String getUsercode() {
//		return usercode;
//	}
//
//	public void setUsercode(String usercode) {
//		this.usercode = usercode;
//	}
//
//	public String getPiccls() {
//		return piccls;
//	}
//
//	public void setPiccls(String piccls) {
//		this.piccls = piccls;
//	}
//
//	public String getPicDtl() {
//		return picDtl;
//	}
//
//	public void setPicDtl(String picDtl) {
//		this.picDtl = picDtl;
//	}
//
//	public int getUpcliamflag() {
//		return upcliamflag;
//	}
//
//	public void setUpcliamflag(int upcliamflag) {
//		this.upcliamflag = upcliamflag;
//	}
//
//	public String getReportno() {
//		return reportno;
//	}
//
//	public void setReportno(String reportno) {
//		this.reportno = reportno;
//	}
//
//	public String getUploadtype() {
//		return uploadtype;
//	}
//
//	public void setUploadtype(String uploadtype) {
//		this.uploadtype = uploadtype;
//	}
//
//	public int getUploadtimes() {
//		return uploadtimes;
//	}
//
//	public void setUploadtimes(int uploadtimes) {
//		this.uploadtimes = uploadtimes;
//	}
//
//	public int getChecked() {
//		return checked;
//	}
//
//	public void setChecked(int checked) {
//		this.checked = checked;
//	}
//
//	public int getDele() {
//		return dele;
//	}
//
//	public void setDele(int dele) {
//		this.dele = dele;
//	}
//
//	public String getMsg() {
//		return msg;
//	}
//
//	public void setMsg(String msg) {
//		this.msg = msg;
//	}
//
//	public double getImagesize() {
//		return imagesize;
//	}
//
//	public void setImagesize(double imagesize) {
//		this.imagesize = imagesize;
//	}
//
//	public String getMark() {
//		return mark;
//	}
//
//	public void setMark(String mark) {
//		this.mark = mark;
//	}
//
//	public String getIsFirstUpload() {
//		return isFirstUpload;
//	}
//
//	public void setIsFirstUpload(String isFirstUpload) {
//		this.isFirstUpload = isFirstUpload;
//	}
//
//	public String getTaskType() {
//		return taskType;
//	}
//
//	public void setTaskType(String taskType) {
//		this.taskType = taskType;
//	}
//
//	public String getDptCde() {
//		return dptCde;
//	}
//
//	public void setDptCde(String dptCde) {
//		this.dptCde = dptCde;
//	}
//
//	public String getImageTaskType() {
//		return imageTaskType;
//	}
//
//	public void setImageTaskType(String imageTaskType) {
//		this.imageTaskType = imageTaskType;
//	}
//
//	public String getPicClsName() {
//		return picClsName;
//	}
//
//	public void setPicClsName(String picClsName) {
//		this.picClsName = picClsName;
//	}
//
//	public String getPicDtlName() {
//		return picDtlName;
//	}
//
//	public void setPicDtlName(String picDtlName) {
//		this.picDtlName = picDtlName;
//	}
//
//	public String getPicCategory() {
//		return picCategory;
//	}
//
//	public void setPicCategory(String picCategory) {
//		this.picCategory = picCategory;
//	}
//
//	@Override
//	public int describeContents() {
//		return 0;
//	}
//
//	@Override
//	public void writeToParcel(Parcel dest, int flags) {
//		dest.writeLong(this.id);
//		dest.writeString(this.path);
//		dest.writeString(this.lossitemserialno);
//		dest.writeString(this.taskid);
//		dest.writeString(this.taskno);
//		dest.writeString(this.imagename);
//		dest.writeString(this.imgdate);
//		dest.writeString(this.usercode);
//		dest.writeString(this.piccls);
//		dest.writeString(this.picDtl);
//		dest.writeInt(this.upcliamflag);
//		dest.writeString(this.reportno);
//		dest.writeString(this.uploadtype);
//		dest.writeInt(this.uploadtimes);
//		dest.writeInt(this.checked);
//		dest.writeInt(this.dele);
//		dest.writeString(this.msg);
//		dest.writeDouble(this.imagesize);
//		dest.writeString(this.mark);
//		dest.writeString(this.isFirstUpload);
//		dest.writeString(this.taskType);
//		dest.writeString(this.dptCde);
//		dest.writeString(this.imageTaskType);
//		dest.writeString(this.picClsName);
//		dest.writeString(this.picDtlName);
//		dest.writeString(this.picCategory);
//	}
//
//	public ImageVO() {
//	}
//
//	protected ImageVO(Parcel in) {
//		this.id = in.readLong();
//		this.path = in.readString();
//		this.lossitemserialno = in.readString();
//		this.taskid = in.readString();
//		this.taskno = in.readString();
//		this.imagename = in.readString();
//		this.imgdate = in.readString();
//		this.usercode = in.readString();
//		this.piccls = in.readString();
//		this.picDtl = in.readString();
//		this.upcliamflag = in.readInt();
//		this.reportno = in.readString();
//		this.uploadtype = in.readString();
//		this.uploadtimes = in.readInt();
//		this.checked = in.readInt();
//		this.dele = in.readInt();
//		this.msg = in.readString();
//		this.imagesize = in.readDouble();
//		this.mark = in.readString();
//		this.isFirstUpload = in.readString();
//		this.taskType = in.readString();
//		this.dptCde = in.readString();
//		this.imageTaskType = in.readString();
//		this.picClsName = in.readString();
//		this.picDtlName = in.readString();
//		this.picCategory = in.readString();
//	}
//
//	public static final Creator<ImageVO> CREATOR = new Creator<ImageVO>() {
//		@Override
//		public ImageVO createFromParcel(Parcel source) {
//			return new ImageVO(source);
//		}
//
//		@Override
//		public ImageVO[] newArray(int size) {
//			return new ImageVO[size];
//		}
//	};
}
