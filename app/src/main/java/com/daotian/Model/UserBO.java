package com.daotian.Model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/9.
 */

public class UserBO implements Serializable{
    private String id;
    private String account;
    private String pwd;
    private String name;
    private String access_token;
    private String balance;
    private String betting;
    private String freeze_balance;
    private String  now_month_spend;
    private String max_zq;
    private String max_bs;


    public String getBetting() {
        return betting;
    }

    public void setBetting(String betting) {
        this.betting = betting;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getFreeze_balance() {
        return freeze_balance;
    }

    public void setFreeze_balance(String freeze_balance) {
        this.freeze_balance = freeze_balance;
    }

    public String getMax_zq() {
        return max_zq;
    }

    public void setMax_zq(String max_zq) {
        this.max_zq = max_zq;
    }

    public String getMax_bs() {
        return max_bs;
    }

    public void setMax_bs(String max_bs) {
        this.max_bs = max_bs;
    }

    public String getNow_month_spend() {
        return now_month_spend;
    }

    public void setNow_month_spend(String now_month_spend) {
        this.now_month_spend = now_month_spend;
    }
}
