package com.estar.hh.survey.entity.request;

/**
 * Created by Administrator on 2017/12/4 0004.
 */

public class MessageRequest {

    private Data data = new Data();

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public class Data{
        private String userName;
        private String userCode;
        private String noticeType;//通知公告类型（1：通知，2:公告）
        private String startDate;
        private String endDate;

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public void setUserCode(String userCode) {
            this.userCode = userCode;
        }

        public void setNoticeType(String noticeType) {
            this.noticeType = noticeType;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getUserName() {
            return userName;
        }

        public String getUserCode() {
            return userCode;
        }

        public String getNoticeType() {
            return noticeType;
        }

        public String getStartDate() {
            return startDate;
        }

        public String getEndDate() {
            return endDate;
        }
    }

}
