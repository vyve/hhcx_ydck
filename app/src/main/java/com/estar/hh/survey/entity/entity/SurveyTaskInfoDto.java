package com.estar.hh.survey.entity.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/20 0020.
 * 现场查勘信息实体
 */

public class SurveyTaskInfoDto extends DataSupport implements Serializable {

    private String reportNo;//报案号
    private String taskNo;//任务号

    private List<CarLossInfoDto> listCarLossInfoDto = new ArrayList<>();//三者车信息
    private List<GoodInfoDto> listGoodInfoDto = new ArrayList<>();//物损信息
    private List<ManInjureDto> listManInjureDto = new ArrayList<>();//人伤信息
    private List<AccountInfoDto> listAccountInfoDto = new ArrayList<>();//收款账户


}
