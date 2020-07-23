package com.estar.hh.survey.entity.entity;

import java.io.Serializable;

/**
 * @Description:〈抄单信息-保单信息〉<br/>
 * 
 * @Provider:广州易星科技有限公司 <br/>
 * @Author: chenhuan@estar-info.com<br/>
 * @Date:2017年10月27日
 */
public class CopyPolicyDto implements Serializable {

	private String policyNo = "";// 保单号
	private String carNo = "";// 车牌号
	private String brandName = "";// 厂牌车型
	private String insuranceName = "";// 被保险人名称
	private String insureStartTime = "";// 保险起期
	private String insureEndTime = "";// 保险止期
	private String insureInstitution = "";// 承保机构简称
	private String policyType = "";// 保单类型（交强、商业）
	
	private String reportNo="";//报案号、
	private String clauseType="";//条款类别名称

	private String insTypeCode="";//险种代码
	private String insTypeName="";//险种名称

	private String salesCommissionerCode="";//特殊业务类型 add by zhengyg 2018年12月28日16:39:01

	private String shareholderIdentity;
	private String shareholderIdentityCode;
	private String vipLevel;
	private String specialClientName;//特殊客户类型

	public String getSpecialClientName() {
		return specialClientName;
	}

	public void setSpecialClientName(String specialClientName) {
		this.specialClientName = specialClientName;
	}

	//	private String isAgriculture;
//
//	public String getIsAgriculture() {
//		return isAgriculture;
//	}
//
//	public void setIsAgriculture(String isAgriculture) {
//		this.isAgriculture = isAgriculture;
//	}


	public String getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(String vipLevel) {
		this.vipLevel = vipLevel;
	}

	public String getShareholderIdentity() {
		return shareholderIdentity;
	}

	public void setShareholderIdentity(String shareholderIdentity) {
		this.shareholderIdentity = shareholderIdentity;
	}

	public String getShareholderIdentityCode() {
		return shareholderIdentityCode;
	}

	public void setShareholderIdentityCode(String shareholderIdentityCode) {
		this.shareholderIdentityCode = shareholderIdentityCode;
	}

	public String getSalesCommissionerCode() {
		return salesCommissionerCode;
	}

	public void setSalesCommissionerCode(String salesCommissionerCode) {
		this.salesCommissionerCode = salesCommissionerCode;
	}

	public void setInsTypeCode(String insTypeCode) {
		this.insTypeCode = insTypeCode;
	}

	public String getInsTypeCode() {
		return insTypeCode;
	}

	public void setInsTypeName(String insTypeName) {
		this.insTypeName = insTypeName;
	}

	public String getInsTypeName() {
		return insTypeName;
	}

	// 险种代码、险种名称
	public String getPolicyNo() {
		return policyNo;
	}

	public String getReportNo() {
		return reportNo;
	}

	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

	public String getClauseType() {
		return clauseType;
	}

	public void setClauseType(String clauseType) {
		this.clauseType = clauseType;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getCarNo() {
		return carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getInsuranceName() {
		return insuranceName;
	}

	public void setInsuranceName(String insuranceName) {
		this.insuranceName = insuranceName;
	}

	public String getInsureStartTime() {
		return insureStartTime;
	}

	public void setInsureStartTime(String insureStartTime) {
		this.insureStartTime = insureStartTime;
	}

	public String getInsureEndTime() {
		return insureEndTime;
	}

	public void setInsureEndTime(String insureEndTime) {
		this.insureEndTime = insureEndTime;
	}

	public String getInsureInstitution() {
		return insureInstitution;
	}

	public void setInsureInstitution(String insureInstitution) {
		this.insureInstitution = insureInstitution;
	}

	public String getPolicyType() {
		return policyType;
	}

	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}

	// private String productName = "";//产品名称
	// private String customerLevel = "";//客户等级
	// private String vinNo = "";//vinNo
	// private String insureInstitution = "";//承保机构简称

}
