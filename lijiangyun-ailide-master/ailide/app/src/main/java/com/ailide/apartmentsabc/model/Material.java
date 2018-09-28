package com.ailide.apartmentsabc.model;

import java.io.Serializable;

public class Material implements Serializable{

    private int id;
    private String image_name;
    private String image_url;
    private int collection_r;
    private int print_num_r;
    private int collection_f;
    private int print_num_f;
    private int tag_id;
    private long create_time;
    private int sort;
    private String reamrk;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getCollection_r() {
        return collection_r;
    }

    public void setCollection_r(int collection_r) {
        this.collection_r = collection_r;
    }

    public int getPrint_num_r() {
        return print_num_r;
    }

    public void setPrint_num_r(int print_num_r) {
        this.print_num_r = print_num_r;
    }

    public int getCollection_f() {
        return collection_f;
    }

    public void setCollection_f(int collection_f) {
        this.collection_f = collection_f;
    }

    public int getPrint_num_f() {
        return print_num_f;
    }

    public void setPrint_num_f(int print_num_f) {
        this.print_num_f = print_num_f;
    }

    public int getTag_id() {
        return tag_id;
    }

    public void setTag_id(int tag_id) {
        this.tag_id = tag_id;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getReamrk() {
        return reamrk;
    }

    public void setReamrk(String reamrk) {
        this.reamrk = reamrk;
    }
}
