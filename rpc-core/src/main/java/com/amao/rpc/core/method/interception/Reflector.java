package com.amao.rpc.core.method.interception;

import com.amao.rpc.core.method.MethodRef;

/**
 * Created by 阿毛 on 2016/6/12.
 */
public interface Reflector<T> {

    Object invoke(MethodRef methodRef, Object[] arguments);

    T newProxy(Class<T> interfaceType, Object handler);
}
