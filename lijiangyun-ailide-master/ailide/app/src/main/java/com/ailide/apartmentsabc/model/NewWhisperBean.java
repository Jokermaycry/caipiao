package com.ailide.apartmentsabc.model;

import java.util.List;

/**
 * Created by Administrator on 2018/3/4 0004.
 */

public class NewWhisperBean {

    /**
     * status : 1
     * data : [{"id":2,"tag_name":"真心话","sort":1,"data":[{"id":1,"content":"你还想我吗","tag_id":"2","create_time":1516372002,"sort":1},{"id":2,"content":"你还记得那个夜晚吗","tag_id":"2","create_time":1516372022,"sort":2},{"id":6,"content":"测试1","tag_id":"2","create_time":1519366944,"sort":4}]},{"id":3,"tag_name":"情感","sort":2,"data":[{"id":3,"content":"那个寂寞的夜晚","tag_id":"3","create_time":1516372054,"sort":3}]},{"id":4,"tag_name":"娱乐","sort":4,"data":[{"id":4,"content":"测试112222","tag_id":"4","create_time":1516372260,"sort":1}]},{"id":5,"tag_name":"测试2","sort":6,"data":[]}]
     * msg : 成功
     */

    private int status;
    private String msg;
    private List<DataBeanX> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBeanX> getData() {
        return data;
    }

    public void setData(List<DataBeanX> data) {
        this.data = data;
    }

    public static class DataBeanX {
        /**
         * id : 2
         * tag_name : 真心话
         * sort : 1
         * data : [{"id":1,"content":"你还想我吗","tag_id":"2","create_time":1516372002,"sort":1},{"id":2,"content":"你还记得那个夜晚吗","tag_id":"2","create_time":1516372022,"sort":2},{"id":6,"content":"测试1","tag_id":"2","create_time":1519366944,"sort":4}]
         */

        private int id;
        private String tag_name;
        private int sort;
        private List<DataBean> data;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTag_name() {
            return tag_name;
        }

        public void setTag_name(String tag_name) {
            this.tag_name = tag_name;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * id : 1
             * content : 你还想我吗
             * tag_id : 2
             * create_time : 1516372002
             * sort : 1
             */

            private int id;
            private String content;
            private String tag_id;
            private int create_time;
            private int sort;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getTag_id() {
                return tag_id;
            }

            public void setTag_id(String tag_id) {
                this.tag_id = tag_id;
            }

            public int getCreate_time() {
                return create_time;
            }

            public void setCreate_time(int create_time) {
                this.create_time = create_time;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }
        }
    }
}
