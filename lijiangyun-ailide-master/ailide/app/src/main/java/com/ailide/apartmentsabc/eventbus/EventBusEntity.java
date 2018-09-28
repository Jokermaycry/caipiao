package com.ailide.apartmentsabc.eventbus;

public class EventBusEntity {

    private String id;
    private Object data;

    public EventBusEntity(String id, Object data) {
        this.id = id;
        this.data = data;
    }

    public EventBusEntity(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
