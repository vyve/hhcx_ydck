package com.estar.hh.survey.view.activity.sketch.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MTaskInfoVO implements Serializable {
	private String reportId="12";// 报案号
	private String damageId="12";// 定损信息ID
	private String taskNo="12";// 任务号
	private String taskAttribute="12";// 任务类型
	private String surveyTaskNo="12";// 查勘定损任务号
	private String taskStatus="12";// 任务状态
	private String carMark="12";// 车牌号
	private String classificationType="12";// 客户类型
	private String reportTime="12";// 报案时间
	private String arriveTime="12";// 派工时间
	private String insuredName="12";// 被保险人
	private List<String> documentUrl= new ArrayList<String>();// 标的车验车照片url
	private String totalCount="12";// 总任务数
	private String answeredNewName="12";//文字描述，比如：待查勘（新报案案件）、查勘暂存、完成处理待查勘发送等。

	private String isAddSurvey="";//taskAttribute=2时（即补勘任务）isAddSurvey=1
	private String addSurveyTimes="";//taskAttribute=2时（即补勘任务）会返回的字段



	public String getIsAddSurvey() {
		return isAddSurvey;
	}
	public void setIsAddSurvey(String isAddSurvey) {
		this.isAddSurvey = isAddSurvey;
	}
	public String getAddSurveyTimes() {
		return addSurveyTimes;
	}
	public void setAddSurveyTimes(String addSurveyTimes) {
		this.addSurveyTimes = addSurveyTimes;
	}
	public String getAnsweredNewName() {
		return answeredNewName;
	}
	public void setAnsweredNewName(String answeredNewName) {
		this.answeredNewName = answeredNewName;
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
	public String getTaskNo() {
		return taskNo;
	}
	public void setTaskNo(String taskNo) {
		this.taskNo = taskNo;
	}
	/**
	 * 1-查勘;3-标的车;4-三者车;5-标的货;6-三者物;8-人伤
	 * @return
	 */
	public String getTaskAttribute() {
		return taskAttribute;
	}
	public void setTaskAttribute(String taskAttribute) {
		this.taskAttribute = taskAttribute;
	}
	public String getSurveyTaskNo() {
		return surveyTaskNo;
	}
	public void setSurveyTaskNo(String surveyTaskNo) {
		this.surveyTaskNo = surveyTaskNo;
	}
	public String getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	public String getCarMark() {
		if(null!=carMark&&!"".equals(carMark))carMark=carMark.replace("-", "");
		return carMark;
	}
	public void setCarMark(String carMark) {
		this.carMark = carMark;
	}
	public String getClassificationType() {
		return classificationType;
	}
	public void setClassificationType(String classificationType) {
		this.classificationType = classificationType;
	}
	public String getReportTime() {
		return reportTime;
	}
	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}
	public String getArriveTime() {
		return arriveTime;
	}
	public void setArriveTime(String arriveTime) {
		this.arriveTime = arriveTime;
	}
	public String getInsuredName() {
		return insuredName;
	}
	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}



	public List<String> getDocumentUrl() {
		return documentUrl;
	}
	public void setDocumentUrl(List<String> documentUrl) {
		this.documentUrl = documentUrl;
	}
	public String getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}

}
