package com.ailide.apartmentsabc.model;

import java.io.Serializable;

public class Bill implements Serializable{

    private int id;
    private String head_img;
    private String tail_img;
    private String middle_img;
    private String img_url;
    private int text_l_px;
    private int text_r_px;
    private long create_time;
    private String sort;
    private int head_px;
    private int tail_px;
    private int middle_px;

    public int getHead_px() {
        return head_px;
    }

    public void setHead_px(int head_px) {
        this.head_px = head_px;
    }

    public int getTail_px() {
        return tail_px;
    }

    public void setTail_px(int tail_px) {
        this.tail_px = tail_px;
    }

    public int getMiddle_px() {
        return middle_px;
    }

    public void setMiddle_px(int middle_px) {
        this.middle_px = middle_px;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHead_img() {
        return head_img;
    }

    public void setHead_img(String head_img) {
        this.head_img = head_img;
    }

    public String getTail_img() {
        return tail_img;
    }

    public void setTail_img(String tail_img) {
        this.tail_img = tail_img;
    }

    public String getMiddle_img() {
        return middle_img;
    }

    public void setMiddle_img(String middle_img) {
        this.middle_img = middle_img;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public int getText_l_px() {
        return text_l_px;
    }

    public void setText_l_px(int text_l_px) {
        this.text_l_px = text_l_px;
    }

    public int getText_r_px() {
        return text_r_px;
    }

    public void setText_r_px(int text_r_px) {
        this.text_r_px = text_r_px;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
