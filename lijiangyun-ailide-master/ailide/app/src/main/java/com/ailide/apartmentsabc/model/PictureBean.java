package com.ailide.apartmentsabc.model;

/**
 * Created by Administrator on 2018/1/24 0024.
 */

public class PictureBean {

    /**
     * status : 1
     * data : {"img_url":"20180124/8b0bd2140ea0df5cf495d4f5cd257be8.jpg"}
     * msg : 服务器卡了
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
         * img_url : 20180124/8b0bd2140ea0df5cf495d4f5cd257be8.jpg
         */

        private String img_url;
        private String id;
        private String profile_image_url;

        public String getProfile_image_url() {
            return profile_image_url;
        }

        public void setProfile_image_url(String profile_image_url) {
            this.profile_image_url = profile_image_url;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }
    }
}
