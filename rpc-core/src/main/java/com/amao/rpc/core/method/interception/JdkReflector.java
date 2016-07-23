package com.amao.rpc.core.method.interception;

import com.amao.rpc.core.method.MethodRef;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by 阿毛 on 2016/6/12.
 */
public class JdkReflector implements Reflector {

    @Override
    public Object invoke(MethodRef methodRef, Object[] arguments) {
        Object result = null;
        try {
            Method method = methodRef.getMethod();
            method.setAccessible(true);
            result = method.invoke(methodRef.getBean(), arguments);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Object newProxy(Class interfaceType, Object handler) {
        Object object = Proxy.newProxyInstance(
                interfaceType.getClassLoader(), new Class<?>[]{interfaceType}, (InvocationHandler) handler);
        return interfaceType.cast(object);
    }
}
