package com.ailide.apartmentsabc.model;

import java.util.List;

public class EmojiGroup {

    private int id;
    private String tag_name;
    private int sort;
    private long create_time;
    private List<Emoji> data;
    private boolean select;

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
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

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public List<Emoji> getData() {
        return data;
    }

    public void setData(List<Emoji> data) {
        this.data = data;
    }
}