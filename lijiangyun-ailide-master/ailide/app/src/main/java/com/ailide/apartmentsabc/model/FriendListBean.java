package com.ailide.apartmentsabc.model;

import java.util.List;

/**
 * Created by Administrator on 2018/2/1 0001.
 */

public class FriendListBean {

    /**
     * status : 1
     * data : [{"id":14,"screen_name":"雲","note_num":"98192061","gender":1,"last_login_time":1517476381,"create_time":1517378423,"address":null,"birthday":0,"signature":null,"profile_image_url":"http://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJrtq9n5cWKficNVOedJDCNmM49wPW1RSpN7byhn9pxbB39qds5SCd3sqAryDv1Aqib77OmbAaV55ZA/132","phone":null,"unionid":"obajP0-grSF26vkP4TqGwwCs9oeo","source":1,"is_via":1},{"id":14,"screen_name":"雲","note_num":"98192061","gender":1,"last_login_time":1517476381,"create_time":1517378423,"address":null,"birthday":0,"signature":null,"profile_image_url":"http://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJrtq9n5cWKficNVOedJDCNmM49wPW1RSpN7byhn9pxbB39qds5SCd3sqAryDv1Aqib77OmbAaV55ZA/132","phone":null,"unionid":"obajP0-grSF26vkP4TqGwwCs9oeo","source":1,"is_via":1}]
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
         * id : 14
         * screen_name : 雲
         * note_num : 98192061
         * gender : 1
         * last_login_time : 1517476381
         * create_time : 1517378423
         * address : null
         * birthday : 0
         * signature : null
         * profile_image_url : http://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJrtq9n5cWKficNVOedJDCNmM49wPW1RSpN7byhn9pxbB39qds5SCd3sqAryDv1Aqib77OmbAaV55ZA/132
         * phone : null
         * unionid : obajP0-grSF26vkP4TqGwwCs9oeo
         * source : 1
         * is_via : 1
         */

        private int id;
        private String screen_name;
        private String note_num;
        private long gender;
        private long last_login_time;
        private long create_time;
        private String address;
        private int birthday;
        private String signature;
        private String profile_image_url;
        private String phone;
        private String unionid;
        private int source;
        private int is_via;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getScreen_name() {
            return screen_name;
        }

        public void setScreen_name(String screen_name) {
            this.screen_name = screen_name;
        }

        public String getNote_num() {
            return note_num;
        }

        public void setNote_num(String note_num) {
            this.note_num = note_num;
        }

        public long getGender() {
            return gender;
        }

        public void setGender(long gender) {
            this.gender = gender;
        }

        public long getLast_login_time() {
            return last_login_time;
        }

        public void setLast_login_time(long last_login_time) {
            this.last_login_time = last_login_time;
        }

        public long getCreate_time() {
            return create_time;
        }

        public void setCreate_time(long create_time) {
            this.create_time = create_time;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getBirthday() {
            return birthday;
        }

        public void setBirthday(int birthday) {
            this.birthday = birthday;
        }


        public String getProfile_image_url() {
            return profile_image_url;
        }

        public void setProfile_image_url(String profile_image_url) {
            this.profile_image_url = profile_image_url;
        }

        public String getUnionid() {
            return unionid;
        }

        public void setUnionid(String unionid) {
            this.unionid = unionid;
        }

        public int getSource() {
            return source;
        }

        public void setSource(int source) {
            this.source = source;
        }

        public int getIs_via() {
            return is_via;
        }

        public void setIs_via(int is_via) {
            this.is_via = is_via;
        }
    }
}
