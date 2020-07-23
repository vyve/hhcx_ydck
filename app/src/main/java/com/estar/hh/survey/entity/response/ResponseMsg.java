package com.estar.hh.survey.entity.response;

/**
 * Created by Administrator on 2017/8/10.
 * 响应报文公共
 */

public class ResponseMsg {

    private String attributes;
    private String msg;
    private boolean success;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean getSuccess(){
        return success;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public String getAttributes() {
        return attributes;
    }

}
