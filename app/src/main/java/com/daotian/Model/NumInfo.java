package com.daotian.Model;

import com.loopj.android.http.PreemptiveAuthorizationHttpRequestInterceptor;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/11/24.
 */

public class NumInfo implements Serializable{

    private String num;
    private String upload_data;
    private boolean is_checked=false;
    private boolean is_spe;
    private boolean touchflag;
    private int flag=0;

    public boolean isTouchflag() {
        return touchflag;
    }

    public void setTouchflag(boolean touchflag) {
        this.touchflag = touchflag;
    }

    public boolean isIs_checked() {
        return is_checked;
    }

    public boolean isIs_spe() {
        return is_spe;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getUpload_data() {
        return upload_data;
    }

    public void setUpload_data(String upload_data) {
        this.upload_data = upload_data;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public boolean is_checked() {
        return is_checked;
    }

    public void setIs_checked(boolean is_checked) {
        this.is_checked = is_checked;
    }

    public boolean is_spe() {
        return is_spe;
    }

    public void setIs_spe(boolean is_spe) {
        this.is_spe = is_spe;
    }
}
