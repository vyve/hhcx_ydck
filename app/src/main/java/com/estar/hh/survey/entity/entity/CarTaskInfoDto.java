package com.estar.hh.survey.entity.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/20 0020.
 * 车辆定损实体信息
 */

public class CarTaskInfoDto extends DataSupport implements Serializable {

    private String reportNo;//报案号
    private String taskNo;//任务号

    private CarLossBaseInfoDto carLossBaseInfoDto;//基本信息

}
