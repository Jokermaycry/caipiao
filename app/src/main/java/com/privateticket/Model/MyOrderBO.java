package com.privateticket.Model;

/**
 * Created by Administrator on 2016/12/17.
 */

public class MyOrderBO {
    private String id;
    private String sh_name;//彩票简称
    private String name;//彩票名称
    private String total_fee;//总购买金额
    private String sery_num;//购买期数，1显示普通订单>1显示追期订单 	int
    private String get_fee;//当前中奖金额
    private String status;//订单状态,1成功未开奖2中奖3未中奖 	int
    private String first_lognum;
    private String ymd;//下单日期
    private String his;//下单日期
    private String day;//下单日期

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSh_name() {
        return sh_name;
    }

    public void setSh_name(String sh_name) {
        this.sh_name = sh_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getSery_num() {
        return sery_num;
    }

    public void setSery_num(String sery_num) {
        this.sery_num = sery_num;
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

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getYmd() {
        return ymd;
    }

    public void setYmd(String ymd) {
        this.ymd = ymd;
    }

    public String getHis() {
        return his;
    }

    public void setHis(String his) {
        this.his = his;
    }

    public String getFirst_lognum() {
        return first_lognum;
    }

    public void setFirst_lognum(String first_lognum) {
        this.first_lognum = first_lognum;
    }
}
