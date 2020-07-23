package com.estar.hh.survey.entity.request;

/**
 * Created by Administrator on 2017/12/4 0004.
 */

public class SaveDataRequest {

    private Data data = new Data();

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public class Data{

        private String fileData;
        private String realName;
        private String telNo;
        private String sign;
        private String sex;//0未知 1男 2女

        public void setFileData(String fileData) {
            this.fileData = fileData;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public void setTelNo(String telNo) {
            this.telNo = telNo;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getFileData() {
            return fileData;
        }

        public String getRealName() {
            return realName;
        }

        public String getTelNo() {
            return telNo;
        }

        public String getSign() {
            return sign;
        }

        public String getSex() {
            return sex;
        }
    }

}
