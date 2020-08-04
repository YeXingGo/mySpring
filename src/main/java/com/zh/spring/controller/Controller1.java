package com.zh.spring.controller;

import com.zh.spring.service.Service1;

public class Controller1 {
    private Service1 service1;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Controller1{" +
                "service1=" + service1 +
                ", name='" + name + '\'' +
                '}';
    }
}
