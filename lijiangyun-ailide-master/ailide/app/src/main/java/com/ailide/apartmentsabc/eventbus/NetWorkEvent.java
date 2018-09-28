package com.ailide.apartmentsabc.eventbus;

/**
 * Created by liwenguo on 2017/5/9.
 *
 */
public class NetWorkEvent {

    private String mMsg;
    private String mValue;
    public NetWorkEvent(String msg) {
        mMsg = msg;
    }

    public String getMsg() {
        return mMsg;
    }

    public String getmValue() {
        return mValue;
    }

    public void setmValue(String mValue) {
        this.mValue = mValue;
    }

    public String getmMsg() {
        return mMsg;
    }

    public void setmMsg(String mMsg) {
        this.mMsg = mMsg;
    }
}
