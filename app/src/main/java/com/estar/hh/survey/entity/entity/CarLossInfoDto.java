package com.estar.hh.survey.entity.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * @Description:〈描述〉<br/>
 * 
 * @Provider:广州易星科技有限公司 <br/>
 * @Author: chenhuan@estar-info.com<br/>
 * @Date:2017年11月1日
 */
public class CarLossInfoDto extends DataSupport implements Serializable {

	public static final String LOSSTYPECAR = "050";//标的车类型
	public static final String LOSSTYPEOTHERCAR = "010";//三者车类型


	/* 报案号 */
	private String reportNo = "";
	/* 任务号 */
	private String taskNo = "";
	private String licenseNo = "";// 车牌号
	private String brandName = "";// 车型名称
	private String licenseNoType = "02";// 号牌种类(默认小型汽车好牌 分类：02)
	private String driverName = "";// 驾驶人姓名
	private String identifyNumber = "";// 证件号码
	private String damageFlag = "1";// 有无损失 0-否 1-是
	private String isFoundOther = "0";// 是否无法找到第三方 0-否 1-是
	private String lossMoney = "";// 损失预估金额
	private String lossItemType = "";// 损失类型 050标的 010三者
	private String isHaveDuty="0";//是否有责 0-否 1-是
	private String createTime = "";

	/**
	 * 新增字段
	 */
	private String ciIndemDuty = "0";//交强险责任类型 100-有责 0-无责
	private String repairFlag = "0";//是否推修 1-是 0-否
	private String noPushReason = "";//不推修原因01盗抢 02代位求偿案件  03补报案  04注销拒赔案件恢复  05涉诉案件  06非车损案件  07修理厂报案 08不推修规则
	private String identifyType = "01";//证件类型(目前界面显示身份证)
	private String drivingLicenseNo = "";//驾驶证号码(目前界面显示身份证)
	private String reserveFlag = "0";//重大赔案标识 1是 0否
	private String exceptSumLossFee = "0";//损失预估金额(与上面重复)
	private String lossPart = "";//损失部位详见损失部位代码
	private String dutyType = "";//商业险赔偿责任

	/*2018-03-20查勘三者车列表新增*/
	private String brandModels = "";// 厂牌车型
	private String companyName = "";// 承保公司
	private String thirdPolicyNo = "";// 三者车保单号

	private String engineNo;//发动机号
	private String frameNo;//车架号

	private String carKindCode;//车辆种类

	public void setCarKindCode(String carKindCode) {
		this.carKindCode = carKindCode;
	}

	public String getCarKindCode() {
		return carKindCode;
	}

	public String getEngineNo() {
		return engineNo;
	}

	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	public String getFrameNo() {
		return frameNo;
	}

	public void setFrameNo(String frameNo) {
		this.frameNo = frameNo;
	}

	public void setBrandModels(String brandModels) {
		this.brandModels = brandModels;
	}

	public String getBrandModels() {
		return brandModels;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setThirdPolicyNo(String thirdPolicyNo) {
		this.thirdPolicyNo = thirdPolicyNo;
	}

	public String getThirdPolicyNo() {
		return thirdPolicyNo;
	}

	public void setDutyType(String dutyType) {
		this.dutyType = dutyType;
	}

	public String getDutyType() {
		return dutyType;
	}

	public void setCiIndemDuty(String ciIndemDuty) {
		this.ciIndemDuty = ciIndemDuty;
	}

	public void setRepairFlag(String repairFlag) {
		this.repairFlag = repairFlag;
	}

	public void setNoPushReason(String noPushReason) {
		this.noPushReason = noPushReason;
	}

	public void setIdentifyType(String identifyType) {
		this.identifyType = identifyType;
	}

	public void setDrivingLicenseNo(String drivingLicenseNo) {
		this.drivingLicenseNo = drivingLicenseNo;
	}

	public void setReserveFlag(String reserveFlag) {
		this.reserveFlag = reserveFlag;
	}

	public void setExceptSumLossFee(String exceptSumLossFee) {
		this.exceptSumLossFee = exceptSumLossFee;
	}

	public void setLossPart(String lossPart) {
		this.lossPart = lossPart;
	}

	public String getCiIndemDuty() {
		return ciIndemDuty;
	}

	public String getRepairFlag() {
		return repairFlag;
	}

	public String getNoPushReason() {
		return noPushReason;
	}

	public String getIdentifyType() {
		return identifyType;
	}

	public String getDrivingLicenseNo() {
		return drivingLicenseNo;
	}

	public String getReserveFlag() {
		return reserveFlag;
	}

	public String getExceptSumLossFee() {
		return exceptSumLossFee;
	}

	public String getLossPart() {
		return lossPart;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setTaskNo(String taskNo) {
		this.taskNo = taskNo;
	}

	public String getTaskNo() {
		return taskNo;
	}

	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

	public String getReportNo() {
		return reportNo;
	}

	public void setIsHaveDuty(String isHaveDuty) {
		this.isHaveDuty = isHaveDuty;
	}

	public String getIsHaveDuty() {
		return isHaveDuty;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getLicenseNoType() {
		return licenseNoType;
	}

	public void setLicenseNoType(String licenseNoType) {
		this.licenseNoType = licenseNoType;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getIdentifyNumber() {
		return identifyNumber;
	}

	public void setIdentifyNumber(String identifyNumber) {
		this.identifyNumber = identifyNumber;
	}

	public String getDamageFlag() {
		return damageFlag;
	}

	public void setDamageFlag(String damageFlag) {
		this.damageFlag = damageFlag;
	}

	public String getIsFoundOther() {
		return isFoundOther;
	}

	public void setIsFoundOther(String isFoundOther) {
		this.isFoundOther = isFoundOther;
	}

	public String getLossMoney() {
		return lossMoney;
	}

	public void setLossMoney(String lossMoney) {
		this.lossMoney = lossMoney;
	}

	public String getLossItemType() {
		return lossItemType;
	}

	public void setLossItemType(String lossItemType) {
		this.lossItemType = lossItemType;
	}



}
