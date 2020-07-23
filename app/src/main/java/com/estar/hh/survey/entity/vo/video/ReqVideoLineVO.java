package com.estar.hh.survey.entity.vo.video;

import android.os.Parcel;
import android.os.Parcelable;

/*手机端发起视频请求*/
public class ReqVideoLineVO implements Parcelable {
	private String lossers = "";// 后台第二定损人代码
	private String lossersName = ""; // 后台定损人名称
	private String pmechanism = ""; // 案件处理机构
	private String reportType = ""; // 案件类型
	private String report = ""; // 案件号
	private String taskType = "";// 任务类型
	private String taskno = ""; // 任务号
	private String usercode = "";// 查勘员代码
	private String username = ""; // 查勘员名称
	private String dptCode="";//前端人员机构代码
	private String dptName="";//前端人员机构名称
	private String videoMark="";//视频类型 0-初始化值(即没有申请视频)； 1-视频模式； 2-图片模式； 3-后台拒绝； 4-后台无闲置人员； 5-网络中断；6-无网络信号。

	public String getLossers() {
		return lossers;
	}

	public void setLossers(String lossers) {
		this.lossers = lossers;
	}

	public String getLossersName() {
		return lossersName;
	}

	public void setLossersName(String lossersName) {
		this.lossersName = lossersName;
	}

	public String getPmechanism() {
		return pmechanism;
	}

	public void setPmechanism(String pmechanism) {
		this.pmechanism = pmechanism;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getTaskno() {
		return taskno;
	}

	public void setTaskno(String taskno) {
		this.taskno = taskno;
	}

	public String getUsercode() {
		return usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDptCode() {
		return dptCode;
	}

	public void setDptCode(String dptCode) {
		this.dptCode = dptCode;
	}

	public String getDptName() {
		return dptName;
	}

	public void setDptName(String dptName) {
		this.dptName = dptName;
	}

	public String getVideoMark() {
		return videoMark;
	}

	public void setVideoMark(String videoMark) {
		this.videoMark = videoMark;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.lossers);
		dest.writeString(this.lossersName);
		dest.writeString(this.pmechanism);
		dest.writeString(this.reportType);
		dest.writeString(this.report);
		dest.writeString(this.taskType);
		dest.writeString(this.taskno);
		dest.writeString(this.usercode);
		dest.writeString(this.username);
		dest.writeString(this.dptCode);
		dest.writeString(this.dptName);
		dest.writeString(this.videoMark);
	}

	public ReqVideoLineVO() {
	}

	protected ReqVideoLineVO(Parcel in) {
		this.lossers = in.readString();
		this.lossersName = in.readString();
		this.pmechanism = in.readString();
		this.reportType = in.readString();
		this.report = in.readString();
		this.taskType = in.readString();
		this.taskno = in.readString();
		this.usercode = in.readString();
		this.username = in.readString();
		this.dptCode = in.readString();
		this.dptName = in.readString();
		this.videoMark = in.readString();
	}

	public static final Creator<ReqVideoLineVO> CREATOR = new Creator<ReqVideoLineVO>() {
		public ReqVideoLineVO createFromParcel(Parcel source) {
			return new ReqVideoLineVO(source);
		}

		public ReqVideoLineVO[] newArray(int size) {
			return new ReqVideoLineVO[size];
		}
	};
}
