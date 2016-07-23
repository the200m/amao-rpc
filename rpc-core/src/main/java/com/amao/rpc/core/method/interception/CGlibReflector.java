package com.amao.rpc.core.method.interception;

import com.amao.rpc.core.method.MethodRef;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by 阿毛 on 2016/6/12.
 */
public class CGlibReflector implements Reflector {

    @Override
    public Object invoke(MethodRef methodRef, Object[] arguments) {
        Object result = null;
        FastClass serviceFastClass = FastClass.create(methodRef.getBean().getClass());
        FastMethod serviceFastMethod = serviceFastClass.getMethod(methodRef.getMethod());

        try {
            result = serviceFastMethod.invoke(methodRef.getBean(), arguments);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Object newProxy(Class interfaceType, Object handler) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(interfaceType);
        enhancer.setCallback((MethodInterceptor) handler);
        enhancer.setClassLoader(interfaceType.getClassLoader());
        return interfaceType.cast(enhancer.create());
    }

}
