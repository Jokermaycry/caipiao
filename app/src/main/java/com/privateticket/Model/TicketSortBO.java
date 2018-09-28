package com.privateticket.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alienware on 2018/8/9.
 */

public class TicketSortBO {

    private String name;
    private List<TicketListInfo> datalist=new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TicketListInfo> getDatalist() {
        return datalist;
    }

    public void setDatalist(List<TicketListInfo> datalist) {
        this.datalist = datalist;
    }
}
