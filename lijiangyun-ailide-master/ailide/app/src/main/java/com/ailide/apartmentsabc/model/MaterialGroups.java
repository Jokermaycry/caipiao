package com.ailide.apartmentsabc.model;

import java.util.List;

public class MaterialGroups {

    private int status;
    private String msg;
    private List<MaterialGroup> data;

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

    public List<MaterialGroup> getData() {
        return data;
    }

    public void setData(List<MaterialGroup> data) {
        this.data = data;
    }
}
