package com.estar.hh.survey.entity.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/10/12 0012.
 * 留言信息
 */

public class Word extends DataSupport implements Serializable {

    private String time;
    private String workNo;
    private String content;

    private String reportNo;
    private String taskNo;
    private String remarkContent;//备注内容
    private String remarkDate;//备注时间
    private String remarkEmp;//备注工号

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public void setRemarkContent(String remarkContent) {
        this.remarkContent = remarkContent;
    }

    public void setRemarkDate(String remarkDate) {
        this.remarkDate = remarkDate;
    }

    public void setRemarkEmp(String remarkEmp) {
        this.remarkEmp = remarkEmp;
    }

    public String getReportNo() {
        return reportNo;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public String getRemarkContent() {
        return remarkContent;
    }

    public String getRemarkDate() {
        return remarkDate;
    }

    public String getRemarkEmp() {
        return remarkEmp;
    }

    public String getTime() {
        return time;
    }

    public String getWorkNo() {
        return workNo;
    }

    public String getContent() {
        return content;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setWorkNo(String workNo) {
        this.workNo = workNo;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
