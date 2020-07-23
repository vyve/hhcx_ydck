package com.estar.hh.survey.entity.request;

/**
 * Created by Administrator on 2017/12/4 0004.
 */

public class RegistRequest {

    private Data data = new Data();

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public class Data{

        private String userName;
        private String sex;
        private String sign;
        private String fileData;
        private String pwd;
        private String claimPwd;
        private String registerId;
        private String realName;
        private String telNo;//电话号码|工号

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public void setFileData(String fileData) {
            this.fileData = fileData;
        }

        public void setPwd(String pwd) {
            this.pwd = pwd;
        }

        public void setClaimPwd(String claimPwd) {
            this.claimPwd = claimPwd;
        }

        public void setRegisterId(String registerId) {
            this.registerId = registerId;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public void setTelNo(String telNo) {
            this.telNo = telNo;
        }

        public String getUserName() {
            return userName;
        }

        public String getSex() {
            return sex;
        }

        public String getSign() {
            return sign;
        }

        public String getFileData() {
            return fileData;
        }

        public String getPwd() {
            return pwd;
        }

        public String getClaimPwd() {
            return claimPwd;
        }

        public String getRegisterId() {
            return registerId;
        }

        public String getRealName() {
            return realName;
        }

        public String getTelNo() {
            return telNo;
        }
    }

}
