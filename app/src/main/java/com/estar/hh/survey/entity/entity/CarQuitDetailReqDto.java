package com.estar.hh.survey.entity.entity;

import java.io.Serializable;
/**
 * 
 * @Description:〈车退回详细查询〉<br/>
 * @Provider:广州易星科技有限公司 <br/>
 * @Author: chenhuan@estar-info.com<br/>
 * @Date:2017年12月1日
 */
public class CarQuitDetailReqDto implements Serializable {
	private String reportNo = "";// 报案号
	private String reqType = "";// 请求类型1车退回 2物退回
	private String taskNo = "";// 任务号
	private String taskStatus = "";//

	public String getReportNo() {
		return reportNo;
	}

	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

	public String getReqType() {
		return reqType;
	}

	public void setReqType(String reqType) {
		this.reqType = reqType;
	}

	public String getTaskNo() {
		return taskNo;
	}

	public void setTaskNo(String taskNo) {
		this.taskNo = taskNo;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

}
