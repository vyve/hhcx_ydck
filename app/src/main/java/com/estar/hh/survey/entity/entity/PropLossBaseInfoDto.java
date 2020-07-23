package com.estar.hh.survey.entity.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * 
 * @Description : 〈物损基本信息〉 <br/>
 * @Provider：广州易星科技有限公司 <br/>
 * @Author : chenhuan@estar-info.com<br/>
 * @Date：2018年1月19日
 */
public class PropLossBaseInfoDto extends DataSupport implements Serializable {

	/* 报案号 */
	private String reportNo = "";
	/* 任务号 */
	private String taskNo = "";
	private String caseFlag = "0";// 互碰自赔 1-是 0-否 –默认否(界面传值,非必传)
	private String defLossDate = "";// 定损时间(隐藏传值，必传)
	private String isRoad = "0";// 是否路面 1是 0否 (界面传值，必传)
	private String defRescueFee = "0";// 施救金额(界面传值，必传)
	private String defRescueRemark = "";// 过程描述(界面传值)
	private String lossOpinion = "";// 定损意见(界面传值，必传)
	private String createTime = "";

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

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public String getCaseFlag() {
		return caseFlag;
	}

	public void setCaseFlag(String caseFlag) {
		this.caseFlag = caseFlag;
	}

	public String getDefLossDate() {
		return defLossDate;
	}

	public void setDefLossDate(String defLossDate) {
		this.defLossDate = defLossDate;
	}

	public String getIsRoad() {
		return isRoad;
	}

	public void setIsRoad(String isRoad) {
		this.isRoad = isRoad;
	}
	
	
 
	public String getDefRescueFee() {
		return defRescueFee;
	}

	public void setDefRescueFee(String defRescueFee) {
		this.defRescueFee = defRescueFee;
	}

	public String getDefRescueRemark() {
		return defRescueRemark;
	}

	public void setDefRescueRemark(String defRescueRemark) {
		this.defRescueRemark = defRescueRemark;
	}

	public String getLossOpinion() {
		return lossOpinion;
	}

	public void setLossOpinion(String lossOpinion) {
		this.lossOpinion = lossOpinion;
	}

}
