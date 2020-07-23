package com.estar.hh.survey.entity.entity;

import java.io.Serializable;

/**
 * @Description:〈抄单-报案信息〉<br/>
 * 
 * @Provider:广州易星科技有限公司 <br/>
 * @Author: chenhuan@estar-info.com<br/>
 * @Date:2017年10月27日
 */
public class CopyReportInfoDto implements Serializable {
	/* 报案号 */
	private String reportNo = "";
	/* 坐席工号 */
	private String seatCode = "";
	/* 坐席姓名 */
	private String seatName = "";
	/* 报案人 */
	private String reporter = "";
	/* 报案人电话 */
	private String reportTelNo = "";
	/* 报案人手机号 */
	private String reportPhoneNo = "";
	
	/* 出险地点 */
	private String damageAddress=""; 
	
	
	/* 出险地点类型 */
	private String damageAdsType=""; 
	/* 互碰自赔 */
	private String isSelfCompenClaim=""; 
	/* 是否现场报案 */
	private String isSceneRep=""; 
 

	/* 联系人 */
	private String linker = "";
	/* 联系人电话 */
	private String linkerTelNo = "";
	/* 联系人手机号 */
	private String linkerPhoneNo = "";

	/* 驾驶人姓名 */
	private String driverName = "";

	/* 驾驶人性别 */
	private String driverSex = "";

	/* 紧急程度 */
	private String urgencyLevel = "";
	/* 赔案类型 */
	private String claimType = "";
	/* 出险日期 */
	private String damageDate = "";
	/* 出险时间 */
	private String damageTime = "";
	/* 出险原因 */
	private String damageReason = "";
	/* 受理原因 */
	private String acceptReason = "";
	/* 报案日期 */
	private String reportDate = "";
	/* 报案时间 */
	private String reportTime = "";

	/* 出现经过 */
	private String dangerAfter = "";

	/* 机构 */
	private String comCode = "";

	public void setComCode(String comCode) {
		this.comCode = comCode;
	}

	public String getComCode() {
		return comCode;
	}

	public void setDangerAfter(String dangerAfter) {
		this.dangerAfter = dangerAfter;
	}

	public String getDangerAfter() {
		return dangerAfter;
	}

	public String getDamageAddress() {
		return damageAddress;
	}
	public void setDamageAddress(String damageAddress) {
		this.damageAddress = damageAddress;
	}
	public String getIsSelfCompenClaim() {
		return isSelfCompenClaim;
	}
	public void setIsSelfCompenClaim(String isSelfCompenClaim) {
		this.isSelfCompenClaim = isSelfCompenClaim;
	}
	public String getIsSceneRep() {
		return isSceneRep;
	}
	public void setIsSceneRep(String isSceneRep) {
		this.isSceneRep = isSceneRep;
	}
	public String getReportNo() {
		return reportNo;
	}
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}
	public String getSeatCode() {
		return seatCode;
	}
	public void setSeatCode(String seatCode) {
		this.seatCode = seatCode;
	}
	public String getSeatName() {
		return seatName;
	}
	public void setSeatName(String seatName) {
		this.seatName = seatName;
	}
	public String getReporter() {
		return reporter;
	}
	public void setReporter(String reporter) {
		this.reporter = reporter;
	}
	public String getReportTelNo() {
		return reportTelNo;
	}
	public void setReportTelNo(String reportTelNo) {
		this.reportTelNo = reportTelNo;
	}
	public String getReportPhoneNo() {
		return reportPhoneNo;
	}
	public void setReportPhoneNo(String reportPhoneNo) {
		this.reportPhoneNo = reportPhoneNo;
	}
	public String getLinker() {
		return linker;
	}
	public void setLinker(String linker) {
		this.linker = linker;
	}
	public String getLinkerTelNo() {
		return linkerTelNo;
	}
	public void setLinkerTelNo(String linkerTelNo) {
		this.linkerTelNo = linkerTelNo;
	}
	public String getLinkerPhoneNo() {
		return linkerPhoneNo;
	}
	public void setLinkerPhoneNo(String linkerPhoneNo) {
		this.linkerPhoneNo = linkerPhoneNo;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getDriverSex() {
		return driverSex;
	}
	public void setDriverSex(String driverSex) {
		this.driverSex = driverSex;
	}
	public String getUrgencyLevel() {
		return urgencyLevel;
	}
	public void setUrgencyLevel(String urgencyLevel) {
		this.urgencyLevel = urgencyLevel;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public String getDamageDate() {
		return damageDate;
	}
	public void setDamageDate(String damageDate) {
		this.damageDate = damageDate;
	}
	public String getDamageTime() {
		return damageTime;
	}
	public void setDamageTime(String damageTime) {
		this.damageTime = damageTime;
	}
	public String getDamageReason() {
		return damageReason;
	}
	public void setDamageReason(String damageReason) {
		this.damageReason = damageReason;
	}
	public String getAcceptReason() {
		return acceptReason;
	}
	public void setAcceptReason(String acceptReason) {
		this.acceptReason = acceptReason;
	}
	public String getReportDate() {
		return reportDate;
	}
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
	public String getReportTime() {
		return reportTime;
	}
	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}
	public String getDamageAdsType() {
		return damageAdsType;
	}
	public void setDamageAdsType(String damageAdsType) {
		this.damageAdsType = damageAdsType;
	}
	 
	
	
	

}
