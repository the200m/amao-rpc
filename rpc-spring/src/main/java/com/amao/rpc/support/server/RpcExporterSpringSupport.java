package com.amao.rpc.support.server;

import com.amao.rpc.core.data.ServiceMeta;
import com.amao.rpc.core.server.RpcServer;
import com.amao.rpc.core.method.MethodsHolder;
import com.amao.rpc.core.registry.ServiceRegistry;
import com.amao.rpc.core.util.CommonUtils;
import com.amao.rpc.support.annotation.RpcService;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

/**
 * Created by 阿毛 on 2016/7/6.
 */
public class RpcExporterSpringSupport implements ApplicationContextAware, InitializingBean {

    private List<ServiceMeta> serviceList = Lists.newArrayList();

    private InetSocketAddress serverAddress;
    private ServiceRegistry serviceRegistry;
    private RpcServer rpcServer;

    public RpcExporterSpringSupport(String serverAddress, ServiceRegistry serviceRegistry, RpcServer rpcServer) {
        this.serverAddress = CommonUtils.convert2Address(serverAddress);
        this.serviceRegistry = serviceRegistry;
        this.rpcServer = rpcServer;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(RpcService.class); // 获取所有带有 RpcService 注解的 Spring Bean
        if (MapUtils.isNotEmpty(serviceBeanMap)) {
            for (Object serviceBean : serviceBeanMap.values()) {
                Class<?> clazz = serviceBean.getClass().getAnnotation(RpcService.class).value();
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

    @Override
    public void afterPropertiesSet() throws Exception {
        export();
        rpcServer.start(serverAddress.getPort());
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
