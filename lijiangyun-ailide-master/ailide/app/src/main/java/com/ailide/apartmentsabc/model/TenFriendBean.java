package com.ailide.apartmentsabc.model;

import java.util.List;

/**
 * Created by Administrator on 2018/2/1 0001.
 */

public class TenFriendBean {

    /**
     * status : 1
     * data : [{"id":12,"screen_name":"Mango","note_num":"81832959","gender":1,"last_login_time":1517228471,"create_time":1517228444,"address":null,"birthday":0,"signature":null,"profile_image_url":"http://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTK8QicvNyFFibYBIEBfQibx2wyTc07icNyS9E6UJH0ibGmx74zNU8uYCbQ97h9aV6wE2KBbqoibH3d9d8sw/132","phone":null,"unionid":"obajP03v7Pfcjm1XtayJq1J5mjTU","source":1},{"id":13,"screen_name":"melody","note_num":"94182786","gender":1,"last_login_time":1517451113,"create_time":1517238110,"address":null,"birthday":0,"signature":null,"profile_image_url":"http://q.qlogo.cn/qqapp/1106615109/EEB83C7167D6190F9C84D7C7AA859960/100","phone":null,"unionid":"EEB83C7167D6190F9C84D7C7AA859960","source":2},{"id":6,"screen_name":"Enter","note_num":"65560453","gender":2,"last_login_time":1517408960,"create_time":1517133208,"address":"厦门","birthday":2018,"signature":"签名是什么鬼","profile_image_url":"http://admin.ileadtek.com/public/upload/http://admin.ileadtek.com/public/upload/20180201/a3a5e480f11811c000db941d36089bde.png","phone":null,"unionid":"o7x9i0nRq3VEaaakfnIQ3irsSDzU","source":1},{"id":14,"screen_name":"雲","note_num":"98192061","gender":1,"last_login_time":1517379266,"create_time":1517378423,"address":null,"birthday":0,"signature":null,"profile_image_url":"http://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJrtq9n5cWKficNVOedJDCNmM49wPW1RSpN7byhn9pxbB39qds5SCd3sqAryDv1Aqib77OmbAaV55ZA/132","phone":null,"unionid":"obajP0-grSF26vkP4TqGwwCs9oeo","source":1},{"id":8,"screen_name":null,"note_num":"75863959","gender":1,"last_login_time":1517138446,"create_time":1517138386,"address":null,"birthday":0,"signature":null,"profile_image_url":null,"phone":null,"unionid":"123455","source":1},{"id":7,"screen_name":null,"note_num":"50683307","gender":1,"last_login_time":1517138358,"create_time":1517138267,"address":null,"birthday":0,"signature":null,"profile_image_url":null,"phone":null,"unionid":"1234565","source":1},{"id":11,"screen_name":"Wade王兴海","note_num":"93519450","gender":1,"last_login_time":1517234975,"create_time":1517206624,"address":null,"birthday":0,"signature":null,"profile_image_url":"http://wx.qlogo.cn/mmopen/vi_32/DYAIOgq83eqctZtMHPIOR9bLaqnV9vqIxdafneuWRgn0xAXQ74fGEr9wIOOoyoTAgiaCagpszSzuZOYMCN5Mg3Q/132","phone":null,"unionid":"obajP04xdCTaa7bPPO3sywGL_5bA","source":1},{"id":10,"screen_name":"心在梦就在","note_num":"94949787","gender":1,"last_login_time":1517293911,"create_time":1517206615,"address":"厦门","birthday":0,"signature":"好","profile_image_url":"http://q.qlogo.cn/qqapp/1106615109/CA5596EEB56C406DD4EA963F2D93BF86/100","phone":null,"unionid":"CA5596EEB56C406DD4EA963F2D93BF86","source":2},{"id":15,"screen_name":"进击de喵酱","note_num":"65999049","gender":1,"last_login_time":1517388648,"create_time":1517385544,"address":null,"birthday":0,"signature":null,"profile_image_url":"http://q.qlogo.cn/qqapp/1106615109/3390C5011B9895FF90C4E52B1D64CD03/100","phone":null,"unionid":"3390C5011B9895FF90C4E52B1D64CD03","source":2},{"id":9,"screen_name":"feioou_杨振旭","note_num":"80382919","gender":1,"last_login_time":1517451098,"create_time":1517206010,"address":null,"birthday":0,"signature":null,"profile_image_url":"http://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTIThJB44nOiatic5LtVPvklxmmpupIIpyUSP59pUZg4oRJiafP5CqnSmgOXQSNVoNspG7FHticIMFPw2w/132","phone":null,"unionid":"obajP07DTPClvUyRUw_taClnyHGU","source":1}]
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
         * id : 12
         * screen_name : Mango
         * note_num : 81832959
         * gender : 1
         * last_login_time : 1517228471
         * create_time : 1517228444
         * address : null
         * birthday : 0
         * signature : null
         * profile_image_url : http://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTK8QicvNyFFibYBIEBfQibx2wyTc07icNyS9E6UJH0ibGmx74zNU8uYCbQ97h9aV6wE2KBbqoibH3d9d8sw/132
         * phone : null
         * unionid : obajP03v7Pfcjm1XtayJq1J5mjTU
         * source : 1
         */

        private int id;
        private String screen_name;
        private String note_num;
        private int gender;
        private int last_login_time;
        private int create_time;
        private String address;
        private int birthday;
        private String signature;
        private String profile_image_url;
        private String phone;
        private String unionid;
        private int source;

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
