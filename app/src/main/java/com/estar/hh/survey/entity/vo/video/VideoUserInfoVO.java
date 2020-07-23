package com.estar.hh.survey.entity.vo.video;

import java.io.Serializable;

public class VideoUserInfoVO implements Serializable {

	private String name="";
	private String userCode="";
	private int meetBuddyID=0;
	private boolean isVideo=false;// 是否正在视频
	private boolean isRedio=false;// 是否打开语音
	private int userType=0;//1为录制视频人员 其他为播放者
	private int redioType=-9999999;//语音类型
	
	
	public int getRedioType() {
		return redioType;
	}
	public void setRedioType(int redioType) {
		this.redioType = redioType;
	}
	public int getUserType() {
		return userType;
	}
	public void setUserType(int userType) {
		this.userType = userType;
	}
	public int getMeetBuddyID() {
		return meetBuddyID;
	}
	public void setMeetBuddyID(int meetBuddyID) {
		this.meetBuddyID = meetBuddyID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public boolean isVideo() {
		return isVideo;
	}
	public void setVideo(boolean isVideo) {
		this.isVideo = isVideo;
	}
	public boolean isRedio() {
		return isRedio;
	}
	public void setRedio(boolean isRedio) {
		this.isRedio = isRedio;
	}

}
