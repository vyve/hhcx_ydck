package com.estar.hh.survey.entity.entity;

import java.io.Serializable;

/**
 * 
 * @Description:〈任务详细〉<br/>
 * @Provider:广州易星科技有限公司 <br/>
 * @Author: chenhuan@estar-info.com<br/>
 * @Date:2017年10月27日
 */
public class TaskInfoDetailDto implements Serializable{
	private String reportNo = "";// 报案号
	private String taskNo = "";// 任务号
	private String taskTypeCode = "";// 任务类型-代码
	private String taskTypeName = "";// 任务类型-名称
	private String taskStateCode = "";// 任务状态-代码
	private String taskStateName = "";// 任务状态-名称
	private String taskCrt = "";// 任务生成标志-后台生成=0；前台生成=1
	private String empCde = "";// 查勘员代码
	private String empNme = "";// 查勘员名称
	private String factEmpCde = "";// 改派后查勘员代码
	private String factEmpNme = "";// 改派后查勘员名称
	private String srvyEmpTel = "";// 查勘员电话
	private String vhlNme = "";// 损失名称
	private String srvyAddr = "";// 查勘地点
	private String longTime = "";// 时长（服务器当前时间-派工时间，随每次查询而变）
	private String dispatchTime = "";// 派工时间(理赔推送)
	private String quitMemo = "";// ****退回原因描述(如果是退回任务，理赔必传)
	private String quitCode = "";// ****退回人(如果是退回任务，理赔必传)
	//returnURL refreshURL lossItemNumber
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

	public String getTaskTypeCode() {
		return taskTypeCode;
	}

	public void setTaskTypeCode(String taskTypeCode) {
		this.taskTypeCode = taskTypeCode;
	}

	public String getTaskTypeName() {
		return taskTypeName;
	}

	public void setTaskTypeName(String taskTypeName) {
		this.taskTypeName = taskTypeName;
	}

	public String getTaskStateCode() {
		return taskStateCode;
	}

	public void setTaskStateCode(String taskStateCode) {
		this.taskStateCode = taskStateCode;
	}

	public String getTaskStateName() {
		return taskStateName;
	}

	public void setTaskStateName(String taskStateName) {
		this.taskStateName = taskStateName;
	}

	public String getTaskCrt() {
		return taskCrt;
	}

	public void setTaskCrt(String taskCrt) {
		this.taskCrt = taskCrt;
	}

	public String getEmpCde() {
		return empCde;
	}

	public void setEmpCde(String empCde) {
		this.empCde = empCde;
	}

	public String getEmpNme() {
		return empNme;
	}

	public void setEmpNme(String empNme) {
		this.empNme = empNme;
	}

	public String getFactEmpCde() {
		return factEmpCde;
	}

	public void setFactEmpCde(String factEmpCde) {
		this.factEmpCde = factEmpCde;
	}

	public String getFactEmpNme() {
		return factEmpNme;
	}

	public void setFactEmpNme(String factEmpNme) {
		this.factEmpNme = factEmpNme;
	}

	public String getSrvyEmpTel() {
		return srvyEmpTel;
	}

	public void setSrvyEmpTel(String srvyEmpTel) {
		this.srvyEmpTel = srvyEmpTel;
	}

	public String getVhlNme() {
		return vhlNme;
	}

	public void setVhlNme(String vhlNme) {
		this.vhlNme = vhlNme;
	}

	public String getSrvyAddr() {
		return srvyAddr;
	}

	public void setSrvyAddr(String srvyAddr) {
		this.srvyAddr = srvyAddr;
	}

	public String getLongTime() {
		return longTime;
	}

	public void setLongTime(String longTime) {
		this.longTime = longTime;
	}

	public String getDispatchTime() {
		return dispatchTime;
	}

	public void setDispatchTime(String dispatchTime) {
		this.dispatchTime = dispatchTime;
	}

	public String getQuitMemo() {
		return quitMemo;
	}

	public void setQuitMemo(String quitMemo) {
		this.quitMemo = quitMemo;
	}

	public String getQuitCode() {
		return quitCode;
	}

	public void setQuitCode(String quitCode) {
		this.quitCode = quitCode;
	}

}
