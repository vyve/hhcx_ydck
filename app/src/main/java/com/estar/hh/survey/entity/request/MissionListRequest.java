package com.estar.hh.survey.entity.request;

/**
 * Created by Administrator on 2017/12/4 0004.
 */

public class MissionListRequest {

    private Data data = new Data();

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public class Data{

        private String conditions;//报案号/车牌号/被保人
        private String disStartTime;
        private String disEndTime;
        private String srvyfaceCde;//查勘员工号

        /**
         * 任务类型：1:现场查勘 2:主车车损 3:三者车损4:主车货物 5:三者车货物 6:其它三者财产
         */
        private String taskType;

        /**
         * 查询类型：0-全部 1-查勘任务 2-车损任务 3-物损任务
         */
        private String searchType;

        /**
         * 任务状态：A:新任务B:已阅读C:已联系D:已到达E:暂存 F:已提交平台,G:已提交理赔
         * H:核价通过I:核损通过J:理算通过K:核赔通过 L:支付完成M:已结案T:退回任务
         */
        private String taskStatu;//暂未使用

        /**
         * 查询状态： 1-新任务、 2-待提交、 3-已提交、 4-退回
         */
        private String searchStatus;

        public void setConditions(String conditions) {
            this.conditions = conditions;
        }

        public void setDisStartTime(String disStartTime) {
            this.disStartTime = disStartTime;
        }

        public void setDisEndTime(String disEndTime) {
            this.disEndTime = disEndTime;
        }

        public void setSrvyfaceCde(String srvyfaceCde) {
            this.srvyfaceCde = srvyfaceCde;
        }

        public void setTaskType(String taskType) {
            this.taskType = taskType;
        }

        public void setSearchType(String searchType) {
            this.searchType = searchType;
        }

        public void setTaskStatu(String taskStatu) {
            this.taskStatu = taskStatu;
        }

        public void setSearchStatus(String searchStatus) {
            this.searchStatus = searchStatus;
        }

        public String getConditions() {
            return conditions;
        }

        public String getDisStartTime() {
            return disStartTime;
        }

        public String getDisEndTime() {
            return disEndTime;
        }

        public String getSrvyfaceCde() {
            return srvyfaceCde;
        }

        public String getTaskType() {
            return taskType;
        }

        public String getSearchType() {
            return searchType;
        }

        public String getTaskStatu() {
            return taskStatu;
        }

        public String getSearchStatus() {
            return searchStatus;
        }
    }

}
