package com.estar.hh.survey.entity.request;

/**
 * Created by Administrator on 2017/12/4 0004.
 */

public class MessageListRequest {

    private Data data = new Data();

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public class Data{

        private String remarkEmp;
        private String reportNo;
        private String taskNo;

        public void setTaskNo(String taskNo) {
            this.taskNo = taskNo;
        }

        public String getTaskNo() {
            return taskNo;
        }

        public void setReportNo(String reportNo) {
            this.reportNo = reportNo;
        }

        public String getReportNo() {
            return reportNo;
        }

        public void setRemarkEmp(String remarkEmp) {
            this.remarkEmp = remarkEmp;
        }

        public String getRemarkEmp() {
            return remarkEmp;
        }
    }

}
