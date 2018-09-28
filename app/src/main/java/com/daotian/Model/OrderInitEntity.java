package com.daotian.Model;

/**
 * Created by Administrator on 2016/12/14.
 */

public class OrderInitEntity {

    private String id;
    private String max_zq;//最大追期数,0表示无追期，>0就表示限制用户购买最大追期
    private String buy_qs;//购买倍数，最大不能超过这个倍数


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMax_zq() {
        return max_zq;
    }

    public void setMax_zq(String max_zq) {
        this.max_zq = max_zq;
    }

    public String getBuy_qs() {
        return buy_qs;
    }

    public void setBuy_qs(String buy_qs) {
        this.buy_qs = buy_qs;
    }
}
