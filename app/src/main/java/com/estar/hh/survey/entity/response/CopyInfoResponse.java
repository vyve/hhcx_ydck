package com.estar.hh.survey.entity.response;


import com.estar.hh.survey.entity.entity.CopyMainInfoDto;

/**
 * Created by Administrator on 2017/12/4 0004.
 */

public class CopyInfoResponse extends ResponseMsg {

    private CopyMainInfoDto obj;

    public void setObj(CopyMainInfoDto obj) {
        this.obj = obj;
    }

    public CopyMainInfoDto getObj() {
        return obj;
    }

}
