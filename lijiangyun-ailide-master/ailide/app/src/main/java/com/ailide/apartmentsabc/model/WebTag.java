package com.ailide.apartmentsabc.model;

import java.util.ArrayList;
import java.util.List;

public class WebTag {

    private int id;
    private String tag_name;
    private String mid;
    private int is_default;
    private long create_time;
    private List<Web> web;

    public WebTag(String tagName) {
        tag_name = tagName;
    }

    public WebTag() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public int getIs_default() {
        return is_default;
    }

    public void setIs_default(int is_default) {
        this.is_default = is_default;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public List<Web> getWeb() {
        if(web == null){
            return  new ArrayList<>();
        }
        return web;
    }

    public void setWeb(List<Web> web) {
        this.web = web;
    }
}
