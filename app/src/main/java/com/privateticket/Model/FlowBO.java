package com.privateticket.Model;

/**
 * Created by Alienware on 2018/8/18.
 */

public class FlowBO {

    String id;
    String fee;
    String fee_type;
    String type_name;
    String create_time;

    String the_balance;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getFee_type() {
        return fee_type;
    }

    public void setFee_type(String fee_type) {
        this.fee_type = fee_type;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getThe_balance() {
        return the_balance;
    }

    public void setThe_balance(String the_balance) {
        this.the_balance = the_balance;
    }
}
