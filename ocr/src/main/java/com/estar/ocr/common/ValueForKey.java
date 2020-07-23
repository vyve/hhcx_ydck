package com.estar.ocr.common;

import java.io.Serializable;

/**
 * Created by xueliang on 2017/3/9.
 */

public class ValueForKey implements Serializable{
    private String key;
    private String value;

    public ValueForKey(){}
    public ValueForKey(String key,String value){
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
