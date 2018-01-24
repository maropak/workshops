package com.citi.liveobject;

import org.redisson.api.annotation.REntity;
import org.redisson.api.annotation.RId;

@REntity
public class MyLiveObject {

    @RId
    private String name;

    private MyOtherObject value;

    public MyLiveObject(String name) {
        this.name = name;
    }
    public MyLiveObject() {
    }
    public String getName() {
        return name;
    }
    public MyOtherObject getValue() {
        return value;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setValue(MyOtherObject value) {
        this.value = value;
    }
}
