package com.estar.hh.survey.entity.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/20 0020.
 * 案件备注实体信息
 */

public class CaseRemarkDto extends DataSupport implements Serializable {

    private String reportNo;//报案号
    private String remarkEmp;//备注工号
    private String remarkDate;//备注时间
    private String remarkContent;//备注内容

    public String getReportNo() {
        return reportNo;
    }

    public String getRemarkEmp() {
        return remarkEmp;
    }

    public String getRemarkDate() {
        return remarkDate;
    }

    public String getRemarkContent() {
        return remarkContent;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public void setRemarkEmp(String remarkEmp) {
        this.remarkEmp = remarkEmp;
    }

    public void setRemarkDate(String remarkDate) {
        this.remarkDate = remarkDate;
    }

    public void setRemarkContent(String remarkContent) {
        this.remarkContent = remarkContent;
    }
}
