package com.ailide.apartmentsabc.model;

import java.util.List;

public class Bubbles {

    private int status;
    private String msg;
    private List<Bubble> data;

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

    public List<Bubble> getData() {
        return data;
    }

    public void setData(List<Bubble> data) {
        this.data = data;
    }
}
