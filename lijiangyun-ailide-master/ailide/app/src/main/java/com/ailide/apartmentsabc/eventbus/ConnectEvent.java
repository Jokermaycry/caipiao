package com.ailide.apartmentsabc.eventbus;

/**
 * Created by Administrator on 2018/3/8 0008.
 */

public class ConnectEvent {
    private int fail;
    private String equitmentName;

    public String getEquitmentName() {
        return equitmentName;
    }

    public void setEquitmentName(String equitmentName) {
        this.equitmentName = equitmentName;
    }

    public int getFail() {
        return fail;
    }

    public void setFail(int fail) {
        this.fail = fail;
    }
}
