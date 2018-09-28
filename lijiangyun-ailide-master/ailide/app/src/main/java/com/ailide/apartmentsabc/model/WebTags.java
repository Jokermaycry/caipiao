package com.ailide.apartmentsabc.model;

import java.util.List;

public class WebTags {

    private int status;
    private String msg;
    private List<WebTag> data;

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

    public List<WebTag> getData() {
        return data;
    }

    public void setData(List<WebTag> data) {
        this.data = data;
    }
}
