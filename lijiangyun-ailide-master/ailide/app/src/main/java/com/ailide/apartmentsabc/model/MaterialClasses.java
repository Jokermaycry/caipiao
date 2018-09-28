package com.ailide.apartmentsabc.model;

import java.util.List;

public class MaterialClasses {

    private int status;
    private String msg;
    private List<MaterialClass> data;

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

    public List<MaterialClass> getData() {
        return data;
    }

    public void setData(List<MaterialClass> data) {
        this.data = data;
    }
}
