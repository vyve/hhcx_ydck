package com.estar.hh.survey.entity.request;

/**
 * Created by Administrator on 2017/12/4 0004.
 */

public class LogUploadRequest {

    private Data data = new Data();

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public class Data{

        private String logData;
        private String surveyCode;

        public void setLogData(String logData) {
            this.logData = logData;
        }

        public void setSurveyCode(String surveyCode) {
            this.surveyCode = surveyCode;
        }

        public String getLogData() {
            return logData;
        }

        public String getSurveyCode() {
            return surveyCode;
        }
    }

}
