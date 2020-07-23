package com.estar.hh.survey.entity.request;

/**
 * Created by Administrator on 2017/12/4 0004.
 */

public class MissionFollowListRequest {

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
    }

}
