package com.estar.hh.survey.entity.response;


import com.estar.hh.survey.entity.entity.User;

/**
 * Created by Administrator on 2017/12/4 0004.
 */

public class SaveDataResponse extends ResponseMsg {

    private OBJ obj = new OBJ();

    public void setObj(OBJ obj) {
        this.obj = obj;
    }

    public OBJ getObj() {
        return obj;
    }

    public class OBJ{

        private String headUrl;
        private String realName;
        private String sign;
        private String sex;//0未知 1男 2女

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getHeadUrl() {
            return headUrl;
        }

        public String getRealName() {
            return realName;
        }

        public String getSign() {
            return sign;
        }

        public String getSex() {
            return sex;
        }
    }

}
