package com.amao.rpc.core.server;


import com.amao.rpc.core.data.ServiceMeta;
import com.amao.rpc.core.registry.ServiceRegistry;
import com.amao.rpc.core.registry.SubscribeListener;
import com.amao.rpc.core.registry.ZookeeperServiceRegistry;
import com.google.common.collect.Lists;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * Created by 阿毛 on 2016/6/26.
 */
public class ServiceDictionary implements SubscribeListener {

    private ServiceDictionary() {
    }

    public static class InstanceHolder {
        public static final ServiceDictionary INSTANCE = new ServiceDictionary();
    }

    public static ServiceDictionary getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private ServiceRegistry registryService;

    private List<ServiceMeta> serviceList = Lists.newArrayList();

    public synchronized void discoverService(List<ServiceMeta> services, InetSocketAddress registryAddress) {
        if (registryService == null) {
            registryService = new ZookeeperServiceRegistry(registryAddress);
        }
        List<ServiceMeta> tmp = registryService.lookup(services);
        serviceList.clear();
        serviceList.addAll(tmp);
    }


    public synchronized List<InetSocketAddress> getServiceAddress(ServiceMeta serviceMeta) {
        List<InetSocketAddress> ret = Lists.newArrayList();
        for (ServiceMeta meta : serviceList) {
            if (meta.getServiceName().equals(serviceMeta.getServiceName())) {
                ret.add(meta.getProviderAddress());
            }
        }
        return ret;
    }

    public synchronized void add(ServiceMeta serviceMeta) {
        serviceList.add(serviceMeta);

    }

    public synchronized void remove(ServiceMeta serviceMeta) {
        serviceList.remove(serviceMeta);
    }

    public synchronized void load(List<ServiceMeta> serviceMetaList) {
        serviceList.clear();
        serviceList.addAll(serviceMetaList);
    }


    @Override
    public void onServiceOnLine(ServiceMeta serviceMeta) {
        add(serviceMeta);
    }

    @Override
    public void onServiceOffLine(ServiceMeta serviceMeta) {
        remove(serviceMeta);
    }
}
