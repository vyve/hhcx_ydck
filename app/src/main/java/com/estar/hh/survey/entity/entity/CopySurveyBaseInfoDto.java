package com.estar.hh.survey.entity.entity;

import java.io.Serializable;

/**
 * @Description:〈查勘基本信息界面〉<br/>
 * @Provider:广州易星科技有限公司 <br/>
 * @Author: chenhuan@estar-info.com<br/>
 * @Date:2017年10月27日
 */
public class CopySurveyBaseInfoDto implements Serializable{
	private String reportNo = "";// 报案号
	private String trafPolicyNo = "";// 交强保单号
	private String busPolicyNo = "";// 商业保单号
	private String reportType = "";// 案件类型
	private String trafBuildRepNo = "";// 交强立案号
	private String busBuildRepNo = "";// 商业立案号
	private String reportTime = "";// 报案时间
	private String insOrg = "";// 交强承保机构
	private String businsOrg = "";// 商业承保机构
	private String insTime = "";// 出险时间
	private String trafInsStartDate = "";// 交强保险起期
	private String businsStartDate = "";// 商业保险起期
	private String trafInsEndDate = "";// 交强保险止期
	private String businsEndDate = "";// 商业保险止期
	private String trafPending = "";// 交强未决
	private String busPending = "";// 商业未决
	private String riskWarn="";//风险预警
	private String trafAlreadyPending = "";// 交强已决
	private String busAlreadyPending = "";// 商业已决
	private String reportPerson = "";// 报案人
	private String insuranceName = "";// 被保险人
	private String customerLevel = "";// 客户等级
	private String telNo="";// 联系电话
	private String licenseNo;// 标的车牌号
	private String vinNo = "";// 车架号
	private String isSubrogation = "";// 是否代位求偿
	private String brandModels = "";// 厂牌车型
	private String engineNo;// 发动机号
	private String accidentType = "";// 事故分类
	private String dangerAddress = "";// 出险地点
	private String accidentResponsibility = "";// 事故责任比例 %
	private String bussArticles = "";// 商业条款类型
	private String billCollectAllTime = "";// 单证首次收集齐全时间
	public String getReportNo() {
		return reportNo;
	}
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}
	public String getTrafPolicyNo() {
		return trafPolicyNo;
	}
	public void setTrafPolicyNo(String trafPolicyNo) {
		this.trafPolicyNo = trafPolicyNo;
	}
	public String getBusPolicyNo() {
		return busPolicyNo;
	}
	public void setBusPolicyNo(String busPolicyNo) {
		this.busPolicyNo = busPolicyNo;
	}
	public String getReportType() {
		return reportType;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	public String getTrafBuildRepNo() {
		return trafBuildRepNo;
	}
	public void setTrafBuildRepNo(String trafBuildRepNo) {
		this.trafBuildRepNo = trafBuildRepNo;
	}
	public String getBusBuildRepNo() {
		return busBuildRepNo;
	}
	public void setBusBuildRepNo(String busBuildRepNo) {
		this.busBuildRepNo = busBuildRepNo;
	}
	public String getReportTime() {
		return reportTime;
	}
	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}
	public String getInsOrg() {
		return insOrg;
	}
	public void setInsOrg(String insOrg) {
		this.insOrg = insOrg;
	}
	public String getBusinsOrg() {
		return businsOrg;
	}
	public void setBusinsOrg(String businsOrg) {
		this.businsOrg = businsOrg;
	}
	public String getInsTime() {
		return insTime;
	}
	public void setInsTime(String insTime) {
		this.insTime = insTime;
	}
	public String getTrafInsStartDate() {
		return trafInsStartDate;
	}
	public void setTrafInsStartDate(String trafInsStartDate) {
		this.trafInsStartDate = trafInsStartDate;
	}
	public String getBusinsStartDate() {
		return businsStartDate;
	}
	public void setBusinsStartDate(String businsStartDate) {
		this.businsStartDate = businsStartDate;
	}
	public String getTrafInsEndDate() {
		return trafInsEndDate;
	}
	public void setTrafInsEndDate(String trafInsEndDate) {
		this.trafInsEndDate = trafInsEndDate;
	}
	public String getBusinsEndDate() {
		return businsEndDate;
	}
	public void setBusinsEndDate(String businsEndDate) {
		this.businsEndDate = businsEndDate;
	}
	public String getTrafPending() {
		return trafPending;
	}
	public void setTrafPending(String trafPending) {
		this.trafPending = trafPending;
	}
	public String getBusPending() {
		return busPending;
	}
	public void setBusPending(String busPending) {
		this.busPending = busPending;
	}
	public String getRiskWarn() {
		return riskWarn;
	}
	public void setRiskWarn(String riskWarn) {
		this.riskWarn = riskWarn;
	}
	public String getTrafAlreadyPending() {
		return trafAlreadyPending;
	}
	public void setTrafAlreadyPending(String trafAlreadyPending) {
		this.trafAlreadyPending = trafAlreadyPending;
	}
	public String getBusAlreadyPending() {
		return busAlreadyPending;
	}
	public void setBusAlreadyPending(String busAlreadyPending) {
		this.busAlreadyPending = busAlreadyPending;
	}
	public String getReportPerson() {
		return reportPerson;
	}
	public void setReportPerson(String reportPerson) {
		this.reportPerson = reportPerson;
	}
	public String getInsuranceName() {
		return insuranceName;
	}
	public void setInsuranceName(String insuranceName) {
		this.insuranceName = insuranceName;
	}
	public String getCustomerLevel() {
		return customerLevel;
	}
	public void setCustomerLevel(String customerLevel) {
		this.customerLevel = customerLevel;
	}
	public String getTelNo() {
		return telNo;
	}
	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}
	public String getLicenseNo() {
		return licenseNo;
	}
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	public String getVinNo() {
		return vinNo;
	}
	public void setVinNo(String vinNo) {
		this.vinNo = vinNo;
	}
	public String getIsSubrogation() {
		return isSubrogation;
	}
	public void setIsSubrogation(String isSubrogation) {
		this.isSubrogation = isSubrogation;
	}
	public String getBrandModels() {
		return brandModels;
	}
	public void setBrandModels(String brandModels) {
		this.brandModels = brandModels;
	}
	public String getEngineNo() {
		return engineNo;
	}
	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}
	public String getAccidentType() {
		return accidentType;
	}
	public void setAccidentType(String accidentType) {
		this.accidentType = accidentType;
	}
	public String getDangerAddress() {
		return dangerAddress;
	}
	public void setDangerAddress(String dangerAddress) {
		this.dangerAddress = dangerAddress;
	}
	public String getAccidentResponsibility() {
		return accidentResponsibility;
	}
	public void setAccidentResponsibility(String accidentResponsibility) {
		this.accidentResponsibility = accidentResponsibility;
	}
	public String getBussArticles() {
		return bussArticles;
	}
	public void setBussArticles(String bussArticles) {
		this.bussArticles = bussArticles;
	}
	public String getBillCollectAllTime() {
		return billCollectAllTime;
	}
	public void setBillCollectAllTime(String billCollectAllTime) {
		this.billCollectAllTime = billCollectAllTime;
	}
	
	
	
}
