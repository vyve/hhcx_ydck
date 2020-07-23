package com.estar.hh.survey.entity.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * @Description:〈描述〉<br/>
 * @Provider:广州易星科技有限公司 <br/>
 * @Author: chenhuan@estar-info.com<br/>
 * @Date:2017年11月1日
 */
public class GoodInfoDto extends DataSupport implements Serializable {


	/* 报案号 */
	private String reportNo = "";
	/* 任务号 */
	private String taskNo = "";

	private String lossItemType="";//损失方
	private String lossItemName="";//财产名称
	private String sumRescueFee="0";//施救费
	private String sumLossFee="0";//损失预估金额
	private String createTime = "";

	/**
	 * 新增字段
	 */
	private String lossDegreeName="";//损失程度 01-轻微受损 02-轻度受损 03-中度受损 04-严重受损 05-非常严重 99-其他
	private String lossSpeciesCode="";//损失种类 01-通讯设施 02-绿化物 03-农产品 04-电力设施 05-车载货物 06-房屋设施 07-随车物品 08-建筑物 99-其他
	private String lossType = "";//03-三者 04-地面财产 07-标的

	/*查勘物损列表新增*/
	private String degree = "";// 损失程度
	private String remark = "";// 备注


	public void setDegree(String degree) {
		this.degree = degree;
	}

	public String getDegree() {
		return degree;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRemark() {
		return remark;
	}

	public void setLossDegreeName(String lossDegreeName) {
		this.lossDegreeName = lossDegreeName;
	}

	public void setLossSpeciesCode(String lossSpeciesCode) {
		this.lossSpeciesCode = lossSpeciesCode;
	}

	public void setLossType(String lossType) {
		this.lossType = lossType;
	}

	public String getLossDegreeName() {
		return lossDegreeName;
	}

	public String getLossSpeciesCode() {
		return lossSpeciesCode;
	}

	public String getLossType() {
		return lossType;
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

	public String getLossItemType() {
		return lossItemType;
	}
	public void setLossItemType(String lossItemType) {
		this.lossItemType = lossItemType;
	}
	public String getLossItemName() {
		return lossItemName;
	}
	public void setLossItemName(String lossItemName) {
		this.lossItemName = lossItemName;
	}
	public String getSumRescueFee() {
		return sumRescueFee;
	}
	public void setSumRescueFee(String sumRescueFee) {
		this.sumRescueFee = sumRescueFee;
	}
	public String getSumLossFee() {
		return sumLossFee;
	}
	public void setSumLossFee(String sumLossFee) {
		this.sumLossFee = sumLossFee;
	}

	// private String licenseNo="";//手机端新增财产任务必录,必传
	// private String userCode="";//用户代码
	// private String mainId="";//
	// private String registNo;//报案号


}
