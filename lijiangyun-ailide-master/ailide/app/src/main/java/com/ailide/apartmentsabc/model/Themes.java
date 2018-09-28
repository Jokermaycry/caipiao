package com.ailide.apartmentsabc.model;

import java.util.List;

public class Themes {

    private int status;
    private String msg;
    private List<Theme> data;

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

    public List<Theme> getData() {
        return data;
    }

    public void setData(List<Theme> data) {
        this.data = data;
    }
}
