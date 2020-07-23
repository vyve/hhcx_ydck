package com.estar.hh.survey.entity.request;

import com.estar.hh.survey.entity.entity.SubmitSurvMainInfoDto;

/**
 * Created by Administrator on 2017/12/4 0004.
 */

public class SurvSaveRequest {

    private SubmitSurvMainInfoDto data = new SubmitSurvMainInfoDto();

    public void setData(SubmitSurvMainInfoDto data) {
        this.data = data;
    }

    public SubmitSurvMainInfoDto getData() {
        return data;
    }
}
