package com.estar.hh.survey.entity.request;

/**
 * Created by Administrator on 2017/12/4 0004.
 */

public class ModifyPassRequest {

    private Data data = new Data();

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public class Data{

        private String newPwd;
        private String oldPwd;
        private String telNo;//电话号码|工号

        public void setNewPwd(String newPwd) {
            this.newPwd = newPwd;
        }

        public void setOldPwd(String oldPwd) {
            this.oldPwd = oldPwd;
        }

        public void setTelNo(String telNo) {
            this.telNo = telNo;
        }

        public String getNewPwd() {
            return newPwd;
        }

        public String getOldPwd() {
            return oldPwd;
        }

        public String getTelNo() {
            return telNo;
        }
    }

}
