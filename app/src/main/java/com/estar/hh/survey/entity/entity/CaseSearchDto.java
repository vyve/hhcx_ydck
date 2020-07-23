package com.estar.hh.survey.entity.entity;

import java.io.Serializable;

/**
 * 
 * @Description:〈查询案件列表响应〉<br/>
 * @Provider:广州易星科技有限公司 <br/>
 * @Author: chenhuan@estar-info.com<br/>
 * @Date:2017年11月30日
 */
public class CaseSearchDto implements Serializable{

	// -----------1.案件列表详细
	private String rptNo = "";// 报案号
	private String lcnNo = "";// 车牌号
	private String taskTyp = "";// 任务类型
	private String incNme = "";// 被保人
	private String cost = "";// 案件时长(当前时间-调度时间)，如果结案（结案时间-调度时间）
	private String status = "";// 案件状态
	private String reporter="";//报案人
	
	

	// -----------2.任务列表详细(查勘+车损+物损公用)
	private String lossName = "";// 损失名称
	private String insName = "";// 被保人
	private String taskType = "";// 损失类型
	private String taskState = "";// 任务状态
	private String taskNo = "";// 任务号
	private String taskCost="";//任务时长

	// ------------车损区域
	private String pjMoney = "";// 配件金额
	private String gshMoney = "";// 工时金额
	private String hsMoney = "";// 核损金额
	private String jaPayAmt = "";// 结案金额(若有结案则显示结案金额，没有则不赋值)
	
	// -------------物损区域
	private String gsMoney = "";// 估损总金额
	private String propMoney = "";// 物损总金额
	private String czMoney = "";// 残值总金额

	public void setReporter(String reporter) {
		this.reporter = reporter;
	}

	public String getReporter() {
		return reporter;
	}

	public String getInsName() {
		return insName;
	}

	public void setInsName(String insName) {
		this.insName = insName;
	}

	public String getTaskState() {
		return taskState;
	}

	public void setTaskState(String taskState) {
		this.taskState = taskState;
	}

	public String getLossName() {
		return lossName;
	}

	public void setLossName(String lossName) {
		this.lossName = lossName;
	}

	public String getRptNo() {
		return rptNo;
	}

	public void setRptNo(String rptNo) {
		this.rptNo = rptNo;
	}

	public String getLcnNo() {
		return lcnNo;
	}

	public void setLcnNo(String lcnNo) {
		this.lcnNo = lcnNo;
	}

	public String getTaskTyp() {
		return taskTyp;
	}

	public void setTaskTyp(String taskTyp) {
		this.taskTyp = taskTyp;
	}

	public String getIncNme() {
		return incNme;
	}

	public void setIncNme(String incNme) {
		this.incNme = incNme;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getTaskNo() {
		return taskNo;
	}

	public void setTaskNo(String taskNo) {
		this.taskNo = taskNo;
	}

	public String getPjMoney() {
		return pjMoney;
	}

	public void setPjMoney(String pjMoney) {
		this.pjMoney = pjMoney;
	}

	public String getGshMoney() {
		return gshMoney;
	}

	public void setGshMoney(String gshMoney) {
		this.gshMoney = gshMoney;
	}

	public String getHsMoney() {
		return hsMoney;
	}

	public void setHsMoney(String hsMoney) {
		this.hsMoney = hsMoney;
	}

	public String getJaPayAmt() {
		return jaPayAmt;
	}

	public void setJaPayAmt(String jaPayAmt) {
		this.jaPayAmt = jaPayAmt;
	}

	public String getGsMoney() {
		return gsMoney;
	}

	public void setGsMoney(String gsMoney) {
		this.gsMoney = gsMoney;
	}

	public String getPropMoney() {
		return propMoney;
	}

	public void setPropMoney(String propMoney) {
		this.propMoney = propMoney;
	}

	public String getCzMoney() {
		return czMoney;
	}

	public void setCzMoney(String czMoney) {
		this.czMoney = czMoney;
	}

	public String getTaskCost() {
		return taskCost;
	}

	public void setTaskCost(String taskCost) {
		this.taskCost = taskCost;
	}
	
	

}
