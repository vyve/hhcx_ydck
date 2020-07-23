package com.estar.hh.survey.entity.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/20 0020.
 * 财产定损实体信息
 */

public class GoodTaskInfoDto extends DataSupport implements Serializable {

    private String reportNo;
    private String taskNo;

    private List<GoodLossInfoDto> listGoodLossInfoDto = new ArrayList<>();//物损明细列表

}
