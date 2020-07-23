package com.estar.hh.survey.entity.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * @Description:〈查勘已提交信息dto〉<br/>
 * @Provider:广州易星科技有限公司 <br/>
 * @Author: chenhuan@estar-info.com<br/>
 * @Date:2017年10月27日
 */
public class SurveyAlreadySubmitDto implements Serializable {
	private String reportNo;// 报案号
	private String taskNo;// 任务号
	private String srvyEmpCde;// 查勘员代码
	private String depCode;// 查勘员所属机构代码

	/* 查勘-车辆信息 */
	private List<CarLossInfoDto> listCarLossInfoDto = new ArrayList<CarLossInfoDto>();
	/* 查勘-物损信息 */
	private List<GoodInfoDto> listGoodInfoDto = new ArrayList<GoodInfoDto>();
	public String getReportNo() {
		return reportNo;
	}
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}
	public String getTaskNo() {
		return taskNo;
	}
	public void setTaskNo(String taskNo) {
		this.taskNo = taskNo;
	}
	public String getSrvyEmpCde() {
		return srvyEmpCde;
	}
	public void setSrvyEmpCde(String srvyEmpCde) {
		this.srvyEmpCde = srvyEmpCde;
	}
	public String getDepCode() {
		return depCode;
	}
	public void setDepCode(String depCode) {
		this.depCode = depCode;
	}
	public List<CarLossInfoDto> getListCarLossInfoDto() {
		return listCarLossInfoDto;
	}
	public void setListCarLossInfoDto(List<CarLossInfoDto> listCarLossInfoDto) {
		this.listCarLossInfoDto = listCarLossInfoDto;
	}
	public List<GoodInfoDto> getListGoodInfoDto() {
		return listGoodInfoDto;
	}
	public void setListGoodInfoDto(List<GoodInfoDto> listGoodInfoDto) {
		this.listGoodInfoDto = listGoodInfoDto;
	}
	
	
	
}
