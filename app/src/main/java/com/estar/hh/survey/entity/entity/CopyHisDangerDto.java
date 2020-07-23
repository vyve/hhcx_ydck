package com.estar.hh.survey.entity.entity;
import java.io.Serializable;

/**
 * @Description:〈历史出险信息〉<br/>
 * 
 * @Provider:广州易星科技有限公司 <br/>
 * @Author: chenhuan@estar-info.com<br/>
 * @Date:2017年10月27日
 */
public class CopyHisDangerDto implements Serializable {

	/* 报案号 */
	private String reportNo = "";
	/* 报案人姓名 */
	// private String reportName = "";
	/* 报案时间 */
	private String reportTime = "";
	/* 出险时间 */
	private String dangerTime = "";
	/* 出险原因 */
	// private String dangerReason = "";
	/* 出险详细地点 */
	private String dangerDetailAddress = "";
	/* 出险经过 */
	// private String dangerAfter = "";
	
	private String insName="";//被保险人
	
	private String policyType="";//保单类型
	
	
	/* 赔款金额 */
	private String amount = "";
	
	
	
	
	public String getInsName() {
		return insName;
	}
	public void setInsName(String insName) {
		this.insName = insName;
	}
	public String getPolicyType() {
		return policyType;
	}
	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}
	public String getReportNo() {
		return reportNo;
	}
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}
	public String getReportTime() {
		return reportTime;
	}
	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}
	public String getDangerTime() {
		return dangerTime;
	}
	public void setDangerTime(String dangerTime) {
		this.dangerTime = dangerTime;
	}
	public String getDangerDetailAddress() {
		return dangerDetailAddress;
	}
	public void setDangerDetailAddress(String dangerDetailAddress) {
		this.dangerDetailAddress = dangerDetailAddress;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	
	

}
