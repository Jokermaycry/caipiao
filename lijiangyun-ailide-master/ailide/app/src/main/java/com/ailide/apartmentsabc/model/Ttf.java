package com.ailide.apartmentsabc.model;

public class Ttf {

    private int id;
    private String name;
    private String image;
    private String file_ttf;
    private long create_time;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFile_ttf() {
        return file_ttf;
    }

    public void setFile_ttf(String file_ttf) {
        this.file_ttf = file_ttf;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }
}
