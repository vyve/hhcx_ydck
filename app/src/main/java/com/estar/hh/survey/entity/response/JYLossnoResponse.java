package com.estar.hh.survey.entity.response;


/**
 * Created by Administrator on 2017/12/4 0004.
 */

public class JYLossnoResponse extends ResponseMsg {

    private Data obj = new Data();

    public void setObj(Data obj) {
        this.obj = obj;
    }

    public Data getObj() {
        return obj;
    }

    public class Data{

        private String reportNo;
        private String taskNo;
        private String lossItemNumber;

        public void setReportNo(String reportNo) {
            this.reportNo = reportNo;
        }

        public void setTaskNo(String taskNo) {
            this.taskNo = taskNo;
        }

        public void setLossItemNumber(String lossItemNumber) {
            this.lossItemNumber = lossItemNumber;
        }

        public String getReportNo() {
            return reportNo;
        }

        public String getTaskNo() {
            return taskNo;
        }

        public String getLossItemNumber() {
            return lossItemNumber;
        }
    }

}
