package com.ailide.apartmentsabc.model;

import java.util.List;

/**
 * Created by Administrator on 2018/1/25 0025.
 */

public class ComonListBean {

    /**
     * status : 1
     * data : [{"id":1,"question":"你是谁111？","content":"<p>你即将揭开了几款零距离<\/p>","create_time":1514879945,"is_show":1},{"id":2,"question":"爱立熊是什么？","content":"<p>是一款高端产品<br/><\/p>","create_time":1516849026,"is_show":1}]
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
         * id : 1
         * question : 你是谁111？
         * content : <p>你即将揭开了几款零距离</p>
         * create_time : 1514879945
         * is_show : 1
         */

        private int id;
        private String question;
        private String content;
        private int create_time;
        private int is_show;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getCreate_time() {
            return create_time;
        }

        public void setCreate_time(int create_time) {
            this.create_time = create_time;
        }

        public int getIs_show() {
            return is_show;
        }

        public void setIs_show(int is_show) {
            this.is_show = is_show;
        }
    }
}
