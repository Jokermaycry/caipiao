package com.ailide.apartmentsabc.model;

import java.io.Serializable;

public class Theme implements Serializable{

    private boolean select;
    private int id;
    private String image_url;
    private String name;
    private String an_image;
    private String head_img;
    private String middle_img;
    private String tail_img;
    private int head_px;
    private int middle_px;
    private int tail_px;
    private long create_time;

    public String getName() {
        return id +"";
    }

    public String getNineName() {
        return id + ".9.png";
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getAn_image() {
        return an_image;
    }

    public void setAn_image(String an_image) {
        this.an_image = an_image;
    }

    public String getHead_img() {
        return head_img;
    }

    public void setHead_img(String head_img) {
        this.head_img = head_img;
    }

    public String getMiddle_img() {
        return middle_img;
    }

    public void setMiddle_img(String middle_img) {
        this.middle_img = middle_img;
    }

    public String getTail_img() {
        return tail_img;
    }

    public void setTail_img(String tail_img) {
        this.tail_img = tail_img;
    }

    public int getHead_px() {
        return head_px;
    }

    public void setHead_px(int head_px) {
        this.head_px = head_px;
    }

    public int getMiddle_px() {
        return middle_px;
    }

    public void setMiddle_px(int middle_px) {
        this.middle_px = middle_px;
    }

    public int getTail_px() {
        return tail_px;
    }

    public void setTail_px(int tail_px) {
        this.tail_px = tail_px;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
