package com.ailide.apartmentsabc.model;

import java.io.Serializable;
import java.util.List;

public class CollectMaterialGroup implements Serializable{

    private int id;
    private String tag_name;
    private long create_time;
    private String class_id;
    private int sort;
    private String image_url;
    private String collection_r;
    private String print_num_r;
    private List<Material> data;

    public String getCollection_r() {
        return collection_r;
    }

    public void setCollection_r(String collection_r) {
        this.collection_r = collection_r;
    }

    public String getPrint_num_r() {
        return print_num_r;
    }

    public void setPrint_num_r(String print_num_r) {
        this.print_num_r = print_num_r;
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

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public List<Material> getData() {
        return data;
    }

    public void setData(List<Material> data) {
        this.data = data;
    }
}
