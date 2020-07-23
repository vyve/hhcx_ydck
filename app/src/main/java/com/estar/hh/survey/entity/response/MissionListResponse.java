package com.estar.hh.survey.entity.response;


import com.estar.hh.survey.entity.entity.Mission;
import com.estar.hh.survey.entity.entity.User;

import java.util.List;

/**
 * Created by Administrator on 2017/12/4 0004.
 */

public class MissionListResponse extends ResponseMsg {

    private List<Mission> obj;

    public void setObj(List<Mission> obj) {
        this.obj = obj;
    }

    public List<Mission> getObj() {
        return obj;
    }
}
