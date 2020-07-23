package com.estar.hh.survey.view.activity.sketch.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *现场查勘提交VO
 */
public class SubSurveyVO implements Serializable {

	private String taskDept = "";//操作机构
	private String[] newSurveyTask=null;//可拆分任务；3:标的定损 4:三者车定损 5:标的货定损 6:三者物定损 8:人伤定损

	private String reportId = "";//报案号
	private String damageId = "";//定损信息ID
	private String dealFlag = "";//处理标记
	private String carMark = "";//标的车牌
	private String carSubmit = "";//验车标记

	private String code17= "";//标的车驾号
	private String remoteSurveyType= "";//前端信息记录


	private String reportAccidentType= "";//事故类型
	private String damagePlace= "";//出险地点
	private String dutyCoefficient= "100%";//商业险责任系数

	private String carNoPayRate= "0%";//车损绝对免赔率

	private String thirdNoPayRate= "";//商业险三者绝对免赔率
	private String carPersonDutyCoefficient= "";//车上人员责任险绝对免赔率
	private String addRate = "";//商业险加扣免赔率
	private String reportDriver= "";//报案驾驶员
	private String accidentDriverLicense= "";//驾驶证号
	private String accidentDriver= "";//出险驾驶员姓名

	private String accidentDriverGender= "";//出险驾驶员性别
	private String insuredTelephone= "";//联系方式
	private String ifAssumpsitDis= "是";//肇事司机是否指定驾驶员

	private String surveyOpinion= "";//勘验结论
	private String surveyRemark= "";//查勘说明
	private String oneBagSend= "";//一袋式送出
	private String licenseNo= "0";//驾驶证号/出险驾驶员档案编号
	private String surveyAccidentType= "";//事故类型
	private String remoteSurveyYype= "";//前端信息记录
	private String isSurveyPort= "0";//免现场案件

	private String accidentDealType= "9";//事故处理方式
	private String checkPlace= "";//查勘类型
	private String companyCode= "";//案件机构
	private String accidentPlaceDef= "";//上海自定义出险地点
	private String isAddSurvey= "";//补勘信息返回字段
	private String level2CompanyCode="";//案件二级机构
	private String level2CommissionCompany="";//委托二级机构
	private String hasAccidentLetter="";//是否有交通事故责任书
	private String isRoadAccident="";//是否属于道路交通事故
	private String accidentLetterNo="";//交通事故责任书编号
	private String dealType="";//事故处理方式

	public String getHasAccidentLetter() {
		return hasAccidentLetter;
	}
	public void setHasAccidentLetter(String hasAccidentLetter) {
		this.hasAccidentLetter = hasAccidentLetter;
	}
	public String getIsRoadAccident() {
		return isRoadAccident;
	}
	public void setIsRoadAccident(String isRoadAccident) {
		this.isRoadAccident = isRoadAccident;
	}
	public String getAccidentLetterNo() {
		return accidentLetterNo;
	}
	public void setAccidentLetterNo(String accidentLetterNo) {
		this.accidentLetterNo = accidentLetterNo;
	}
	public String getDealType() {
		return dealType;
	}
	public void setDealType(String dealType) {
		this.dealType = dealType;
	}
	/**
	 *
	 * 保存草图的信息V
	 */
	private List<SketchVO> sketchVOList = new ArrayList<SketchVO>();
	private String ifDrivie="";//准驾车型
	private int sketchbg;
	private String insuredDuty="";//属于保险责任


	public String getInsuredDuty() {
		return insuredDuty;
	}
	public void setInsuredDuty(String insuredDuty) {
		this.insuredDuty = insuredDuty;
	}
	public String getLevel2CompanyCode() {
		return level2CompanyCode;
	}
	public void setLevel2CompanyCode(String level2CompanyCode) {
		this.level2CompanyCode = level2CompanyCode;
	}
	public String getLevel2CommissionCompany() {
		return level2CommissionCompany;
	}
	public void setLevel2CommissionCompany(String level2CommissionCompany) {
		this.level2CommissionCompany = level2CommissionCompany;
	}



	public int getSketchbg() {
		return sketchbg;
	}
	public void setSketchbg(int sketchbg) {
		this.sketchbg = sketchbg;
	}
	public String getIfDrivie() {
		return ifDrivie;
	}
	public void setIfDrivie(String ifDrivie) {
		this.ifDrivie = ifDrivie;
	}
	public List<SketchVO> getSketchVOList() {
		return sketchVOList;
	}
	public void setSketchVOList(List<SketchVO> sketchVOList) {
		this.sketchVOList = sketchVOList;
	}
	public String getIsAddSurvey() {
		return isAddSurvey;
	}
	public void setIsAddSurvey(String isAddSurvey) {
		this.isAddSurvey = isAddSurvey;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getAccidentPlaceDef() {
		return accidentPlaceDef;
	}
	public void setAccidentPlaceDef(String accidentPlaceDef) {
		this.accidentPlaceDef = accidentPlaceDef;
	}

	/**核损查勘意见**/
	private String estimateSurveyOpinion = "";
	/**质检意见**/
	private String qualityCheckSurveyOpinion = "";
	/**退回案件处理标识   =Y，则是退回案件处理 **/
	private String backCaseHandle = "";
	/**补勘说明**/
	private String addSurveyRemark = "";
	/**退回 意见下拉值**/
	private String backSurveyOpinion = "";
	/**退回 暂存时，意见下拉是否可以选择  Y:可选择**/
	private String chooseAnswer = "";


	public String getChooseAnswer() {
		return chooseAnswer;
	}
	public void setChooseAnswer(String chooseAnswer) {
		this.chooseAnswer = chooseAnswer;
	}
	public String getBackSurveyOpinion() {
		return backSurveyOpinion;
	}
	public void setBackSurveyOpinion(String backSurveyOpinion) {
		this.backSurveyOpinion = backSurveyOpinion;
	}
	public String getAddSurveyRemark() {
		return addSurveyRemark;
	}
	public void setAddSurveyRemark(String addSurveyRemark) {
		this.addSurveyRemark = addSurveyRemark;
	}
	public String getBackCaseHandle() {
		return backCaseHandle;
	}
	public void setBackCaseHandle(String backCaseHandle) {
		this.backCaseHandle = backCaseHandle;
	}
	public String getEstimateSurveyOpinion() {
		return estimateSurveyOpinion;
	}
	public void setEstimateSurveyOpinion(String estimateSurveyOpinion) {
		this.estimateSurveyOpinion = estimateSurveyOpinion;
	}
	public String getQualityCheckSurveyOpinion() {
		return qualityCheckSurveyOpinion;
	}
	public void setQualityCheckSurveyOpinion(String qualityCheckSurveyOpinion) {
		this.qualityCheckSurveyOpinion = qualityCheckSurveyOpinion;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public String getDamageId() {
		return damageId;
	}
	public void setDamageId(String damageId) {
		this.damageId = damageId;
	}
	public String getDealFlag() {
		return dealFlag;
	}
	public void setDealFlag(String dealFlag) {
		this.dealFlag = dealFlag;
	}
	public String getCarMark() {
		if(null!=carMark&&!"".equals(carMark))carMark=carMark.replace("-", "");
		return carMark;
	}
	public void setCarMark(String carMark) {
		this.carMark = carMark;
	}
	public String getCode17() {
		return code17;
	}
	public void setCode17(String code17) {
		this.code17 = code17;
	}
	public String getReportAccidentType() {
		return reportAccidentType;
	}
	public void setReportAccidentType(String reportAccidentType) {
		this.reportAccidentType = reportAccidentType;
	}
	public String getDamagePlace() {
		return damagePlace;
	}
	public void setDamagePlace(String damagePlace) {
		this.damagePlace = damagePlace;
	}
	public String getDutyCoefficient() {
		return dutyCoefficient;
	}
	public void setDutyCoefficient(String dutyCoefficient) {
		this.dutyCoefficient = dutyCoefficient;
	}
	public String getCarNoPayRate() {
		return carNoPayRate;
	}
	public void setCarNoPayRate(String carNoPayRate) {
		this.carNoPayRate = carNoPayRate;
	}
	public String getThirdNoPayRate() {
		return thirdNoPayRate;
	}
	public void setThirdNoPayRate(String thirdNoPayRate) {
		this.thirdNoPayRate = thirdNoPayRate;
	}
	public String getCarPersonDutyCoefficient() {
		return carPersonDutyCoefficient;
	}
	public void setCarPersonDutyCoefficient(String carPersonDutyCoefficient) {
		this.carPersonDutyCoefficient = carPersonDutyCoefficient;
	}
	public String getAddRate() {
		return addRate;
	}
	public void setAddRate(String addRate) {
		this.addRate = addRate;
	}
	public String getAccidentDriver() {
		return accidentDriver;
	}
	public void setAccidentDriver(String accidentDriver) {
		this.accidentDriver = accidentDriver;
	}
	public String getAccidentDriverGender() {
		return accidentDriverGender;
	}
	public void setAccidentDriverGender(String accidentDriverGender) {
		this.accidentDriverGender = accidentDriverGender;
	}
	public String getInsuredTelephone() {
		return insuredTelephone;
	}
	public void setInsuredTelephone(String insuredTelephone) {
		this.insuredTelephone = insuredTelephone;
	}
	public String getIfAssumpsitDis() {
		return ifAssumpsitDis;
	}
	public void setIfAssumpsitDis(String ifAssumpsitDis) {
		this.ifAssumpsitDis = ifAssumpsitDis;
	}
	public String getSurveyOpinion() {
		return surveyOpinion;
	}
	public void setSurveyOpinion(String surveyOpinion) {
		this.surveyOpinion = surveyOpinion;
	}
	public String getSurveyRemark() {
		return surveyRemark;
	}
	public void setSurveyRemark(String surveyRemark) {
		this.surveyRemark = surveyRemark;
	}
	public String getOneBagSend() {
		return oneBagSend;
	}
	public void setOneBagSend(String oneBagSend) {
		this.oneBagSend = oneBagSend;
	}
	public String getLicenseNo() {
		return licenseNo;
	}
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	public String getSurveyAccidentType() {
		return surveyAccidentType;
	}
	public void setSurveyAccidentType(String surveyAccidentType) {
		this.surveyAccidentType = surveyAccidentType;
	}
	public String getRemoteSurveyYype() {
		return remoteSurveyYype;
	}
	public void setRemoteSurveyYype(String remoteSurveyYype) {
		this.remoteSurveyYype = remoteSurveyYype;
	}
	public String getIsSurveyPort() {
		return isSurveyPort;
	}
	public void setIsSurveyPort(String isSurveyPort) {
		this.isSurveyPort = isSurveyPort;
	}
	public String getAccidentDealType() {
		return accidentDealType;
	}
	public void setAccidentDealType(String accidentDealType) {
		this.accidentDealType = accidentDealType;
	}
	public String getCheckPlace() {
		return checkPlace;
	}
	public void setCheckPlace(String checkPlace) {
		this.checkPlace = checkPlace;
	}
	public String getTaskDept() {
		return taskDept;
	}
	public void setTaskDept(String taskDept) {
		this.taskDept = taskDept;
	}
	public String[] getNewSurveyTask() {
		return newSurveyTask;
	}
	public void setNewSurveyTask(String[] newSurveyTask) {
		this.newSurveyTask = newSurveyTask;
	}
	public String getRemoteSurveyType() {
		return remoteSurveyType;
	}
	public void setRemoteSurveyType(String remoteSurveyType) {
		this.remoteSurveyType = remoteSurveyType;
	}
	public String getCarSubmit() {
		return carSubmit;
	}
	public void setCarSubmit(String carSubmit) {
		this.carSubmit = carSubmit;
	}
	public String getReportDriver() {
		return reportDriver;
	}
	public void setReportDriver(String reportDriver) {
		this.reportDriver = reportDriver;
	}
	public String getAccidentDriverLicense() {
		return accidentDriverLicense;
	}
	public void setAccidentDriverLicense(String accidentDriverLicense) {
		this.accidentDriverLicense = accidentDriverLicense;
	}


}

