package com.ailide.apartmentsabc.model;

/**
 * Created by Administrator on 2018/3/5 0005.
 */

public class ShareTwoBean {

    /**
     * status : 1
     * data : {"title":"真心话","content":"你还想我吗","url":"http://admin.ileadtek.com/public/admin/qiao/deng/mid/5/whisper_id/19"}
     * msg : 成功
     */

    private int status;
    private DataBean data;
    private String msg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        /**
         * title : 真心话
         * content : 你还想我吗
         * url : http://admin.ileadtek.com/public/admin/qiao/deng/mid/5/whisper_id/19
         */

        private String title;
        private String content;
        private String url;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
