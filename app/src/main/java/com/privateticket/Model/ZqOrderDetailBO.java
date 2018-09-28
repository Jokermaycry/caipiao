package com.privateticket.Model;

import java.util.List;

/**
 * Created by Administrator on 2016/12/19.
 */

public class ZqOrderDetailBO {
    private String id;//总订单id
    private String order_sn;//订单号
    private String total_fee;//总购买金额
    private String name;//彩票名称
    private String get_fee;//当前中奖金额
    private String status;//	订单状态,1成功未开奖2中奖3未中奖 	int
    private String is_sery_stop;//	追期中奖后停止追期，1是0否 	int
    private String sery_num;//总共追期数 	int
    private String sery_now;//已开追期数 	int
    private List<ChildOrder> child_order;//	子订单列表 	array
    private String img;//	图片地址 	array


    public class ChildOrder{
        String id;
        String log_num;
        String get_fee;
        String total_money;
        String status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLog_num() {
            return log_num;
        }

        public void setLog_num(String log_num) {
            this.log_num = log_num;
        }

        public String getGet_fee() {
            return get_fee;
        }

        public void setGet_fee(String get_fee) {
            this.get_fee = get_fee;
        }

        public String getTotal_money() {
            return total_money;
        }

        public void setTotal_money(String total_money) {
            this.total_money = total_money;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGet_fee() {
        return get_fee;
    }

    public void setGet_fee(String get_fee) {
        this.get_fee = get_fee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIs_sery_stop() {
        return is_sery_stop;
    }

    public void setIs_sery_stop(String is_sery_stop) {
        this.is_sery_stop = is_sery_stop;
    }

    public String getSery_num() {
        return sery_num;
    }

    public void setSery_num(String sery_num) {
        this.sery_num = sery_num;
    }

    public String getSery_now() {
        return sery_now;
    }

    public void setSery_now(String sery_now) {
        this.sery_now = sery_now;
    }

    public List<ChildOrder> getChild_order() {
        return child_order;
    }

    public void setChild_order(List<ChildOrder> child_order) {
        this.child_order = child_order;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
