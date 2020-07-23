package com.estar.hh.survey.entity.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/26 0026.
 */

public class Message implements Serializable {

    private String noticeTitle;
    private String noticeContent;
    private String noticeType;
    private String readTerm;
    private String createTm;

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public void setReadTerm(String readTerm) {
        this.readTerm = readTerm;
    }

    public void setCreateTm(String createTm) {
        this.createTm = createTm;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public String getReadTerm() {
        return readTerm;
    }

    public String getCreateTm() {
        return createTm;
    }
}
