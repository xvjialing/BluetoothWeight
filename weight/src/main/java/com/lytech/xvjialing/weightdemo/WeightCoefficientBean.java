package com.lytech.xvjialing.weightdemo;

/**
 * Created by xjl on 17-4-17.
 */

public class WeightCoefficientBean {

    /**
     * lp : true
     * data : {"msg":"请求成功","datalist":{"id":"1","mac":"b827eb948836","coefficient":"10591.86"}}
     */

    private boolean lp;
    private DataBean data;

    public boolean isLp() {
        return lp;
    }

    public void setLp(boolean lp) {
        this.lp = lp;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * msg : 请求成功
         * datalist : {"id":"1","mac":"b827eb948836","coefficient":"10591.86"}
         */

        private String msg;
        private DatalistBean datalist;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public DatalistBean getDatalist() {
            return datalist;
        }

        public void setDatalist(DatalistBean datalist) {
            this.datalist = datalist;
        }

        public static class DatalistBean {
            /**
             * id : 1
             * mac : b827eb948836
             * coefficient : 10591.86
             */

            private String id;
            private String mac;
            private String coefficient;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getMac() {
                return mac;
            }

            public void setMac(String mac) {
                this.mac = mac;
            }

            public String getCoefficient() {
                return coefficient;
            }

            public void setCoefficient(String coefficient) {
                this.coefficient = coefficient;
            }
        }
    }
}
