package com.estar.hh.survey.entity.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/4 0004.
 * 任务项
 */

public class Mission extends DataSupport implements Serializable {

    private String srvyfactNme;//查勘员姓名
    private String dispatchTm;//派工时间
    private String rptNo;//报案号
    private String taskId;//任务号
    private String vhl;//车牌号
    private String rptTm;//报案时间
    /**
     * 任务类型：
     * 0、现场查勘；
     * 1、主车车损；
     * 2、三者车损；
     * 3、主车货物；
     * 4、三者车货物；
     * 5、其它三者财产。
     */
    private String taskType;
    private String longTime;

    /**
     * 任务状态
     * A:新任务
     * B:已阅读
     * C:已联系
     * D:已到达
     * E:暂存
     * F:已提交平台
     * G:已提交理赔
     * H:核价通过
     * I:核损通过
     * J:理算通过
     * K:核赔通过
     * L:支付完成
     * M:已结案
     * T:退回任务
     */
    private String taskStatus;

    private String returnURL;//定损数据返回URL，如果是推送车损时必传
    private String refreshURL;//理赔页面刷新URL，如果是推送车损时必传

    private String typeName;//类型名称

    public void setReturnURL(String returnURL) {
        this.returnURL = returnURL;
    }

    public void setRefreshURL(String refreshURL) {
        this.refreshURL = refreshURL;
    }

    public String getReturnURL() {
        return returnURL;
    }

    public String getRefreshURL() {
        return refreshURL;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setLongTime(String longTime) {
        this.longTime = longTime;
    }

    public String getLongTime() {
        return longTime;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setSrvyfactNme(String srvyfactNme) {
        this.srvyfactNme = srvyfactNme;
    }

    public void setDispatchTm(String dispatchTm) {
        this.dispatchTm = dispatchTm;
    }

    public void setRptNo(String rptNo) {
        this.rptNo = rptNo;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public void setVhl(String vhl) {
        this.vhl = vhl;
    }

    public void setRptTm(String rptTm) {
        this.rptTm = rptTm;
    }

    public String getSrvyfactNme() {
        return srvyfactNme;
    }

    public String getDispatchTm() {
        return dispatchTm;
    }

    public String getRptNo() {
        return rptNo;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getVhl() {
        return vhl;
    }

    public String getRptTm() {
        return rptTm;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

}
