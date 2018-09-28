package com.privateticket.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/23.
 */

public class TicketResultListInfo {
    private String id;
    private List<NumInfo> numbers1=new ArrayList<>();
    private List<NumInfo> numbers2=new ArrayList<>();
    private List<NumInfo> numbers3=new ArrayList<>();
    private int mode;
    private String num;
    private String price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<NumInfo> getNumbers1() {
        return numbers1;
    }

    public void setNumbers1(List<NumInfo> numbers1) {
        this.numbers1 = numbers1;
    }

    public List<NumInfo> getNumbers2() {
        return numbers2;
    }

    public void setNumbers2(List<NumInfo> numbers2) {
        this.numbers2 = numbers2;
    }

    public List<NumInfo> getNumbers3() {
        return numbers3;
    }

    public void setNumbers3(List<NumInfo> numbers3) {
        this.numbers3 = numbers3;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


}
