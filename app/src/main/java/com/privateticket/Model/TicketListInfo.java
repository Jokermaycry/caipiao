package com.privateticket.Model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/23.
 */

public class TicketListInfo implements Serializable{

    private String name;//彩票昵称 	String
    private String sh_name;//彩票简称，这个以后各种彩票的标识都得用到 	String
    private String img;//logo 	String
    private String type;//1是11选5，2是快3 	int
    private String before_time;//延长多久买入 	int
    private String after_time;//提早多久禁止买进 	int
    private String first_time;//每日首次开奖时间，例如09:00:00 	time 这个类型没有就换string
    private String status;//1正常2关闭 	int
    private String now_time;//系统当前时间戳 	int
    private String open_time;//关闭时间 	int
    private String current_time;//当前时间
    private String now_qs;//当前期数 	int
    private String bref_code;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }



    public String getNow_qs() {
        return now_qs;
    }

    public void setNow_qs(String now_qs) {
        this.now_qs = now_qs;
    }

    public String getBref_code() {
        return bref_code;
    }

    public void setBref_code(String bref_code) {
        this.bref_code = bref_code;
    }

    public String getBefore_time() {
        return before_time;
    }

    public void setBefore_time(String before_time) {
        this.before_time = before_time;
    }

    public String getAfter_time() {
        return after_time;
    }

    public void setAfter_time(String after_time) {
        this.after_time = after_time;
    }

    public String getFirst_time() {
        return first_time;
    }

    public void setFirst_time(String first_time) {
        this.first_time = first_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNow_time() {
        return now_time;
    }

    public void setNow_time(String now_time) {
        this.now_time = now_time;
    }

    public String getOpen_time() {
        return open_time;
    }

    public void setOpen_time(String open_time) {
        this.open_time = open_time;
    }

    public String getCurrent_time() {
        return current_time;
    }

    public void setCurrent_time(String current_time) {
        this.current_time = current_time;
    }
}
