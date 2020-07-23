package com.estar.hh.survey.entity.request;

/**
 * Created by Administrator on 2018/1/16 0016.
 */

public class JYLossnoRequest {

    private Data data = new Data();

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{

        private String reportNo;
        private String taskNo;

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

        public String getReportNo() {
            return reportNo;
        }

        public void setTaskNo(String taskNo) {
            this.taskNo = taskNo;
        }

        public String getTaskNo() {
            return taskNo;
        }
    }

}
