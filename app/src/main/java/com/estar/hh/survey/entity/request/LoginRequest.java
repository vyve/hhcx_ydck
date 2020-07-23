package com.estar.hh.survey.entity.request;

/**
 * Created by Administrator on 2017/12/4 0004.
 */

public class LoginRequest {

    private Data data = new Data();

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public class Data{

        private String userName;
        private String passWord;
        private String telNo;
        private String registerId;//极光推送id

        public void setRegisterId(String registerId) {
            this.registerId = registerId;
        }

        public String getRegisterId() {
            return registerId;
        }

        public void setPassWord(String passWord) {
            this.passWord = passWord;
        }

        public String getPassWord() {
            return passWord;
        }

        public void setTelNo(String telNo) {
            this.telNo = telNo;
        }

        public String getTelNo() {
            return telNo;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserName() {
            return userName;
        }
    }

}
