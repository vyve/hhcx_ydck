package com.estar.hh.survey.entity.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/31 0031.
 */

public class CrashLog extends DataSupport implements Serializable {

    private String logName;
    private String logPath;
    private String createTime;

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLogName() {
        return logName;
    }

    public String getLogPath() {
        return logPath;
    }

    public String getCreateTime() {
        return createTime;
    }
}
