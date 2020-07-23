package com.estar.hh.survey.entity.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:〈主抄单dto〉<br/>
 * 
 * @Provider:广州易星科技有限公司 <br/>
 * @Author: chenhuan@estar-info.com<br/>
 * @Date:2017年10月27日
 */
public class CopyMainInfoDto implements Serializable {
	private SubmitSurvMainInfoDto  submitSurvMainInfoDto = new SubmitSurvMainInfoDto();
	/* 任务详细信息 */
	private List<TaskInfoDetailDto> listTask = new ArrayList<TaskInfoDetailDto>();
	/* 抄单-查勘基本信息 */
	private CopySurveyBaseInfoDto copySurveyBaseInfo = new CopySurveyBaseInfoDto();
	/* 抄单-保单信息列表 */
	private List<CopyPolicyDto> listPolicy = new ArrayList<CopyPolicyDto>();
	/* 抄单-承保险别列表 */
	private List<CopyRiskDto> listRisk = new ArrayList<CopyRiskDto>();
	/* 抄单-历史出险信息 */
	private List<CopyHisDangerDto> listHisDanger = new ArrayList<CopyHisDangerDto>();
	/* 抄单-报案信息 */
	private CopyReportInfoDto copyReportInfo = new CopyReportInfoDto();
	/* 抄单车辆信息 */
	private CopyCarInfoDto copyCarInfo = new CopyCarInfoDto();
	/* 查勘已提交信息 */
	private SurveyAlreadySubmitDto surveyAlreadySubmit = new SurveyAlreadySubmitDto();
	/* 新任务推送-查勘-三者车辆信息 */
	private List<CarLossInfoDto> survyListCarLossInfoDto = new ArrayList<CarLossInfoDto>();
	/* 新任务推送-查勘-物损信息 */
	private List<GoodInfoDto> survyListGoodInfoDto = new ArrayList<GoodInfoDto>();

	public SubmitSurvMainInfoDto getSubmitSurvMainInfoDto() {
		return submitSurvMainInfoDto;
	}

	public void setSubmitSurvMainInfoDto(SubmitSurvMainInfoDto submitSurvMainInfoDto) {
		this.submitSurvMainInfoDto = submitSurvMainInfoDto;
	}

	public void setSurvyListCarLossInfoDto(List<CarLossInfoDto> survyListCarLossInfoDto) {
		this.survyListCarLossInfoDto = survyListCarLossInfoDto;
	}

	public List<CarLossInfoDto> getSurvyListCarLossInfoDto() {
		return survyListCarLossInfoDto;
	}

	public void setSurvyListGoodInfoDto(List<GoodInfoDto> survyListGoodInfoDto) {
		this.survyListGoodInfoDto = survyListGoodInfoDto;
	}

	public List<GoodInfoDto> getSurvyListGoodInfoDto() {
		return survyListGoodInfoDto;
	}

	public CopyReportInfoDto getCopyReportInfo() {
		return copyReportInfo;
	}

	public void setCopyReportInfo(CopyReportInfoDto copyReportInfo) {
		this.copyReportInfo = copyReportInfo;
	}

	public List<TaskInfoDetailDto> getListTask() {
		return listTask;
	}

	public void setListTask(List<TaskInfoDetailDto> listTask) {
		this.listTask = listTask;
	}

	public CopySurveyBaseInfoDto getCopySurveyBaseInfo() {
		return copySurveyBaseInfo;
	}

	public void setCopySurveyBaseInfo(CopySurveyBaseInfoDto copySurveyBaseInfo) {
		this.copySurveyBaseInfo = copySurveyBaseInfo;
	}

	public List<CopyPolicyDto> getListPolicy() {
		return listPolicy;
	}

	public void setListPolicy(List<CopyPolicyDto> listPolicy) {
		this.listPolicy = listPolicy;
	}

	public List<CopyRiskDto> getListRisk() {
		return listRisk;
	}

	public void setListRisk(List<CopyRiskDto> listRisk) {
		this.listRisk = listRisk;
	}

	public List<CopyHisDangerDto> getListHisDanger() {
		return listHisDanger;
	}

	public void setListHisDanger(List<CopyHisDangerDto> listHisDanger) {
		this.listHisDanger = listHisDanger;
	}

	public SurveyAlreadySubmitDto getSurveyAlreadySubmit() {
		return surveyAlreadySubmit;
	}

	public void setSurveyAlreadySubmit(SurveyAlreadySubmitDto surveyAlreadySubmit) {
		this.surveyAlreadySubmit = surveyAlreadySubmit;
	}

	public CopyCarInfoDto getCopyCarInfo() {
		return copyCarInfo;
	}

	public void setCopyCarInfo(CopyCarInfoDto copyCarInfo) {
		this.copyCarInfo = copyCarInfo;
	}

	
	
}
