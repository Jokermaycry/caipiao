package com.ailide.apartmentsabc.model;

/**
 * Created by Administrator on 2018/2/2 0002.
 */

public class BaseDateBean {
    private int status;
    private String msg;
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
