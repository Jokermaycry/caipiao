package com.ailide.apartmentsabc.model;

public class Bubble {

    private boolean select;

    private int id;
    private String image_name;
    private String image_url;
    private int add_x_px;
    private int reduce_x_px;
    private int add_y_px;
    private int reduce_y_px;
    private long create_time;

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

    public int getAdd_x_px() {
        return add_x_px;
    }

    public void setAdd_x_px(int add_x_px) {
        this.add_x_px = add_x_px;
    }

    public int getReduce_x_px() {
        return reduce_x_px;
    }

    public void setReduce_x_px(int reduce_x_px) {
        this.reduce_x_px = reduce_x_px;
    }

    public int getAdd_y_px() {
        return add_y_px;
    }

    public void setAdd_y_px(int add_y_px) {
        this.add_y_px = add_y_px;
    }

    public int getReduce_y_px() {
        return reduce_y_px;
    }

    public void setReduce_y_px(int reduce_y_px) {
        this.reduce_y_px = reduce_y_px;
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
