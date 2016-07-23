package com.amao.rpc.core.registry;

import com.amao.rpc.core.data.ServiceMeta;
import com.amao.rpc.core.util.CommonUtils;
import com.google.common.collect.Lists;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by 阿毛 on 2016/6/27.
 */
public class ZookeeperServiceRegistry implements ServiceRegistry {

    private static Logger logger = LoggerFactory.getLogger(ZookeeperServiceRegistry.class);
    private CuratorFramework configClient;
    private Set<ServiceMeta> registerMetaSet = new HashSet<>();

    public ZookeeperServiceRegistry(String registryAddress) {
        init(CommonUtils.convert2Address(registryAddress));
    }

    public ZookeeperServiceRegistry(InetSocketAddress registryAddress) {
        init(registryAddress);
    }

    private void init(InetSocketAddress registryAddress) {
        configClient = CuratorFrameworkFactory.newClient(
                CommonUtils.convert2AddressString(registryAddress), 5000, 5000, new ExponentialBackoffRetry(500, 20));
        configClient.start();
    }


    @Override
    public void register(final ServiceMeta meta) {
        final String directory = String.format("/amao-rpc/provider/%s/%s",
                meta.getServiceName(), CommonUtils.convert2AddressString(meta.getProviderAddress()));
        try {
            if (configClient.checkExists().forPath(directory) == null) {
                configClient.create().creatingParentsIfNeeded().forPath(directory);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        try {
            configClient.create().withMode(CreateMode.EPHEMERAL).inBackground(new BackgroundCallback() {
                @Override
                public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                    registerMetaSet.add(meta);
                    logger.info("register:" + directory + "|" + event);
                }
            }).forPath(directory);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    @Override
    public void unregister(final ServiceMeta meta) {
        final String directory = String.format("/amao-rpc/provider/%s/%s",
                meta.getServiceName(), CommonUtils.convert2AddressString(meta.getProviderAddress()));

        try {
            if (configClient.checkExists().forPath(directory) == null) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            configClient.delete().inBackground(new BackgroundCallback() {
                @Override
                public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                    registerMetaSet.remove(meta);
                    logger.info("unregister:" + directory);
                }
            }).forPath(directory);
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    @Override
    public void subscribe(ServiceMeta serviceMeta, final SubscribeListener listener) {
        String directory = String.format("/jupiter/provider/%s",
                serviceMeta.getServiceName());

        PathChildrenCache childrenCache = new PathChildrenCache(configClient, directory, false);
        childrenCache.getListenable().addListener(new PathChildrenCacheListener() {

            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                switch (event.getType()) {
                    case CHILD_ADDED: {
                        ServiceMeta meta = parseRegisterMeta(event.getData().getPath());
                        listener.onServiceOnLine(meta);
                        break;
                    }
                    case CHILD_REMOVED: {
                        ServiceMeta meta = parseRegisterMeta(event.getData().getPath());
                        listener.onServiceOffLine(meta);
                        break;
                    }
                }
            }
        });

        try {
            childrenCache.start();
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    @Override
    public List<ServiceMeta> lookup(List<ServiceMeta> serviceList) {
        List<ServiceMeta> registerMetaList = Lists.newArrayList();
        for (ServiceMeta serviceMeta : serviceList) {
            List<ServiceMeta> list = lookup(serviceMeta);
            registerMetaList.addAll(list);
        }
        return registerMetaList;
    }


    public List<ServiceMeta> lookup(ServiceMeta serviceMeta) {
        final String directory = String.format("/amao-rpc/provider/%s",
                serviceMeta.getServiceName());

        List<ServiceMeta> registerMetaList = Lists.newArrayList();
        try {
            List<String> paths = configClient.getChildren().forPath(directory);
            for (String p : paths) {
                ServiceMeta sm = parseRegisterMeta(p);
                sm.setServiceName(serviceMeta.getServiceName());
                registerMetaList.add(sm);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
        return registerMetaList;
    }

    private ServiceMeta parseRegisterMeta(String data) {
        ServiceMeta meta = new ServiceMeta();
        meta.setProviderAddress(CommonUtils.convert2Address(data));
        return meta;
    }


    public static void main(String[] args) throws InterruptedException {
        InetSocketAddress address = new InetSocketAddress("121.41.76.228", 2181);

        ServiceRegistry zookeeperRegisterService = new ZookeeperServiceRegistry(address);
        ServiceMeta registerMeta = new ServiceMeta();
        registerMeta.setServiceName("TimeService");
        registerMeta.setProviderAddress(new InetSocketAddress("127.0.0.1", 34568));

        zookeeperRegisterService.register(registerMeta);
        zookeeperRegisterService.register(registerMeta);

    }
}
