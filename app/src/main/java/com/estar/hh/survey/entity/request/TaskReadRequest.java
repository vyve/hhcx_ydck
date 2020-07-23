package com.estar.hh.survey.entity.request;

/**
 * Created by Administrator on 2017/12/4 0004.
 */

public class TaskReadRequest {

    private Data data = new Data();

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public class Data{

        private String reportNo;
        private String taskNo;
        private String readingTaskTime;
        private String requestType = "1";//请求类型 1阅读任务 2联系客户

        /**
         * 安全整改
         * add by zhengyg 2018年12月6日16:02:56
         */
        private String srvyfaceCde;

        public String getSrvyfaceCde() {
            return srvyfaceCde;
        }

        public void setSrvyfaceCde(String srvyfaceCde) {
            this.srvyfaceCde = srvyfaceCde;
        }

        public void setReportNo(String reportNo) {
            this.reportNo = reportNo;
        }

        public void setTaskNo(String taskNo) {
            this.taskNo = taskNo;
        }

        public void setReadingTaskTime(String readingTaskTime) {
            this.readingTaskTime = readingTaskTime;
        }

        public String getReportNo() {
            return reportNo;
        }

        public String getTaskNo() {
            return taskNo;
        }

        public String getReadingTaskTime() {
            return readingTaskTime;
        }
    }

}
