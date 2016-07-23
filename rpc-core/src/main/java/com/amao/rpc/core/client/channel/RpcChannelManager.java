package com.amao.rpc.core.client.channel;


import com.amao.rpc.core.client.RpcClient;
import com.amao.rpc.core.config.Config;
import com.amao.rpc.core.data.ServiceMeta;
import com.amao.rpc.core.exception.ConnectFailedException;
import com.amao.rpc.core.exception.RpcException;
import com.amao.rpc.core.load.LoadBalancer;
import com.amao.rpc.core.server.ServiceDictionary;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 阿毛 on 2016/6/23.
 */
public class RpcChannelManager {

    private static Logger logger = LoggerFactory.getLogger(RpcChannelManager.class);
    private RpcClient rpcClient;
    private final ConcurrentHashMap<InetSocketAddress, RpcChannel> channelMap = new ConcurrentHashMap<>();
    private LoadBalancer loadBalancer;

    public RpcChannelManager(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
        loadBalancer = Config.loadBalancer;
    }


    public void initChannels(List<ServiceMeta> serviceList) throws RpcException {
        for (ServiceMeta serviceMeta : serviceList) {
            List<InetSocketAddress> providerAddressList = ServiceDictionary.getInstance().getServiceAddress(serviceMeta);
            for (InetSocketAddress address : providerAddressList) {
                rpcClient.connect(address);
            }
        }
    }

    public RpcChannel getRpcChannel(ServiceMeta serviceMeta) throws RpcException {
        List<InetSocketAddress> providerAddressList = ServiceDictionary.getInstance().getServiceAddress(serviceMeta);
        if (CollectionUtils.isEmpty(providerAddressList)) {
            throw new RpcException("no provider address list");
        }
        RpcChannel rpcChannel = loadBalancer.select(providerAddressList, channelMap);
        if (rpcChannel == null) {
            try {
                rpcChannel = Config.connector.connect(rpcClient, providerAddressList);
            } catch (ConnectFailedException e) {
                throw new RpcException("no valid channel");
            }
            logger.info("new channel with " + Config.connector.getName() + ":" + rpcChannel);
            return rpcChannel;
        }
        return rpcChannel;
    }

    public void addRpcChannel(InetSocketAddress inetSocketAddress, RpcChannel rpcChannel) {
        channelMap.put(inetSocketAddress, rpcChannel);
    }

    public void clear() {
        channelMap.clear();
    }

}
