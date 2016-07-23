package com.amao.rpc.core.util;

import java.lang.reflect.Method;

/**
 * Created by 阿毛 on 2016/6/24.
 */
public class ClassUtils {

    public static String getMethodSignature(String className, String methodName) {
        return className + "#" + methodName;
    }

    public static String getMethodSignature(Method method) {
        return getMethodSignature(method.getDeclaringClass().getName(), method.getName());
    }

    public static String getServiceName(Method method) {
        return method.getDeclaringClass().getName();
    }
}
