package com.estar.hh.survey.entity.entity;

/**
 * Created by Administrator on 2017/10/10 0010.
 */

public class KeyValue {

    private String key;
    private String value;

    private String tag;//备注属性

    public void setTag(String tag) {
        this.tag = tag;
    }

    public KeyValue(String key, String value){
        this.key = key;
        this.value = value;
    }

    public String getTag() {
        return tag;
    }

    public KeyValue(){

    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
