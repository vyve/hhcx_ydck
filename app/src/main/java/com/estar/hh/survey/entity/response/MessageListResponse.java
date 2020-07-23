package com.estar.hh.survey.entity.response;


import com.estar.hh.survey.entity.entity.Mission;
import com.estar.hh.survey.entity.entity.Word;

import java.util.List;

/**
 * Created by Administrator on 2017/12/4 0004.
 */

public class MessageListResponse extends ResponseMsg {

    private List<Word> obj;

    public void setObj(List<Word> obj) {
        this.obj = obj;
    }

    public List<Word> getObj() {
        return obj;
    }
}
