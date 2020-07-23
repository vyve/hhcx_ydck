package com.estar.hh.survey.entity.response;


import com.estar.hh.survey.entity.entity.CaseSearchDto;

import java.util.List;

/**
 * Created by Administrator on 2017/12/4 0004.
 */

public class MissionFollowListResponse extends ResponseMsg {

    private List<CaseSearchDto> obj;

    public void setObj(List<CaseSearchDto> obj) {
        this.obj = obj;
    }

    public List<CaseSearchDto> getObj() {
        return obj;
    }
}
