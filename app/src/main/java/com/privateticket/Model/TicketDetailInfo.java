package com.privateticket.Model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/23.
 */

public class TicketDetailInfo implements Serializable{

    private String sh_name;//彩票简称
    private String name;
    private String type;//1是11选5，2是快3 	int
    private String open_num;//开奖号码,以','隔开 	string
    private String now_qs;//当前的期数 	int
    private String last_qs;//最后一次的期数 	int
    private String now_time;//系统当前时间戳
    private String open_time;//	开奖时间 	int
    private String after_time;//	开奖时间 	int
    private String current_time;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOpen_num() {
        return open_num;
    }

    public void setOpen_num(String open_num) {
        this.open_num = open_num;
    }

    public String getNow_qs() {
        return now_qs;
    }

    public void setNow_qs(String now_qs) {
        this.now_qs = now_qs;
    }

    public String getLast_qs() {
        return last_qs;
    }

    public void setLast_qs(String last_qs) {
        this.last_qs = last_qs;
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

    public String getAfter_time() {
        return after_time;
    }

    public void setAfter_time(String after_time) {
        this.after_time = after_time;
    }
}
