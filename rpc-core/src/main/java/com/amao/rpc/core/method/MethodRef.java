package com.amao.rpc.core.method;

import java.lang.reflect.Method;

/**
 * Created by 阿毛 on 2016/6/17.
 */
public class MethodRef {

    private Object bean;
    private Method method;


    public MethodRef(Object bean, Method method) {
        this.bean = bean;
        this.method = method;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
