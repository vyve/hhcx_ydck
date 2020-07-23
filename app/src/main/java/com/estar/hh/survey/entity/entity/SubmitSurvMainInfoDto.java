package com.estar.hh.survey.entity.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * @Description:〈查勘总提交dto〉<br/>
 * 
 * @Provider:广州易星科技有限公司 <br/>
 * @Author: chenhuan@estar-info.com<br/>
 * @Date:2017年10月27日
 */
public class SubmitSurvMainInfoDto extends DataSupport implements Serializable {
	/* 报案号 */
	private String reportNo = "";
	/* 任务号 */
	private String taskNo = "";
	/* 查勘员工号 */
	private String userName = "";
	/* 查勘-基本信息 */
	private SurvBaseInfoDto survBaseInfoDto = new SurvBaseInfoDto();
	/* 查勘-标的信息 */
	private CarLossInfoDto carLossInfoDto = new CarLossInfoDto();
	/* 查勘-车辆信息 */
	private List<CarLossInfoDto> listCarLossInfoDto = new ArrayList<CarLossInfoDto>();
	/* 查勘-物损信息 */
	private List<GoodInfoDto> listGoodInfoDto = new ArrayList<GoodInfoDto>();
	/* 查勘-创建时间 */
	private String createTime;

	/**
	 * 任务完成状态
	 * 0未完成
	 * 1已完成
	 */
	private int readFlag = 0;//阅读任务完成标志
	private int contactFlag = 0;//联系客户完成标志
	private int baseInfoFlag = 0;//基础信息完成标志
	private int carInfoFlag = 0;//三者车完成标志
	private int goodInfoFlag = 0;//物损完成标志

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

	public void setCarInfoFlag(int carInfoFlag) {
		this.carInfoFlag = carInfoFlag;
	}

	public void setGoodInfoFlag(int goodInfoFlag) {
		this.goodInfoFlag = goodInfoFlag;
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

	public int getCarInfoFlag() {
		return carInfoFlag;
	}

	public int getGoodInfoFlag() {
		return goodInfoFlag;
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

	public SurvBaseInfoDto getSurvBaseInfoDto() {
		return survBaseInfoDto;
	}

	public void setSurvBaseInfoDto(SurvBaseInfoDto survBaseInfoDto) {
		this.survBaseInfoDto = survBaseInfoDto;
	}

	public void setCarLossInfoDto(CarLossInfoDto carLossInfoDto) {
		this.carLossInfoDto = carLossInfoDto;
	}

	public CarLossInfoDto getCarLossInfoDto() {
		return carLossInfoDto;
	}

	public List<CarLossInfoDto> getListCarLossInfoDto() {
		return listCarLossInfoDto;
	}

	public void setListCarLossInfoDto(List<CarLossInfoDto> listCarLossInfoDto) {
		this.listCarLossInfoDto = listCarLossInfoDto;
	}

	public List<GoodInfoDto> getListGoodInfoDto() {
		return listGoodInfoDto;
	}

	public void setListGoodInfoDto(List<GoodInfoDto> listGoodInfoDto) {
		this.listGoodInfoDto = listGoodInfoDto;
	}

	@Override
	public String toString() {
		return "SubmitSurvMainInfoDto{" +
				"reportNo='" + reportNo + '\'' +
				", taskNo='" + taskNo + '\'' +
				", userName='" + userName + '\'' +
				", survBaseInfoDto=" + survBaseInfoDto +
				", carLossInfoDto=" + carLossInfoDto +
				", listCarLossInfoDto=" + listCarLossInfoDto +
				", listGoodInfoDto=" + listGoodInfoDto +
				", createTime='" + createTime + '\'' +
				", readFlag=" + readFlag +
				", contactFlag=" + contactFlag +
				", baseInfoFlag=" + baseInfoFlag +
				", carInfoFlag=" + carInfoFlag +
				", goodInfoFlag=" + goodInfoFlag +
				'}';
	}
}
