package com.citi.liveobject;

import org.redisson.api.annotation.REntity;
import org.redisson.api.annotation.RId;

@REntity
public class Password {

    @RId
    private String key;

    private String password;

    public Password() {
    }

//    public Password(String name) {
//        this.name = name;
//    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
