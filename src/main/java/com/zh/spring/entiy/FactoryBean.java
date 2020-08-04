package com.zh.spring.entiy;

public class FactoryBean {
    /**
     * 父节点
     */
    private FactoryBean parent;
    /**
     * 对象名称
     */
    private String name;
    /**
     * 对象的全类名
     */
    private String className;
    /**
     * 作用域
     */
    private String scope;
    /**
     * 实例
     */
    private Object item;

    public Object getItem() {
        return item;
    }

    public void setItem(Object item) {
        this.item = item;
    }

    public FactoryBean getParent() {
        return parent;
    }

    public void setParent(FactoryBean parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public String toString() {
        return "FactoryBean{" +
                "parent=" + parent +
                ", name='" + name + '\'' +
                ", className='" + className + '\'' +
                ", scope='" + scope + '\'' +
                ", item=" + item +
                '}';
    }
}
