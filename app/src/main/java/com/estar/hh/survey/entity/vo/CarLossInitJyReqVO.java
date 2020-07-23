package com.estar.hh.survey.entity.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CarLossInitJyReqVO implements Serializable {

	/**
	 * 精友请求字段
	 */
	private static final long serialVersionUID = 1L;
	private String insureVehicleCode="";//承保车型编码
	private String insureVehicleName="";//承保车型编码
	private String comCode="";//定损员所属分公司代码
	private String company="";//定损员所属分公司名称
	private String branchComCode="";//定损员所属中支代码
	private String branchComName="";//定损员所属中支名称
	private String handlerCode="";//定损员代码
	private String handlerName="";//定损员姓名
	private String isSubjectVehicle="";//是否标的车
	private String plateNo="";//车牌号码
	private String vinNo="";//VIN码
	private String driverName="";//驾驶员
	private String enrolDate="";//初登日期
	private String markColor="";//牌照颜色
	private String evalTypeCode="";//定损方式
	private String engineNo="";//发动机号
	private String LossNo="";//定损单号
	private String requestType="";//固定001：进入定损固定002：核损退回
	private List<LossRiskVO> riskList = new ArrayList<LossRiskVO>();

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getLossNo() {
		return LossNo;
	}

	public void setLossNo(String lossNo) {
		LossNo = lossNo;
	}

	public String getInsureVehicleCode() {
		return insureVehicleCode;
	}

	public void setInsureVehicleCode(String insureVehicleCode) {
		this.insureVehicleCode = insureVehicleCode;
	}

	

	public String getInsureVehicleName() {
		return insureVehicleName;
	}

	public void setInsureVehicleName(String insureVehicleName) {
		this.insureVehicleName = insureVehicleName;
	}

	public String getComCode() {
		return comCode;
	}

	public void setComCode(String comCode) {
		this.comCode = comCode;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getBranchComCode() {
		return branchComCode;
	}

	public void setBranchComCode(String branchComCode) {
		this.branchComCode = branchComCode;
	}

	public String getBranchComName() {
		return branchComName;
	}

	public void setBranchComName(String branchComName) {
		this.branchComName = branchComName;
	}

	public String getHandlerCode() {
		return handlerCode;
	}

	public void setHandlerCode(String handlerCode) {
		this.handlerCode = handlerCode;
	}

	public String getHandlerName() {
		return handlerName;
	}

	public void setHandlerName(String handlerName) {
		this.handlerName = handlerName;
	}

	public String getIsSubjectVehicle() {
		return isSubjectVehicle;
	}

	public void setIsSubjectVehicle(String isSubjectVehicle) {
		this.isSubjectVehicle = isSubjectVehicle;
	}

	public String getPlateNo() {
		return plateNo;
	}

	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}

	public String getVinNo() {
		return vinNo;
	}

	public void setVinNo(String vinNo) {
		this.vinNo = vinNo;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getEnrolDate() {
		return enrolDate;
	}

	public void setEnrolDate(String enrolDate) {
		this.enrolDate = enrolDate;
	}

	public String getMarkColor() {
		return markColor;
	}

	public void setMarkColor(String markColor) {
		this.markColor = markColor;
	}

	public String getEvalTypeCode() {
		return evalTypeCode;
	}

	public void setEvalTypeCode(String evalTypeCode) {
		this.evalTypeCode = evalTypeCode;
	}

	public String getEngineNo() {
		return engineNo;
	}

	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	public List<LossRiskVO> getRiskList() {
		return riskList;
	}

	public void setRiskList(List<LossRiskVO> riskList) {
		this.riskList = riskList;
	}
}

