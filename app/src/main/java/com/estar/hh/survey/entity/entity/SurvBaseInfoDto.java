package com.estar.hh.survey.entity.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * @Description:〈查勘基本信息〉<br/>
 * 
 * @Provider:广州易星科技有限公司 <br/>
 * @Author: chenhuan@estar-info.com<br/>f
 * @Date:2017年11月1日
 */
public class SurvBaseInfoDto extends DataSupport implements Serializable {

	/* 报案号 */
	private String reportNo = "";
	/* 任务号 */
	private String taskNo = "";
	private String lossType = "";// 损失类别
	private String damageCode = "";// 出险原因
	private String damageCaseCode = "";// 事故类型
	private String checkSite = "";// 查勘地点
	private String ciIndemDuty = "0";// 是否属于交强险责任 0否 1是
	private String ciDutyFlag = "0";// 交强险责任 0否 100是
	private String claimFlag = "0";// 是否属于商业险责任 0否 1是
	private String indemnityDuty = "";// 商业险赔偿责任
	private String caseFlag = "0";// 互碰自赔
	private String isSubrogationMark = "0";// 代位标识
	private String isCoverSubrogationMark = "0";// 被代位标识
	private String context = "";// 查勘意见
	private String createTime = "";

	/**
	 * 新增字段
	 */
	private String sumLossFee	 = "0";//损失预估金额
	private String reserveFlag	 = "0";//重大赔案标识
	private String checkDate	 = "";//查勘日期 2017-12-12
	private String checker1IdentNum = "";// 查勘人1身份证号码
	private String firstSiteFlagQuery = "";// 是否第一现场查勘 0-无现场 1-第一现场 2-第二现场
	private String isFloodedCar = "0";// 是否水淹车 1是 0否
	private String damageTypeCode = "";// 事故原因

	/**
	 * add by zhengyg
	 * 2018年11月23日15:17:19
	 * 新增 北分所需字段，有无交管事故书
	 */
	private String haveBook = "0";//有无交管事故书 1-是,0-否

	/**
	 * 如果选择了水淹是，则必须传水淹等级
	 * 1 水淹一级
	 * 2 水淹二级
	 * 3 水淹三级
	 * 4 水淹四级
	 */
	private String floodedLevel="";//水淹等级
	private String isPerson = "0";// 是否新增人伤 1是  0否
	private String insureAccident="";//保险事故分类(数据字典)

	public String getHaveBook() {
		return haveBook;
	}

	public void setHaveBook(String haveBook) {
		this.haveBook = haveBook;
	}

	public void setDamageTypeCode(String damageTypeCode) {
		this.damageTypeCode = damageTypeCode;
	}

	public String getDamageTypeCode() {
		return damageTypeCode;
	}

	public void setInsureAccident(String insureAccident) {
		this.insureAccident = insureAccident;
	}

	public String getInsureAccident() {
		return insureAccident;
	}

	public void setFloodedLevel(String floodedLevel) {
		this.floodedLevel = floodedLevel;
	}

	public String getFloodedLevel() {
		return floodedLevel;
	}

	public void setSumLossFee(String sumLossFee) {
		this.sumLossFee = sumLossFee;
	}

	public void setReserveFlag(String reserveFlag) {
		this.reserveFlag = reserveFlag;
	}

	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
	}

	public String getSumLossFee() {
		return sumLossFee;
	}

	public String getReserveFlag() {
		return reserveFlag;
	}

	public String getCheckDate() {
		return checkDate;
	}

	public void setChecker1IdentNum(String checker1IdentNum) {
		this.checker1IdentNum = checker1IdentNum;
	}

	public void setFirstSiteFlagQuery(String firstSiteFlagQuery) {
		this.firstSiteFlagQuery = firstSiteFlagQuery;
	}

	public void setIsFloodedCar(String isFloodedCar) {
		this.isFloodedCar = isFloodedCar;
	}

	public void setIsPerson(String isPerson) {
		this.isPerson = isPerson;
	}

	public String getChecker1IdentNum() {
		return checker1IdentNum;
	}

	public String getFirstSiteFlagQuery() {
		return firstSiteFlagQuery;
	}

	public String getIsFloodedCar() {
		return isFloodedCar;
	}

	public String getIsPerson() {
		return isPerson;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreateTime() {
		return createTime;
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

	public String getLossType() {
		return lossType;
	}

	public void setLossType(String lossType) {
		this.lossType = lossType;
	}

	public String getDamageCode() {
		return damageCode;
	}

	public void setDamageCode(String damageCode) {
		this.damageCode = damageCode;
	}

	public String getDamageCaseCode() {
		return damageCaseCode;
	}

	public void setDamageCaseCode(String damageCaseCode) {
		this.damageCaseCode = damageCaseCode;
	}

	public String getCheckSite() {
		return checkSite;
	}

	public void setCheckSite(String checkSite) {
		this.checkSite = checkSite;
	}

	public String getCiIndemDuty() {
		return ciIndemDuty;
	}

	public void setCiIndemDuty(String ciIndemDuty) {
		this.ciIndemDuty = ciIndemDuty;
	}

	public String getCiDutyFlag() {
		return ciDutyFlag;
	}

	public void setCiDutyFlag(String ciDutyFlag) {
		this.ciDutyFlag = ciDutyFlag;
	}

	public String getClaimFlag() {
		return claimFlag;
	}

	public void setClaimFlag(String claimFlag) {
		this.claimFlag = claimFlag;
	}

	public String getIndemnityDuty() {
		return indemnityDuty;
	}

	public void setIndemnityDuty(String indemnityDuty) {
		this.indemnityDuty = indemnityDuty;
	}

	public String getCaseFlag() {
		return caseFlag;
	}

	public void setCaseFlag(String caseFlag) {
		this.caseFlag = caseFlag;
	}

	public String getIsSubrogationMark() {
		return isSubrogationMark;
	}

	public void setIsSubrogationMark(String isSubrogationMark) {
		this.isSubrogationMark = isSubrogationMark;
	}

	public String getIsCoverSubrogationMark() {
		return isCoverSubrogationMark;
	}

	public void setIsCoverSubrogationMark(String isCoverSubrogationMark) {
		this.isCoverSubrogationMark = isCoverSubrogationMark;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	// private String checkType="";//查勘类型
	// private String damageCaseName="";//事故类型名称
	// private String insureAccident="";//保险事故分类
	// private String damageName="";//出险原因名称
	// private String riskAddrType="";//出险地点类型
	//
	// private String dutyFlag="";//有无交警事故认定书
	// private String importantFlag;//重案标志
	// private String checker1Code="";//查勘人代码
	// private String checker1IdentNum="";//查勘人身份证号码
	// private String checker1="";//查勘人名称
	// private String firstSiteFlag="";//是否第一现场
	//
	// private String checkDate="";//查勘日期
	// private String fixType="";//修理厂类型

}
