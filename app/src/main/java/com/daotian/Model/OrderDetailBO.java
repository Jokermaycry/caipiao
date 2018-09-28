package com.daotian.Model;

import java.util.List;

/**
 * Created by Administrator on 2016/12/17.
 */

public class OrderDetailBO {
    private String img;
    private String order_sn;//订单号 	string
    private String log_num;//当前期数 	string
    private String name;//彩票名称 	string
    private String open_num;//开奖号码 	string
    private String status;//1中奖0等待开奖2不中奖3撤单
    private String total_money;//总购买金额 	 	string
    private String get_fee;//当前中奖金额 	string
    private List<BuyListBO> buy_detail;//购买详情 	string
    private String buy_bs;//
    private String buy_ts;//
    private String day;//

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getLog_num() {
        return log_num;
    }

    public void setLog_num(String log_num) {
        this.log_num = log_num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpen_num() {
        return open_num;
    }

    public void setOpen_num(String open_num) {
        this.open_num = open_num;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotal_money() {
        return total_money;
    }

    public void setTotal_money(String total_money) {
        this.total_money = total_money;
    }

    public String getGet_fee() {
        return get_fee;
    }

    public void setGet_fee(String get_fee) {
        this.get_fee = get_fee;
    }

    public List<BuyListBO> getBuy_detail() {
        return buy_detail;
    }

    public void setBuy_detail(List<BuyListBO> buy_detail) {
        this.buy_detail = buy_detail;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getBuy_bs() {
        return buy_bs;
    }

    public void setBuy_bs(String buy_bs) {
        this.buy_bs = buy_bs;
    }

    public String getBuy_ts() {
        return buy_ts;
    }

    public void setBuy_ts(String buy_ts) {
        this.buy_ts = buy_ts;
    }
}
