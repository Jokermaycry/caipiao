package com.ailide.apartmentsabc.model;

import java.util.List;

/**
 * Created by Administrator on 2018/3/7 0007.
 */

public class ReplyWhisperBean {

    /**
     * status : 1
     * data : [{"id":1,"mid":5,"friend_name":"雲","content":"<p>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;我对TA的悄悄话 &nbsp; &nbsp; 哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈<img src=\"/public/upload/20180307/b7a3b9064e1272ce32e8118f00235b8d.png\" title=\"2018-03-07 16:22:06:797.png\" alt=\"2018-03-07 16:22:06:797.png\"/> &nbsp; &nbsp; &nbsp; &nbsp;<\/p>","wid":4,"create_time":1520419065,"source":1,"is_hui":1,"openid":"obAn20ekX2-PVkR6K0N_IuXnHwOE"}]
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
         * mid : 5
         * friend_name : 雲
         * content : <p>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;我对TA的悄悄话 &nbsp; &nbsp; 哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈哈<img src="/public/upload/20180307/b7a3b9064e1272ce32e8118f00235b8d.png" title="2018-03-07 16:22:06:797.png" alt="2018-03-07 16:22:06:797.png"/> &nbsp; &nbsp; &nbsp; &nbsp;</p>
         * wid : 4
         * create_time : 1520419065
         * source : 1
         * is_hui : 1
         * openid : obAn20ekX2-PVkR6K0N_IuXnHwOE
         */

        private int id;
        private int mid;
        private String friend_name;
        private String content;
        private int wid;
        private long create_time;
        private int source;
        private int is_hui;
        private String openid;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getMid() {
            return mid;
        }

        public void setMid(int mid) {
            this.mid = mid;
        }

        public String getFriend_name() {
            return friend_name;
        }

        public void setFriend_name(String friend_name) {
            this.friend_name = friend_name;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getWid() {
            return wid;
        }

        public void setWid(int wid) {
            this.wid = wid;
        }

        public long getCreate_time() {
            return create_time;
        }

        public void setCreate_time(long create_time) {
            this.create_time = create_time;
        }

        public int getSource() {
            return source;
        }

        public void setSource(int source) {
            this.source = source;
        }

        public int getIs_hui() {
            return is_hui;
        }

        public void setIs_hui(int is_hui) {
            this.is_hui = is_hui;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }
    }
}
