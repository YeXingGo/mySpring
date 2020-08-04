package com.zh.spring.main;

import com.zh.spring.ClassPathXMLApplicationContext;

public class Test {
    public static void main(String[] args) {
        ClassPathXMLApplicationContext classPathXMLApplicationContext = new ClassPathXMLApplicationContext();
        System.out.println(classPathXMLApplicationContext.getBean("controller1"));
    }
}
