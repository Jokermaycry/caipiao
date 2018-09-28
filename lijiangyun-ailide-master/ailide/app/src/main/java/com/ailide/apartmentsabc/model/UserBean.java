package com.ailide.apartmentsabc.model;

/**
 * Created by Administrator on 2018/1/24 0024.
 */

public class UserBean {

    /**
     * status : 1
     * data : {"id":3,"screen_name":"凌風夜弦","note_num":"809923","gender":1,"last_login_time":1516761938,"create_time":1516697559,"address":null,"birthday":0,"signature":null,"profile_image_url":"http://q.qlogo.cn/qqapp/1106615109/CFC105BF02136C1B823F9717A0A7AE58/100","phone":null,"unionid":"CFC105BF02136C1B823F9717A0A7AE58","source":2}
     * msg : 成功
     */

    private String status;
    private DataBean data;
    private String msg;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
         * id : 3
         * screen_name : 凌風夜弦
         * note_num : 809923
         * gender : 1
         * last_login_time : 1516761938
         * create_time : 1516697559
         * address : null
         * birthday : 0
         * signature : null
         * profile_image_url : http://q.qlogo.cn/qqapp/1106615109/CFC105BF02136C1B823F9717A0A7AE58/100
         * phone : null
         * unionid : CFC105BF02136C1B823F9717A0A7AE58
         * source : 2
         */

        private int id;
        private String screen_name;
        private String note_num;
        private int gender;
        private int last_login_time;
        private int create_time;
        private String address;
        private long birthday;
        private String signature;
        private String profile_image_url;
        private String phone;
        private String unionid;
        private int source;


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

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public int getLast_login_time() {
            return last_login_time;
        }

        public void setLast_login_time(int last_login_time) {
            this.last_login_time = last_login_time;
        }

        public int getCreate_time() {
            return create_time;
        }

        public void setCreate_time(int create_time) {
            this.create_time = create_time;
        }


        public long getBirthday() {
            return birthday;
        }

        public void setBirthday(long birthday) {
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
    }
}
