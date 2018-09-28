package com.ailide.apartmentsabc.model;

import java.util.List;

/**
 * Created by Administrator on 2018/2/6 0006.
 */

public class ChatBean {

    /**
     * status : 1
     * data : [{"id":5,"friend_id":18,"content":"20180205/c6d26ac1c6a69df68d4d24c481b32fc3.png","mid":5,"fid":14,"status":0,"create_time":1517824960,"m_delete":0,"f_delete":0,"type":2}]
     * msg : 成功
     */

    private int status;
    private String msg;
    private List<FriendChatBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<FriendChatBean> getData() {
        return data;
    }

    public void setData(List<FriendChatBean> data) {
        this.data = data;
    }
}
