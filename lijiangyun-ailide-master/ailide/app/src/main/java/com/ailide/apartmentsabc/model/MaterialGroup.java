package com.ailide.apartmentsabc.model;

import java.io.Serializable;
import java.util.List;

public class MaterialGroup implements Serializable{

    private int id;
    private String tag_name;
    private long create_time;
    private String class_id;
    private int sort;
    private String image_url;
    private int collection_r;
    private int print_num_r;
    private List<Material> material;

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
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

    public List<Material> getMaterial() {
        return material;
    }

    public void setMaterial(List<Material> material) {
        this.material = material;
    }
}
