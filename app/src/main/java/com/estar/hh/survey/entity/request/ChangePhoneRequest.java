package com.estar.hh.survey.entity.request;

/**
 * Created by Administrator on 2017/12/4 0004.
 */

public class ChangePhoneRequest {

    private Data data = new Data();

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public class Data{

        private String oldTelNo;
        private String newTelNo;

        /**
         * 安全整改
         * add by zhengyg 2018年12月6日16:02:56
         */
        private String telNo;

        public String getTelNo() {
            return telNo;
        }

        public void setTelNo(String telNo) {
            this.telNo = telNo;
        }

        public void setOldTelNo(String oldTelNo) {
            this.oldTelNo = oldTelNo;
        }

        public void setNewTelNo(String newTelNo) {
            this.newTelNo = newTelNo;
        }

        public String getOldTelNo() {
            return oldTelNo;
        }

        public String getNewTelNo() {
            return newTelNo;
        }
    }

}
