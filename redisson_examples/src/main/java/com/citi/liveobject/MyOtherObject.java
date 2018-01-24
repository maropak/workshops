package com.citi.liveobject;

import org.redisson.api.annotation.REntity;
import org.redisson.api.annotation.RId;

@REntity
public class MyOtherObject {

    @RId
    private String name;

    private String value;

    public MyOtherObject(String name) {
        this.name = name;
    }
    public MyOtherObject() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}