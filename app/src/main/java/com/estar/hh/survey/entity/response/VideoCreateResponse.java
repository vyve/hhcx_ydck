package com.estar.hh.survey.entity.response;


import com.estar.hh.survey.entity.vo.VideoResult;

/**
 * Created by Administrator on 2017/12/4 0004.
 */

public class VideoCreateResponse extends ResponseMsg {

    private VideoResult obj;

    public void setObj(VideoResult obj) {
        this.obj = obj;
    }

    public VideoResult getObj() {
        return obj;
    }

}
