package com.ailide.apartmentsabc.model;

import java.util.List;

public class StatusResponse {

    private int status;
    private String msg;
    private List data;

    public List getData() {
        return data;
    }

    public void setData(List data) {
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
