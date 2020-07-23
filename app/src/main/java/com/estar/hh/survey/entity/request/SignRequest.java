package com.estar.hh.survey.entity.request;

/**
 * Created by Administrator on 2017/12/4 0004.
 */

public class SignRequest {

    private Data data = new Data();

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public class Data{

        private String dptCde;
        private String dtpNme;
        private String empCde;
        private String offworkAddress;
        private String onworkAddress;
        private String signType;//签到类型:1上班 2下班

        public void setDptCde(String dptCde) {
            this.dptCde = dptCde;
        }

        public void setDtpNme(String dtpNme) {
            this.dtpNme = dtpNme;
        }

        public void setEmpCde(String empCde) {
            this.empCde = empCde;
        }

        public void setOffworkAddress(String offworkAddress) {
            this.offworkAddress = offworkAddress;
        }

        public void setOnworkAddress(String onworkAddress) {
            this.onworkAddress = onworkAddress;
        }

        public void setSignType(String signType) {
            this.signType = signType;
        }

        public String getDptCde() {
            return dptCde;
        }

        public String getDtpNme() {
            return dtpNme;
        }

        public String getEmpCde() {
            return empCde;
        }

        public String getOffworkAddress() {
            return offworkAddress;
        }

        public String getOnworkAddress() {
            return onworkAddress;
        }

        public String getSignType() {
            return signType;
        }
    }

}
