package com.example.andriod.daigoutally;

public class ExchangeRateBean {

    /**
     * success : 1
     * result : {"status":"ALREADY","scur":"USD","tcur":"CNY","ratenm":"美元/人民币","rate":"6.727800","update":"2019-03-14 21:39:00"}
     */

    private String success;
    private ResultBean result;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * status : ALREADY
         * scur : USD
         * tcur : CNY
         * ratenm : 美元/人民币
         * rate : 6.727800
         * update : 2019-03-14 21:39:00
         */

        private String status;
        private String scur;
        private String tcur;
        private String ratenm;
        private String rate;
        private String update;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getScur() {
            return scur;
        }

        public void setScur(String scur) {
            this.scur = scur;
        }

        public String getTcur() {
            return tcur;
        }

        public void setTcur(String tcur) {
            this.tcur = tcur;
        }

        public String getRatenm() {
            return ratenm;
        }

        public void setRatenm(String ratenm) {
            this.ratenm = ratenm;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getUpdate() {
            return update;
        }

        public void setUpdate(String update) {
            this.update = update;
        }
    }
}
