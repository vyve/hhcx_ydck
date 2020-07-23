package com.estar.hh.survey.entity.vo;

/**
 * Created by Administrator on 2017/10/13 0013.
 * 查勘常识vo
 */

public class KnowlegeVO {

    private String title;
    private String content;

    public KnowlegeVO(String title, String content){
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
