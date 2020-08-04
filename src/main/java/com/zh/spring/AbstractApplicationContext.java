package com.zh.spring;

import com.zh.spring.entiy.FactoryBean;

import java.util.HashMap;

public abstract class AbstractApplicationContext implements  BeanFactory {

    /**
     * 三级缓存
     */
    protected HashMap<String, FactoryBean> cache3 = new HashMap<>();

    public abstract  void refresh ();

}
