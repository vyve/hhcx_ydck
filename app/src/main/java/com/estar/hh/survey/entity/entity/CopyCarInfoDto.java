package com.estar.hh.survey.entity.entity;

import java.io.Serializable;

/**
 * 抄单-保险车辆信息 
 * @author Administrator
 *
 */
public class CopyCarInfoDto implements Serializable{
	private String dangerDricerName = "";// 出险驾驶员姓名
	private String driverNo = "";// 驾驶证号
	private String driverTelNo = "";// 驾驶员电话
	private String driverIdType = "";// 驾驶员证件类型
	private String idNo = "";// 证件号码
	private String insPersonTelNo = "";// 被保险人电话
	private String linkPersonName = "";// 联系人姓名
	private String linkTelNo = "";// 联系人电话
	private String carNo = "";// 标的车牌号
	private String brandModels = "";// 厂牌车型
	private String carTypeCode = "";// 车型编码
	private String carNoType = "";// 号牌种类
	private String vinNo = "";// 车架号
	private String enginNo = "";// 发动机号
	
	//2017-04-21添加
	private String nMonDespRate = "";// 折旧
	private String sYCFstRegYm = ""; // 商业初登时间
	private String jQCFstRegYm = "";// 交强初登时间
	private String nNewVal = "";// 新车购置价

	public String getDangerDricerName() {
		return dangerDricerName;
	}

	public void setDangerDricerName(String dangerDricerName) {
		this.dangerDricerName = dangerDricerName;
	}

	public String getDriverNo() {
		return driverNo;
	}

	public void setDriverNo(String driverNo) {
		this.driverNo = driverNo;
	}

	public String getDriverTelNo() {
		return driverTelNo;
	}

	public void setDriverTelNo(String driverTelNo) {
		this.driverTelNo = driverTelNo;
	}

	public String getDriverIdType() {
		return driverIdType;
	}

	public void setDriverIdType(String driverIdType) {
		this.driverIdType = driverIdType;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getInsPersonTelNo() {
		return insPersonTelNo;
	}

	public void setInsPersonTelNo(String insPersonTelNo) {
		this.insPersonTelNo = insPersonTelNo;
	}

	public String getLinkPersonName() {
		return linkPersonName;
	}

	public void setLinkPersonName(String linkPersonName) {
		this.linkPersonName = linkPersonName;
	}

	public String getLinkTelNo() {
		return linkTelNo;
	}

	public void setLinkTelNo(String linkTelNo) {
		this.linkTelNo = linkTelNo;
	}

	public String getCarNo() {
		return carNo;
	}

	public void setCarNo(String carNo) {
		this.carNo = carNo;
	}

	public String getBrandModels() {
		return brandModels;
	}

	public void setBrandModels(String brandModels) {
		this.brandModels = brandModels;
	}

	public String getCarTypeCode() {
		return carTypeCode;
	}

	public void setCarTypeCode(String carTypeCode) {
		this.carTypeCode = carTypeCode;
	}

	public String getCarNoType() {
		return carNoType;
	}

	public void setCarNoType(String carNoType) {
		this.carNoType = carNoType;
	}

	public String getVinNo() {
		return vinNo;
	}

	public void setVinNo(String vinNo) {
		this.vinNo = vinNo;
	}

	public String getEnginNo() {
		return enginNo;
	}

	public void setEnginNo(String enginNo) {
		this.enginNo = enginNo;
	}

	public String getnMonDespRate() {
		return nMonDespRate;
	}

	public void setnMonDespRate(String nMonDespRate) {
		this.nMonDespRate = nMonDespRate;
	}

	public String getsYCFstRegYm() {
		return sYCFstRegYm;
	}

	public void setsYCFstRegYm(String sYCFstRegYm) {
		this.sYCFstRegYm = sYCFstRegYm;
	}

	public String getjQCFstRegYm() {
		return jQCFstRegYm;
	}

	public void setjQCFstRegYm(String jQCFstRegYm) {
		this.jQCFstRegYm = jQCFstRegYm;
	}

	public String getnNewVal() {
		return nNewVal;
	}

	public void setnNewVal(String nNewVal) {
		this.nNewVal = nNewVal;
	}

	
	
}
