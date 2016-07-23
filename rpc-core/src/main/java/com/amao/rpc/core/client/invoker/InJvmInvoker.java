package com.amao.rpc.core.client.invoker;


import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by 阿毛 on 2016/6/10.
 */
public class InJvmInvoker implements InvocationHandler, MethodInterceptor {

    private Object bean;

    public InJvmInvoker(Object bean) {
        this.bean = bean;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(method, args);
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        return method.invoke(method, args);
    }


}
