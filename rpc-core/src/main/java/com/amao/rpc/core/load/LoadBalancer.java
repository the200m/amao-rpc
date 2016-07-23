package com.amao.rpc.core.load;

import com.amao.rpc.core.client.channel.RpcChannel;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 阿毛 on 2016/7/8.
 */
public interface LoadBalancer {

    RpcChannel select(List<InetSocketAddress> providerAddressList, ConcurrentHashMap<InetSocketAddress, RpcChannel> map);
}
