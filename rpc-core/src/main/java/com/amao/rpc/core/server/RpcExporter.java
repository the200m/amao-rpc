package com.amao.rpc.core.server;

import com.amao.rpc.core.data.ServiceMeta;
import com.amao.rpc.core.method.MethodsHolder;
import com.amao.rpc.core.registry.ServiceRegistry;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

/**
 * Created by 阿毛 on 2016/7/6.
 */
public class RpcExporter {

    private static Logger logger = LoggerFactory.getLogger(RpcExporter.class);

    private List<ServiceMeta> serviceList = Lists.newArrayList();

    private InetSocketAddress serverAddress;
    private ServiceRegistry serviceRegistry;
    private RpcServer rpcServer;

    public RpcExporter(InetSocketAddress serverAddress, ServiceRegistry serviceRegistry, RpcServer rpcServer) {
        this.serverAddress = serverAddress;
        this.serviceRegistry = serviceRegistry;
        this.rpcServer = rpcServer;
    }

    public void mockRegisterBean(Map<String, Object> serviceBeanMap) {
        if (MapUtils.isNotEmpty(serviceBeanMap)) {
            for (Object serviceBean : serviceBeanMap.values()) {
                Class<?> clazz = serviceBean.getClass().getInterfaces()[0];
                try {
                    MethodsHolder.register(clazz, serviceBean);
                    ServiceMeta meta = new ServiceMeta();
                    meta.setServiceName(clazz.getName());
                    meta.setProviderAddress(serverAddress);
                    serviceList.add(meta);
                } catch (IllegalAccessException | InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void run() {
        start();
        try {
            rpcServer.start(serverAddress.getPort());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            stop();
        }
    }

    public void start() {
        export();
    }


    public void stop() {
        unExport();
        rpcServer.stop();
    }

    public void export() {
        for (ServiceMeta meta : serviceList) {
            serviceRegistry.register(meta);
        }
    }

    public void unExport() {
        for (ServiceMeta meta : serviceList) {
            serviceRegistry.unregister(meta);
        }
    }
}
