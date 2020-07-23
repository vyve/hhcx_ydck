package com.estar.hh.survey.entity.vo.video;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/*手机发起视频响应*/
public class ResVideoLineVO extends DataSupport implements Parcelable {
	private String meetingId = "";// 视频Id(前后端视频链接需要)
	private String opAble = "";// 0:全忙 1:有可分配的人
	private String opPersion = "";// 工号+姓名
	private String status="";


	//界面需展示
	private String reportno;// 报案号
	private String taskNo = "";// 任务号
	private String licenseno;// 车牌号
	private String lossers;// 后端定损员
	private String reporttime;// 报案时间
	private String damageTime = ""; // 出险时间
	private String tasktype;// 任务来源
	private String insNme = "";// 被保险人
	private String dangerAfter = "";// 出险经过

	public String getMeetingId() {
		return meetingId;
	}

	public void setMeetingId(String meetingId) {
		this.meetingId = meetingId;
	}

	public String getOpAble() {
		return opAble;
	}

	public void setOpAble(String opAble) {
		this.opAble = opAble;
	}

	public String getOpPersion() {
		return opPersion;
	}

	public void setOpPersion(String opPersion) {
		this.opPersion = opPersion;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReportno() {
		return reportno;
	}

	public void setReportno(String reportno) {
		this.reportno = reportno;
	}

	public String getTaskNo() {
		return taskNo;
	}

	public void setTaskNo(String taskNo) {
		this.taskNo = taskNo;
	}

	public String getLicenseno() {
		return licenseno;
	}

	public void setLicenseno(String licenseno) {
		this.licenseno = licenseno;
	}

	public String getLossers() {
		return lossers;
	}

	public void setLossers(String lossers) {
		this.lossers = lossers;
	}

	public String getReporttime() {
		return reporttime;
	}

	public void setReporttime(String reporttime) {
		this.reporttime = reporttime;
	}

	public String getDamageTime() {
		return damageTime;
	}

	public void setDamageTime(String damageTime) {
		this.damageTime = damageTime;
	}

	public String getTasktype() {
		return tasktype;
	}

	public void setTasktype(String tasktype) {
		this.tasktype = tasktype;
	}

	public String getInsNme() {
		return insNme;
	}

	public void setInsNme(String insNme) {
		this.insNme = insNme;
	}

	public String getDangerAfter() {
		return dangerAfter;
	}

	public void setDangerAfter(String dangerAfter) {
		this.dangerAfter = dangerAfter;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.meetingId);
		dest.writeString(this.opAble);
		dest.writeString(this.opPersion);
		dest.writeString(this.status);
		dest.writeString(this.reportno);
		dest.writeString(this.taskNo);
		dest.writeString(this.licenseno);
		dest.writeString(this.lossers);
		dest.writeString(this.reporttime);
		dest.writeString(this.damageTime);
		dest.writeString(this.tasktype);
		dest.writeString(this.insNme);
		dest.writeString(this.dangerAfter);
	}

	public ResVideoLineVO() {
	}

	protected ResVideoLineVO(Parcel in) {
		this.meetingId = in.readString();
		this.opAble = in.readString();
		this.opPersion = in.readString();
		this.status = in.readString();
		this.reportno = in.readString();
		this.taskNo = in.readString();
		this.licenseno = in.readString();
		this.lossers = in.readString();
		this.reporttime = in.readString();
		this.damageTime = in.readString();
		this.tasktype = in.readString();
		this.insNme = in.readString();
		this.dangerAfter = in.readString();
	}

	public static final Creator<ResVideoLineVO> CREATOR = new Creator<ResVideoLineVO>() {
		public ResVideoLineVO createFromParcel(Parcel source) {
			return new ResVideoLineVO(source);
		}

		public ResVideoLineVO[] newArray(int size) {
			return new ResVideoLineVO[size];
		}
	};
}
