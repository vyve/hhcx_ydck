package com.estar.hh.survey.entity.request;

/**
 * Created by Administrator on 2017/12/4 0004.
 */

public class VideoCreateRequest {

    private Data data = new Data();

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public class Data{

        private String pmechanism = ""; // 案件处理机构
        private String reportType = ""; // 案件类型
        private String reportNo = ""; // 案件号
        private String taskType = "";// 任务类型
        private String taskNo = ""; // 任务号
        private String userCode = "";// 查勘员代码
        private String userName = ""; // 查勘员名称
        private String dptCode="";//前端人员机构代码
        private String dptName="";//前端人员机构名称
        private String videoMark="";//视频类型 0-初始化值(即没有申请视频)； 1-视频模式； 2-图片模式； 3-后台拒绝； 4-后台无闲置人员； 5-网络中断；6-无网络信号。

        public void setPmechanism(String pmechanism) {
            this.pmechanism = pmechanism;
        }

        public void setReportType(String reportType) {
            this.reportType = reportType;
        }

        public void setReportNo(String reportNo) {
            this.reportNo = reportNo;
        }

        public void setTaskType(String taskType) {
            this.taskType = taskType;
        }

        public void setTaskNo(String taskNo) {
            this.taskNo = taskNo;
        }

        public void setUserCode(String userCode) {
            this.userCode = userCode;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public void setDptCode(String dptCode) {
            this.dptCode = dptCode;
        }

        public void setDptName(String dptName) {
            this.dptName = dptName;
        }

        public void setVideoMark(String videoMark) {
            this.videoMark = videoMark;
        }

        public String getPmechanism() {
            return pmechanism;
        }

        public String getReportType() {
            return reportType;
        }

        public String getReportNo() {
            return reportNo;
        }

        public String getTaskType() {
            return taskType;
        }

        public String getTaskNo() {
            return taskNo;
        }

        public String getUserCode() {
            return userCode;
        }

        public String getUserName() {
            return userName;
        }

        public String getDptCode() {
            return dptCode;
        }

        public String getDptName() {
            return dptName;
        }

        public String getVideoMark() {
            return videoMark;
        }
    }

}
