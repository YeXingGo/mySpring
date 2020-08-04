package com.zh.spring.service.impl;

import com.zh.spring.controller.Controller1;
import com.zh.spring.service.Service1;

public class ServiceImpl1 implements Service1 {
    public ServiceImpl1 (){
        System.out.println("======创建的对象 "+ ServiceImpl1.class.getSimpleName());
    }
}
