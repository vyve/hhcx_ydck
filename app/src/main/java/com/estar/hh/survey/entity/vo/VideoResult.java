package com.estar.hh.survey.entity.vo;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/25 0025.
 */

public class VideoResult implements Serializable {

    private String meetingId = "";// 视频Id(前后端视频链接需要)
    private String opAble = "";// 0:全忙 1:有可分配的人
    private String opPersion = "";// 工号+姓名


    //界面需展示
    private String reportno;// 报案号
    private String taskNo = "";// 任务号
    private String licenseno;// 车牌号
    private String lossers;// 后端定损员
    private String reporttime;// 报案时间
    private String damageTime = ""; // 出险时间
    private String tasktype;// 任务来源
    private String insNme = "";// 被保险人
    private String dangerAfter = "";// 出险经过

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public void setOpAble(String opAble) {
        this.opAble = opAble;
    }

    public void setOpPersion(String opPersion) {
        this.opPersion = opPersion;
    }

    public void setReportno(String reportno) {
        this.reportno = reportno;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public void setLicenseno(String licenseno) {
        this.licenseno = licenseno;
    }

    public void setLossers(String lossers) {
        this.lossers = lossers;
    }

    public void setReporttime(String reporttime) {
        this.reporttime = reporttime;
    }

    public void setDamageTime(String damageTime) {
        this.damageTime = damageTime;
    }

    public void setTasktype(String tasktype) {
        this.tasktype = tasktype;
    }

    public void setInsNme(String insNme) {
        this.insNme = insNme;
    }

    public void setDangerAfter(String dangerAfter) {
        this.dangerAfter = dangerAfter;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public String getOpAble() {
        return opAble;
    }

    public String getOpPersion() {
        return opPersion;
    }

    public String getReportno() {
        return reportno;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public String getLicenseno() {
        return licenseno;
    }

    public String getLossers() {
        return lossers;
    }

    public String getReporttime() {
        return reporttime;
    }

    public String getDamageTime() {
        return damageTime;
    }

    public String getTasktype() {
        return tasktype;
    }

    public String getInsNme() {
        return insNme;
    }

    public String getDangerAfter() {
        return dangerAfter;
    }

}
