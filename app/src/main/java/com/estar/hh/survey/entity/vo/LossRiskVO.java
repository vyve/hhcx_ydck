package com.estar.hh.survey.entity.vo;

import java.io.Serializable;

public class LossRiskVO implements Serializable {
	
	/**
	 * 险别信息
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;// 承保险别
	private String code;// 险别代码
	private String lossNo="";//定损单号
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getLossNo() {
		return lossNo;
	}
	public void setLossNo(String lossNo) {
		this.lossNo = lossNo;
	}
}

