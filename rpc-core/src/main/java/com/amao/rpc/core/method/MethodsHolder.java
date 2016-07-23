package com.amao.rpc.core.method;

import com.amao.rpc.core.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 阿毛 on 2016/6/17.
 */
public class MethodsHolder {
    /**
     * key:xxx.xxx.HelloService.hello
     * value:MethodRef
     */
    private static Map<String, MethodRef> methodMap = new HashMap<>();


    public static void register(Class<?> serviceClass, Object bean) throws IllegalAccessException, InstantiationException {
        String interfaceName = serviceClass.getName();
        Method[] methods = serviceClass.getMethods();
        for (Method method : methods) {
            MethodRef methodRef = new MethodRef(bean, method);
            String methodSignature = ClassUtils.getMethodSignature(interfaceName, method.getName());
            methodMap.put(methodSignature, methodRef);
        }
    }


    public static MethodRef getMethodRef(String className, String methodName) {
        String signature = ClassUtils.getMethodSignature(className, methodName);
        return methodMap.get(signature);
    }


    public static int getSize() {
        return methodMap.size();
    }
}
