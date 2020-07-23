package com.estar.hh.survey.entity.request;

/**
 * Created by Administrator on 2017/12/4 0004.
 */

public class CopyInfoRequest {

    private Data data = new Data();

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public class Data{

        private String reportNo;

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
    }

}
