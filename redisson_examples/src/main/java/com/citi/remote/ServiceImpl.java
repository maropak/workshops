package com.citi.remote;

public class ServiceImpl implements Service {

    @Override
    public String execute() {
        System.out.println("executed");
        return "returned";
    }
}
