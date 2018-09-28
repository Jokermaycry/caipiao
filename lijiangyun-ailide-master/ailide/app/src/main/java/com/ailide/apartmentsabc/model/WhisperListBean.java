package com.ailide.apartmentsabc.model;

import java.util.List;

/**
 * Created by Administrator on 2018/3/5 0005.
 */

public class WhisperListBean {

    /**
     * status : 1
     * data : [{"id":13,"mid":"5","content":"你还想我吗","create_time":1520216165,"source":null},{"id":19,"mid":"5","content":"你还想我吗","create_time":1520216662,"source":null},{"id":20,"mid":"5","content":"你还想我吗","create_time":1520217017,"source":null},{"id":21,"mid":"5","content":"你还想我吗","create_time":1520217127,"source":null}]
     * msg : 成功
     */

    private int status;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 13
         * mid : 5
         * content : 你还想我吗
         * create_time : 1520216165
         * source : null
         */

        private int id;
        private String mid;
        private String content;
        private long create_time;
        private String source;
        private String title;
        private String url;


        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMid() {
            return mid;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public long getCreate_time() {
            return create_time;
        }

        public void setCreate_time(long create_time) {
            this.create_time = create_time;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }
    }
}
