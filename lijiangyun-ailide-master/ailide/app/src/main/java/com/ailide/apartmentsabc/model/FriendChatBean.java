package com.ailide.apartmentsabc.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by Administrator on 2018/1/26 0026.
 */

public class FriendChatBean implements MultiItemEntity {
    private int id;
    private int friend_id;
    private String content;
    private int mid;
    private int fid;
    private int status;
    private long create_time;
    private int m_delete;
    private int f_delete;
    private String profile_image_url;
    private String content_yu;
    private String note_num;

    public String getNote_num() {
        return note_num;
    }

    public void setNote_num(String note_num) {
        this.note_num = note_num;
    }

    public String getContent_yu() {
        return content_yu;
    }

    public void setContent_yu(String content_yu) {
        this.content_yu = content_yu;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFriend_id() {
        return friend_id;
    }

    public void setFriend_id(int friend_id) {
        this.friend_id = friend_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public int getM_delete() {
        return m_delete;
    }

    public void setM_delete(int m_delete) {
        this.m_delete = m_delete;
    }

    public int getF_delete() {
        return f_delete;
    }

    public void setF_delete(int f_delete) {
        this.f_delete = f_delete;
    }

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int getItemType() {
        return Integer.valueOf(type);
    }
}
