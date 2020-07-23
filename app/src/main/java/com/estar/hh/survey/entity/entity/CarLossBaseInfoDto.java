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
public class CarLossBaseInfoDto extends DataSupport implements Serializable {

	/* 报案号 */
	private String reportNo = "";
	/* 任务号 */
	private String taskNo = "";
	private String licsnseNo = "";// 车牌号
	private String licsnseNoType = "";// 号牌种类
	private String driverName = "";// 驾驶人姓名
	private String identifyNumber = "";// 证件号码
	private String defSite = "";// 定损地点
	private String repairSite = "";// 维修地址
	private String ciIndemDuty = "";// 交强险责任类型 0无责，100有责
	private String isCoopRepair = "0";// 是否合作修理厂
	private String repairComcodeFlag = "0";// 无机构地区修理
	private String repairFactoryCode = "";// 修理厂代码
	private String lossOpinion = "";// 定损意见
	private String createTime = "";
	private String repairProvinceCode = "";//维修地点所在省
	private String repairCityCode = "";//维修地点所在市
	private String defProvinceCode = "";//定损地点所在省
	private String defCityCode = "";//定损地点所在市
	private String caseFlag = "0";//互碰自赔

	/**
	 * 新增字段
	 */
	private String returnRepairMode = "";//送回修方式
	/**
	 * 定损方式
	 * 01	修复定损
	 * 02	一次性协议定损
	 * 03	推定全损
	 * 04	法院判决
	 */
	private String cetainLossType = "";
	private String vinNo = "";
	private String engineNo = "";
	private String driverTelNo = "";
	private String lossItemNo = "";//精友定损单号

	private String cetainlossTypeFlag = "0";//是否一次性协议 0否 1是
	private String drivingSex = "1";//驾驶人员性别 1男 2女
	private String isFloodedCar = "0";// 是否水淹车 1是 0否
	private String floodedLevel = "";//水淹等级
	private String driverApanage = "";//驾驶人员属地

	private String comCode = "";//机构
	private String carKindCode = "";//车辆种类

	public String getReturnRepairMode() {
		return returnRepairMode;
	}

	public void setReturnRepairMode(String returnRepairMode) {
		this.returnRepairMode = returnRepairMode;
	}

	public void setCarKindCode(String carKindCode) {
		this.carKindCode = carKindCode;
	}

	public String getCarKindCode() {
		return carKindCode;
	}

	public void setComCode(String comCode) {
		this.comCode = comCode;
	}

	public String getComCode() {
		return comCode;
	}

	public void setIsFloodedCar(String isFloodedCar) {
		this.isFloodedCar = isFloodedCar;
	}

	public String getIsFloodedCar() {
		return isFloodedCar;
	}

	public String getCetainlossTypeFlag() {
		return cetainlossTypeFlag;
	}

	public void setCetainlossTypeFlag(String cetainlossTypeFlag) {
		this.cetainlossTypeFlag = cetainlossTypeFlag;
	}

	public String getDrivingSex() {
		return drivingSex;
	}

	public void setDrivingSex(String drivingSex) {
		this.drivingSex = drivingSex;
	}

	public String getFloodedLevel() {
		return floodedLevel;
	}

	public void setFloodedLevel(String floodedLevel) {
		this.floodedLevel = floodedLevel;
	}

	public String getDriverApanage() {
		return driverApanage;
	}

	public void setDriverApanage(String driverApanage) {
		this.driverApanage = driverApanage;
	}

	public void setRepairSite(String repairSite) {
		this.repairSite = repairSite;
	}

	public String getRepairSite() {
		return repairSite;
	}

	public void setLossItemNo(String lossItemNo) {
		this.lossItemNo = lossItemNo;
	}

	public String getLossItemNo() {
		return lossItemNo;
	}

	public void setDriverTelNo(String driverTelNo) {
		this.driverTelNo = driverTelNo;
	}

	public String getDriverTelNo() {
		return driverTelNo;
	}

	public void setVinNo(String vinNo) {
		this.vinNo = vinNo;
	}

	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	public String getVinNo() {
		return vinNo;
	}

	public String getEngineNo() {
		return engineNo;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public String getLicsnseNo() {
		return licsnseNo;
	}
	public void setLicsnseNo(String licsnseNo) {
		this.licsnseNo = licsnseNo;
	}
	public String getLicsnseNoType() {
		return licsnseNoType;
	}
	public void setLicsnseNoType(String licsnseNoType) {
		this.licsnseNoType = licsnseNoType;
	}
	public String getCetainLossType() {
		return cetainLossType;
	}
	public void setCetainLossType(String cetainLossType) {
		this.cetainLossType = cetainLossType;
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
	public String getDefSite() {
		return defSite;
	}
	public void setDefSite(String defSite) {
		this.defSite = defSite;
	}
	public String getCiIndemDuty() {
		return ciIndemDuty;
	}
	public void setCiIndemDuty(String ciIndemDuty) {
		this.ciIndemDuty = ciIndemDuty;
	}
	public String getIsCoopRepair() {
		return isCoopRepair;
	}
	public void setIsCoopRepair(String isCoopRepair) {
		this.isCoopRepair = isCoopRepair;
	}
	public String getRepairComcodeFlag() {
		return repairComcodeFlag;
	}

	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

	public String getReportNo() {
		return reportNo;
	}

	public void setTaskNo(String taskNo) {
		this.taskNo = taskNo;
	}

	public String getTaskNo() {
		return taskNo;
	}

	public void setRepairComcodeFlag(String repairComcodeFlag) {
		this.repairComcodeFlag = repairComcodeFlag;
	}
	public String getRepairFactoryCode() {
		return repairFactoryCode;
	}
	public void setRepairFactoryCode(String repairFactoryCode) {
		this.repairFactoryCode = repairFactoryCode;
	}
	public String getLossOpinion() {
		return lossOpinion;
	}
	public void setLossOpinion(String lossOpinion) {
		this.lossOpinion = lossOpinion;
	}

	// private String drivingLicenseNo="";//驾驶证号码
	// private String identifyType="";//证件类型
	// private String repairFactoryName="";//修理厂名称
	// private String repairFactoryType="";//修理厂类型
	// private String deflossMinusFlag="";//是否定损扣减
	// private String isWater="";//是否水淹
	// private String defLossDate="";//定损日期


	public String getRepairProvinceCode() {
		return repairProvinceCode;
	}

	public void setRepairProvinceCode(String repairProvinceCode) {
		this.repairProvinceCode = repairProvinceCode;
	}

	public String getRepairCityCode() {
		return repairCityCode;
	}

	public void setRepairCityCode(String repairCityCode) {
		this.repairCityCode = repairCityCode;
	}

	public String getDefProvinceCode() {
		return defProvinceCode;
	}

	public void setDefProvinceCode(String defProvinceCode) {
		this.defProvinceCode = defProvinceCode;
	}

	public String getDefCityCode() {
		return defCityCode;
	}

	public void setDefCityCode(String defCityCode) {
		this.defCityCode = defCityCode;
	}

	public String getCaseFlag() {
		return caseFlag;
	}

	public void setCaseFlag(String caseFlag) {
		this.caseFlag = caseFlag;
	}
}
