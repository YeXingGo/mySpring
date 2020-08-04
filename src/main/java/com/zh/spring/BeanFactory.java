package com.zh.spring;

public interface BeanFactory {
    Object getBean (String name);

    Object getBean (Class<?> clazz);
}
