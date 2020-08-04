package com.zh.spring;

import com.zh.spring.entiy.FactoryBean;
import com.zh.spring.entiy.Scope;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class ClassPathXMLApplicationContext extends AbstractApplicationContext {
    private String name = "application.xml";
    private String path = "";
    /**
     * 配置文件中的 beans
     */
    private List<Element> beans;

    public ClassPathXMLApplicationContext() {
        refresh();
    }

    @Override
    public Object getBean(String name) {
        FactoryBean factoryBean = super.cache3.get(name);
        if (factoryBean != null && Scope.SINGLE.equals(factoryBean.getScope())) {
            return factoryBean.getItem();
        }
        return null;
    }

    @Override
    public Object getBean(Class<?> clazz) {
        return null;
    }

    private File file;

    /**
     * 刷新方法
     */
    @Override
    public void refresh() {
        // 获取资源
        getResource();
        // 放入bean中
        createBean();
        System.out.println(super.cache3);
    }

    /**
     * 创建bean
     */
    private void createBean() {
        try {
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(file);
            // 获取根节点
            Element rootElement = document.getRootElement();
            List<Element> beans = rootElement.elements("bean");
            this.beans = beans;
            for (Element bean : beans) {
                addBean(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建bean对象
     *
     * @param bean
     * @throws Exception
     */
    private FactoryBean addBean(Element bean) throws Exception {
        Attribute nameAttr = bean.attribute("name");
        Attribute classAttr = bean.attribute("class");
        Attribute scopeAttr = bean.attribute("scope");
        // 对象别名
        String name = nameAttr.getValue();
        FactoryBean factoryBean = super.cache3.get(name);
        if (factoryBean != null) {
            return factoryBean;
        }
        // 对象作用域
        String scope = scopeAttr != null && scopeAttr.getValue() == null
                ? scopeAttr.getValue() : Scope.SINGLE;
        // 对象的全类名
        String className = classAttr.getValue();
        Class<?> clazz = Class.forName(className);
        // 获取对象
        Object o = clazz.newInstance();
        // 父类对象
         factoryBean = new FactoryBean();
        factoryBean.setClassName(className);
        factoryBean.setName(name);
        factoryBean.setScope(scope);
        factoryBean.setItem(o);
        List<Element> properties = bean.elements("properties");
        // 注入子对象 和属性值
        for (Element propertyEle : properties) {
            Attribute attributeName = propertyEle.attribute("name");
            String sonName = attributeName.getValue();
            Attribute refAttr = propertyEle.attribute("ref");
            Attribute valueAttr = propertyEle.attribute("value");
            if (refAttr != null) {
                FactoryBean sonfactoryBean = super.cache3.get(refAttr.getValue());
                // 说明还未加入 bean容器，那就是可能没有加载
                if (sonfactoryBean == null) {
                    // 找到对应 bean对象
                    Element beanElemenet = getBeanElement(refAttr.getValue());
                    // 说明没有此对象的装载
                    if (beanElemenet == null) {
                        throw new RuntimeException("找不对对应的类：" + refAttr.getValue());
                    }
                    // 使用递归把对应的子元素加入
                    sonfactoryBean = addBean(beanElemenet);

                }
                // 获取子类实例
                Object sonItem = sonfactoryBean.getItem();
                // 依赖注入
                ioc(o, clazz, sonName, sonItem);
                sonfactoryBean.setParent(factoryBean);
            } else {
                // 说明是注入值
                if (valueAttr != null) {
                    String value = valueAttr.getValue();
                    Object handlerValue = value;
                    //多种类型
                    if (Arrays.asList(new Class[]{
                            Byte.class,
                            Short.class,
                            Integer.class,
                            Long.class,
                            Float.class,
                            Double.class,

                    }).contains(sonName.getClass())) {
                        Class<?> valueClazz = sonName.getClass();
                        handlerValue = valueClazz.getConstructor(String.class).newInstance(value);
                    }
                    ioc(o, clazz, sonName, handlerValue);
                }
            }
        }

        super.cache3.put(name, factoryBean);
        return factoryBean;
    }

    /**
     * 根据 bean标签中 属性name 获取 对应的节点
     *
     * @param value
     * @return
     */
    private Element getBeanElement(String value) {
        for (Element beaneElement : beans) {
            Attribute name = beaneElement.attribute("name");
            if (value.equals(name.getValue())) {
                return beaneElement;
            }
        }
        return null;
    }


    /**
     * @param o
     * @param clazz
     * @param name
     * @param value
     */
    private void ioc(Object o, Class<?> clazz, String name, Object value) throws Exception {
        // 两种加载方式
        String setMethodName = "set" + String.valueOf(name.charAt(0)).toUpperCase()
                + name.substring(1);

        // set方法
        Method setMethod = null;
        try {
            // 使用set 方法注入
            setMethod = clazz.getDeclaredMethod(setMethodName, value.getClass());
            setMethod.invoke(o, value);
            System.out.println("=======使用set方法注入==========" + setMethodName);
        } catch (Exception e) {
            // 如果set 方法不可以注入那么就使用 field xieru
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            field.set(o, value);
            System.out.println("==========使用字段注入==============" + field.getName());
        }
    }

    /**
     * 获取配置文件地址
     */
    private void getResource() {
        String path = ClassPathXMLApplicationContext.class.getClassLoader().getResource("").getPath();
        File file = new File(path, name);
        this.file = file;
    }

}
