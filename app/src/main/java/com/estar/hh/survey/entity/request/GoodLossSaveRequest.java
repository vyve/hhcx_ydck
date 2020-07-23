package com.estar.hh.survey.entity.request;


import com.estar.hh.survey.entity.entity.SubmitGoodLossMainInfoDto;

/**
 * Created by Administrator on 2017/12/4 0004.
 */

public class GoodLossSaveRequest {

    private SubmitGoodLossMainInfoDto data = new SubmitGoodLossMainInfoDto();

    public void setData(SubmitGoodLossMainInfoDto data) {
        this.data = data;
    }

    public SubmitGoodLossMainInfoDto getData() {
        return data;
    }
}
