package com.ailide.apartmentsabc.model;

import java.util.List;

public class CollectMaterialGroups {

    private int status;
    private String msg;
    private List<CollectMaterialGroup> data;

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

    public List<CollectMaterialGroup> getData() {
        return data;
    }

    public void setData(List<CollectMaterialGroup> data) {
        this.data = data;
    }
}
