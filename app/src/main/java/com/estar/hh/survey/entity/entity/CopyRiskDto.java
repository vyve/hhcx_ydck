package com.estar.hh.survey.entity.entity;

import java.io.Serializable;

/**
 * @Description:〈承保险别信息〉<br/>
 * @Provider:广州易星科技有限公司 <br/>
 * @Author: chenhuan@estar-info.com<br/>
 * @Date:2017年10月27日
 */
public class CopyRiskDto implements Serializable {
	/* 险别代码*/
	private String insuranceCode;
	/* 险别名称*/
	private String insuranceName;
	/* 不计免赔*/
	private String noFreeAmount;
	/* 绝对免赔额*/
	private String absolFreeAmount;
	/* 保险金额*/
	private String insuranceAmount;
	public String getInsuranceCode() {
		return insuranceCode;
	}
	public void setInsuranceCode(String insuranceCode) {
		this.insuranceCode = insuranceCode;
	}
	public String getInsuranceName() {
		return insuranceName;
	}
	public void setInsuranceName(String insuranceName) {
		this.insuranceName = insuranceName;
	}
	public String getNoFreeAmount() {
		return noFreeAmount;
	}
	public void setNoFreeAmount(String noFreeAmount) {
		this.noFreeAmount = noFreeAmount;
	}
	public String getAbsolFreeAmount() {
		return absolFreeAmount;
	}
	public void setAbsolFreeAmount(String absolFreeAmount) {
		this.absolFreeAmount = absolFreeAmount;
	}
	public String getInsuranceAmount() {
		return insuranceAmount;
	}
	public void setInsuranceAmount(String insuranceAmount) {
		this.insuranceAmount = insuranceAmount;
	}
	
	
	
}
