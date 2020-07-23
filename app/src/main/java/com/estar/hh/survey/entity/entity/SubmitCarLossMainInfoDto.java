package com.estar.hh.survey.entity.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * @Description:〈车辆定损-基本信息〉<br/>
 * 
 * @Provider:广州易星科技有限公司 <br/>
 * @Author: chenhuan@estar-info.com<br/>
 * @Date:2017年11月1日
 */
public class SubmitCarLossMainInfoDto extends DataSupport implements Serializable {
	/* 报案号 */
	private String reportNo = "";
	/* 任务号 */
	private String taskNo = "";
	/* 定损员工号 */
	private String userName = "";
	/* 定损员证件号 */
	private String userIdNo = "";
	/* 车辆定损-基本信息 */
	private CarLossBaseInfoDto carLossBaseInfoDto = new CarLossBaseInfoDto();

	private String createTime = "";

	/**
	 * 任务完成状态
	 * 0未完成
	 * 1已完成
	 */
	private int readFlag = 0;//阅读任务完成标志
	private int contactFlag = 0;//联系客户完成标志
	private int baseInfoFlag = 0;//基本信息完成标志
	private int losscheckFlag = 0;//定损完成标志

	public void setUserIdNo(String userIdNo) {
		this.userIdNo = userIdNo;
	}

	public String getUserIdNo() {
		return userIdNo;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setReadFlag(int readFlag) {
		this.readFlag = readFlag;
	}

	public void setContactFlag(int contactFlag) {
		this.contactFlag = contactFlag;
	}

	public void setBaseInfoFlag(int baseInfoFlag) {
		this.baseInfoFlag = baseInfoFlag;
	}

	public void setLosscheckFlag(int losscheckFlag) {
		this.losscheckFlag = losscheckFlag;
	}

	public int getReadFlag() {
		return readFlag;
	}

	public int getContactFlag() {
		return contactFlag;
	}

	public int getBaseInfoFlag() {
		return baseInfoFlag;
	}

	public int getLosscheckFlag() {
		return losscheckFlag;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public String getReportNo() {
		return reportNo;
	}

	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

	public String getTaskNo() {
		return taskNo;
	}

	public void setTaskNo(String taskNo) {
		this.taskNo = taskNo;
	}

	public CarLossBaseInfoDto getCarLossBaseInfoDto() {
		return carLossBaseInfoDto;
	}

	public void setCarLossBaseInfoDto(CarLossBaseInfoDto carLossBaseInfoDto) {
		this.carLossBaseInfoDto = carLossBaseInfoDto;
	}


}
