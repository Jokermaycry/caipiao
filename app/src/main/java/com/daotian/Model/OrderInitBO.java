package com.daotian.Model;

/**
 * Created by Administrator on 2016/12/16.
 */

public class OrderInitBO {

    private String id;
    private String max_zq;//,0表示无追期，>0就表示限制用户购买最大追期
    private String max_bs;//最大不能超过这个倍数
    private String sh_name;//最大不能超过这个倍数

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

    public String getMax_bs() {
        return max_bs;
    }

    public void setMax_bs(String max_bs) {
        this.max_bs = max_bs;
    }

    public String getSh_name() {
        return sh_name;
    }

    public void setSh_name(String sh_name) {
        this.sh_name = sh_name;
    }
}
