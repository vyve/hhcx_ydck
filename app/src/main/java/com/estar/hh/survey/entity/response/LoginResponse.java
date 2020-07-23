package com.estar.hh.survey.entity.response;


import com.estar.hh.survey.entity.entity.User;

/**
 * Created by Administrator on 2017/12/4 0004.
 */

public class LoginResponse extends ResponseMsg {

    private User obj;

    public void setObj(User obj) {
        this.obj = obj;
    }

    public User getObj() {
        return obj;
    }
}
