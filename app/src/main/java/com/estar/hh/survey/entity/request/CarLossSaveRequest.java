package com.estar.hh.survey.entity.request;


import com.estar.hh.survey.entity.entity.SubmitCarLossMainInfoDto;

/**
 * Created by Administrator on 2017/12/4 0004.
 */

public class CarLossSaveRequest {

    private SubmitCarLossMainInfoDto data = new SubmitCarLossMainInfoDto();

    public void setData(SubmitCarLossMainInfoDto data) {
        this.data = data;
    }

    public SubmitCarLossMainInfoDto getData() {
        return data;
    }
}
