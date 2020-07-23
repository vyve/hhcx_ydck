package com.estar.hh.survey.entity.response;



import com.estar.hh.survey.entity.entity.Message;

import java.util.List;

/**
 * Created by Administrator on 2017/12/4 0004.
 */

public class MessageResponse extends ResponseMsg {

    private List<Message> obj;

    public void setObj(List<Message> obj) {
        this.obj = obj;
    }

    public List<Message> getObj() {
        return obj;
    }
}
