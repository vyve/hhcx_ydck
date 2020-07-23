package com.estar.hh.survey.entity.entity;
 
import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * @Description:〈描述〉<br/>
 * @Provider:广州易星科技有限公司 <br/>
 * @Author: chenhuan@estar-info.com<br/>
 * @Date:2017年11月1日
 */
public class GoodLossInfoDto extends DataSupport implements Serializable {

	/* 报案号 */
	private String reportNo = "";
	/* 任务号 */
	private String taskNo = "";
	private String  lossParty="";//损失方
    private String  lossItemName="";//财产名称
    private String  lossMoney="";//定损金额
	private String  recyclePrice  ="";//残值金额
	private String  kinCode  ="";//险别代码
    private String  lossOpinion="";//定损意见
	private String createTime = "";

	/**
	 * 新增字段
	 */
	private String  lossRate="100";//报损比例
	private String  sumLoss="";//报损金额

	public void setLossRate(String lossRate) {
		this.lossRate = lossRate;
	}

	public void setSumLoss(String sumLoss) {
		this.sumLoss = sumLoss;
	}

	public String getLossRate() {
		return lossRate;
	}

	public String getSumLoss() {
		return sumLoss;
	}

	public void setKinCode(String kinCode) {
		this.kinCode = kinCode;
	}

	public String getKinCode() {
		return kinCode;
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

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public String getLossParty() {
		return lossParty;
	}
	public void setLossParty(String lossParty) {
		this.lossParty = lossParty;
	}
	public String getLossItemName() {
		return lossItemName;
	}
	public void setLossItemName(String lossItemName) {
		this.lossItemName = lossItemName;
	}
	public String getLossMoney() {
		return lossMoney;
	}
	public void setLossMoney(String lossMoney) {
		this.lossMoney = lossMoney;
	}
	public String getRecyclePrice() {
		return recyclePrice;
	}
	public void setRecyclePrice(String recyclePrice) {
		this.recyclePrice = recyclePrice;
	}
	public String getLossOpinion() {
		return lossOpinion;
	}
	public void setLossOpinion(String lossOpinion) {
		this.lossOpinion = lossOpinion;
	}

	// private String kindCode="";//险别
	// private String kindName="";//险别名称
	// private String lossItemtype="";//损失类型
	// private String licenseNo="";//手机端新增财产损失必录项
	// private String lossQuantity="";//数量
	// private String unitPrice ="";//单价
	// private String sumLoss ="";//报损金额
	//
	// private String lossRate ="";//损失比例
    
    
    

}
