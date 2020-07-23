package com.estar.hh.survey.entity.response;

public class VersionVO {


    /**
     * msg : 操作成功
     * success : true
     * obj : {"createTime":1574994079000,"createUser":"admin","updateTime":1574994080000,"systemType":"1","appName":"黄河销售","isValid":"Y","updateUser":"admin","currVersion":"1.0","downloadSource":"https://ww.baodui.com","versionDetail":"1111111","isForce":"Y","id":"1"}
     * attributes : null
     */

    private String msg;
    private boolean success;
    private ObjBean obj;
    private Object attributes;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ObjBean getObj() {
        return obj;
    }

    public void setObj(ObjBean obj) {
        this.obj = obj;
    }

    public Object getAttributes() {
        return attributes;
    }

    public void setAttributes(Object attributes) {
        this.attributes = attributes;
    }

    public static class ObjBean {
        /**
         * createTime : 1574994079000
         * createUser : admin
         * updateTime : 1574994080000
         * systemType : 1
         * appName : 黄河销售
         * isValid : Y
         * updateUser : admin
         * currVersion : 1.0
         * downloadSource : https://ww.baodui.com
         * versionDetail : 1111111
         * isForce : Y
         * id : 1
         */

        private long createTime;
        private String createUser;
        private long updateTime;
        private String systemType;
        private String appName;
        private String isValid;
        private String updateUser;
        private String currVersion;
        private String downloadSource;
        private String versionDetail;
        private String isForce;
        private String id;

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getCreateUser() {
            return createUser;
        }

        public void setCreateUser(String createUser) {
            this.createUser = createUser;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public String getSystemType() {
            return systemType;
        }

        public void setSystemType(String systemType) {
            this.systemType = systemType;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getIsValid() {
            return isValid;
        }

        public void setIsValid(String isValid) {
            this.isValid = isValid;
        }

        public String getUpdateUser() {
            return updateUser;
        }

        public void setUpdateUser(String updateUser) {
            this.updateUser = updateUser;
        }

        public String getCurrVersion() {
            return currVersion;
        }

        public void setCurrVersion(String currVersion) {
            this.currVersion = currVersion;
        }

        public String getDownloadSource() {
            return downloadSource;
        }

        public void setDownloadSource(String downloadSource) {
            this.downloadSource = downloadSource;
        }

        public String getVersionDetail() {
            return versionDetail;
        }

        public void setVersionDetail(String versionDetail) {
            this.versionDetail = versionDetail;
        }

        public String getIsForce() {
            return isForce;
        }

        public void setIsForce(String isForce) {
            this.isForce = isForce;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
