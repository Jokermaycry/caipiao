package com.privateticket.Model;

/**
 * Created by Administrator on 2016/12/20.
 */

public class OpenListBO {
    private String name;//彩票昵称 	String
    private String sh_name;//彩票简称，这个以后各种彩票的标识都得用到 	String
    private String per_time;//每几分钟一期 	int
    private String status;//	1正常2关闭 	int
    private String last_qs;//最后的期数 	int
    private String last_open_time;//开奖时间 	String
    private String open_num;//开奖时间 	String

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSh_name() {
        return sh_name;
    }

    public void setSh_name(String sh_name) {
        this.sh_name = sh_name;
    }

    public String getPer_time() {
        return per_time;
    }

    public void setPer_time(String per_time) {
        this.per_time = per_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLast_qs() {
        return last_qs;
    }

    public void setLast_qs(String last_qs) {
        this.last_qs = last_qs;
    }

    public String getLast_open_time() {
        return last_open_time;
    }

    public void setLast_open_time(String last_open_time) {
        this.last_open_time = last_open_time;
    }

    public String getOpen_num() {
        return open_num;
    }

    public void setOpen_num(String open_num) {
        this.open_num = open_num;
    }
}
