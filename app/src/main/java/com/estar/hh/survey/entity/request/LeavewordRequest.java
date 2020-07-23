package com.estar.hh.survey.entity.request;

/**
 * Created by Administrator on 2017/12/4 0004.
 */

public class LeavewordRequest {

    private Data data = new Data();

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public class Data{

        private String remarkContent;//备注内容
        private String remarkDate;//备注日期
        private String remarkEmp;//备注工号
        private String reportNo;
        private String taskNo;

        public void setRemarkContent(String remarkContent) {
            this.remarkContent = remarkContent;
        }

        public void setRemarkDate(String remarkDate) {
            this.remarkDate = remarkDate;
        }

        public void setRemarkEmp(String remarkEmp) {
            this.remarkEmp = remarkEmp;
        }

        public void setReportNo(String reportNo) {
            this.reportNo = reportNo;
        }

        public void setTaskNo(String taskNo) {
            this.taskNo = taskNo;
        }

        public String getRemarkContent() {
            return remarkContent;
        }

        public String getRemarkDate() {
            return remarkDate;
        }

        public String getRemarkEmp() {
            return remarkEmp;
        }

        public String getReportNo() {
            return reportNo;
        }

        public String getTaskNo() {
            return taskNo;
        }
    }

}
