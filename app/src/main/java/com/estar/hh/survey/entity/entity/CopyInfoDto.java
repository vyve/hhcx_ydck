package com.estar.hh.survey.entity.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/12/5 0005.
 * 抄单信息
 */

public class CopyInfoDto implements Serializable {

    private Object copyReportInfo;//报案信息
    private List<Object> listHisDanger;//历史赔案
    private Object copySurveyBaseInfo;//查勘基本信息（暂未用）
    private Object copyCarInfo;//抄单车辆信息（暂未用）
    private List<Object> listPolicy;//保单信息
    private List<Object> listRisk;//险别信息
    private List<Object> listTask;//

}
