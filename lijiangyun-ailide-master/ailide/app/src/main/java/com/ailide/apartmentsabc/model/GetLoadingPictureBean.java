package com.ailide.apartmentsabc.model;

/**
 * Created by Administrator on 2018/3/26 0026.
 */

public class GetLoadingPictureBean {

    /**
     * status : 1
     * data : {"id":1,"image_an":"admin/20180321/9c5ac3ca6299424759e745ffd003817b.jpg","ios_750":"admin/20180321/9c5ac3ca6299424759e745ffd003817b.jpg","ios_828":"admin/20180113/ae051de5a5f711efa40cac069a38347d.jpg","ios_1125":"admin/20180207/54ebd286b368d0fa5d900eb9fb45f1f6.png"}
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
         * id : 1
         * image_an : admin/20180321/9c5ac3ca6299424759e745ffd003817b.jpg
         * ios_750 : admin/20180321/9c5ac3ca6299424759e745ffd003817b.jpg
         * ios_828 : admin/20180113/ae051de5a5f711efa40cac069a38347d.jpg
         * ios_1125 : admin/20180207/54ebd286b368d0fa5d900eb9fb45f1f6.png
         */

        private int id;
        private String image_an;
        private String ios_750;
        private String ios_828;
        private String ios_1125;
        private int is_show;

        public int getIs_show() {
            return is_show;
        }

        public void setIs_show(int is_show) {
            this.is_show = is_show;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImage_an() {
            return image_an;
        }

        public void setImage_an(String image_an) {
            this.image_an = image_an;
        }

        public String getIos_750() {
            return ios_750;
        }

        public void setIos_750(String ios_750) {
            this.ios_750 = ios_750;
        }

        public String getIos_828() {
            return ios_828;
        }

        public void setIos_828(String ios_828) {
            this.ios_828 = ios_828;
        }

        public String getIos_1125() {
            return ios_1125;
        }

        public void setIos_1125(String ios_1125) {
            this.ios_1125 = ios_1125;
        }
    }
}
