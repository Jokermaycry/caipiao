package com.ailide.apartmentsabc.model;

import java.util.List;

public class Ttfs {

    private int status;
    private String msg;
    private List<Ttf> data;

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

    public List<Ttf> getData() {
        return data;
    }

    public void setData(List<Ttf> data) {
        this.data = data;
    }
}
