package com.amao.rpc.core.load;

import com.amao.rpc.core.client.channel.RpcChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 阿毛 on 2016/7/8.
 */
public class RoundLoadBalancer implements LoadBalancer {

    private static Logger logger = LoggerFactory.getLogger(RoundLoadBalancer.class);

    @Override
    public RpcChannel select(List<InetSocketAddress> providerAddressList, ConcurrentHashMap<InetSocketAddress, RpcChannel> map) {
        RpcChannel rpcChannel;
        for (InetSocketAddress address : providerAddressList) {
            rpcChannel = map.get(address);
            if (rpcChannel != null) {
                logger.info("get channel from pool:" + rpcChannel);
                return rpcChannel;
            }
        }
        return null;
    }
}
