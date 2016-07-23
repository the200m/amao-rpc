package com.amao.rpc.core.registry;

import com.amao.rpc.core.data.ServiceMeta;

import java.util.List;

/**
 * Created by 阿毛 on 2016/6/27.
 */
public interface ServiceRegistry {

    void register(ServiceMeta meta);


    void unregister(ServiceMeta meta);


    void subscribe(ServiceMeta serviceMeta, SubscribeListener listener);


    List<ServiceMeta> lookup(List<ServiceMeta> serviceList);
}
