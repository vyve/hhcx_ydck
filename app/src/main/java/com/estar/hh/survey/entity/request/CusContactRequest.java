package com.estar.hh.survey.entity.request;

/**
 * Created by Administrator on 2017/12/4 0004.
 */

public class CusContactRequest {

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
        private String callPhoneTime;//拨号时间
        private String talkTime;//拨号时长
        private String arrivedTime;//预计到达时间
        private String requestType = "2";//请求类型 1阅读任务 2联系客户
        private String tFromLinkToArri;//预计到达时长

        /**
         * 安全整改
         * add by zhengyg 2018年12月6日16:02:56
         */
        private String srvyfaceCde;//查勘员代码

        public String getSrvyfaceCde() {
            return srvyfaceCde;
        }

        public void setSrvyfaceCde(String srvyfaceCde) {
            this.srvyfaceCde = srvyfaceCde;
        }

        public void settFromLinkToArri(String tFromLinkToArri) {
            this.tFromLinkToArri = tFromLinkToArri;
        }

        public String gettFromLinkToArri() {
            return tFromLinkToArri;
        }

        public void setArrivedTime(String arrivedTime) {
            this.arrivedTime = arrivedTime;
        }

        public String getArrivedTime() {
            return arrivedTime;
        }

        public void setRequestType(String requestType) {
            this.requestType = requestType;
        }

        public String getRequestType() {
            return requestType;
        }

        public void setReportNo(String reportNo) {
            this.reportNo = reportNo;
        }

        public void setTaskNo(String taskNo) {
            this.taskNo = taskNo;
        }

        public void setCallPhoneTime(String callPhoneTime) {
            this.callPhoneTime = callPhoneTime;
        }

        public void setTalkTime(String talkTime) {
            this.talkTime = talkTime;
        }

        public String getReportNo() {
            return reportNo;
        }

        public String getTaskNo() {
            return taskNo;
        }

        public String getCallPhoneTime() {
            return callPhoneTime;
        }

        public String getTalkTime() {
            return talkTime;
        }
    }

}
