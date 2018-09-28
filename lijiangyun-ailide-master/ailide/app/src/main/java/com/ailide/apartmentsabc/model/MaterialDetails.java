package com.ailide.apartmentsabc.model;

public class MaterialDetails {

    private int status;
    private MaterialDetail data;
    private String msg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public MaterialDetail getData() {
        return data;
    }

    public void setData(MaterialDetail data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
